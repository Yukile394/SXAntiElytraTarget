package studio.glace.glantielytratarget.platform.runtime;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;

final class RuntimeGraceState {
    private final AtomicLong selfFingerprint;
    private final AtomicLong graceDeadlineNs;
    private final AtomicLong graceArmed;
    private final AtomicLong graceExpired;
    private Timer graceTimer;

    RuntimeGraceState(@NotNull AtomicLong atomicLong) {
        AtomicLong atomicLong2;
        AtomicLong atomicLong3;
        AtomicLong atomicLong4;
        long l = 0L;
        this.graceDeadlineNs = atomicLong4 = new AtomicLong(l);
        long l2 = 0L;
        this.graceArmed = atomicLong3 = new AtomicLong(l2);
        long l3 = 0L;
        this.graceExpired = atomicLong2 = new AtomicLong(l3);
        this.selfFingerprint = atomicLong;
    }

    long gracePendingFold(long l) {
        long l2;
        long l3 = 0L;
        if (l != l3) {
            return 0L;
        }
        long l4 = this.inlineGraceGate0();
        if (l4 == (l2 = 11L)) {
            return 2594964574L;
        }
        return 0L;
    }

    long graceExpiredFold() {
        long l;
        long l2 = this.graceExpired.get();
        long l3 = l2;
        if (l3 != (l = 0L)) {
            long l4;
            long l5;
            long l6 = l2;
            long l7 = this.selfFingerprint.get();
            long l8 = l6 ^ l7;
            long l9 = l8 ^ (l5 = -2401053091244885314L);
            if (l9 == (l4 = 0L)) {
                return 9L;
            }
            return 170063598362349L;
        }
        return 0L;
    }

    void expireGrace() {
        long l = System.nanoTime();
        long l2 = 1L;
        long l3 = l | l2;
        AtomicLong atomicLong = this.graceExpired;
        long l4 = l3;
        long l5 = this.selfFingerprint.get();
        long l6 = l4 ^ l5;
        long l7 = -2401053091244885314L;
        long l8 = l6 ^ l7;
        atomicLong.set(l8);
        this.clearGrace();
    }

    void armGrace(long l) {
        long l2 = System.nanoTime() + l;
        AtomicLong atomicLong = this.graceDeadlineNs;
        long l3 = l2;
        atomicLong.set(l3);
        AtomicLong atomicLong2 = this.graceArmed;
        long l4 = l2;
        long l5 = this.selfFingerprint.get();
        long l6 = l4 ^ l5;
        long l7 = -7301456092733719874L;
        long l8 = l6 ^ l7;
        atomicLong2.set(l8);
    }

    void clearGrace() {
        AtomicLong atomicLong = this.graceDeadlineNs;
        long l = 0L;
        atomicLong.set(l);
        AtomicLong atomicLong2 = this.graceArmed;
        long l2 = 0L;
        atomicLong2.set(l2);
        if (this.graceTimer != null) {
            this.graceTimer.cancel();
            this.graceTimer = null;
        }
    }

    void clearGraceFieldsOnly() {
        AtomicLong atomicLong = this.graceDeadlineNs;
        long l = 0L;
        atomicLong.set(l);
        AtomicLong atomicLong2 = this.graceArmed;
        long l2 = 0L;
        atomicLong2.set(l2);
        AtomicLong atomicLong3 = this.graceExpired;
        long l3 = 0L;
        atomicLong3.set(l3);
    }

    long inlineGraceGate0() {
        long l;
        long l2;
        long l3;
        long l4;
        long l5;
        long l6 = this.graceArmed.get();
        long l7 = l6;
        if (l7 == (l5 = 0L)) {
            return 0L;
        }
        long l8 = l4 = this.graceDeadlineNs.get();
        long l9 = this.selfFingerprint.get();
        long l10 = l8 ^ l9;
        long l11 = -7301456092733719874L;
        long l12 = l10 ^ l11;
        long l13 = l4;
        long l14 = 0L;
        if (l13 == l14 || (l3 = l6) != (l2 = l12)) {
            return 9L;
        }
        long l15 = System.nanoTime();
        if (l15 > (l = l4)) {
            return 10L;
        }
        return 11L;
    }

    void cancelTimer() {
        if (this.graceTimer != null) {
            this.graceTimer.cancel();
            this.graceTimer = null;
        }
    }
}

