package studio.yukilem.yukilem.libs.nixlib;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import studio.yukilem.yukilem.libs.nixlib.scheduler.Scheduler;
import studio.yukilem.yukilem.libs.nixlib.scheduler.SchedulerProvider;
import studio.yukilem.yukilem.libs.nixlib.version.ServerCapabilities;
import studio.yukilem.yukilem.libs.nixlib.version.ServerType;
import studio.yukilem.yukilem.libs.nixlib.version.ServerVersion;

public final class NixLib {
    private static volatile NixLib instance;
    private final Plugin plugin;
    private final Scheduler scheduler;
    private final ServerCapabilities capabilities;

    private NixLib(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = SchedulerProvider.create(plugin);
        this.capabilities = NixLib.detectCapabilities();
    }

    public static synchronized NixLib bootstrap(Plugin plugin) {
        if (instance != null) {
            throw new IllegalStateException("nixLib already bootstrapped by " + NixLib.instance.plugin.getName());
        }
        instance = new NixLib(plugin);
        NixLib.instance.scheduler.plugin().getLogger().info("nixLib started on " + String.valueOf(NixLib.instance.capabilities) + " (regionThreaded=" + NixLib.instance.scheduler.isRegionThreaded() + ")");
        return instance;
    }

    public static synchronized void shutdown() {
        if (instance == null) {
            return;
        }
        NixLib.instance.scheduler.cancelAll();
        instance = null;
    }

    public static NixLib get() {
        NixLib nixLib = instance;
        if (nixLib == null) {
            throw new IllegalStateException("nixLib not bootstrapped; call NixLib.bootstrap(plugin) in onEnable()");
        }
        return nixLib;
    }

    public static Logger logger() {
        return NixLib.get().plugin.getLogger();
    }

    public Plugin plugin() {
        return this.plugin;
    }

    public Scheduler scheduler() {
        return this.scheduler;
    }

    public ServerCapabilities capabilities() {
        return this.capabilities;
    }

    private static ServerCapabilities detectCapabilities() {
        ServerVersion serverVersion;
        ServerType serverType = ServerType.detect();
        try {
            serverVersion = ServerVersion.parse(Bukkit.getBukkitVersion());
        }
        catch (Throwable e) {
            serverVersion = ServerVersion.V1_20_6;
        }
        return new ServerCapabilities(serverType, serverVersion);
    }
}

