package studio.glace.glantielytratarget;

import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.glAntiElytraTarget;
import studio.glace.glantielytratarget.platform.runtime.RuntimeGate;

public final class LicenseBootstrap {
    private LicenseBootstrap() {
    }

    public static boolean completeIfGracePending(@NotNull PluginContext pluginContext) {
        long l;
        long l2;
        long l3;
        long l4 = RuntimeGate.bootReady(pluginContext);
        if (l4 == (l3 = 0L)) {
            return false;
        }
        long l5 = RuntimeGate.graceBootAllowed(pluginContext);
        if (l5 == (l2 = 0L)) {
            return false;
        }
        long l6 = RuntimeGate.graceExpired(pluginContext);
        if (l6 != (l = 0L)) {
            return false;
        }
        glAntiElytraTarget glAntiElytraTarget2 = pluginContext.plugin();
        glAntiElytraTarget glAntiElytraTarget3 = glAntiElytraTarget2;
        if (glAntiElytraTarget3 != null) {
            glAntiElytraTarget glAntiElytraTarget4 = glAntiElytraTarget2;
            return glAntiElytraTarget4.completeLicenseBootInternal();
        }
        return false;
    }
}

