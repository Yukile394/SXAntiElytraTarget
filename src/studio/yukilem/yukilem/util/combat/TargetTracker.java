package studio.yukilem.yukilem.util.combat;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TargetTracker {
    private static final long DEFAULT_TARGET_EXPIRY_MS = 5000L;
    private static final int MAX_TRACKED_TARGETS = 1000;
    private final Map<UUID, TrackedTarget> targets;
    private final Map<UUID, Long> unresolvedTargets = new ConcurrentHashMap<UUID, Long>();
    private volatile long targetExpiryMs;

    public TargetTracker() {
        long l;
        ConcurrentHashMap<UUID, TrackedTarget> concurrentHashMap = new ConcurrentHashMap<UUID, TrackedTarget>();
        this.targets = concurrentHashMap;
        this.targetExpiryMs = l = 5000L;
    }

    public void setTargetExpiryMs(long l) {
        long l2 = 0L;
        long l3 = l > l2 ? l : 5000L;
        this.targetExpiryMs = l3;
    }

    public long getTargetExpiryMs() {
        return this.targetExpiryMs;
    }

    public void recordAttack(@NotNull Player player, @NotNull Entity entity) {
        UUID uniqueId = player.getUniqueId();
        if (this.targets.size() > 1000) {
            this.cleanupStaleEntries();
        }
        Map<UUID, TrackedTarget> map = this.targets;
        UUID uUID = uniqueId;
        long l = System.currentTimeMillis();
        TrackedTarget trackedTarget = new TrackedTarget(entity, l);
        TrackedTarget trackedTarget2 = map.put(uUID, trackedTarget);
    }

    /** Records a hit on an entity that has no Bukkit counterpart (packet based NPCs). */
    public void recordUnresolvedAttack(@NotNull Player player) {
        if (this.unresolvedTargets.size() > 1000) {
            long now = System.currentTimeMillis();
            this.unresolvedTargets.values().removeIf(time -> now - time > this.targetExpiryMs);
        }
        this.unresolvedTargets.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public boolean hasUnresolvedTarget(@NotNull UUID uUID) {
        Long time = this.unresolvedTargets.get(uUID);
        if (time == null) {
            return false;
        }
        if (System.currentTimeMillis() - time > this.targetExpiryMs) {
            this.unresolvedTargets.remove(uUID);
            return false;
        }
        return true;
    }

    @Nullable
    public Entity getTarget(@NotNull UUID uUID) {
        Entity entity;
        TrackedTarget trackedTarget = this.targets.get(uUID);
        TrackedTarget trackedTarget2 = trackedTarget;
        if (trackedTarget2 == null) {
            return null;
        }
        long l = System.currentTimeMillis();
        TrackedTarget trackedTarget3 = trackedTarget;
        long l2 = trackedTarget3.timestamp;
        long l3 = l - l2;
        long l4 = this.targetExpiryMs;
        if (l3 > l4) {
            TrackedTarget trackedTarget4 = this.targets.remove(uUID);
            return null;
        }
        TrackedTarget trackedTarget5 = trackedTarget;
        Entity entity2 = (Entity)trackedTarget5.reference.get();
        Entity entity3 = entity2;
        if (entity3 == null || !(entity = entity2).isValid()) {
            TrackedTarget trackedTarget6 = this.targets.remove(uUID);
            return null;
        }
        Entity entity4 = entity2;
        return entity4;
    }

    public void removePlayer(@NotNull UUID uUID) {
        TrackedTarget trackedTarget = this.targets.remove(uUID);
        this.unresolvedTargets.remove(uUID);
    }

    public void clear() {
        this.targets.clear();
        this.unresolvedTargets.clear();
    }

    private void cleanupStaleEntries() {
        long l = System.currentTimeMillis();
        long l2 = this.targetExpiryMs;
        Set<Map.Entry<UUID, TrackedTarget>> set = this.targets.entrySet();
        long l3 = l;
        long l4 = l2;
        Predicate<Map.Entry> predicate = entry -> {
            TrackedTarget trackedTarget;
            TrackedTarget trackedTarget2 = trackedTarget = (TrackedTarget)entry.getValue();
            Entity entity = (Entity)trackedTarget2.reference.get();
            Entity entity2 = entity;
            if (entity2 == null) return true;
            Entity entity3 = entity;
            if (!entity3.isValid()) return true;
            TrackedTarget trackedTarget3 = trackedTarget;
            long timestamp = trackedTarget3.timestamp;
            if (l3 - timestamp <= l4) return false;
            return true;
        };
        boolean bl = set.removeIf(predicate);
    }

    private static final class TrackedTarget {
        final WeakReference<Entity> reference;
        long timestamp;

        TrackedTarget(Entity entity, long l) {
            WeakReference<Entity> weakReference = new WeakReference<Entity>(entity);
            this.reference = weakReference;
            this.timestamp = l;
        }
    }
}

