package studio.yukilem.yukilem.platform.runtime;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.platform.runtime.RuntimeIntegrityHost;

final class RuntimeIntegrityWatchdog {
    private static final long PRIMARY_MS = 45000L;
    private static final long SECONDARY_MS = 97000L;

    private RuntimeIntegrityWatchdog() {
    }

    static void start(@NotNull RuntimeIntegrityHost runtimeIntegrityHost) {
        runtimeIntegrityHost.cancelWatchTimers();
        Timer timer = RuntimeIntegrityWatchdog.schedulePrimary(runtimeIntegrityHost);
        runtimeIntegrityHost.setPrimaryWatchTimer(timer);
        Timer timer2 = RuntimeIntegrityWatchdog.scheduleSecondary(runtimeIntegrityHost);
        runtimeIntegrityHost.setSecondaryWatchTimer(timer2);
    }

    static void scheduleRenew(final @NotNull RuntimeIntegrityHost runtimeIntegrityHost, long l, final @NotNull Runnable runnable) {
        Timer timer;
        Timer timer2 = timer = new Timer("AET-Renew", true);
        TimerTask timerTask = new TimerTask(){

            @Override
            public void run() {
                try {
                    runtimeIntegrityHost.renewVerify();
                }
                catch (IllegalStateException e) {
                    IllegalStateException e2 = e;
                    runnable.run();
                }
            }
        };
        timer2.scheduleAtFixedRate(timerTask, l, l);
        Timer timer3 = timer;
        runtimeIntegrityHost.setRenewTimer(timer3);
    }

    static void selfPulse(@NotNull RuntimeIntegrityHost runtimeIntegrityHost, @NotNull AtomicLong atomicLong, long l, long l2) {
        long l3;
        String string;
        String string2;
        String string3;
        int n;
        long l4 = atomicLong.get();
        long l5 = l4;
        if (l2 - l5 < l) {
            return;
        }
        long l6 = l4;
        if (!atomicLong.compareAndSet(l6, l2)) {
            return;
        }
        String string4 = runtimeIntegrityHost.baselineClassHash();
        String string5 = string4;
        if (string5 == null) {
            return;
        }
        String string6 = runtimeIntegrityHost.currentClassHash();
        String string7 = string6;
        if (string7 == null) {
            return;
        }
        String string8 = string6;
        int n2 = string8.hashCode();
        if (n2 != (n = (string3 = string4).hashCode()) || !(string2 = string6).equals(string = string4)) {
            runtimeIntegrityHost.logSevere("Runtime integrity: class hash drift detected.");
            runtimeIntegrityHost.fatalIntegrity();
            return;
        }
        long l7 = runtimeIntegrityHost.artifactDrift();
        if (l7 != (l3 = 0L)) {
            runtimeIntegrityHost.logSevere("Runtime integrity: jar hash drift detected.");
            runtimeIntegrityHost.fatalIntegrity();
        }
    }

    private static Timer schedulePrimary(final @NotNull RuntimeIntegrityHost runtimeIntegrityHost) {
        Timer timer;
        Timer timer2 = timer = new Timer("AET-Watch", true);
        TimerTask timerTask = new TimerTask(){

            @Override
            public void run() {
                long l;
                long l2;
                long l3 = runtimeIntegrityHost.resolveRuntimeFold();
                if (l3 != (l2 = 0L)) {
                    runtimeIntegrityHost.logSevere("Runtime integrity: boot gate drift detected.");
                    runtimeIntegrityHost.fatalIntegrity();
                    return;
                }
                long l4 = runtimeIntegrityHost.artifactDrift();
                if (l4 != (l = 0L)) {
                    runtimeIntegrityHost.logSevere("Runtime integrity: artifact hash drift detected.");
                    runtimeIntegrityHost.fatalIntegrity();
                }
            }
        };
        long l = 45000L;
        long l2 = 45000L;
        timer2.scheduleAtFixedRate(timerTask, l, l2);
        Timer timer3 = timer;
        return timer3;
    }

    private static Timer scheduleSecondary(final @NotNull RuntimeIntegrityHost runtimeIntegrityHost) {
        Timer timer;
        Timer timer2 = timer = new Timer("AET-Watch2", true);
        TimerTask timerTask = new TimerTask(){

            @Override
            public void run() {
                long l;
                long l2;
                long l3;
                long l4;
                long l5;
                long l6;
                long l7 = runtimeIntegrityHost.resolveRuntimeFold();
                if (l7 != (l6 = 0L)) {
                    runtimeIntegrityHost.logSevere("Runtime integrity: inline gate drift detected.");
                    runtimeIntegrityHost.fatalIntegrity();
                    return;
                }
                long l8 = runtimeIntegrityHost.graceGate();
                long l9 = l8;
                if (l9 == (l5 = 10L) || (l4 = l8) == (l3 = 9L)) {
                    runtimeIntegrityHost.logSevere("Runtime integrity: license grace gate drift detected.");
                    runtimeIntegrityHost.fatalIntegrity();
                    return;
                }
                long l10 = runtimeIntegrityHost.epochSeal();
                long l11 = l10 ^ (l2 = runtimeIntegrityHost.sealMagic());
                if (l11 != (l = 0L)) {
                    runtimeIntegrityHost.logSevere("Runtime integrity: epoch seal drift detected.");
                    runtimeIntegrityHost.fatalIntegrity();
                }
            }
        };
        long l = 97000L;
        long l2 = 97000L;
        timer2.scheduleAtFixedRate(timerTask, l, l2);
        Timer timer3 = timer;
        return timer3;
    }
}

