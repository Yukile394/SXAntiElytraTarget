package studio.yukilem.yukilem.platform;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.ConfigurationService;
import studio.yukilem.yukilem.config.settings.LagProtectionSettings;

public final class ServerHealthMonitor {
    private static final long TPS_CACHE_DURATION_MS = 1000L;
    private static final long PING_COUNT_CACHE_DURATION_MS = 2000L;
    private static final long PING_CACHE_DURATION_MS = 2000L;
    private final ConfigurationService config;
    private final Map<UUID, PingEntry> pingCache;
    private volatile double cachedTps;
    private volatile long lastTpsUpdate;
    private volatile int cachedHighPingCount;
    private volatile long lastPingCountUpdate;

    public ServerHealthMonitor(@NotNull ConfigurationService configurationService) {
        long l;
        long l2;
        double d;
        ConcurrentHashMap<UUID, PingEntry> concurrentHashMap = new ConcurrentHashMap<UUID, PingEntry>();
        this.pingCache = concurrentHashMap;
        this.cachedTps = d = 20.0;
        this.lastTpsUpdate = l2 = 0L;
        this.cachedHighPingCount = 0;
        this.lastPingCountUpdate = l = 0L;
        this.config = configurationService;
    }

    public double getCurrentTps() {
        long l;
        long l2;
        long l3 = System.currentTimeMillis();
        long l4 = l3;
        long l5 = l4 - (l2 = this.lastTpsUpdate);
        if (l5 > (l = 1000L)) {
            long l6;
            double d;
            this.cachedTps = d = this.fetchTps();
            this.lastTpsUpdate = l6 = l3;
        }
        return this.cachedTps;
    }

    private double fetchTps() {
        try {
            double[] dArray;
            double[] dArray2;
            double[] dArray3 = dArray2 = Bukkit.getServer().getTPS();
            if (dArray3 != null && (dArray = dArray2).length > 0) {
                double[] dArray4 = dArray2;
                double d = dArray4[0];
                return d;
            }
        }
        catch (Throwable e) {
            Throwable e2 = e;
        }
        return 20.0;
    }

    public int getPlayerPing(@NotNull Player player) {
        int n;
        UUID uniqueId = player.getUniqueId();
        long l = System.currentTimeMillis();
        Map<UUID, PingEntry> map = this.pingCache;
        UUID uUID = uniqueId;
        PingEntry pingEntry = map.get(uUID);
        PingEntry pingEntry2 = pingEntry;
        if (pingEntry2 != null) {
            long l2 = l;
            PingEntry pingEntry3 = pingEntry;
            long l3 = pingEntry3.timestamp;
            long l4 = l2 - l3;
            long l5 = 2000L;
            if (l4 <= l5) {
                PingEntry pingEntry4 = pingEntry;
                return pingEntry4.ping;
            }
        }
        try {
            n = player.getPing();
        }
        catch (Throwable e) {
            Throwable e2 = e;
            n = 0;
        }
        Map<UUID, PingEntry> map2 = this.pingCache;
        UUID uUID2 = uniqueId;
        int n2 = n;
        long l6 = l;
        PingEntry pingEntry5 = new PingEntry(n2, l6);
        PingEntry pingEntry6 = map2.put(uUID2, pingEntry5);
        int n3 = n;
        return n3;
    }

    public boolean shouldSkipDetection(@NotNull Player player) {
        LagProtectionSettings lagProtectionSettings4;
        int n;
        LagProtectionSettings lagProtectionSettings;
        double d;
        double d2;
        LagProtectionSettings lagProtection = this.config.getLagProtection();
        LagProtectionSettings lagProtectionSettings2 = lagProtection;
        if (lagProtectionSettings2.tpsEnabled() && (d2 = this.getCurrentTps()) < (d = (lagProtectionSettings = lagProtection).minTps())) {
            return true;
        }
        LagProtectionSettings lagProtectionSettings3 = lagProtection;
        if (!lagProtectionSettings3.pingEnabled()) return false;
        int n2 = this.countHighPingPlayers();
        if (n2 < (n = (lagProtectionSettings4 = lagProtection).minAffectedPlayers())) return false;
        return true;
    }

    public void onPlayerQuit(@NotNull UUID uUID) {
        PingEntry pingEntry = this.pingCache.remove(uUID);
    }

    private int countHighPingPlayers() {
        long l;
        long l2;
        long l3 = System.currentTimeMillis();
        long l4 = l3;
        long l5 = l4 - (l2 = this.lastPingCountUpdate);
        if (l5 > (l = 2000L)) {
            long l6;
            int n;
            Iterator<? extends Player> iterator;
            int n2 = this.config.getLagProtection().maxPing();
            int n3 = 0;
            Iterator<? extends Player> iterator2 = Bukkit.getOnlinePlayers().iterator();
            while ((iterator = iterator2).hasNext()) {
                int n4;
                Iterator<? extends Player> iterator3 = iterator2;
                Player player = iterator3.next();
                int playerPing = this.getPlayerPing(player);
                if (playerPing <= (n4 = n2)) continue;
                ++n3;
            }
            this.cachedHighPingCount = n = n3;
            this.lastPingCountUpdate = l6 = l3;
        }
        return this.cachedHighPingCount;
    }

    private record PingEntry(int ping, long timestamp) {
    }
}

