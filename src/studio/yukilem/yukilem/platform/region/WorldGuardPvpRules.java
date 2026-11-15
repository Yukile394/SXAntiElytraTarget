package studio.glace.glantielytratarget.platform.region;

import org.jetbrains.annotations.Nullable;

final class WorldGuardPvpRules {
    private WorldGuardPvpRules() {
    }

    static boolean pvpDenied(boolean bl, boolean bl2, @Nullable Boolean bl3, boolean bl4) {
        if (!bl) {
            return true;
        }
        if (bl4) {
            return true;
        }
        if (bl2) {
            Boolean bl5 = Boolean.TRUE;
            boolean bl6 = bl3 != bl5;
            return bl6;
        }
        Boolean bl7 = Boolean.FALSE;
        boolean bl8 = bl3 == bl7;
        return bl8;
    }
}

