package studio.yukilem.yukilem.checks.impl;

import org.jetbrains.annotations.NotNull;

public final class LegitSwapLogic {
    public static final long SWAP_PAIR_MAX_NS = 800000000L;
    static final int PLAYER_INV_SLOT_MIN = 9;
    static final int PLAYER_INV_SLOT_MAX = 44;
    static final int HOTBAR_CONTAINER_BASE = 36;

    private LegitSwapLogic() {
    }

    static boolean canArmFirstSwap(int n, int n2, int n3, boolean bl) {
        if (n2 < 0 || n2 > 8) {
            return false;
        }
        if (n < 9 || n > 44) {
            return false;
        }
        if (n2 != n3) {
            return false;
        }
        int n4 = 36 + n2;
        if (n == n4) {
            return false;
        }
        boolean bl2 = !bl;
        return bl2;
    }

    static boolean isMatchingReturnSwap(@NotNull SwapTrack swapTrack, int n, int n2, long l) {
        long l2;
        long l3;
        long l4;
        long l5;
        if (swapTrack.swapHotbarButton() < 0) {
            return false;
        }
        if (swapTrack.swapInvSlot() != n || swapTrack.swapHotbarButton() != n2) {
            return false;
        }
        int n3 = swapTrack.heldSlot();
        if (n2 != n3) {
            return false;
        }
        long l6 = swapTrack.swapFirstNs();
        boolean bl = l6 > (l5 = 0L) && (l4 = l - (l3 = swapTrack.swapFirstNs())) <= (l2 = 800000000L);
        return bl;
    }

    public static boolean shouldFlag(@NotNull SwapTrack swapTrack) {
        int n;
        long l;
        long l2;
        long l3;
        long l4;
        long l5;
        long l6;
        long l7;
        if (swapTrack.swapHotbarButton() < 0 || (l7 = swapTrack.swapFirstNs()) <= (l6 = 0L)) {
            return false;
        }
        long l8 = swapTrack.elytraBoostNs();
        if (l8 <= (l5 = 0L)) {
            return false;
        }
        long l9 = swapTrack.elytraBoostNs();
        long l10 = l9 - (l4 = swapTrack.swapFirstNs());
        long l11 = l10;
        if (l11 < (l3 = 0L) || (l2 = l10) > (l = 800000000L)) {
            return false;
        }
        int n2 = swapTrack.heldSlot();
        if (n2 != (n = swapTrack.swapHotbarButton())) {
            return false;
        }
        if (!swapTrack.baselineNonRocket()) {
            return false;
        }
        return swapTrack.returnSwapSeen();
    }

    public record SwapTrack(int heldSlot, int swapInvSlot, int swapHotbarButton, long swapFirstNs, boolean baselineNonRocket, long elytraBoostNs, boolean returnSwapSeen) {
        @NotNull
        static SwapTrack empty(int n) {
            long l = 0L;
            long l2 = 0L;
            SwapTrack swapTrack = new SwapTrack(n, -1, -1, l, false, l2, false);
            return swapTrack;
        }
    }
}

