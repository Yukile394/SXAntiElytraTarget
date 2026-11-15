package studio.yukilem.yukilem.libs.nixlib.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.bukkit.plugin.Plugin;
import studio.yukilem.yukilem.libs.nixlib.scheduler.Scheduler;
import studio.yukilem.yukilem.libs.nixlib.version.ServerType;

public final class SchedulerProvider {
    private SchedulerProvider() {
    }

    public static Scheduler create(Plugin plugin) {
        if (ServerType.detect().isMultithreaded()) {
            return SchedulerProvider.tryInstantiate("studio.yukilem.yukilem.libs.nixlib.scheduler.FoliaScheduler", plugin).orElseGet(() -> SchedulerProvider.instantiateOrThrow("studio.yukilem.yukilem.libs.nixlib.scheduler.BukkitScheduler", plugin));
        }
        return SchedulerProvider.instantiateOrThrow("studio.yukilem.yukilem.libs.nixlib.scheduler.BukkitScheduler", plugin);
    }

    private static Optional<Scheduler> tryInstantiate(String string, Plugin plugin) {
        try {
            Class<?> clazz = Class.forName(string);
            return Optional.of((Scheduler)clazz.getConstructor(Plugin.class).newInstance(plugin));
        }
        catch (ClassNotFoundException e) {
            return Optional.empty();
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalStateException("failed to construct scheduler " + string, e);
        }
    }

    private static Scheduler instantiateOrThrow(String string, Plugin plugin) {
        return SchedulerProvider.tryInstantiate(string, plugin).orElseThrow(() -> new IllegalStateException("scheduler implementation not on classpath: " + string));
    }
}

