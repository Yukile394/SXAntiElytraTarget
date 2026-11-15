package studio.yukilem.yukilem.checks.impl;

final class SequenceLogic {
    static final long USE_SWAP_PAIR_MAX_NS = 25000000L;
    static final long BOOST_BEFORE_SWAP_MAX_NS = 500000000L;
    static final long BOOST_AFTER_SWAP_MAX_NS = 200000000L;

    private SequenceLogic() {
    }

    static boolean shouldArm(boolean bl, boolean bl2, boolean bl3) {
        boolean bl4 = bl && !bl2 && bl3;
        return bl4;
    }

    static boolean isArmExpired(long l, long l2) {
        long l3;
        long l4;
        long l5 = 0L;
        boolean bl = l <= l5 || (l4 = l2 - l) > (l3 = 25000000L);
        return bl;
    }

    static boolean swapCompletesPair(int n, int n2, long l, long l2) {
        long l3;
        long l4;
        if (n < 0 || n != n2) {
            return false;
        }
        long l5 = 0L;
        if (l <= l5) {
            return false;
        }
        long l6 = l2 - l;
        long l7 = l6;
        long l8 = 0L;
        boolean bl = l7 >= l8 && (l4 = l6) <= (l3 = 25000000L);
        return bl;
    }

    static boolean pairsWithBoost(long l, long l2) {
        long l3;
        long l4;
        long l5;
        long l6 = 0L;
        if (l <= l6 || l2 <= (l5 = 0L)) {
            return false;
        }
        long l7 = l - l2;
        long l8 = l7;
        long l9 = -200000000L;
        boolean bl = l8 >= l9 && (l4 = l7) <= (l3 = 500000000L);
        return bl;
    }
}

