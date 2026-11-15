package studio.yukilem.yukilem.checks.impl.pattern;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternSession;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternThresholds;
import studio.yukilem.yukilem.util.math.RotationMath;

public final class ElytraPatternTargetGate {
    private ElytraPatternTargetGate() {
    }

    @Nullable
    public static String skipBoostReason(@NotNull Player player, @Nullable Entity entity, @NotNull ElytraPatternSession elytraPatternSession, @NotNull ElytraPatternThresholds.PatternThresholds patternThresholds, long l) {
        Object object;
        block7: {
            long l2;
            block6: {
                block4: {
                    block5: {
                        long l3;
                        if (!patternThresholds.trReqHit()) break block4;
                        long l4 = elytraPatternSession.lastHitTime;
                        long l5 = 0L;
                        if (l4 == l5) {
                            l3 = Long.MAX_VALUE;
                        } else {
                            long l6 = elytraPatternSession.lastHitTime;
                            l3 = l - l6;
                        }
                        long l7 = l2 = l3;
                        long l8 = patternThresholds.trWinMs();
                        if (l7 <= l8) break block5;
                        long l9 = l2;
                        long l10 = Long.MAX_VALUE;
                        if (l9 != l10) break block6;
                        object = "never";
                        break block7;
                    }
                    if (entity == null || !ElytraPatternTargetGate.withinDistance(player, entity, patternThresholds)) {
                        return "skip target missing or out of range";
                    }
                }
                if (patternThresholds.trReqLook() && entity != null && !ElytraPatternTargetGate.isLookingAt(player, entity, patternThresholds)) {
                    return "skip not looking at target";
                }
                return null;
            }
            long l11 = l2;
            object = l11;
        }
        String string = String.valueOf(object);
        long l12 = patternThresholds.trWinMs();
        return "skip no recent hit sinceMs=" + string + " windowMs=" + l12;
    }

    public static boolean isLookingAt(@NotNull Player player, @Nullable Entity entity, @NotNull ElytraPatternThresholds.PatternThresholds patternThresholds) {
        if (entity == null) {
            return false;
        }
        double d = patternThresholds.trCosLookTol();
        double d2 = patternThresholds.trSinLookTol();
        return RotationMath.lookingNearTarget(player, entity, d, d2);
    }

    public static boolean withinDistance(@NotNull Player player, @NotNull Entity entity, @NotNull ElytraPatternThresholds.PatternThresholds patternThresholds) {
        double d;
        Location location;
        double d2;
        double d3;
        double d4;
        Location location2;
        double d5;
        Location location3;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        Location location4;
        double d11;
        Location location5;
        World world;
        Location location6;
        World world2;
        Location location7 = player.getLocation();
        Location location8 = entity.getLocation();
        Location location9 = location7;
        if (location9.getWorld() != null && !(world2 = (location6 = location7).getWorld()).equals(world = (location5 = location8).getWorld())) {
            return false;
        }
        Location location10 = location7;
        double x = location10.getX();
        double d12 = x - (d11 = (location4 = location8).getX());
        double d13 = d12;
        double d14 = d13 * (d10 = d12);
        double d15 = d14 + (d9 = (d8 = (d7 = (d6 = (location3 = location7).getZ()) - (d5 = (location2 = location8).getZ()))) * (d4 = d7));
        if (d15 > (d3 = patternThresholds.trMaxHSq())) {
            return false;
        }
        Location location11 = location7;
        double y = location11.getY();
        double d16 = Math.abs(y - (d2 = (location = location8).getY()));
        boolean bl = d16 <= (d = patternThresholds.trMaxV());
        return bl;
    }
}

