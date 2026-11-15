package studio.yukilem.yukilem.libs.nixlib.scheduler;

public interface ScheduledTask {
    public void cancel();

    public boolean isCancelled();

    public boolean isRunning();
}

