package studio.glace.glantielytratarget.checks.impl.pattern;

import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.config.ChecksConfigurationService;
import studio.glace.glantielytratarget.platform.runtime.RuntimeAuthority;
import studio.glace.glantielytratarget.platform.runtime.RuntimeProfile;

public final class ElytraPatternThresholds {
    public static final int MAX_SAMPLE_CAPACITY = 64;

    private ElytraPatternThresholds() {
    }

    @NotNull
    public static PatternThresholds load(@NotNull PluginContext pluginContext) {
        long l;
        double d;
        int n;
        ChecksConfigurationService checksConfigurationService = pluginContext.checksConfig();
        String string = "heuristic-checks.elytra-pattern-check." + "target-track.";
        RuntimeAuthority runtimeAuthority = pluginContext.runtime();
        ChecksConfigurationService checksConfigurationService2 = checksConfigurationService;
        String string2 = "heuristic-checks.elytra-pattern-check." + "samp-count";
        int intVal = checksConfigurationService2.getInt(string2, 8);
        int n2 = intVal;
        if (n2 < 4) {
            intVal = 4;
        } else {
            int n3 = intVal;
            if (n3 > 64) {
                intVal = 64;
            }
        }
        int n4 = intVal;
        int n5 = n4 / 2 + 1;
        ChecksConfigurationService checksConfigurationService3 = checksConfigurationService;
        String string3 = "heuristic-checks.elytra-pattern-check." + "required-samp-tight";
        int n6 = n5;
        int n7 = n = checksConfigurationService3.getInt(string3, n6);
        if (n7 < 3) {
            n = 3;
        } else {
            int n8 = n;
            int n9 = intVal;
            if (n8 > n9) {
                int n10;
                n = n10 = intVal;
            }
        }
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        int n11 = n;
        int n12 = n;
        int n13 = n12 - 1;
        int n14 = Math.max(3, n13);
        int n15 = RuntimeProfile.foldInt(runtimeAuthority2, 4, n11, n14);
        ChecksConfigurationService checksConfigurationService4 = checksConfigurationService;
        String string4 = "heuristic-checks.elytra-pattern-check." + "ms-range";
        long l2 = 60L;
        long longVal = checksConfigurationService4.getLong(string4, l2);
        ChecksConfigurationService checksConfigurationService5 = checksConfigurationService;
        String string5 = "heuristic-checks.elytra-pattern-check." + "min-cl-ms";
        long l3 = 200L;
        long longVal2 = checksConfigurationService5.getLong(string5, l3);
        ChecksConfigurationService checksConfigurationService6 = checksConfigurationService;
        String string6 = "heuristic-checks.elytra-pattern-check." + "max-cl-ms";
        long l4 = 500L;
        long longVal3 = checksConfigurationService6.getLong(string6, l4);
        ChecksConfigurationService checksConfigurationService7 = checksConfigurationService;
        String string7 = "heuristic-checks.elytra-pattern-check." + "win-ms";
        long l5 = 10000L;
        long longVal4 = checksConfigurationService7.getLong(string7, l5);
        ChecksConfigurationService checksConfigurationService8 = checksConfigurationService;
        String string8 = string;
        String string9 = string8 + "look-deg-t";
        double d2 = 20.0;
        double d3 = d = checksConfigurationService8.getDouble(string9, d2);
        double d4 = 0.0;
        if (d3 < d4) {
            d = 0.0;
        } else {
            double d5 = d;
            double d6 = 89.0;
            if (d5 > d6) {
                d = 89.0;
            }
        }
        ChecksConfigurationService checksConfigurationService9 = checksConfigurationService;
        String string10 = string;
        String string11 = string10 + "win-sech";
        long l6 = 8L;
        long l7 = l = checksConfigurationService9.getLong(string11, l6);
        long l8 = 1L;
        if (l7 < l8) {
            l = 1L;
        }
        double d7 = d;
        double d8 = Math.toRadians(d7);
        double d9 = 1.0;
        ChecksConfigurationService checksConfigurationService10 = checksConfigurationService;
        String string12 = string;
        String string13 = string12 + "max-h-d";
        double d10 = 20.0;
        double double_ = checksConfigurationService10.getDouble(string13, d10);
        double d11 = Math.max(d9, double_);
        int n16 = intVal;
        int n17 = n15;
        long l9 = 1L;
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        long l10 = longVal;
        long l11 = 1L;
        long l12 = longVal;
        long l13 = 10L;
        long l14 = l12 - l13;
        long l15 = Math.max(l11, l14);
        long l16 = RuntimeProfile.foldLong(runtimeAuthority3, 5, l10, l15);
        long l17 = Math.max(l9, l16);
        long l18 = 0L;
        long l19 = longVal2;
        long l20 = Math.max(l18, l19);
        long l21 = 0L;
        long l22 = longVal2;
        long l23 = Math.max(l21, l22);
        long l24 = 1L;
        long l25 = l23 + l24;
        long l26 = longVal3;
        long l27 = Math.max(l25, l26);
        long l28 = 1L;
        long l29 = longVal4;
        long l30 = Math.max(l28, l29);
        ChecksConfigurationService checksConfigurationService11 = checksConfigurationService;
        String string14 = string;
        String string15 = string14 + "enabled";
        boolean boolean_ = checksConfigurationService11.getBoolean(string15, true);
        ChecksConfigurationService checksConfigurationService12 = checksConfigurationService;
        String string16 = string;
        String string17 = string16 + "require-hit";
        boolean boolean_2 = checksConfigurationService12.getBoolean(string17, true);
        long l31 = 1L;
        long l32 = l;
        long l33 = 1000L;
        long l34 = l32 * l33;
        long l35 = Math.max(l31, l34);
        ChecksConfigurationService checksConfigurationService13 = checksConfigurationService;
        String string18 = string;
        String string19 = string18 + "require-looking-at-target";
        boolean boolean_3 = checksConfigurationService13.getBoolean(string19, true);
        double d12 = d8;
        double d13 = Math.cos(d12);
        double d14 = d8;
        double d15 = Math.sin(d14);
        double d16 = d11;
        double d17 = d11;
        double d18 = d16 * d17;
        double d19 = 0.5;
        ChecksConfigurationService checksConfigurationService14 = checksConfigurationService;
        String string20 = string;
        String string21 = string20 + "max-v-d";
        double d20 = 10.0;
        double double_2 = checksConfigurationService14.getDouble(string21, d20);
        double d21 = Math.max(d19, double_2);
        PatternThresholds patternThresholds = new PatternThresholds(n16, n17, l17, l20, l27, l30, boolean_, boolean_2, l35, boolean_3, d13, d15, d18, d21);
        return patternThresholds;
    }

    public record PatternThresholds(int sampCount, int reqSampTight, long msRange, long minClMs, long maxClMs, long winMs, boolean trEnabled, boolean trReqHit, long trWinMs, boolean trReqLook, double trCosLookTol, double trSinLookTol, double trMaxHSq, double trMaxV) {
    }
}

