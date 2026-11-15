package studio.glace.glantielytratarget.punishments;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.libs.caffeine.cache.Cache;
import studio.glace.glantielytratarget.libs.caffeine.cache.Caffeine;

final class ConsoleCommandDeduplicator {
    private static final long CLAIM_TTL_SECONDS = 5L;
    private final Cache<Claim, Boolean> claims;

    ConsoleCommandDeduplicator() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        long l = 5L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        Caffeine<Object, Object> caffeine2 = caffeine.expireAfterWrite(l, timeUnit);
        long l2 = 5000L;
        Cache cache = caffeine2.maximumSize(l2).build();
        this.claims = cache;
    }

    boolean claim(@NotNull UUID uUID, @NotNull String string) {
        Boolean bl;
        String string2;
        Claim claim;
        String string3 = string.trim();
        String string4 = string3;
        if (string4.isEmpty()) {
            return false;
        }
        ConcurrentMap<Claim, Boolean> concurrentMap = this.claims.asMap();
        boolean bl2 = concurrentMap.putIfAbsent(claim = new Claim(uUID, string2 = string3), bl = Boolean.TRUE) == null;
        return bl2;
    }

    void clear() {
        this.claims.invalidateAll();
    }

    private record Claim(UUID uuid, String command) {
    }
}

