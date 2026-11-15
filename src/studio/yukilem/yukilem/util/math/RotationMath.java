package studio.glace.glantielytratarget.util.math;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class RotationMath {
    private static final double MIN_DISTANCE_SQUARED = 0.25;

    private RotationMath() {
    }

    public static boolean lookingNearTarget(@NotNull Player player, @NotNull Entity entity, double d, double d2) {
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
        double d13;
        double d14;
        double d15;
        double d16;
        double d17;
        Vector vector;
        double d18;
        double d19;
        double d20;
        Vector vector2;
        double d21;
        double d22;
        double d23;
        double d24;
        double d25;
        Location location;
        double d26;
        Location location2;
        double d27;
        double d28;
        double d29;
        double d30;
        double d31;
        Location location3;
        double d32;
        double d33;
        Location location4;
        double d34;
        double d35;
        double d36;
        double d37;
        double d38;
        double d39;
        Location location5;
        double d40;
        Location eyeLocation = player.getEyeLocation();
        Location location6 = entity.getLocation();
        double height = entity.getHeight();
        double d41 = 0.5;
        double d42 = height * d41;
        Location location7 = location6;
        double x = location7.getX();
        double d43 = x - (d40 = (location5 = eyeLocation).getX());
        double d44 = d43;
        double d45 = d44 * (d39 = d43);
        double d46 = d45 + (d38 = (d37 = (d36 = (d35 = (d34 = (location4 = location6).getY()) + (d33 = d42)) - (d32 = (location3 = eyeLocation).getY()))) * (d31 = d36));
        double d47 = d46 + (d30 = (d29 = (d28 = (d27 = (location2 = location6).getZ()) - (d26 = (location = eyeLocation).getZ()))) * (d25 = d28));
        double d48 = d47;
        if (d48 < (d24 = 0.25)) {
            return true;
        }
        double d49 = d47;
        double d50 = Math.sqrt(d49);
        Location location8 = eyeLocation;
        Vector direction = location8.getDirection();
        Vector vector3 = direction;
        double x2 = vector3.getX();
        double d51 = x2 * (d23 = d43);
        double d52 = d51 + (d22 = (d21 = (vector2 = direction).getY()) * (d20 = d36));
        double d53 = d52 + (d19 = (d18 = (vector = direction).getZ()) * (d17 = d28));
        double d54 = d53 / (d16 = d50);
        double d55 = d54;
        boolean bl = d55 >= (d15 = (d14 = (d13 = (d12 = d * (d11 = d50)) - (d10 = d2 * (d9 = d42))) / (d8 = (d7 = Math.sqrt((d6 = d47) + (d5 = (d4 = d42) * (d3 = d42)))))));
        return bl;
    }
}

