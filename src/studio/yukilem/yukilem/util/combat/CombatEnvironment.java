package studio.yukilem.yukilem.util.combat;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public final class CombatEnvironment {
   private CombatEnvironment() {
   }

   public static boolean intersectsCobweb(@NotNull Player player0) {
      BoundingBox boundingBox6 = player0.getBoundingBox();
      World world7 = player0.getWorld();
      BoundingBox boundingBox20 = boundingBox6;
      int i8 = (int)Math.floor(boundingBox20.getMinX());
      BoundingBox boundingBox26 = boundingBox6;
      int i9 = (int)Math.floor(boundingBox26.getMaxX());
      BoundingBox boundingBox32 = boundingBox6;
      int i10 = (int)Math.floor(boundingBox32.getMinY());
      BoundingBox boundingBox38 = boundingBox6;
      int i11 = (int)Math.floor(boundingBox38.getMaxY());
      BoundingBox boundingBox44 = boundingBox6;
      int i12 = (int)Math.floor(boundingBox44.getMinZ());
      BoundingBox boundingBox50 = boundingBox6;
      int i13 = (int)Math.floor(boundingBox50.getMaxZ());
      int i56 = i8;
      int i14 = i56;

      while (true) {
         int i57 = i14;
         int i3 = i9;
         if (i57 > i3) {
            return false;
         }

         int i59 = i10;
         int i15 = i59;

         while (true) {
            int i60 = i15;
            int i67 = i11;
            if (i60 > i67) {
               i14++;
               break;
            }

            int i61 = i12;
            int i16 = i61;

            while (true) {
               int i62 = i16;
               int i68 = i13;
               if (i62 > i68) {
                  i15++;
                  break;
               }

               World world63 = world7;
               int i69 = i14;
               int i4 = i15;
               int i5 = i16;
               Material material65 = world63.getBlockAt(i69, i4, i5).getType();
               Material material70 = Material.COBWEB;
               if (material65 == material70) {
                  return true;
               }

               i16++;
            }
         }
      }
   }
}
