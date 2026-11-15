package studio.glace.glantielytratarget.libs.nixlib.scheduler;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import studio.glace.glantielytratarget.libs.nixlib.scheduler.ScheduledTask;
import studio.glace.glantielytratarget.libs.nixlib.scheduler.Scheduler;

public final class FoliaScheduler
implements Scheduler {
    private static final long MS_PER_TICK = 50L;
    private final Plugin plugin;
    private final GlobalRegionScheduler global;
    private final RegionScheduler region;
    private final AsyncScheduler async;
    private final Set<FoliaTask> tracked = Collections.synchronizedSet(new HashSet());

    public FoliaScheduler(Plugin plugin) {
        this.plugin = plugin;
        this.global = Bukkit.getGlobalRegionScheduler();
        this.region = Bukkit.getRegionScheduler();
        this.async = Bukkit.getAsyncScheduler();
    }

    @Override
    public Plugin plugin() {
        return this.plugin;
    }

    @Override
    public boolean isRegionThreaded() {
        return true;
    }

    @Override
    public ScheduledTask runGlobal(Runnable runnable) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.global.run(this.plugin, scheduledTask -> runnable.run());
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runGlobalLater(Runnable runnable, long l) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.global.runDelayed(this.plugin, scheduledTask -> runnable.run(), Math.max(1L, l));
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runGlobalTimer(Runnable runnable, long l, long l2) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.global.runAtFixedRate(this.plugin, scheduledTask -> runnable.run(), Math.max(1L, l), Math.max(1L, l2));
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runAsync(Runnable runnable) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.async.runNow(this.plugin, scheduledTask -> runnable.run());
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runAsyncLater(Runnable runnable, long l) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.async.runDelayed(this.plugin, scheduledTask -> runnable.run(), Math.max(1L, l) * 50L, TimeUnit.MILLISECONDS);
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runAsyncTimer(Runnable runnable, long l, long l2) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.async.runAtFixedRate(this.plugin, scheduledTask -> runnable.run(), Math.max(1L, l) * 50L, Math.max(1L, l2) * 50L, TimeUnit.MILLISECONDS);
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runAt(Location location, Runnable runnable) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.region.run(this.plugin, location, scheduledTask -> runnable.run());
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runAtLater(Location location, Runnable runnable, long l) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.region.runDelayed(this.plugin, location, scheduledTask -> runnable.run(), Math.max(1L, l));
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runAtTimer(Location location, Runnable runnable, long l, long l2) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = this.region.runAtFixedRate(this.plugin, location, scheduledTask -> runnable.run(), Math.max(1L, l), Math.max(1L, l2));
        return this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runFor(Entity entity, Runnable runnable) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = entity.getScheduler().run(this.plugin, scheduledTask -> runnable.run(), null);
        return scheduledTask2 == null ? FoliaScheduler.cancelled() : this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runForLater(Entity entity, Runnable runnable, long l) {
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask2 = entity.getScheduler().runDelayed(this.plugin, scheduledTask -> runnable.run(), null, Math.max(1L, l));
        return scheduledTask2 == null ? FoliaScheduler.cancelled() : this.track(new FoliaTask(scheduledTask2));
    }

    @Override
    public ScheduledTask runForTimer(Entity entity, Consumer<ScheduledTask> consumer, long l, long l2) {
        FoliaTask foliaTask = new FoliaTask(null);
        EntityScheduler scheduler = entity.getScheduler();
        io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask = scheduler.runAtFixedRate(this.plugin, arg_0 -> FoliaScheduler.runForTimerBody(consumer, foliaTask, arg_0), null, Math.max(1L, l), Math.max(1L, l2));
        if (scheduledTask == null) {
            return FoliaScheduler.cancelled();
        }
        foliaTask.bind(scheduledTask);
        return this.track(foliaTask);
    }

    @Override
    public void cancelAll() {
        this.global.cancelTasks(this.plugin);
        this.async.cancelTasks(this.plugin);
        Set<FoliaTask> set = this.tracked;
        synchronized (set) {
            this.tracked.forEach(FoliaTask::cancel);
            this.tracked.clear();
        }
    }

    private FoliaTask track(FoliaTask foliaTask) {
        this.tracked.add(foliaTask);
        return foliaTask;
    }

    private static ScheduledTask cancelled() {
        return new ScheduledTask(){

            @Override
            public void cancel() {
            }

            @Override
            public boolean isCancelled() {
                return true;
            }

            @Override
            public boolean isRunning() {
                return false;
            }
        };
    }

    private static /* synthetic */ void runForTimerBody(Consumer consumer, FoliaTask foliaTask, io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask) {
        consumer.accept(foliaTask);
    }

    private static final class FoliaTask
    implements ScheduledTask {
        private volatile io.papermc.paper.threadedregions.scheduler.ScheduledTask raw;
        private volatile boolean cancelled;

        FoliaTask(io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask) {
            this.raw = scheduledTask;
        }

        void bind(io.papermc.paper.threadedregions.scheduler.ScheduledTask scheduledTask) {
            this.raw = scheduledTask;
        }

        @Override
        public void cancel() {
            this.cancelled = true;
            if (this.raw != null) {
                this.raw.cancel();
            }
        }

        @Override
        public boolean isCancelled() {
            return this.cancelled;
        }

        @Override
        public boolean isRunning() {
            return this.raw != null && !this.cancelled;
        }
    }
}

