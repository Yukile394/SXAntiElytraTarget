package studio.glace.glantielytratarget.util.math;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public final class DistanceMath {
    private DistanceMath() {
    }

    public static boolean isWithinBounds(@NotNull Location location, @NotNull Location location2, double d, double d2) {
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        World world;
        World world2;
        if (location.getWorld() == null || !(world2 = location.getWorld()).equals(world = location2.getWorld())) {
            return false;
        }
        double y = location.getY();
        if (Math.abs(y - (d12 = location2.getY())) > d2) {
            return false;
        }
        double x = location.getX();
        double d13 = x - (d11 = location2.getX());
        double d14 = d13;
        double d15 = d14 * (d10 = d13);
        double d16 = d15 + (d9 = (d8 = (d7 = (d6 = location.getZ()) - (d5 = location2.getZ()))) * (d4 = d7));
        boolean bl = d16 <= (d3 = d * d);
        return bl;
    }

    public static double distanceSquared2D(@NotNull Location location, @NotNull Location location2) {
        World world;
        World world2;
        if (location.getWorld() == null || !(world2 = location.getWorld()).equals(world = location2.getWorld())) {
            return -1.0;
        }
        double x = location.getX();
        double x2 = location2.getX();
        double d = x - x2;
        double z = location.getZ();
        double z2 = location2.getZ();
        double d2 = z - z2;
        double d3 = d;
        double d4 = d;
        double d5 = d3 * d4;
        double d6 = d2;
        double d7 = d2;
        double d8 = d6 * d7;
        return d5 + d8;
    }

    public static double distanceSquared3D(@NotNull Location location, @NotNull Location location2) {
        World world;
        World world2;
        if (location.getWorld() == null || !(world2 = location.getWorld()).equals(world = location2.getWorld())) {
            return Double.POSITIVE_INFINITY;
        }
        double x = location.getX();
        double x2 = location2.getX();
        double d = x - x2;
        double y = location.getY();
        double y2 = location2.getY();
        double d2 = y - y2;
        double z = location.getZ();
        double z2 = location2.getZ();
        double d3 = z - z2;
        double d4 = d;
        double d5 = d;
        double d6 = d4 * d5;
        double d7 = d2;
        double d8 = d2;
        double d9 = d7 * d8;
        double d10 = d6 + d9;
        double d11 = d3;
        double d12 = d3;
        double d13 = d11 * d12;
        return d10 + d13;
    }
}

