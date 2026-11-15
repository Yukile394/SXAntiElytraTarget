package studio.yukilem.yukilem.libs.nixlib.world;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.version.ServerType;

public final class WorldThreadAccess {
   private static final boolean MULTITHREADED = ServerType.detect().isMultithreaded();
   private static final MethodHandle TICK_THREAD_FOR_ENTITY;
   private static final MethodHandle TICK_THREAD_FOR_CHUNK;

   private WorldThreadAccess() {
   }

   public static boolean canReadBlocks(@NotNull Entity entity0) {
      return !MULTITHREADED ? Bukkit.isPrimaryThread() : tickThreadForEntity(entity0);
   }

   public static boolean canReadBlocks(@NotNull Location location0) {
      World world1 = location0.getWorld();
      if (world1 == null) {
         return false;
      } else {
         return !MULTITHREADED ? Bukkit.isPrimaryThread() : tickThreadForChunk(world1, location0.getBlockX(), location0.getBlockZ());
      }
   }

   public static boolean isMultithreadedServer() {
      return MULTITHREADED;
   }

   private static boolean tickThreadForEntity(@NotNull Entity entity0) {
      MethodHandle methodHandle1 = TICK_THREAD_FOR_ENTITY;
      if (methodHandle1 == null) {
         return false;
      }

      try {
         return (boolean)methodHandle1.invoke((Entity)entity0);
      } catch (Throwable throwable3) {
         return false;
      }
   }

   private static boolean tickThreadForChunk(@NotNull World world0, int i1, int i2) {
      MethodHandle methodHandle3 = TICK_THREAD_FOR_CHUNK;
      if (methodHandle3 == null) {
         return false;
      }

      try {
         return (boolean)methodHandle3.invoke((World)world0, (int)i1, (int)i2);
      } catch (Throwable throwable5) {
         return false;
      }
   }

   static {
      MethodHandle methodHandle0 = null;
      MethodHandle methodHandle1 = null;
      if (MULTITHREADED) {
         try {
            Class clazz2 = Class.forName("ca.spottedleaf.moonrise.common.util.TickThread");
            methodHandle0 = MethodHandles.publicLookup().unreflect(clazz2.getMethod("isTickThreadFor", Entity.class));
            methodHandle1 = MethodHandles.publicLookup().unreflect(clazz2.getMethod("isTickThreadFor", World.class, int.class, int.class));
         } catch (Throwable throwable3) {
         }
      }

      TICK_THREAD_FOR_ENTITY = methodHandle0;
      TICK_THREAD_FOR_CHUNK = methodHandle1;
   }
}
