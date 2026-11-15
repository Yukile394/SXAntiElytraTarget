package studio.glace.glantielytratarget.libs.nixlib.scheduler;

public interface ScheduledTask {
    public void cancel();

    public boolean isCancelled();

    public boolean isRunning();
}

