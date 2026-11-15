package studio.yukilem.yukilem;

import com.github.retrooper.packetevents.PacketEvents;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import lombok.Generated;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.bukkit.Metrics;
import studio.yukilem.yukilem.LicensedBootCoordinator;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.bootstrap.ServiceRegistry;
import studio.yukilem.yukilem.commands.PluginCommandRouter;
import studio.yukilem.yukilem.commands.SupportCommand;
import studio.yukilem.yukilem.commands.SupportCommandPacketGuard;
import studio.yukilem.yukilem.config.ChecksConfigurationService;
import studio.yukilem.yukilem.config.ConfigurationService;
import studio.yukilem.yukilem.config.PunishmentsConfigurationService;
import studio.yukilem.yukilem.config.WebhookConfigurationService;
import studio.yukilem.yukilem.config.settings.CommandSettings;
import studio.yukilem.yukilem.libs.nixlib.command.CommandRegistrar;
import studio.yukilem.yukilem.libs.nixlib.command.CommandSpec;
import studio.yukilem.yukilem.listener.CommandPriorityListener;
import studio.yukilem.yukilem.listener.DamageListener;
import studio.yukilem.yukilem.listener.ElytraBoostListener;
import studio.yukilem.yukilem.listener.FireworkListener;
import studio.yukilem.yukilem.listener.PlayerLifecycleListener;
import studio.yukilem.yukilem.listener.RegionProtectionListener;
import studio.yukilem.yukilem.packet.PacketDispatcher;
import studio.yukilem.yukilem.platform.BedrockPlatformProbe;
import studio.yukilem.yukilem.platform.runtime.RuntimeAuthority;
import studio.yukilem.yukilem.platform.runtime.RuntimeGate;
import studio.yukilem.yukilem.update.UpdateChecker;

public final class glAntiElytraTarget
extends JavaPlugin {
    private static final long INIT_SEAL = 3237998146L;
    private static final long GRACE_SEAL = 2594964574L;
    private static glAntiElytraTarget instance;
    private PluginContext context;
    private ServiceRegistry services;
    private PacketDispatcher packetDispatcher;
    private SupportCommand supportCommand;
    private SupportCommandPacketGuard supportCommandPacketGuard;
    private CommandSpec mainCommandSpec;
    private LicensedBootCoordinator bootCoordinator;
    private final AtomicLong listenerLatch;
    private final AtomicBoolean licenseVerifyInFlight;
    private final AtomicBoolean shutdownStarted;
    private final Object licenseBootLock;

    public glAntiElytraTarget() {
        Object object;
        AtomicBoolean atomicBoolean;
        AtomicBoolean atomicBoolean2;
        AtomicLong atomicLong;
        long l = 0L;
        this.listenerLatch = atomicLong = new AtomicLong(l);
        this.licenseVerifyInFlight = atomicBoolean2 = new AtomicBoolean(false);
        this.shutdownStarted = atomicBoolean = new AtomicBoolean(false);
        this.licenseBootLock = object = new Object();
    }

    @Override
    public void onEnable() {
        long l;
        LicensedBootCoordinator licensedBootCoordinator;
        instance = this;
        AtomicBoolean atomicBoolean = this.licenseVerifyInFlight;
        Object object = this.licenseBootLock;
        this.bootCoordinator = licensedBootCoordinator = new LicensedBootCoordinator(this, atomicBoolean, object);
        if (!this.isPacketEventsReady()) {
            this.getLogger().severe("PacketEvents plugin not found or not initialized. glAntiElytraTarget relies on the PacketEvents plugin installed on the server. Install PacketEvents and restart. Disabling.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        new Metrics(this, 30688);
        long l2 = l = this.initializeComponents();
        long l3 = 2594964574L;
        long l4 = l2 ^ l3;
        long l5 = 0L;
        if (l4 == l5) {
            this.registerCommands();
            return;
        }
        long l6 = l;
        long l7 = 3237998146L;
        long l8 = l6 ^ l7;
        long l9 = 0L;
        if (l8 != l9) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.registerListeners();
        this.registerCommands();
        long l10 = this.finalizeLicensedBoot();
        long l11 = 0L;
        if (l10 != l11) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (BedrockPlatformProbe.isGeyserAvailable(5791842) || BedrockPlatformProbe.isFloodgateAvailable(1490920131)) {
            this.getLogger().info("Geyser/Floodgate detected. Bedrock players will be bypassed.");
        }
        this.getLogger().info("All components initialized successfully!");
    }

    @Override
    public void onDisable() {
        Thread thread;
        Thread thread2;
        ServiceRegistry serviceRegistry;
        CommandSpec commandSpec;
        if (!this.shutdownStarted.compareAndSet(false, true)) {
            return;
        }
        PacketDispatcher packetDispatcher = this.packetDispatcher;
        this.packetDispatcher = null;
        SupportCommandPacketGuard supportCommandPacketGuard = this.supportCommandPacketGuard;
        this.supportCommandPacketGuard = null;
        SupportCommand supportCommand = this.supportCommand;
        this.supportCommand = null;
        CommandSpec commandSpec2 = this.mainCommandSpec;
        this.mainCommandSpec = null;
        ServiceRegistry serviceRegistry2 = this.services;
        this.services = null;
        SupportCommand supportCommand2 = supportCommand;
        if (supportCommand2 != null) {
            try {
                SupportCommand supportCommand3 = supportCommand;
                supportCommand3.unregister();
            }
            catch (Throwable e) {
                Throwable e2;
                Throwable e3 = e2 = e;
                this.logShutdownFailure("supportCommand", e3);
            }
        }
        if ((commandSpec = commandSpec2) != null) {
            try {
                CommandSpec commandSpec3 = commandSpec2;
                CommandRegistrar.unregister(this, commandSpec3);
            }
            catch (Throwable e) {
                Throwable e4;
                Throwable e5 = e4 = e;
                this.logShutdownFailure("mainCommand", e5);
            }
        }
        if ((serviceRegistry = serviceRegistry2) != null) {
            try {
                ServiceRegistry serviceRegistry3 = serviceRegistry2;
                serviceRegistry3.shutdownMainThread();
            }
            catch (Throwable e) {
                Throwable e6;
                Throwable e7 = e6 = e;
                this.logShutdownFailure("mainThreadServices", e7);
            }
        }
        PacketDispatcher packetDispatcher2 = packetDispatcher;
        SupportCommandPacketGuard supportCommandPacketGuard2 = supportCommandPacketGuard;
        ServiceRegistry serviceRegistry4 = serviceRegistry2;
        Runnable runnable = () -> this.runShutdownCleanup(packetDispatcher2, supportCommandPacketGuard2, serviceRegistry4);
        Thread thread3 = thread2 = (thread = new Thread(runnable, "AET-PluginShutdown"));
        thread3.setDaemon(true);
        Thread thread4 = thread2;
        thread4.start();
    }

    private void runShutdownCleanup(@Nullable PacketDispatcher packetDispatcher, @Nullable SupportCommandPacketGuard supportCommandPacketGuard, @Nullable ServiceRegistry serviceRegistry) {
        if (packetDispatcher != null) {
            try {
                PacketEvents.getAPI().getEventManager().unregisterListener(packetDispatcher);
            }
            catch (Throwable e) {
                Throwable e2;
                Throwable e3 = e2 = e;
                this.logShutdownFailure("packetDispatcher", e3);
            }
        }
        if (supportCommandPacketGuard != null) {
            try {
                PacketEvents.getAPI().getEventManager().unregisterListener(supportCommandPacketGuard);
            }
            catch (Throwable e) {
                Throwable e4;
                Throwable e5 = e4 = e;
                this.logShutdownFailure("supportCommandPacketGuard", e5);
            }
        }
        if (serviceRegistry != null) {
            try {
                serviceRegistry.shutdownAll();
            }
            catch (Throwable e) {
                Throwable e6;
                Throwable e7 = e6 = e;
                this.logShutdownFailure("services", e7);
            }
        }
        this.getLogger().info("Shutdown cleanup completed.");
    }

    private void logShutdownFailure(@NotNull String string, @NotNull Throwable e) {
        Logger logger = this.getLogger();
        String simpleName = e.getClass().getSimpleName();
        String string2 = String.valueOf(e.getMessage());
        String string3 = "Failed to shut down " + string + ": " + simpleName + ": " + string2;
        logger.warning(string3);
    }

    boolean completeLicenseBootInternal() {
        return this.bootCoordinator.completeFromReload();
    }

    @NotNull
    PluginContext context() {
        return this.context;
    }

    @Nullable
    ServiceRegistry services() {
        return this.services;
    }

    void setServices(@NotNull ServiceRegistry serviceRegistry) {
        this.services = serviceRegistry;
    }

    boolean isShutdownStarted() {
        return this.shutdownStarted.get();
    }

    void setPacketDispatcher(@NotNull PacketDispatcher packetDispatcher) {
        this.packetDispatcher = packetDispatcher;
    }

    @Nullable
    SupportCommand supportCommand() {
        return this.supportCommand;
    }

    void setSupportCommand(@NotNull SupportCommand supportCommand) {
        this.supportCommand = supportCommand;
    }

    @Nullable
    SupportCommandPacketGuard supportCommandPacketGuard() {
        return this.supportCommandPacketGuard;
    }

    void setSupportCommandPacketGuard(@NotNull SupportCommandPacketGuard supportCommandPacketGuard) {
        this.supportCommandPacketGuard = supportCommandPacketGuard;
    }

    @NotNull
    File artifactFile() {
        return this.getFile();
    }

    @NotNull
    ClassLoader pluginClassLoader() {
        return this.getClassLoader();
    }

    void registerListeners() {
        long l;
        long l2;
        long l3;
        long l4 = RuntimeGate.bootReady(this.context);
        long l5 = l4 | (l3 = this.inlineBootFold());
        if (l5 != (l2 = 0L)) {
            return;
        }
        RuntimeAuthority runtimeAuthority = this.context.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 == null) {
            return;
        }
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
        long l6 = runtimeAuthority3._listenerLatch0();
        long l7 = this.listenerLatch.get();
        if (l7 == (l = l6)) {
            return;
        }
        PluginManager pluginManager = this.getServer().getPluginManager();
        PluginContext pluginContext = this.context;
        FireworkListener fireworkListener = new FireworkListener(pluginContext);
        pluginManager.registerEvents(fireworkListener, this);
        PluginManager pluginManager2 = this.getServer().getPluginManager();
        PluginContext pluginContext2 = this.context;
        PlayerLifecycleListener playerLifecycleListener = new PlayerLifecycleListener(pluginContext2);
        pluginManager2.registerEvents(playerLifecycleListener, this);
        PluginManager pluginManager3 = this.getServer().getPluginManager();
        PluginContext pluginContext3 = this.context;
        RegionProtectionListener regionProtectionListener = new RegionProtectionListener(pluginContext3);
        pluginManager3.registerEvents(regionProtectionListener, this);
        PluginManager pluginManager4 = this.getServer().getPluginManager();
        PluginContext pluginContext4 = this.context;
        DamageListener damageListener = new DamageListener(pluginContext4);
        pluginManager4.registerEvents(damageListener, this);
        PluginManager pluginManager5 = this.getServer().getPluginManager();
        PluginContext pluginContext5 = this.context;
        ElytraBoostListener elytraBoostListener = new ElytraBoostListener(pluginContext5);
        pluginManager5.registerEvents(elytraBoostListener, this);
        PluginManager pluginManager6 = this.getServer().getPluginManager();
        PluginContext pluginContext6 = this.context;
        CommandPriorityListener commandPriorityListener = new CommandPriorityListener(pluginContext6);
        pluginManager6.registerEvents(commandPriorityListener, this);
        AtomicLong atomicLong = this.listenerLatch;
        long l8 = l6;
        atomicLong.set(l8);
        long l9 = RuntimeGate.bootReady(this.context);
        long l10 = this.inlineBootFold();
        long l11 = l9 | l10;
        long l12 = 0L;
        if (l11 != l12) {
            return;
        }
    }

    void registerCommands() {
        if (this.context.commandSettings() != null) {
            CommandSpec commandSpec;
            PluginContext pluginContext = this.context;
            PluginCommandRouter pluginCommandRouter = new PluginCommandRouter(pluginContext);
            CommandSpec commandSpec2 = this.mainCommandSpec = (commandSpec = pluginCommandRouter.spec());
            boolean bl = CommandRegistrar.register(this, commandSpec2);
        }
        if (this.context.config().isSupportCommandEnabled()) {
            if (this.supportCommand == null) {
                SupportCommand supportCommand;
                PluginContext pluginContext = this.context;
                this.supportCommand = supportCommand = new SupportCommand(pluginContext);
            }
            this.supportCommand.register();
        }
    }

    long finalizeLicensedBoot() {
        long l;
        long l2;
        long l3;
        RuntimeAuthority runtimeAuthority = this.context.runtime();
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        if (runtimeAuthority2 != null) {
            long l4;
            long l5;
            long l6;
            long l7;
            long l8 = RuntimeGate.detectionFold(this.context);
            long l9 = l8 | (l7 = RuntimeGate.drift(this.context));
            if (l9 != (l6 = 0L)) {
                return 14L;
            }
            RuntimeAuthority runtimeAuthority3 = runtimeAuthority;
            long l10 = RuntimeGate.epoch(runtimeAuthority3);
            long l11 = l10 ^ (l5 = 3237998146L);
            if (l11 != (l4 = 0L)) {
                return 13L;
            }
        }
        if ((l3 = (l2 = RuntimeGate.bootReady(this.context))) != (l = 0L)) {
            long l12 = l2;
            return l12;
        }
        if (this.context.config().getUpdateChecker().enabled() && this.context.config().getUpdateChecker().checkOnEnable()) {
            this.checkForUpdates();
        }
        return 0L;
    }

    private boolean isPacketEventsReady() {
        if (this.getServer().getPluginManager().getPlugin("packetevents") == null) {
            return false;
        }
        try {
            boolean bl = PacketEvents.getAPI() != null;
            return bl;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return false;
        }
    }

    private long initializeComponents() {
        String string;
        ConfigurationService configurationService;
        block5: {
            block4: {
                try {
                    PunishmentsConfigurationService punishmentsConfigurationService;
                    ChecksConfigurationService checksConfigurationService;
                    WebhookConfigurationService webhookConfigurationService;
                    ServiceRegistry serviceRegistry;
                    PluginContext pluginContext;
                    this.context = pluginContext = new PluginContext(this);
                    Logger logger = this.getLogger();
                    this.services = serviceRegistry = new ServiceRegistry(logger);
                    WebhookConfigurationService webhookConfigurationService2 = webhookConfigurationService = new WebhookConfigurationService(this);
                    webhookConfigurationService2.initialize();
                    PluginContext pluginContext2 = this.context;
                    WebhookConfigurationService webhookConfigurationService3 = webhookConfigurationService;
                    pluginContext2.setWebhookConfig(webhookConfigurationService3);
                    ConfigurationService configurationService2 = configurationService = new ConfigurationService(this);
                    configurationService2.initialize();
                    PluginContext pluginContext3 = this.context;
                    ConfigurationService configurationService3 = configurationService;
                    pluginContext3.setConfig(configurationService3);
                    PluginContext pluginContext4 = this.context;
                    ConfigurationService configurationService4 = configurationService;
                    CommandSettings commandSettings = CommandSettings.load(configurationService4.view());
                    pluginContext4.setCommandSettings(commandSettings);
                    ChecksConfigurationService checksConfigurationService2 = checksConfigurationService = new ChecksConfigurationService(this);
                    checksConfigurationService2.initialize();
                    PluginContext pluginContext5 = this.context;
                    ChecksConfigurationService checksConfigurationService3 = checksConfigurationService;
                    pluginContext5.setChecksConfig(checksConfigurationService3);
                    PunishmentsConfigurationService punishmentsConfigurationService2 = punishmentsConfigurationService = new PunishmentsConfigurationService(this);
                    punishmentsConfigurationService2.initialize();
                    PluginContext pluginContext6 = this.context;
                    PunishmentsConfigurationService punishmentsConfigurationService3 = punishmentsConfigurationService;
                    pluginContext6.setPunishments(punishmentsConfigurationService3);
                    ConfigurationService configurationService5 = configurationService;
                    String string2 = string = configurationService5.getLicenseKey();
                    break block4;
                }
                catch (Exception e) {
                    Exception e2 = e;
                    Logger logger = this.getLogger();
                    Exception e3 = e2;
                    String string3 = "Failed to initialize components: " + e3.getMessage();
                    logger.severe(string3);
                    Exception e4 = e2;
                    e4.printStackTrace();
                    return glAntiElytraTarget.abortFold(1184850535);
                }
            }
            String string4 = string;
            break block5;
        }
        LicensedBootCoordinator licensedBootCoordinator = this.bootCoordinator;
        ConfigurationService configurationService7 = configurationService;
        String string5 = string;
        LicensedBootCoordinator.Source source = LicensedBootCoordinator.Source.DIRECT_ENABLE;
        licensedBootCoordinator.beginAsyncStartup(configurationService7, string5, source);
        long l = 2594964574L;
        return l;
    }

    private long inlineBootFold() {
        long l;
        long l2;
        RuntimeAuthority runtimeAuthority = this.context != null ? this.context.runtime() : null;
        RuntimeAuthority runtimeAuthority2 = runtimeAuthority;
        RuntimeAuthority runtimeAuthority3 = runtimeAuthority2;
        if (runtimeAuthority3 == null) {
            return 1L;
        }
        RuntimeAuthority runtimeAuthority4 = runtimeAuthority2;
        long l3 = runtimeAuthority4._seal0();
        long l4 = l3 ^ (l2 = 3237998146L);
        long l5 = l4;
        if (l5 != (l = 0L)) {
            long l6 = l4;
            return l6;
        }
        RuntimeAuthority runtimeAuthority5 = runtimeAuthority2;
        return runtimeAuthority5._co0();
    }

    private static long abortFold(int n) {
        int n2 = 0x541BF09A ^ (0x3E55356 ^ (0x7769FF38 ^ n));
        n2 = 0x1A7928B1 ^ n2;
        long l = ThreadLocalRandom.current().nextLong();
        long l2 = 1L;
        return l | l2;
    }

    private void checkForUpdates() {
        UpdateChecker updateChecker = this.context.updateChecker();
        BiConsumer<Boolean, String> biConsumer = (bl, string) -> {
            if (bl.booleanValue()) {
                Logger logger = this.getLogger();
                String string2 = "A new update is available! Current: " + this.getDescription().getVersion() + " Latest: " + string;
                logger.info(string2);
            }
        };
        updateChecker.checkForUpdates(biConsumer);
    }

    @Generated
    public static glAntiElytraTarget getInstance() {
        return instance;
    }
}

