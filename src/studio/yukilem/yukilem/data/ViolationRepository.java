package studio.glace.glantielytratarget.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.data.ViolationRecord;
import studio.glace.glantielytratarget.libs.caffeine.cache.Cache;
import studio.glace.glantielytratarget.libs.caffeine.cache.Caffeine;
import studio.glace.glantielytratarget.libs.nixlib.database.Database;
import studio.glace.glantielytratarget.libs.nixlib.database.RowMapper;

public final class ViolationRepository {
    private static final String CREATE_TABLE_SQLITE = "CREATE TABLE IF NOT EXISTS aet_violations (id INTEGER PRIMARY KEY AUTOINCREMENT, uuid VARCHAR(36) NOT NULL, name VARCHAR(16), check_name VARCHAR(64), seq INTEGER, created_at BIGINT, bps REAL, client_version VARCHAR(48), ping INTEGER, tps VARCHAR(8), world VARCHAR(64), x REAL, y REAL, z REAL, yaw REAL, pitch REAL, gamemode VARCHAR(16), gliding INTEGER, on_ground INTEGER, sprinting INTEGER)";
    private static final String CREATE_TABLE_MYSQL = "CREATE TABLE IF NOT EXISTS aet_violations (id BIGINT AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(36) NOT NULL, name VARCHAR(16), check_name VARCHAR(64), seq INT, created_at BIGINT, bps DOUBLE, client_version VARCHAR(48), ping INT, tps VARCHAR(8), world VARCHAR(64), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, gamemode VARCHAR(16), gliding TINYINT, on_ground TINYINT, sprinting TINYINT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
    private static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS idx_aet_violations_uuid ON aet_violations(uuid)";
    private static final String INSERT = "INSERT INTO aet_violations (uuid, name, check_name, seq, created_at, bps, client_version, ping, tps, world, x, y, z, yaw, pitch, gamemode, gliding, on_ground, sprinting) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String COUNT = "SELECT COUNT(*) AS c FROM aet_violations WHERE uuid = ?";
    private static final String SELECT_FOR_PLAYER = "SELECT * FROM aet_violations WHERE uuid = ? ORDER BY id DESC LIMIT ?";
    private static final String KEEP_THRESHOLD = "SELECT id FROM aet_violations WHERE uuid = ? ORDER BY id DESC LIMIT 1 OFFSET ?";
    private static final String DELETE_OLDER_THAN = "DELETE FROM aet_violations WHERE uuid = ? AND id < ?";
    private static final String DELETE_FOR_PLAYER = "DELETE FROM aet_violations WHERE uuid = ?";
    private static final String DELETE_ALL = "DELETE FROM aet_violations";
    private static final int HARD_SELECT_LIMIT = 1000;
    private final Database database;
    private final boolean mysql;
    private final Cache<UUID, AtomicInteger> recordCounts;
    private volatile int maxPerPlayer;
    private volatile boolean overwriteOldest;

    public ViolationRepository(@NotNull Database database) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        long l = 30L;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        Caffeine<Object, Object> caffeine2 = caffeine.expireAfterAccess(l, timeUnit);
        long l2 = 5000L;
        Cache cache = caffeine2.maximumSize(l2).build();
        this.recordCounts = cache;
        this.maxPerPlayer = 250;
        this.overwriteOldest = true;
        this.database = database;
        String string = database.dialect();
        boolean bl = !"sqlite".equalsIgnoreCase(string);
        this.mysql = bl;
        String string2 = this.mysql ? CREATE_TABLE_MYSQL : CREATE_TABLE_SQLITE;
        Object[] objectArray = new Object[]{};
        database.execute(string2, objectArray);
        this.safeExecute(CREATE_INDEX);
    }

    public void configure(int n, boolean bl) {
        int n2;
        this.maxPerPlayer = n2 = Math.max(1, n);
        this.overwriteOldest = bl;
        this.recordCounts.invalidateAll();
    }

    public void record(@NotNull ViolationRecord violationRecord) {
        Database database = this.database;
        Runnable runnable = () -> this.writeRecord(violationRecord);
        CompletableFuture<Void> completableFuture = database.runAsync(runnable);
    }

    private void writeRecord(@NotNull ViolationRecord violationRecord) {
        AtomicInteger atomicInteger;
        AtomicInteger atomicInteger2;
        int n = this.maxPerPlayer;
        Cache<UUID, AtomicInteger> cache = this.recordCounts;
        UUID uUID2 = violationRecord.uuid();
        Function<UUID, AtomicInteger> function = uUID -> {
            long l = Integer.MAX_VALUE;
            long size = this.count((UUID)uUID);
            int capped = (int)Math.min(l, size);
            AtomicInteger counter = new AtomicInteger(capped);
            return counter;
        };
        AtomicInteger atomicInteger3 = atomicInteger2 = (atomicInteger = cache.get(uUID2, function));
        synchronized (atomicInteger2) {
            try {
                int n2;
                AtomicInteger atomicInteger4 = atomicInteger;
                int n3 = n2 = atomicInteger4.get();
                int n4 = n;
                if (n3 >= n4 && !this.overwriteOldest) {
                    AtomicInteger atomicInteger5 = atomicInteger3;
                    // ** MonitorExit[arg24_13] (shouldn't be in output)
                    return;
                }
                Database database = this.database;
                Object[] objectArray = new Object[19];
                String string = violationRecord.uuid().toString();
                objectArray[0] = string;
                String string2 = violationRecord.name();
                objectArray[1] = string2;
                String string3 = violationRecord.checkName();
                objectArray[2] = string3;
                Integer n5 = violationRecord.seq();
                objectArray[3] = n5;
                Long l = violationRecord.createdAt();
                objectArray[4] = l;
                Double d = violationRecord.bps();
                objectArray[5] = d;
                String string4 = violationRecord.clientVersion();
                objectArray[6] = string4;
                Integer n6 = violationRecord.ping();
                objectArray[7] = n6;
                String string5 = violationRecord.tps();
                objectArray[8] = string5;
                String string6 = violationRecord.world();
                objectArray[9] = string6;
                Double d2 = violationRecord.x();
                objectArray[10] = d2;
                Double d3 = violationRecord.y();
                objectArray[11] = d3;
                Double d4 = violationRecord.z();
                objectArray[12] = d4;
                Float f = Float.valueOf(violationRecord.yaw());
                objectArray[13] = f;
                Float f2 = Float.valueOf(violationRecord.pitch());
                objectArray[14] = f2;
                String string7 = violationRecord.gamemode();
                objectArray[15] = string7;
                int n7 = violationRecord.gliding() ? 1 : 0;
                Integer n8 = n7;
                objectArray[16] = n8;
                int n9 = violationRecord.onGround() ? 1 : 0;
                Integer n10 = n9;
                objectArray[17] = n10;
                int n11 = violationRecord.sprinting() ? 1 : 0;
                Integer n12 = n11;
                objectArray[18] = n12;
                database.execute(INSERT, objectArray);
                int n13 = n2;
                int n14 = n;
                if (n13 >= n14) {
                    UUID uUID3 = violationRecord.uuid();
                    int n15 = n;
                    this.pruneToCap(uUID3, n15);
                }
                AtomicInteger atomicInteger6 = atomicInteger;
                int n16 = n;
                int n17 = n2;
                int n18 = n17 + 1;
                int n19 = Math.min(n16, n18);
                atomicInteger6.set(n19);
                AtomicInteger atomicInteger7 = atomicInteger3;
                // ** MonitorExit[arg33_47] (shouldn't be in output)
                return;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                AtomicInteger atomicInteger8 = atomicInteger3;
                // ** MonitorExit[arg26_50] (shouldn't be in output)
                throw e;
            }
        }
    }

    private void pruneToCap(@NotNull UUID uUID, int n) {
        if (n <= 0) {
            Database database = this.database;
            Object[] objectArray = new Object[1];
            String string = uUID.toString();
            objectArray[0] = string;
            database.execute(DELETE_FOR_PLAYER, objectArray);
            return;
        }
        Database database = this.database;
        RowMapper<Long> rowMapper = resultSet -> resultSet.getLong("id");
        Object[] objectArray = new Object[2];
        String string = uUID.toString();
        objectArray[0] = string;
        Integer n2 = n - 1;
        objectArray[1] = n2;
        Long l = database.queryOne(KEEP_THRESHOLD, rowMapper, objectArray).orElse(null);
        Long l2 = l;
        if (l2 != null) {
            Database database2 = this.database;
            Object[] objectArray2 = new Object[2];
            String string2 = uUID.toString();
            objectArray2[0] = string2;
            Long l3 = l;
            objectArray2[1] = l3;
            database2.execute(DELETE_OLDER_THAN, objectArray2);
        }
    }

    public long count(@NotNull UUID uUID) {
        Database database = this.database;
        RowMapper<Long> rowMapper = resultSet -> resultSet.getLong("c");
        Object[] objectArray = new Object[1];
        String string = uUID.toString();
        objectArray[0] = string;
        Optional<Long> optional = database.queryOne(COUNT, rowMapper, objectArray);
        Long l = 0L;
        return optional.orElse(l);
    }

    @NotNull
    public List<ViolationRecord> load(@NotNull UUID uUID) {
        Database database = this.database;
        RowMapper<ViolationRecord> rowMapper = resultSet -> {
            long longVal = resultSet.getLong("id");
            String string = ViolationRepository.orEmpty(resultSet.getString("name"));
            String string2 = ViolationRepository.orEmpty(resultSet.getString("check_name"));
            int intVal = resultSet.getInt("seq");
            long longVal2 = resultSet.getLong("created_at");
            double double_ = resultSet.getDouble("bps");
            String string3 = ViolationRepository.orEmpty(resultSet.getString("client_version"));
            int intVal2 = resultSet.getInt("ping");
            String string4 = ViolationRepository.orEmpty(resultSet.getString("tps"));
            String string5 = ViolationRepository.orEmpty(resultSet.getString("world"));
            double double_2 = resultSet.getDouble("x");
            double double_3 = resultSet.getDouble("y");
            double double_4 = resultSet.getDouble("z");
            float float_ = resultSet.getFloat("yaw");
            float float_2 = resultSet.getFloat("pitch");
            String string6 = ViolationRepository.orEmpty(resultSet.getString("gamemode"));
            boolean bl = resultSet.getInt("gliding") != 0;
            boolean bl2 = resultSet.getInt("on_ground") != 0;
            boolean bl3 = resultSet.getInt("sprinting") != 0;
            ViolationRecord violationRecord = new ViolationRecord(longVal, uUID, string, string2, intVal, longVal2, double_, string3, intVal2, string4, string5, double_2, double_3, double_4, float_, float_2, string6, bl, bl2, bl3);
            return violationRecord;
        };
        Object[] objectArray = new Object[2];
        String string = uUID.toString();
        objectArray[0] = string;
        Integer n = 1000;
        objectArray[1] = n;
        List<ViolationRecord> list = database.queryMany(SELECT_FOR_PLAYER, rowMapper, objectArray);
        List<ViolationRecord> list2 = list;
        return list2 == null ? new ArrayList<>() : list;
    }

    public void clear(@NotNull UUID uUID) {
        this.recordCounts.invalidate(uUID);
        Database database = this.database;
        Object[] objectArray = new Object[1];
        String string = uUID.toString();
        objectArray[0] = string;
        CompletableFuture<Void> completableFuture = database.executeAsync(DELETE_FOR_PLAYER, objectArray);
    }

    public void clearAll() {
        this.recordCounts.invalidateAll();
        Database database = this.database;
        Object[] objectArray = new Object[]{};
        CompletableFuture<Void> completableFuture = database.executeAsync(DELETE_ALL, objectArray);
    }

    public void close() {
        this.recordCounts.invalidateAll();
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

    @NotNull
    private static String orEmpty(String string) {
        String string2 = string == null ? "" : string;
        return string2;
    }
}

