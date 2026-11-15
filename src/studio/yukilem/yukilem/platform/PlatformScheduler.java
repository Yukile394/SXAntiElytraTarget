package studio.yukilem.yukilem.platform;

import java.util.logging.Logger;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.libs.nixlib.scheduler.ScheduledTask;
import studio.yukilem.yukilem.libs.nixlib.scheduler.Scheduler;
import studio.yukilem.yukilem.libs.nixlib.scheduler.SchedulerProvider;

public final class PlatformScheduler {
    private final JavaPlugin plugin;
    private final Scheduler scheduler;

    public PlatformScheduler(@NotNull JavaPlugin javaPlugin) {
        Scheduler scheduler;
        this.plugin = javaPlugin;
        this.scheduler = scheduler = SchedulerProvider.create(javaPlugin);
    }

    @NotNull
    public JavaPlugin plugin() {
        return this.plugin;
    }

    public boolean isFolia() {
        return this.scheduler.isRegionThreaded();
    }

    @NotNull
    public Scheduler scheduler() {
        return this.scheduler;
    }

    public void runAsync(@NotNull Runnable runnable) {
        Scheduler scheduler = this.scheduler;
        Runnable runnable2 = () -> this.safeRun(runnable);
        ScheduledTask scheduledTask = scheduler.runAsync(runnable2);
    }

    public void runGlobal(@NotNull Runnable runnable) {
        Scheduler scheduler = this.scheduler;
        Runnable runnable2 = () -> this.safeRun(runnable);
        ScheduledTask scheduledTask = scheduler.runGlobal(runnable2);
    }

    public void runEntity(@Nullable Entity entity, @NotNull Runnable runnable) {
        if (entity == null) {
            this.runGlobal(runnable);
            return;
        }
        Scheduler scheduler = this.scheduler;
        Runnable runnable2 = () -> this.safeRun(runnable);
        ScheduledTask scheduledTask = scheduler.runFor(entity, runnable2);
    }

    public void runEntityLater(@Nullable Entity entity, @NotNull Runnable runnable, long l) {
        if (entity == null) {
            this.runGlobalLater(runnable, l);
            return;
        }
        Scheduler scheduler = this.scheduler;
        Runnable runnable2 = () -> this.safeRun(runnable);
        ScheduledTask scheduledTask = scheduler.runForLater(entity, runnable2, l);
    }

    public void runGlobalLater(@NotNull Runnable runnable, long l) {
        Scheduler scheduler = this.scheduler;
        Runnable runnable2 = () -> this.safeRun(runnable);
        ScheduledTask scheduledTask = scheduler.runGlobalLater(runnable2, l);
    }

    @NotNull
    public ScheduledTask runRepeating(@NotNull Runnable runnable, long l, long l2) {
        Scheduler scheduler = this.scheduler;
        Runnable runnable2 = () -> this.safeRun(runnable);
        return scheduler.runGlobalTimer(runnable2, l, l2);
    }

    public void shutdown() {
        try {
            this.scheduler.cancelAll();
        }
        catch (Exception e) {
            Exception e2 = e;
        }
    }

    private void safeRun(@NotNull Runnable runnable) {
        try {
            runnable.run();
        }
        catch (Throwable e) {
            Throwable e2 = e;
            Logger logger = this.plugin.getLogger();
            Throwable e3 = e2;
            String string = "Scheduled task failed: " + e3.getMessage();
            logger.warning(string);
        }
    }
}

