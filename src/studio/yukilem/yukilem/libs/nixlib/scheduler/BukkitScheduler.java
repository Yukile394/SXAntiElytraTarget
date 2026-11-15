package studio.glace.glantielytratarget.libs.nixlib.scheduler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import studio.glace.glantielytratarget.libs.nixlib.scheduler.ScheduledTask;
import studio.glace.glantielytratarget.libs.nixlib.scheduler.Scheduler;

public final class BukkitScheduler
implements Scheduler {
    private final Plugin plugin;
    private final Set<BukkitWrapper> tracked = Collections.synchronizedSet(new HashSet());

    public BukkitScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin plugin() {
        return this.plugin;
    }

    @Override
    public boolean isRegionThreaded() {
        return false;
    }

    @Override
    public ScheduledTask runGlobal(Runnable runnable) {
        return this.wrap(Bukkit.getScheduler().runTask(this.plugin, runnable));
    }

    @Override
    public ScheduledTask runGlobalLater(Runnable runnable, long l) {
        return this.wrap(Bukkit.getScheduler().runTaskLater(this.plugin, runnable, Math.max(1L, l)));
    }

    @Override
    public ScheduledTask runGlobalTimer(Runnable runnable, long l, long l2) {
        return this.wrap(Bukkit.getScheduler().runTaskTimer(this.plugin, runnable, Math.max(1L, l), Math.max(1L, l2)));
    }

    @Override
    public ScheduledTask runAsync(Runnable runnable) {
        return this.wrap(Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable));
    }

    @Override
    public ScheduledTask runAsyncLater(Runnable runnable, long l) {
        return this.wrap(Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, Math.max(1L, l)));
    }

    @Override
    public ScheduledTask runAsyncTimer(Runnable runnable, long l, long l2) {
        return this.wrap(Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, Math.max(1L, l), Math.max(1L, l2)));
    }

    @Override
    public ScheduledTask runAt(Location location, Runnable runnable) {
        return this.runGlobal(runnable);
    }

    @Override
    public ScheduledTask runAtLater(Location location, Runnable runnable, long l) {
        return this.runGlobalLater(runnable, l);
    }

    @Override
    public ScheduledTask runAtTimer(Location location, Runnable runnable, long l, long l2) {
        return this.runGlobalTimer(runnable, l, l2);
    }

    @Override
    public ScheduledTask runFor(Entity entity, Runnable runnable) {
        return this.runGlobal(runnable);
    }

    @Override
    public ScheduledTask runForLater(Entity entity, Runnable runnable, long l) {
        return this.runGlobalLater(runnable, l);
    }

    @Override
    public ScheduledTask runForTimer(Entity entity, Consumer<ScheduledTask> consumer, long l, long l2) {
        BukkitTimerHandle bukkitTimerHandle = new BukkitTimerHandle();
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> consumer.accept(bukkitTimerHandle), Math.max(1L, l), Math.max(1L, l2));
        bukkitTimerHandle.attach(bukkitTask);
        this.tracked.add(bukkitTimerHandle);
        return bukkitTimerHandle;
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
        Set<BukkitWrapper> set = this.tracked;
        synchronized (set) {
            this.tracked.forEach(BukkitWrapper::cancel);
            this.tracked.clear();
        }
    }

    private BukkitWrapper wrap(BukkitTask bukkitTask) {
        BukkitWrapper bukkitWrapper = new BukkitWrapper(bukkitTask);
        this.tracked.add(bukkitWrapper);
        return bukkitWrapper;
    }

    private static class BukkitWrapper
    implements ScheduledTask {
        protected BukkitTask task;
        protected volatile boolean cancelled;

        BukkitWrapper(BukkitTask bukkitTask) {
            this.task = bukkitTask;
        }

        @Override
        public void cancel() {
            this.cancelled = true;
            if (this.task != null && !this.task.isCancelled()) {
                this.task.cancel();
            }
        }

        @Override
        public boolean isCancelled() {
            return this.cancelled || this.task != null && this.task.isCancelled();
        }

        @Override
        public boolean isRunning() {
            return this.task != null && !this.isCancelled();
        }
    }

    private static final class BukkitTimerHandle
    extends BukkitWrapper {
        BukkitTimerHandle() {
            super(null);
        }

        void attach(BukkitTask bukkitTask) {
            this.task = bukkitTask;
        }
    }
}

