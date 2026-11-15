package studio.glace.glantielytratarget.platform.runtime;

import java.util.Timer;

interface RuntimeIntegrityHost {
    public long resolveRuntimeFold();

    public void fatalIntegrity();

    public long artifactDrift();

    public long graceGate();

    public long epochSeal();

    public long sealMagic();

    public void renewVerify();

    public String currentClassHash();

    public String baselineClassHash();

    public void logSevere(String local1);

    public void cancelWatchTimers();

    public void setPrimaryWatchTimer(Timer local1);

    public void setSecondaryWatchTimer(Timer local1);

    public void setRenewTimer(Timer local1);
}

