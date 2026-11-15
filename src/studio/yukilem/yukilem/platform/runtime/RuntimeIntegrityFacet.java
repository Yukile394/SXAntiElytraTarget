package studio.yukilem.yukilem.platform.runtime;

import java.util.Timer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.platform.runtime.RuntimeAuthority;
import studio.yukilem.yukilem.platform.runtime.RuntimeIntegrityHost;

final class RuntimeIntegrityFacet
implements RuntimeIntegrityHost {
    private final RuntimeAuthority root;

    RuntimeIntegrityFacet(@NotNull RuntimeAuthority runtimeAuthority) {
        this.root = runtimeAuthority;
    }

    @Override
    public long resolveRuntimeFold() {
        return this.root._resolveRuntimeFold0();
    }

    @Override
    public void fatalIntegrity() {
        this.root._fatal0();
    }

    @Override
    public long artifactDrift() {
        return this.root._artifactDrift0();
    }

    @Override
    public long graceGate() {
        return this.root._graceGate0();
    }

    @Override
    public long epochSeal() {
        return this.root._epoch0();
    }

    @Override
    public long sealMagic() {
        return RuntimeAuthority._sealMagic0(883719069);
    }

    @Override
    public void renewVerify() {
        this.root._vx0();
        long l = this.root._pairLicensedFold0();
        long l2 = 0L;
        if (l != l2) {
            throw new IllegalStateException("renew");
        }
    }

    @Override
    @Nullable
    public String currentClassHash() {
        return this.root._ch0();
    }

    @Override
    @Nullable
    public String baselineClassHash() {
        return this.root._baselineClassHash();
    }

    @Override
    public void logSevere(@NotNull String string) {
        this.root._log().severe(string);
    }

    @Override
    public void cancelWatchTimers() {
        this.root._cancelWatchTimers0();
    }

    @Override
    public void setPrimaryWatchTimer(@NotNull Timer timer) {
        this.root._setPrimaryWatchTimer0(timer);
    }

    @Override
    public void setSecondaryWatchTimer(@NotNull Timer timer) {
        this.root._setSecondaryWatchTimer0(timer);
    }

    @Override
    public void setRenewTimer(@NotNull Timer timer) {
        this.root._setRenewTimer0(timer);
    }
}

