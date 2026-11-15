package studio.glace.glantielytratarget.libs.nixlib.scheduler;

import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import studio.glace.glantielytratarget.libs.nixlib.scheduler.ScheduledTask;

public interface Scheduler {
    public Plugin plugin();

    public ScheduledTask runGlobal(Runnable local1);

    public ScheduledTask runGlobalLater(Runnable local1, long local2);

    public ScheduledTask runGlobalTimer(Runnable local1, long local2, long l4);

    public ScheduledTask runAsync(Runnable local1);

    public ScheduledTask runAsyncLater(Runnable local1, long local2);

    public ScheduledTask runAsyncTimer(Runnable local1, long local2, long l4);

    public ScheduledTask runAt(Location local1, Runnable local2);

    public ScheduledTask runAtLater(Location local1, Runnable local2, long l3);

    public ScheduledTask runAtTimer(Location local1, Runnable local2, long l3, long l5);

    public ScheduledTask runFor(Entity local1, Runnable local2);

    public ScheduledTask runForLater(Entity local1, Runnable local2, long l3);

    public ScheduledTask runForTimer(Entity local1, Consumer<ScheduledTask> local2, long l3, long l5);

    public void cancelAll();

    public boolean isRegionThreaded();
}

