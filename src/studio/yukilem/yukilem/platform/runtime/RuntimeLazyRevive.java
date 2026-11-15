package studio.yukilem.yukilem.platform.runtime;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.platform.runtime.RuntimeAuthority;

final class RuntimeLazyRevive {
    private static final long REVIVE_COOLDOWN_NS = 300000000000L;

    private RuntimeLazyRevive() {
    }

    static void attempt(@NotNull RuntimeAuthority runtimeAuthority) {
        long l;
        long l2;
        long l3;
        long l4;
        long l5;
        long l6;
        long l7;
        long l8;
        if (runtimeAuthority._inLazyVerify0() != 0) {
            return;
        }
        long l9 = runtimeAuthority._pairLicensedFold0();
        if (l9 == (l8 = 0L) && (l7 = runtimeAuthority._resolveRuntimeFold0()) == (l6 = 0L)) {
            return;
        }
        long l10 = System.nanoTime();
        long l11 = runtimeAuthority._lastReviveNanos0();
        long l12 = l11;
        if (l12 != (l5 = 0L) && (l4 = (l3 = l10) - (l2 = l11)) < (l = 300000000000L)) {
            return;
        }
        long l13 = l11;
        long l14 = l10;
        if (!runtimeAuthority._compareAndSetReviveNanos(l13, l14)) {
            return;
        }
        runtimeAuthority._setInLazyVerify(1);
        try {
            try {
                runtimeAuthority._vx0();
                long l15 = runtimeAuthority._pairLicensedFold0();
                long l16 = 0L;
                if (l15 != l16) {
                    runtimeAuthority._fatal0();
                }
            }
            catch (Throwable e) {
                long l17 = runtimeAuthority._pairLicensedFold0();
                long l18 = 0L;
                if (l17 != l18) {
                    runtimeAuthority._fatal0();
                }
            }
        }
        finally {
            runtimeAuthority._setInLazyVerify(0);
        }
    }
}

