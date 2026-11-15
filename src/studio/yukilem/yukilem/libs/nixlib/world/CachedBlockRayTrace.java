package studio.yukilem.yukilem.libs.nixlib.world;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.scheduler.Scheduler;

public final class CachedBlockRayTrace {
   private static final long FRESH_NANOS = 300000000L;
   private static final long REFRESH_COOLDOWN_NANOS = 250000000L;
   private static final float VIEW_ANGLE_EPSILON_DEG = 2.0F;
   private static final double EYE_SHIFT_EPSILON_SQ = 0.25;
   private static final Map<UUID, CachedBlockRayTrace.CacheEntry> CACHE = new ConcurrentHashMap<>();
   private static final Map<UUID, AtomicBoolean> REFRESH_PENDING = new ConcurrentHashMap<>();

   private CachedBlockRayTrace() {
   }

   public static boolean solidBlockInReach(@NotNull LivingEntity livingEntity0, double d1) {
      CachedBlockRayTrace.CacheEntry cacheEntry3 = CACHE.get(livingEntity0.getUniqueId());
      if (cacheEntry3 == null) {
         return true;
      } else if (cacheEntry3.isFresh(300000000L)) {
         return cacheEntry3.blockInReach;
      } else {
         return cacheEntry3.matchesView(livingEntity0.getEyeLocation()) ? cacheEntry3.blockInReach : true;
      }
   }

   public static void refreshIfStale(@NotNull Scheduler scheduler0, @NotNull Entity entity1, double d2) {
      if (entity1 instanceof LivingEntity livingEntity4) {
         UUID uuid5 = entity1.getUniqueId();
         if (needsProbe(CACHE.get(uuid5), livingEntity4.getEyeLocation(), System.nanoTime())) {
            if (WorldThreadAccess.canReadBlocks(entity1)) {
               probeAndCache(uuid5, livingEntity4, d2);
            } else {
               AtomicBoolean atomicBoolean6 = REFRESH_PENDING.computeIfAbsent(uuid5, arg0x -> new AtomicBoolean());
               if (atomicBoolean6.compareAndSet(false, true)) {
                  scheduler0.runFor(entity1, () -> {
                     try {
                        if (entity1.isValid() && entity1 instanceof LivingEntity arg5x && needsProbe(CACHE.get(uuid5), arg5x.getEyeLocation(), System.nanoTime())) {
                           probeAndCache(uuid5, arg5x, d2);
                        }
                     } finally {
                        atomicBoolean6.set(false);
                     }
                  });
               }
            }
         }
      }
   }

   public static void clear(@NotNull UUID uuid0) {
      CACHE.remove(uuid0);
      REFRESH_PENDING.remove(uuid0);
   }

   static boolean needsProbe(CachedBlockRayTrace.CacheEntry cacheEntry0, @NotNull Location location1, long l2) {
      if (cacheEntry0 == null) {
         return true;
      } else {
         return cacheEntry0.matchesView(location1) ? false : l2 - cacheEntry0.updatedAt > 250000000L;
      }
   }

   private static void probeAndCache(@NotNull UUID uuid0, @NotNull LivingEntity livingEntity1, double d2) {
      Location location4 = livingEntity1.getEyeLocation();
      World world5 = location4.getWorld();
      boolean flag6 = world5 != null && rayTraceHitsSolid(world5, location4, d2);
      CACHE.put(uuid0, new CachedBlockRayTrace.CacheEntry(flag6, System.nanoTime(), location4));
   }

   private static boolean rayTraceHitsSolid(@NotNull World world0, @NotNull Location location1, double d2) {
      try {
         RayTraceResult rayTraceResult4 = world0.rayTraceBlocks(location1, location1.getDirection(), d2, FluidCollisionMode.NEVER, true);
         if (rayTraceResult4 == null) {
            return false;
         }

         Block block5 = rayTraceResult4.getHitBlock();
         return block5 != null && !block5.getType().isAir();
      } catch (Throwable throwable6) {
         return false;
      }
   }

   private static float angleDelta(float f0, float f1) {
      float f2 = Math.abs(f0 - f1) % 360.0F;
      return f2 > 180.0F ? 360.0F - f2 : f2;
   }

   static final class CacheEntry {
      private final boolean blockInReach;
      private final long updatedAt;
      private final float yaw;
      private final float pitch;
      private final double eyeX;
      private final double eyeY;
      private final double eyeZ;

      CacheEntry(boolean local1, long local2, @NotNull Location local4) {
         this.blockInReach = local1;
         this.updatedAt = local2;
         this.yaw = local4.getYaw();
         this.pitch = local4.getPitch();
         this.eyeX = local4.getX();
         this.eyeY = local4.getY();
         this.eyeZ = local4.getZ();
      }

      private boolean isFresh(long local1) {
         return System.nanoTime() - this.updatedAt <= local1;
      }

      boolean matchesView(@NotNull Location local1) {
         if (!(CachedBlockRayTrace.angleDelta(this.yaw, local1.getYaw()) >= 2.0F) && !(Math.abs(this.pitch - local1.getPitch()) >= 2.0F)) {
            double local2 = local1.getX() - this.eyeX;
            double local4 = local1.getY() - this.eyeY;
            double d6 = local1.getZ() - this.eyeZ;
            return local2 * local2 + local4 * local4 + d6 * d6 < 0.25;
         } else {
            return false;
         }
      }
   }
}
