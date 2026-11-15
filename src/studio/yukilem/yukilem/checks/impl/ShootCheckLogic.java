package studio.glace.glantielytratarget.checks.impl;

final class ShootCheckLogic {
    static final long ARM_EXPIRY_NS = 1000000000L;
    static final long SWING_RECONCILIATION_NS = 600000000L;
    static final long SWING_BEFORE_USE_NS = 250000000L;
    static final long INVENTORY_TRANSITION_NS = 250000000L;
    static final long CONFIRMED_MISS_STREAK_NS = 10000000000L;
    static final int REQUIRED_CONFIRMED_MISSES = 3;

    private ShootCheckLogic() {
    }

    static boolean shouldArm(boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        boolean bl6 = bl && !bl2 && bl3 && !bl4 && !bl5;
        return bl6;
    }

    static boolean swingMatchesUse(long l, long l2) {
        long l3;
        long l4;
        long l5;
        long l6;
        long l7 = 0L;
        boolean bl = l > l7 && l2 >= (l6 = l - (l5 = SWING_BEFORE_USE_NS)) && l2 <= (l4 = l + (l3 = SWING_RECONCILIATION_NS));
        return bl;
    }

    static boolean boostMatchesUse(long l, long l2) {
        long l3;
        long l4;
        long l5 = 0L;
        boolean bl = l > l5 && l2 >= l && (l4 = l2 - l) <= (l3 = 1000000000L);
        return bl;
    }

    static boolean reconciliationComplete(long l, long l2) {
        long l3;
        long l4;
        long l5 = 0L;
        boolean bl = l > l5 && (l4 = l2 - l) >= (l3 = SWING_RECONCILIATION_NS);
        return bl;
    }

    static boolean shouldFlag(boolean bl, boolean bl2, long l, long l2, int n) {
        boolean bl3 = !bl && bl2 && ShootCheckLogic.reconciliationComplete(l, l2) && n >= REQUIRED_CONFIRMED_MISSES;
        return bl3;
    }

    static boolean inventoryTransitionActive(long l, long l2) {
        long l3 = 0L;
        boolean bl = l > l3 && l2 < l;
        return bl;
    }

    static int nextConfirmedMissCount(int n, long l, long l2) {
        long l3;
        long l4;
        long l5 = 0L;
        if (l <= l5 || l2 < l || (l4 = l2 - l) > (l3 = 10000000000L)) {
            return 1;
        }
        return n + 1;
    }

    static boolean isArmExpired(long l, long l2) {
        long l3;
        long l4;
        long l5 = 0L;
        boolean bl = l <= l5 || (l4 = l2 - l) > (l3 = 1000000000L);
        return bl;
    }
}

