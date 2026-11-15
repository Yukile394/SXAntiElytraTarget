package studio.yukilem.yukilem.checks.impl.fwinput;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.checks.impl.LegitSwapLogic;

public final class FWInputSession {
    public boolean valid = true;
    public int heldSlot = -1;
    public int swapInvSlot = -1;
    public int swapHotbarButton = -1;
    public long swapFirstNs;
    public boolean swapBaselineNonRocket;
    public long elytraBoostNs;
    public boolean returnSwapSeen;

    @NotNull
    public LegitSwapLogic.SwapTrack track(int n) {
        int n2 = this.swapInvSlot;
        int n3 = this.swapHotbarButton;
        long l = this.swapFirstNs;
        boolean bl = this.swapBaselineNonRocket;
        long l2 = this.elytraBoostNs;
        boolean bl2 = this.returnSwapSeen;
        LegitSwapLogic.SwapTrack swapTrack = new LegitSwapLogic.SwapTrack(n, n2, n3, l, bl, l2, bl2);
        return swapTrack;
    }
}

