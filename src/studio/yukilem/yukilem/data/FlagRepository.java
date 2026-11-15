package studio.glace.glantielytratarget.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.libs.nixlib.database.Database;
import studio.glace.glantielytratarget.libs.nixlib.database.RowMapper;

public final class FlagRepository {
    private static final String CREATE_TABLE_SQLITE = "CREATE TABLE IF NOT EXISTS aet_flags (uuid VARCHAR(36) PRIMARY KEY, total_violations INTEGER NOT NULL DEFAULT 0, name VARCHAR(16))";
    private static final String CREATE_TABLE_MYSQL = "CREATE TABLE IF NOT EXISTS aet_flags (uuid VARCHAR(36) PRIMARY KEY, total_violations INT NOT NULL DEFAULT 0, name VARCHAR(16)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
    private static final String UPSERT_SQLITE = "INSERT INTO aet_flags (uuid, total_violations, name) VALUES (?, 1, ?) ON CONFLICT(uuid) DO UPDATE SET total_violations = total_violations + 1, name = ?";
    private static final String UPSERT_MYSQL = "INSERT INTO aet_flags (uuid, total_violations, name) VALUES (?, 1, ?) ON DUPLICATE KEY UPDATE total_violations = total_violations + 1, name = VALUES(name)";
    private static final String SELECT_FLAGS = "SELECT total_violations FROM aet_flags WHERE uuid = ?";
    private static final String SELECT_ALL_NAMES = "SELECT uuid, name FROM aet_flags WHERE name IS NOT NULL";
    private static final String RESET_FLAGS = "DELETE FROM aet_flags WHERE uuid = ?";
    private static final String RESET_ALL = "DELETE FROM aet_flags";
    private final Database database;
    private final boolean mysql;
    private final String upsertSql;
    private final Map<UUID, AtomicInteger> cache;
    private final Map<String, UUID> nameToUuid;
    private final Map<UUID, String> uuidToName;

    public FlagRepository(@NotNull Database database) {
        ConcurrentHashMap<UUID, AtomicInteger> concurrentHashMap = new ConcurrentHashMap<UUID, AtomicInteger>();
        this.cache = concurrentHashMap;
        ConcurrentHashMap<String, UUID> concurrentHashMap2 = new ConcurrentHashMap<String, UUID>();
        this.nameToUuid = concurrentHashMap2;
        ConcurrentHashMap<UUID, String> concurrentHashMap3 = new ConcurrentHashMap<UUID, String>();
        this.uuidToName = concurrentHashMap3;
        this.database = database;
        String string = database.dialect();
        boolean bl = !"sqlite".equalsIgnoreCase(string);
        this.mysql = bl;
        String string2 = this.mysql ? CREATE_TABLE_MYSQL : CREATE_TABLE_SQLITE;
        Object[] objectArray = new Object[]{};
        database.execute(string2, objectArray);
        this.migrateLegacySchema();
        String string3 = this.mysql ? UPSERT_MYSQL : UPSERT_SQLITE;
        this.upsertSql = string3;
        this.loadNameIndex();
    }

    private void migrateLegacySchema() {
        this.safeExecute("ALTER TABLE aet_flags RENAME COLUMN total_flags TO total_violations");
        this.safeExecute("ALTER TABLE aet_flags ADD COLUMN name VARCHAR(16)");
    }

    private void loadNameIndex() {
        try {
            Iterator<String[]> iterator;
            List<String[]> list;
            Database database = this.database;
            RowMapper<String[]> rowMapper = resultSet -> {
                String string;
                String string2 = resultSet.getString("uuid");
                String[] stringArray = new String[]{string2, string = resultSet.getString("name")};
                return stringArray;
            };
            Object[] objectArray = new Object[]{};
            List<String[]> list2 = list = database.queryMany(SELECT_ALL_NAMES, rowMapper, objectArray);
            if (list2 == null) {
                return;
            }
            List<String[]> list3 = list;
            Iterator<String[]> iterator2 = list3.iterator();
            while ((iterator = iterator2).hasNext()) {
                String[] stringArray;
                Iterator<String[]> iterator3 = iterator2;
                String[] stringArray2 = iterator3.next();
                String[] stringArray3 = stringArray2;
                if (stringArray3[0] == null || (stringArray = stringArray2)[1] == null) continue;
                try {
                    String[] stringArray4 = stringArray2;
                    UUID uUID = UUID.fromString(stringArray4[0]);
                    String[] stringArray5 = stringArray2;
                    String string = stringArray5[1];
                    this.indexName(uUID, string);
                }
                catch (IllegalArgumentException e) {
                    IllegalArgumentException e2 = e;
                }
            }
        }
        catch (Throwable e) {
            Throwable e3 = e;
        }
    }

    private void indexName(@NotNull UUID uUID, @NotNull String string) {
        String string2 = this.uuidToName.put(uUID, string);
        Map<String, UUID> map = this.nameToUuid;
        Locale locale = Locale.ROOT;
        String lowerCase = string.toLowerCase(locale);
        UUID uUID2 = map.put(lowerCase, uUID);
    }

    public void loadPlayer(@NotNull UUID uUID) {
        Database database = this.database;
        RowMapper<Integer> rowMapper = resultSet -> resultSet.getInt("total_violations");
        Object[] objectArray = new Object[1];
        String string = uUID.toString();
        objectArray[0] = string;
        Optional<Integer> optional = database.queryOne(SELECT_FLAGS, rowMapper, objectArray);
        Consumer<Integer> consumer = n -> {
            Map<UUID, AtomicInteger> map = this.cache;
            int n2 = n;
            AtomicInteger atomicInteger = new AtomicInteger(n2);
            AtomicInteger atomicInteger2 = map.put(uUID, atomicInteger);
        };
        Runnable runnable = () -> {
            Map<UUID, AtomicInteger> map = this.cache;
            AtomicInteger atomicInteger = new AtomicInteger(0);
            AtomicInteger atomicInteger2 = map.put(uUID, atomicInteger);
        };
        optional.ifPresentOrElse(consumer, runnable);
    }

    public void loadPlayerAsync(@NotNull UUID uUID) {
        Database database = this.database;
        RowMapper<Integer> rowMapper = resultSet -> resultSet.getInt("total_violations");
        Object[] objectArray = new Object[1];
        String string = uUID.toString();
        objectArray[0] = string;
        CompletableFuture<Optional<Integer>> completableFuture = database.queryOneAsync(SELECT_FLAGS, rowMapper, objectArray);
        Consumer<Optional> consumer = optional -> {
            Consumer<Integer> onPresent = n -> {
                Map<UUID, AtomicInteger> map = this.cache;
                int n2 = n;
                AtomicInteger atomicInteger = new AtomicInteger(n2);
                AtomicInteger atomicInteger2 = map.put(uUID, atomicInteger);
            };
            Runnable runnable = () -> {
                Map<UUID, AtomicInteger> map = this.cache;
                AtomicInteger atomicInteger = new AtomicInteger(0);
                AtomicInteger atomicInteger2 = map.put(uUID, atomicInteger);
            };
            optional.ifPresentOrElse(onPresent, runnable);
        };
        CompletionStage completionStage = completableFuture.thenAccept(consumer);
    }

    public int getTotalViolations(@NotNull UUID uUID) {
        AtomicInteger atomicInteger = this.cache.get(uUID);
        AtomicInteger atomicInteger2 = atomicInteger;
        if (atomicInteger2 != null) {
            AtomicInteger atomicInteger3 = atomicInteger;
            return atomicInteger3.get();
        }
        Database database = this.database;
        RowMapper<Integer> rowMapper = resultSet -> resultSet.getInt("total_violations");
        Object[] objectArray = new Object[1];
        String string = uUID.toString();
        objectArray[0] = string;
        Optional<Integer> optional = database.queryOne(SELECT_FLAGS, rowMapper, objectArray);
        Integer n = 0;
        return optional.orElse(n);
    }

    public void incrementViolations(@NotNull UUID uUID, @NotNull String string) {
        AtomicInteger atomicInteger = this.cache.get(uUID);
        AtomicInteger atomicInteger2 = atomicInteger;
        if (atomicInteger2 != null) {
            AtomicInteger atomicInteger3 = atomicInteger;
            int n = atomicInteger3.incrementAndGet();
        } else {
            Map<UUID, AtomicInteger> map = this.cache;
            AtomicInteger atomicInteger4 = new AtomicInteger(1);
            AtomicInteger atomicInteger5 = map.put(uUID, atomicInteger4);
        }
        this.indexName(uUID, string);
        if (this.mysql) {
            Database database = this.database;
            String string2 = this.upsertSql;
            Object[] objectArray = new Object[2];
            String string3 = uUID.toString();
            objectArray[0] = string3;
            objectArray[1] = string;
            CompletableFuture<Void> completableFuture = database.executeAsync(string2, objectArray);
        } else {
            Database database = this.database;
            String string4 = this.upsertSql;
            Object[] objectArray = new Object[3];
            String string5 = uUID.toString();
            objectArray[0] = string5;
            objectArray[1] = string;
            objectArray[2] = string;
            CompletableFuture<Void> completableFuture = database.executeAsync(string4, objectArray);
        }
    }

    public void resetTotalViolations(@NotNull UUID uUID) {
        AtomicInteger atomicInteger = this.cache.remove(uUID);
        Database database = this.database;
        Object[] objectArray = new Object[1];
        String string = uUID.toString();
        objectArray[0] = string;
        CompletableFuture<Void> completableFuture = database.executeAsync(RESET_FLAGS, objectArray);
    }

    public void resetAll() {
        this.cache.clear();
        Database database = this.database;
        Object[] objectArray = new Object[]{};
        CompletableFuture<Void> completableFuture = database.executeAsync(RESET_ALL, objectArray);
    }

    public void evictPlayer(@NotNull UUID uUID) {
        AtomicInteger atomicInteger = this.cache.remove(uUID);
    }

    @NotNull
    public List<String> knownNames() {
        Collection<String> collection = this.uuidToName.values();
        ArrayList<String> arrayList = new ArrayList<String>(collection);
        return arrayList;
    }

    @Nullable
    public UUID uuidForName(@NotNull String string) {
        Map<String, UUID> map = this.nameToUuid;
        Locale locale = Locale.ROOT;
        String lowerCase = string.toLowerCase(locale);
        return map.get(lowerCase);
    }

    @Nullable
    public String nameForUuid(@NotNull UUID uUID) {
        return this.uuidToName.get(uUID);
    }

    public boolean hasData(@NotNull UUID uUID) {
        boolean bl = this.getTotalViolations(uUID) > 0 || this.uuidToName.containsKey(uUID);
        return bl;
    }

    public void close() {
        this.cache.clear();
        this.nameToUuid.clear();
        this.uuidToName.clear();
    }

    private void safeExecute(@NotNull String string) {
        try {
            Database database = this.database;
            Object[] objectArray = new Object[]{};
            database.execute(string, objectArray);
        }
        catch (Throwable e) {
            Throwable e2 = e;
        }
    }
}

