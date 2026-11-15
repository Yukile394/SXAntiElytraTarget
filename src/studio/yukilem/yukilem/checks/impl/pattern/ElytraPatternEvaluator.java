package studio.yukilem.yukilem.checks.impl.pattern;

import java.util.Arrays;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternSession;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternThresholds;
import studio.yukilem.yukilem.libs.nixlib.collection.LongSampleQueue;

public final class ElytraPatternEvaluator {
    private ElytraPatternEvaluator() {
    }

    public static void evaluate(@NotNull Player player, @NotNull ElytraPatternSession elytraPatternSession, @NotNull ElytraPatternThresholds.PatternThresholds patternThresholds, @NotNull Consumer<Player> consumer) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        double[] dArray;
        if (!player.isOnline() || !player.isGliding()) {
            elytraPatternSession.intervals.clear();
            return;
        }
        int length = elytraPatternSession.scratch.length;
        int size = elytraPatternSession.intervals.size();
        int n6 = size;
        if (length < n6) {
            int n7 = size;
            double[] dArray2 = new double[n7];
            elytraPatternSession.scratch = dArray2;
        }
        LongSampleQueue longSampleQueue = elytraPatternSession.intervals;
        double[] dArray3 = elytraPatternSession.scratch;
        double[] dArray4 = dArray = longSampleQueue.copyAsDoublesInto(dArray3);
        int n8 = size;
        Arrays.sort(dArray4, 0, n8);
        int n9 = patternThresholds.reqSampTight();
        int n10 = size;
        int n11 = Math.min(n9, n10);
        int n12 = -1;
        int n13 = 0;
        while ((n5 = (n4 = ++n13) + (n3 = n11)) <= (n2 = size)) {
            int n14;
            double d;
            double d2;
            double d3;
            double d4;
            double[] dArray5 = dArray;
            int n15 = n13;
            int n16 = n11;
            int n17 = n15 + n16 - 1;
            double d5 = dArray5[n17];
            double d6 = d5;
            double[] dArray6 = dArray;
            int n18 = n13;
            double d7 = dArray6[n18];
            double d8 = d7;
            double d9 = d6 - d8;
            double d10 = patternThresholds.msRange();
            if (d9 > d10 || (d4 = d7) < (d3 = (double)patternThresholds.minClMs()) || (d2 = d5) > (d = (double)patternThresholds.maxClMs())) continue;
            n12 = n14 = n13;
            break;
        }
        if ((n = n12) < 0) {
            elytraPatternSession.intervals.clear();
            return;
        }
        elytraPatternSession.intervals.clear();
        consumer.accept(player);
    }
}

