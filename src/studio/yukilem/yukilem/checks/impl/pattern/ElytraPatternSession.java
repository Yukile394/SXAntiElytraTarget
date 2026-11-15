package studio.yukilem.yukilem.checks.impl.pattern;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.collection.LongSampleQueue;

public final class ElytraPatternSession {
    public LongSampleQueue intervals;
    public double[] scratch;
    public long lastFireworkTime;
    public long lastHitTime;

    @NotNull
    public static ElytraPatternSession create(int n) {
        LongSampleQueue longSampleQueue;
        ElytraPatternSession elytraPatternSession;
        ElytraPatternSession elytraPatternSession2 = elytraPatternSession = new ElytraPatternSession();
        elytraPatternSession2.intervals = longSampleQueue = new LongSampleQueue(n);
        ElytraPatternSession elytraPatternSession3 = elytraPatternSession;
        double[] dArray = new double[n];
        elytraPatternSession3.scratch = dArray;
        ElytraPatternSession elytraPatternSession4 = elytraPatternSession;
        return elytraPatternSession4;
    }

    public static void resize(@NotNull ElytraPatternSession elytraPatternSession, int n) {
        long l;
        LongSampleQueue longSampleQueue;
        if (elytraPatternSession.scratch.length == n && elytraPatternSession.intervals.capacity() == n) {
            return;
        }
        elytraPatternSession.intervals = longSampleQueue = new LongSampleQueue(n);
        double[] dArray = new double[n];
        elytraPatternSession.scratch = dArray;
        elytraPatternSession.lastFireworkTime = l = 0L;
    }
}

