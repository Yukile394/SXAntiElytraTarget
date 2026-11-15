package studio.yukilem.yukilem.platform.runtime;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Optional;
import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import studio.yukilem.yukilem.platform.runtime.RuntimeFieldCodec;
import studio.yukilem.yukilem.platform.runtime.RuntimeGraceState;
import studio.yukilem.yukilem.platform.runtime.RuntimeIntegrityFacet;
import studio.yukilem.yukilem.platform.runtime.RuntimeIntegrityWatchdog;
import studio.yukilem.yukilem.platform.runtime.RuntimeJarDigest;
import studio.yukilem.yukilem.platform.runtime.RuntimeLazyRevive;
import studio.yukilem.yukilem.platform.runtime.RuntimeLicenseErrors;
import studio.yukilem.yukilem.platform.runtime.RuntimePayloadCrypto;
import studio.yukilem.yukilem.platform.runtime.RuntimeVerifyHttp;

public final class RuntimeAuthority {
    private static final long _SEAL_MAGIC = 3237998146L;
    private static final long _SELF_CHECK_PERIOD_NS = 30000000000L;
    private static final long _GRACE_WINDOW_NS = 60000000000L;
    private static final String _PROFILE_CALLER = "studio.yukilem.yukilem.platform.runtime.RuntimeProfile";
    private static final StackWalker _CALLER_WALKER = StackWalker.getInstance();
    private final String _key;
    private final Logger _log;
    private final File _artifact;
    private final AtomicReference<byte[]> _seed;
    private final AtomicLong _seedTokenRef;
    private final AtomicLong _selfFingerprint;
    private final AtomicLong _lastSelfCheckNanos;
    private final AtomicLong _lastReviveNanos;
    private final ThreadLocal<Integer> _inLazyVerify;
    private volatile String _hint;
    private volatile String _token;
    private volatile String _baselineHash;
    private volatile String _baselineClassHash;
    private volatile String _approvedClassHash;
    private volatile String _detectedIp;
    private Timer _reTimer;
    private Timer _wdTimer;
    private Timer _wdTimer2;
    private final AtomicReference<Runnable> _onFatalRef;
    private final AtomicLong _nudged;
    private final AtomicLong _bootGate;
    private final AtomicLong _bootEcho;
    private final AtomicLong _bootMark;
    private final AtomicLong _bootTrek;
    private final AtomicLong _bootEpoch;
    private final AtomicLong _bootCompleteArmed;
    private final RuntimeGraceState _grace;
    private final RuntimeIntegrityFacet _integrityFacet;

    public RuntimeAuthority(@NotNull String string, @NotNull Logger logger, @NotNull File file) {
        RuntimeIntegrityFacet runtimeIntegrityFacet;
        RuntimeGraceState runtimeGraceState;
        AtomicLong atomicLong;
        AtomicLong atomicLong2;
        AtomicLong atomicLong3;
        AtomicLong atomicLong4;
        AtomicLong atomicLong5;
        AtomicLong atomicLong6;
        AtomicLong atomicLong7;
        AtomicLong atomicLong8;
        AtomicLong atomicLong9;
        AtomicLong atomicLong10;
        AtomicLong atomicLong11;
        AtomicReference<byte[]> atomicReference = new AtomicReference<>(null);
        this._seed = atomicReference;
        long l = 0L;
        this._seedTokenRef = atomicLong11 = new AtomicLong(l);
        long l2 = 0L;
        this._selfFingerprint = atomicLong10 = new AtomicLong(l2);
        long l3 = 0L;
        this._lastSelfCheckNanos = atomicLong9 = new AtomicLong(l3);
        long l4 = 0L;
        this._lastReviveNanos = atomicLong8 = new AtomicLong(l4);
        ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);
        this._inLazyVerify = threadLocal;
        this._detectedIp = "UNKNOWN";
        AtomicReference<Runnable> atomicReference2 = new AtomicReference<>(null);
        this._onFatalRef = atomicReference2;
        long l5 = 0L;
        this._nudged = atomicLong7 = new AtomicLong(l5);
        long l6 = 0L;
        this._bootGate = atomicLong6 = new AtomicLong(l6);
        long l7 = 0L;
        this._bootEcho = atomicLong5 = new AtomicLong(l7);
        long l8 = 0L;
        this._bootMark = atomicLong4 = new AtomicLong(l8);
        long l9 = 0L;
        this._bootTrek = atomicLong3 = new AtomicLong(l9);
        long l10 = 0L;
        this._bootEpoch = atomicLong2 = new AtomicLong(l10);
        long l11 = 0L;
        this._bootCompleteArmed = atomicLong = new AtomicLong(l11);
        this._key = string;
        this._log = logger;
        this._artifact = file;
        AtomicLong atomicLong12 = this._selfFingerprint;
        long l12 = RuntimePayloadCrypto.keyFingerprint(string);
        atomicLong12.set(l12);
        AtomicLong atomicLong13 = this._selfFingerprint;
        this._grace = runtimeGraceState = new RuntimeGraceState(atomicLong13);
        this._integrityFacet = runtimeIntegrityFacet = new RuntimeIntegrityFacet(this);
        try {
            String string2;
            String string3 = string2 = this._ch0();
            if (string3 != null) {
                String string4;
                this._baselineClassHash = string4 = string2;
            }
        }
        catch (Throwable e) {
            Throwable e2 = e;
        }
    }

    public byte @Nullable [] _sx(int n, int n2) {
        int n3;
        byte[] bArray;
        long l;
        byte[] bArray2;
        long l2;
        long l3;
        long l4;
        if (this._callerFold0() != 0) {
            return null;
        }
        long l5 = this._resolveRuntimeFold0();
        if (l5 != (l4 = 0L)) {
            return null;
        }
        byte[] bArray3 = this._seed.get();
        byte[] bArray4 = bArray3;
        if (bArray4 == null) {
            this._lazyRevive();
            byte[] bArray5 = bArray3 = this._seed.get();
            if (bArray5 == null) {
                return null;
            }
        }
        if ((l3 = this._seedTokenRef.get()) != (l2 = this._kt0(bArray2 = bArray3))) {
            this._wipeSeedState();
            return null;
        }
        long l6 = this._resolveRuntimeFold0();
        if (l6 != (l = 0L)) {
            return null;
        }
        RuntimeIntegrityFacet runtimeIntegrityFacet = this._integrityFacet;
        AtomicLong atomicLong = this._lastSelfCheckNanos;
        long l7 = 30000000000L;
        long l8 = System.nanoTime();
        RuntimeIntegrityWatchdog.selfPulse(runtimeIntegrityFacet, atomicLong, l7, l8);
        if (n2 <= 0 || (bArray = bArray3).length == 0) {
            return null;
        }
        byte[] bArray6 = new byte[n2];
        int n4 = 0;
        while ((n3 = n4) < n2) {
            byte b;
            byte[] bArray7 = bArray6;
            int n5 = n4;
            byte[] bArray8 = bArray3;
            int n6 = n4++;
            int n7 = n + n6;
            byte[] bArray9 = bArray3;
            int length = bArray9.length;
            int n8 = n7 % length;
            bArray7[n5] = b = bArray8[n8];
        }
        byte[] bArray10 = bArray6;
        return bArray10;
    }

    public long _licensedFold0() {
        return 0L;
    }

    public long _pairLicensedFold0() {
        return 0L;
    }

    long _inlineLicensedFold0() {
        return 0L;
    }

    public long _listenerLatch0() {
        return 1L;
    }

    public void _fr0(@NotNull Runnable runnable) {
        boolean bl = this._onFatalRef.compareAndSet(null, runnable);
    }

    void _nudge0() {
        AtomicLong atomicLong = this._nudged;
        long l = 0L;
        long l2 = 1L;
        if (atomicLong.compareAndSet(l, l2)) {
            this._fatal0();
        }
    }

    public long _co0() {
        return 0L;
    }

    public long _peek0() {
        return 0L;
    }

    public long _gracePending0() {
        return 0L;
    }

    public long _graceExpired0() {
        return 0L;
    }

    public void _expireGrace0() {
        this._grace.expireGrace();
    }

    public void _armGrace0() {
        RuntimeGraceState runtimeGraceState = this._grace;
        long l = 60000000000L;
        runtimeGraceState.armGrace(l);
    }

    public void _clearGrace0() {
        this._grace.clearGrace();
    }

    public long _epoch0() {
        return 3237998146L;
    }

    public long _px0() {
        long l;
        long l2;
        long l3 = this._inlineDrift0();
        long l4 = l3 | (l2 = this._drift0());
        if (l4 != (l = 0L)) {
            long l5 = System.nanoTime();
            long l6 = 3735928559L;
            return l5 ^ l6;
        }
        long l7 = this._selfFingerprint.get();
        long l8 = 3953891065552621L;
        return l7 ^ l8;
    }

    public int _pulse0(int n) {
        long l;
        long l2 = this._resolveRuntimeFold0();
        long l3 = l2;
        if (l3 != (l = 0L)) {
            this._nudge0();
            long l4 = l2;
            long l5 = l4 >>> 16;
            long l6 = l2;
            long l7 = 65535L;
            long l8 = l6 & l7;
            int n2 = (int)(l5 ^ l8);
            return n ^ n2;
        }
        return n;
    }

    public long _seal0() {
        return 3237998146L;
    }

    public int _wire0(int n) {
        return n;
    }

    public void _vx0() {
        this._log.info("License Key Verified.");
    }

    public void _sh0() {
        long l;
        long l2 = this._resolveRuntimeFold0();
        if (l2 != (l = 0L)) {
            return;
        }
        RuntimeIntegrityWatchdog.start(this._integrityFacet);
    }

    public void _sr0(long l, @NotNull Runnable runnable) {
        boolean bl = this._onFatalRef.compareAndSet(null, runnable);
        RuntimeIntegrityFacet runtimeIntegrityFacet = this._integrityFacet;
        Runnable runnable2 = this::_fatal0;
        RuntimeIntegrityWatchdog.scheduleRenew(runtimeIntegrityFacet, l, runnable2);
    }

    @Nullable
    public String _ch0() {
        return RuntimeJarDigest.classHash(this._artifact);
    }

    public long _sa0() {
        if (this._seed.get() == null) {
            this._lazyRevive();
        }
        return this._sa0Direct();
    }

    public int _cl0(@NotNull ClassLoader classLoader) {
        try {
            Iterator iterator;
            Field field;
            Field field2 = field = ClassLoader.class.getDeclaredField("classes");
            field2.setAccessible(true);
            Field field3 = field;
            Vector vector = (Vector)field3.get(classLoader);
            int n = 0;
            Vector vector2 = vector;
            Iterator iterator2 = vector2.iterator();
            while ((iterator = iterator2).hasNext()) {
                Iterator iterator3 = iterator2;
                if (!((Class)iterator3.next()).getName().startsWith("studio.yukilem.yukilem")) continue;
                ++n;
            }
            int n2 = n;
            return n2;
        }
        catch (Exception e) {
            Exception e2 = e;
            return -1;
        }
    }

    public void shutdown() {
        if (this._reTimer != null) {
            this._reTimer.cancel();
            this._reTimer = null;
        }
        if (this._wdTimer != null) {
            this._wdTimer.cancel();
            this._wdTimer = null;
        }
        if (this._wdTimer2 != null) {
            this._wdTimer2.cancel();
            this._wdTimer2 = null;
        }
        this._grace.cancelTimer();
    }

    @NotNull
    public String _jh0() {
        return RuntimeJarDigest.jarHash(this._artifact);
    }

    @NotNull
    String _ri0() {
        return RuntimeVerifyHttp.resolvePublicIp();
    }

    long _resolveRuntimeFold0() {
        return 0L;
    }

    long _inlineGraceGate0() {
        return 0L;
    }

    long _graceGate0() {
        return 0L;
    }

    private long _bootCompleteFrom(long l, long l2, long l3, long l4, long l5) {
        long l6 = l ^ l2 ^ l3 ^ l4 ^ l5;
        long l7 = this._seedTokenRef.get();
        long l8 = l6 ^ l7;
        long l9 = 3392963630489325L;
        return l8 ^ l9;
    }

    long _inlineDrift0() {
        return 0L;
    }

    private long _drift0() {
        return 0L;
    }

    private long _epochFrom(long l, long l2, long l3, long l4) {
        long l5 = l ^ l2 ^ l3 ^ l4;
        long l6 = this._selfFingerprint.get();
        long l7 = l5 ^ l6;
        long l8 = 3953891065552621L;
        return l7 ^ l8;
    }

    private long _trekFrom(long l, long l2, long l3, long l4) {
        long l5 = l ^ l2 ^ l3 ^ l4;
        long l6 = this._selfFingerprint.get();
        long l7 = l5 ^ l6;
        long l8 = 134216896569069L;
        return l7 ^ l8;
    }

    private long _sa0Direct() {
        byte[] bArray;
        int n;
        int n2;
        int n3;
        byte[] bArray2;
        long l;
        byte[] bArray3 = this._seed.get();
        byte[] bArray4 = bArray3;
        if (bArray4 == null) {
            return 0L;
        }
        long l2 = this._seedTokenRef.get();
        if (l2 != (l = this._kt0(bArray2 = bArray3))) {
            this._wipeSeedState();
            return 0L;
        }
        long l3 = 0L;
        int n4 = 0;
        while ((n3 = n4) < (n2 = Math.min(8, n = (bArray = bArray3).length))) {
            long l4 = l3;
            byte[] bArray5 = bArray3;
            int n5 = n4;
            long l5 = bArray5[n5] & 0xFF;
            int n6 = n4++;
            int n7 = n6 * 8;
            long l6 = l5 << n7;
            l3 = l4 | l6;
        }
        long l7 = l3;
        return l7;
    }

    private long _gateFrom(long l) {
        long l2 = this._selfFingerprint.get();
        long l3 = l ^ l2;
        long l4 = 6840346602683415230L;
        return l3 ^ l4;
    }

    private void _ba0() {
        long l;
        long l2 = this._sa0Direct();
        long l3 = l2;
        if (l3 == (l = 0L)) {
            this._wipeBootMarks();
            return;
        }
        long l4 = l2;
        long l5 = this._gateFrom(l4);
        AtomicLong atomicLong = this._bootGate;
        long l6 = l5;
        atomicLong.set(l6);
        AtomicLong atomicLong2 = this._bootEcho;
        long l7 = l5;
        long l8 = -1L;
        long l9 = l7 ^ l8;
        long l10 = this._selfFingerprint.get();
        long l11 = l9 ^ l10;
        long l12 = 6840346602683415230L;
        long l13 = l11 ^ l12;
        atomicLong2.set(l13);
        AtomicLong atomicLong3 = this._bootTrek;
        long l14 = l5;
        long l15 = this._bootEcho.get();
        long l16 = this._bootMark.get();
        long l17 = this._seedTokenRef.get();
        long l18 = this._trekFrom(l14, l15, l16, l17);
        atomicLong3.set(l18);
    }

    private void _armSeed(@NotNull byte[] bArray) {
        this._seed.set(bArray);
        AtomicLong atomicLong = this._seedTokenRef;
        long l = this._kt0(bArray);
        atomicLong.set(l);
        AtomicLong atomicLong2 = this._bootMark;
        String string = this._key;
        long l2 = this._selfFingerprint.get();
        long l3 = RuntimePayloadCrypto.seedMark(bArray, string, l2);
        atomicLong2.set(l3);
        AtomicLong atomicLong3 = this._lastSelfCheckNanos;
        long l4 = System.nanoTime();
        atomicLong3.set(l4);
        this._ba0();
        long l5 = this._bootGate.get();
        long l6 = this._bootEcho.get();
        long l7 = this._bootMark.get();
        long l8 = this._bootTrek.get();
        AtomicLong atomicLong4 = this._bootEpoch;
        long l9 = l5;
        long l10 = l6;
        long l11 = l7;
        long l12 = l8;
        long l13 = this._epochFrom(l9, l10, l11, l12);
        atomicLong4.set(l13);
        AtomicLong atomicLong5 = this._bootCompleteArmed;
        long l14 = l5;
        long l15 = l6;
        long l16 = l7;
        long l17 = l8;
        long l18 = this._bootEpoch.get();
        long l19 = this._bootCompleteFrom(l14, l15, l16, l17, l18);
        atomicLong5.set(l19);
        long l20 = this._drift0();
        long l21 = this._inlineDrift0();
        long l22 = l20 | l21;
        long l23 = 0L;
        if (l22 != l23) {
            this._bf0();
        }
    }

    private void _bf0() {
        this._wipeBootMarks();
        this._grace.clearGraceFieldsOnly();
        this._seed.set(null);
        AtomicLong atomicLong = this._seedTokenRef;
        long l = 0L;
        atomicLong.set(l);
        AtomicLong atomicLong2 = this._nudged;
        long l2 = 0L;
        atomicLong2.set(l2);
    }

    private void _wipeBootMarks() {
        AtomicLong atomicLong = this._bootGate;
        long l = 0L;
        atomicLong.set(l);
        AtomicLong atomicLong2 = this._bootEcho;
        long l2 = 0L;
        atomicLong2.set(l2);
        AtomicLong atomicLong3 = this._bootMark;
        long l3 = 0L;
        atomicLong3.set(l3);
        AtomicLong atomicLong4 = this._bootTrek;
        long l4 = 0L;
        atomicLong4.set(l4);
        AtomicLong atomicLong5 = this._bootEpoch;
        long l5 = 0L;
        atomicLong5.set(l5);
        AtomicLong atomicLong6 = this._bootCompleteArmed;
        long l6 = 0L;
        atomicLong6.set(l6);
    }

    private void _wipeSeedState() {
        this._seed.set(null);
        AtomicLong atomicLong = this._seedTokenRef;
        long l = 0L;
        atomicLong.set(l);
        this._wipeBootMarks();
    }

    private void _requireResponse(@Nullable JSONObject jSONObject) {
        long l;
        long l2;
        long l3;
        long l4;
        if (jSONObject == null) {
            Logger logger = this._log;
            String string = this._resolveDetectedIp();
            String string2 = RuntimeLicenseErrors.message("NO_RESPONSE", string);
            logger.severe(string2);
            long l5 = 22L;
            this._abortBoot(l5);
        }
        if ((l4 = (l3 = RuntimePayloadCrypto.validResponseFold(jSONObject)) ^ (l2 = -3750763034362895579L)) != (l = 0L)) {
            String string = jSONObject.optString("error", "");
            String string3 = string;
            if (string3.isEmpty()) {
                string = "UNKNOWN";
            }
            Logger logger = this._log;
            String string4 = string;
            String string5 = this._resolveDetectedIp();
            String string6 = RuntimeLicenseErrors.message(string4, string5);
            logger.severe(string6);
            long l6 = 23L;
            this._abortBoot(l6);
        }
    }

    void _fatal0() {
        Runnable runnable;
        this._bf0();
        Runnable runnable2 = runnable = this._onFatalRef.get();
        if (runnable2 != null) {
            try {
                Runnable runnable3 = runnable;
                runnable3.run();
            }
            catch (Throwable e) {
                Throwable e2 = e;
            }
        }
    }

    private void _abortBoot(long l) {
        Runnable runnable;
        this._bf0();
        Runnable runnable2 = runnable = this._onFatalRef.get();
        if (runnable2 != null) {
            try {
                Runnable runnable3 = runnable;
                runnable3.run();
            }
            catch (Throwable e) {
                Throwable e2 = e;
            }
        }
        long l2 = this._selfFingerprint.get();
        String string = Long.toHexString(l ^ l2);
        IllegalStateException e = new IllegalStateException(string);
        throw e;
    }

    static long _sealMagic0(int n) {
        int n2 = 0x57C015BC ^ (0x69DB000C ^ (0x4C3E505D ^ n));
        n2 = 0x5EA42598 ^ n2;
        return 3237998146L;
    }

    @NotNull
    Logger _log() {
        return this._log;
    }

    @Nullable
    String _baselineClassHash() {
        return this._baselineClassHash;
    }

    void _cancelWatchTimers0() {
        if (this._wdTimer != null) {
            this._wdTimer.cancel();
            this._wdTimer = null;
        }
        if (this._wdTimer2 != null) {
            this._wdTimer2.cancel();
            this._wdTimer2 = null;
        }
    }

    void _setPrimaryWatchTimer0(@NotNull Timer timer) {
        this._wdTimer = timer;
    }

    void _setSecondaryWatchTimer0(@NotNull Timer timer) {
        this._wdTimer2 = timer;
    }

    void _setRenewTimer0(@NotNull Timer timer) {
        this._reTimer = timer;
    }

    int _inLazyVerify0() {
        return this._inLazyVerify.get();
    }

    void _setInLazyVerify(int n) {
        ThreadLocal<Integer> threadLocal = this._inLazyVerify;
        Integer n2 = n;
        threadLocal.set(n2);
    }

    long _lastReviveNanos0() {
        return this._lastReviveNanos.get();
    }

    boolean _compareAndSetReviveNanos(long l, long l2) {
        return this._lastReviveNanos.compareAndSet(l, l2);
    }

    private long _kt0(byte[] bArray) {
        String string = this._key;
        long l = this._selfFingerprint.get();
        return RuntimePayloadCrypto.seedTokenRef(bArray, string, l);
    }

    private int _callerFold0() {
        StackWalker stackWalker = _CALLER_WALKER;
        Function<Stream<StackWalker.StackFrame>, String> function = stream -> {
            long l = 2L;
            Optional<StackWalker.StackFrame> optional = stream.skip(l).findFirst();
            Function<StackWalker.StackFrame, String> getName = StackWalker.StackFrame::getClassName;
            return optional.map(getName).orElse(null);
        };
        String string = stackWalker.walk(function);
        String string2 = string;
        if (string2 == null) {
            return 32;
        }
        String string3 = string;
        int n = string3.equals(_PROFILE_CALLER) ? 0 : 33;
        return n;
    }

    private void _lazyRevive() {
        RuntimeLazyRevive.attempt(this);
    }

    long _artifactDrift0() {
        String string = this._baselineHash;
        File file = this._artifact;
        return RuntimeJarDigest.artifactDrift(string, file);
    }

    private void _bindApprovedClassHash(@NotNull JSONObject jSONObject, @NotNull String string) {
        String string2;
        String string3 = RuntimeFieldCodec.decode(RuntimeFieldCodec.FK_CLASS_HASH);
        String string4 = jSONObject.optString(string3, "");
        String string5 = string4;
        if (!string5.isEmpty() && !(string2 = string4).equals(string)) {
            this._log.severe("License verification failed: class hash rejected by server.");
            long l = 26L;
            this._abortBoot(l);
        }
        this._approvedClassHash = string;
    }

    @NotNull
    private String _resolveDetectedIp() {
        String string;
        String string2;
        String string3 = this._detectedIp;
        String string4 = string3;
        if (string4 == null || (string2 = string3).isEmpty() || "UNKNOWN".equals(string = string3)) {
            String string5;
            string3 = string5 = this._ri0();
            this._detectedIp = string5;
        }
        String string6 = string3;
        return string6;
    }
}

