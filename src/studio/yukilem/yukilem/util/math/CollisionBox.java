package studio.glace.glantielytratarget.util.math;

import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public final class CollisionBox {
    private CollisionBox() {
    }

    public static double distance(@NotNull Entity entity, @NotNull Entity entity2) {
        BoundingBox boundingBox = entity.getBoundingBox();
        BoundingBox boundingBox2 = entity2.getBoundingBox();
        double d = 0.0;
        BoundingBox boundingBox3 = boundingBox2;
        double minX = boundingBox3.getMinX();
        BoundingBox boundingBox4 = boundingBox;
        double maxX = boundingBox4.getMaxX();
        double d2 = minX - maxX;
        BoundingBox boundingBox5 = boundingBox;
        double minX2 = boundingBox5.getMinX();
        BoundingBox boundingBox6 = boundingBox2;
        double maxX2 = boundingBox6.getMaxX();
        double d3 = minX2 - maxX2;
        double d4 = Math.max(d2, d3);
        double d5 = Math.max(d, d4);
        double d6 = 0.0;
        BoundingBox boundingBox7 = boundingBox2;
        double minY = boundingBox7.getMinY();
        BoundingBox boundingBox8 = boundingBox;
        double maxY = boundingBox8.getMaxY();
        double d7 = minY - maxY;
        BoundingBox boundingBox9 = boundingBox;
        double minY2 = boundingBox9.getMinY();
        BoundingBox boundingBox10 = boundingBox2;
        double maxY2 = boundingBox10.getMaxY();
        double d8 = minY2 - maxY2;
        double d9 = Math.max(d7, d8);
        double d10 = Math.max(d6, d9);
        double d11 = 0.0;
        BoundingBox boundingBox11 = boundingBox2;
        double minZ = boundingBox11.getMinZ();
        BoundingBox boundingBox12 = boundingBox;
        double maxZ = boundingBox12.getMaxZ();
        double d12 = minZ - maxZ;
        BoundingBox boundingBox13 = boundingBox;
        double minZ2 = boundingBox13.getMinZ();
        BoundingBox boundingBox14 = boundingBox2;
        double maxZ2 = boundingBox14.getMaxZ();
        double d13 = minZ2 - maxZ2;
        double d14 = Math.max(d12, d13);
        double d15 = Math.max(d11, d14);
        double d16 = d5;
        double d17 = d5;
        double d18 = d16 * d17;
        double d19 = d10;
        double d20 = d10;
        double d21 = d19 * d20;
        double d22 = d18 + d21;
        double d23 = d15;
        double d24 = d15;
        double d25 = d23 * d24;
        return Math.sqrt(d22 + d25);
    }
}

