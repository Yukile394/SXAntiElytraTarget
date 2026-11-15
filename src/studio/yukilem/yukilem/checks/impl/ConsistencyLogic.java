package studio.glace.glantielytratarget.checks.impl;

final class ConsistencyLogic {
    static final int TRANSITION_SETTLE_TICKS = 2;

    private ConsistencyLogic() {
    }

    static boolean shouldTrackUseItem(boolean bl, boolean bl2) {
        boolean bl3 = bl && bl2;
        return bl3;
    }

    static boolean matchesCurrent(float f, float f2, float f3, float f4) {
        boolean bl = f == f3 && f2 == f4;
        return bl;
    }

    static boolean matchesPrevious(float f, float f2, float f3, float f4, boolean bl) {
        boolean bl2 = bl && f == f3 && f2 == f4;
        return bl2;
    }

    static int resumeUseAtSeq(int n) {
        return n + 2;
    }

    static boolean shouldIgnoreTransitionUse(int n, int n2) {
        boolean bl = n2 >= 0 && n < n2;
        return bl;
    }
}

