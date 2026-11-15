package studio.glace.glantielytratarget.platform.runtime;

import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.platform.runtime.RuntimeAuthority;

public final class RuntimeGate {
    private RuntimeGate() {
    }

    public static long detectionFold(@Nullable PluginContext pluginContext) {
        long l;
        long l2;
        if (pluginContext == null) {
            return 1L;
        }
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 == null) {
            return 2L;
        }
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        long l3 = runtimeAuthority3._co0();
        long l4 = l3;
        if (l4 != (l2 = 0L)) {
            long l5 = l3;
            return l5;
        }
        RuntimeAuthority runtimeAuthority4 = runtimeAuthority;
        long l6 = runtimeAuthority4._licensedFold0();
        long l7 = l6;
        if (l7 != (l = 0L)) {
            long l8 = l6;
            return l8;
        }
        RuntimeAuthority runtimeAuthority5 = runtimeAuthority;
        return runtimeAuthority5._inlineLicensedFold0();
    }

    public static long drift(@Nullable PluginContext pluginContext) {
        if (pluginContext == null) {
            return 1L;
        }
        return RuntimeGate.drift(pluginContext.runtime());
    }

    public static long drift(@Nullable RuntimeAuthority runtimeAuthority) {
        long l;
        if (runtimeAuthority == null) {
            return 1L;
        }
        long l2 = runtimeAuthority._peek0();
        long l3 = l2;
        if (l3 != (l = 0L)) {
            long l4 = l2;
            return l4;
        }
        return runtimeAuthority._pairLicensedFold0();
    }

    public static long operating(@Nullable PluginContext pluginContext) {
        long l;
        if (pluginContext == null) {
            return 1L;
        }
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 == null) {
            return 2L;
        }
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        long l2 = runtimeAuthority3._co0();
        long l3 = l2;
        if (l3 != (l = 0L)) {
            long l4 = l2;
            return l4;
        }
        RuntimeAuthority runtimeAuthority4 = runtimeAuthority;
        return runtimeAuthority4._pairLicensedFold0();
    }

    public static void nudge(@Nullable PluginContext pluginContext) {
        if (pluginContext == null) {
            return;
        }
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 != null) {
            RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
            runtimeAuthority3._nudge0();
        }
    }

    public static int wire(@Nullable RuntimeAuthority runtimeAuthority, int n) {
        if (runtimeAuthority == null) {
            return n ^ 0x7F7F;
        }
        return runtimeAuthority._wire0(n);
    }

    public static int pulse(@Nullable PluginContext pluginContext, int n) {
        if (pluginContext == null) {
            return n ^ 0x3C3C;
        }
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 == null) {
            return n ^ 0x3C3C;
        }
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        return runtimeAuthority3._pulse0(n);
    }

    public static long epoch(@Nullable RuntimeAuthority runtimeAuthority) {
        if (runtimeAuthority == null) {
            return 7L;
        }
        return runtimeAuthority._epoch0();
    }

    public static long gracePending(@Nullable RuntimeAuthority runtimeAuthority) {
        if (runtimeAuthority == null) {
            return 0L;
        }
        return runtimeAuthority._gracePending0();
    }

    public static long graceBootAllowed(@Nullable PluginContext pluginContext) {
        if (pluginContext == null) {
            return 0L;
        }
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 == null) {
            return 0L;
        }
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        return runtimeAuthority3._gracePending0();
    }

    public static long graceExpired(@Nullable PluginContext pluginContext) {
        if (pluginContext == null) {
            return 0L;
        }
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 == null) {
            return 0L;
        }
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        return runtimeAuthority3._graceExpired0();
    }

    public static long bootReady(@Nullable PluginContext pluginContext) {
        if (pluginContext == null) {
            return 1L;
        }
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 == null) {
            return 2L;
        }
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        return runtimeAuthority3._pairLicensedFold0();
    }

    public static long fold(@Nullable RuntimeAuthority runtimeAuthority, long l) {
        if (runtimeAuthority == null) {
            long l2 = 47824L;
            return l ^ l2;
        }
        long l3 = runtimeAuthority._px0();
        return l ^ l3;
    }
}

