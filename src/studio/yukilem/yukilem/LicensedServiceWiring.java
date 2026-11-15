package studio.glace.glantielytratarget;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import java.io.File;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.alerts.AlertService;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.bootstrap.ServiceRegistry;
import studio.glace.glantielytratarget.checks.CheckRegistry;
import studio.glace.glantielytratarget.commands.SupportCommand;
import studio.glace.glantielytratarget.commands.SupportCommandPacketGuard;
import studio.glace.glantielytratarget.config.ConfigurationService;
import studio.glace.glantielytratarget.config.LanguageService;
import studio.glace.glantielytratarget.config.MenuConfigurationService;
import studio.glace.glantielytratarget.config.settings.DatabaseSettings;
import studio.glace.glantielytratarget.data.DatabaseService;
import studio.glace.glantielytratarget.data.FlagRepository;
import studio.glace.glantielytratarget.data.ViolationRepository;
import studio.glace.glantielytratarget.discord.DiscordWebhookService;
import studio.glace.glantielytratarget.glAntiElytraTarget;
import studio.glace.glantielytratarget.libs.nixlib.database.Database;
import studio.glace.glantielytratarget.menu.MenuActionService;
import studio.glace.glantielytratarget.menu.MenuManager;
import studio.glace.glantielytratarget.packet.PacketDispatcher;
import studio.glace.glantielytratarget.packet.PacketRouter;
import studio.glace.glantielytratarget.platform.AETPlaceholderExpansion;
import studio.glace.glantielytratarget.platform.PlatformScheduler;
import studio.glace.glantielytratarget.platform.PlayerSessionCache;
import studio.glace.glantielytratarget.platform.ServerHealthMonitor;
import studio.glace.glantielytratarget.platform.region.RegionProtection;
import studio.glace.glantielytratarget.platform.region.RegionProtectionPolicy;
import studio.glace.glantielytratarget.platform.runtime.RuntimeAuthority;
import studio.glace.glantielytratarget.platform.runtime.RuntimeGate;
import studio.glace.glantielytratarget.punishments.ViolationService;
import studio.glace.glantielytratarget.update.UpdateChecker;
import studio.glace.glantielytratarget.util.combat.TargetTracker;

final class LicensedServiceWiring {
    private static final long INIT_SEAL = 3237998146L;
    private static final int WIRE_B = 21431;
    private static final int WIRE_C = 25289;
    private static final int WIRE_D = 29140;
    private static final int WIRE_F = 39667;
    private static final int WIRE_G = 42689;

    private LicensedServiceWiring() {
    }

    static long apply(@NotNull glAntiElytraTarget glAntiElytraTarget2, @NotNull RuntimeAuthority runtimeAuthority) throws Exception {
        long l;
        long l2;
        long l3;
        long l4;
        long l5;
        long l6;
        long l7;
        long l8;
        ServiceRegistry serviceRegistry;
        ServiceRegistry serviceRegistry2;
        long l9;
        long l10;
        long l11;
        long l12;
        long l13;
        PacketDispatcher packetDispatcher;
        PacketDispatcher packetDispatcher2;
        UpdateChecker updateChecker;
        MenuManager menuManager;
        MenuActionService menuActionService;
        MenuConfigurationService menuConfigurationService;
        CheckRegistry checkRegistry;
        TargetTracker targetTracker;
        ViolationService violationService;
        DiscordWebhookService discordWebhookService;
        AlertService alertService;
        ViolationRepository violationRepository;
        ViolationRepository violationRepository2;
        FlagRepository flagRepository;
        Database database;
        ServerHealthMonitor serverHealthMonitor;
        PlatformScheduler platformScheduler;
        LanguageService languageService;
        PluginContext pluginContext;
        PluginContext pluginContext2 = pluginContext = glAntiElytraTarget2.context();
        pluginContext2.setRuntime(runtimeAuthority);
        PluginContext pluginContext3 = pluginContext;
        ConfigurationService configurationService = pluginContext3.config();
        PluginContext pluginContext4 = pluginContext;
        LanguageService languageService2 = languageService = pluginContext4.language();
        if (languageService2 == null) {
            LanguageService languageService3;
            LanguageService languageService4;
            ConfigurationService configurationService2 = configurationService;
            LanguageService languageService5 = languageService4 = (languageService3 = new LanguageService(glAntiElytraTarget2, configurationService2));
            languageService5.initialize();
            PluginContext pluginContext5 = pluginContext;
            LanguageService languageService6 = languageService4;
            pluginContext5.setLanguage(languageService6);
        } else {
            LanguageService languageService7 = languageService;
            languageService7.logStartupSummary();
        }
        PluginContext pluginContext6 = pluginContext;
        PlatformScheduler platformScheduler2 = platformScheduler = pluginContext6.scheduler();
        if (platformScheduler2 == null) {
            platformScheduler = new PlatformScheduler(glAntiElytraTarget2);
            PluginContext pluginContext7 = pluginContext;
            PlatformScheduler platformScheduler3 = platformScheduler;
            pluginContext7.setScheduler(platformScheduler3);
        }
        PlayerSessionCache playerSessionCache = new PlayerSessionCache();
        PluginContext pluginContext8 = pluginContext;
        PlayerSessionCache playerSessionCache2 = playerSessionCache;
        pluginContext8.setSessionCache(playerSessionCache2);
        ConfigurationService configurationService3 = configurationService;
        ServerHealthMonitor serverHealthMonitor2 = serverHealthMonitor = new ServerHealthMonitor(configurationService3);
        PluginContext pluginContext9 = pluginContext;
        ServerHealthMonitor serverHealthMonitor3 = serverHealthMonitor2;
        pluginContext9.setHealthMonitor(serverHealthMonitor3);
        PluginContext pluginContext10 = pluginContext;
        RegionProtectionPolicy regionProtectionPolicy = RegionProtection.create(pluginContext10);
        PluginContext pluginContext11 = pluginContext;
        RegionProtectionPolicy regionProtectionPolicy2 = regionProtectionPolicy;
        pluginContext11.setRegionProtection(regionProtectionPolicy2);
        ConfigurationService configurationService4 = configurationService;
        DatabaseSettings database2 = configurationService4.getDatabase();
        File dataFolder = glAntiElytraTarget2.getDataFolder();
        Database database3 = database = DatabaseService.create(database2, dataFolder);
        FlagRepository flagRepository2 = flagRepository = new FlagRepository(database3);
        PluginContext pluginContext12 = pluginContext;
        FlagRepository flagRepository3 = flagRepository2;
        pluginContext12.setFlagRepository(flagRepository3);
        Database database4 = database;
        ViolationRepository violationRepository3 = violationRepository2 = (violationRepository = new ViolationRepository(database4));
        ConfigurationService configurationService5 = configurationService;
        int intVal = configurationService5.getInt("violation-history.max-per-player", 250);
        ConfigurationService configurationService6 = configurationService;
        boolean boolean_ = configurationService6.getBoolean("violation-history.overwrite-oldest", true);
        violationRepository3.configure(intVal, boolean_);
        PluginContext pluginContext13 = pluginContext;
        ViolationRepository violationRepository4 = violationRepository2;
        pluginContext13.setViolationRepository(violationRepository4);
        PluginContext pluginContext14 = pluginContext;
        AlertService alertService2 = alertService = new AlertService(pluginContext14);
        PluginContext pluginContext15 = pluginContext;
        AlertService alertService3 = alertService2;
        pluginContext15.setAlerts(alertService3);
        PluginContext pluginContext16 = pluginContext;
        DiscordWebhookService discordWebhookService2 = discordWebhookService = new DiscordWebhookService(pluginContext16);
        PluginContext pluginContext17 = pluginContext;
        DiscordWebhookService discordWebhookService3 = discordWebhookService2;
        pluginContext17.setDiscord(discordWebhookService3);
        PluginContext pluginContext18 = pluginContext;
        ViolationService violationService2 = violationService = new ViolationService(pluginContext18);
        PluginContext pluginContext19 = pluginContext;
        ViolationService violationService3 = violationService2;
        pluginContext19.setWarnings(violationService3);
        TargetTracker targetTracker2 = targetTracker = new TargetTracker();
        ConfigurationService configurationService7 = configurationService;
        long l14 = 8000L;
        long longVal = configurationService7.getLong("combat.target-expiry-ms", l14);
        targetTracker2.setTargetExpiryMs(longVal);
        PluginContext pluginContext20 = pluginContext;
        TargetTracker targetTracker3 = targetTracker;
        pluginContext20.setTargetTracker(targetTracker3);
        PluginContext pluginContext21 = pluginContext;
        CheckRegistry checkRegistry2 = checkRegistry = new CheckRegistry(pluginContext21);
        PluginContext pluginContext22 = pluginContext;
        CheckRegistry checkRegistry3 = checkRegistry2;
        pluginContext22.setChecks(checkRegistry3);
        RegionProtectionPolicy regionProtectionPolicy3 = regionProtectionPolicy;
        regionProtectionPolicy3.refreshSettings();
        MenuConfigurationService menuConfigurationService2 = menuConfigurationService = new MenuConfigurationService(glAntiElytraTarget2);
        menuConfigurationService2.initialize();
        PluginContext pluginContext23 = pluginContext;
        MenuConfigurationService menuConfigurationService3 = menuConfigurationService;
        pluginContext23.setMenuConfig(menuConfigurationService3);
        PluginContext pluginContext24 = pluginContext;
        MenuActionService menuActionService2 = menuActionService = new MenuActionService(pluginContext24);
        PluginContext pluginContext25 = pluginContext;
        MenuActionService menuActionService3 = menuActionService2;
        pluginContext25.setMenuActions(menuActionService3);
        PluginContext pluginContext26 = pluginContext;
        MenuManager menuManager2 = menuManager = new MenuManager(pluginContext26);
        PluginContext pluginContext27 = pluginContext;
        MenuManager menuManager3 = menuManager2;
        pluginContext27.setMenus(menuManager3);
        long l15 = runtimeAuthority._wire0(21431) ^ 0x53B7;
        long l16 = runtimeAuthority._co0();
        long l17 = l15 | l16;
        long l18 = 0L;
        if (l17 != l18) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        PluginContext pluginContext28 = pluginContext;
        String version = glAntiElytraTarget2.getDescription().getVersion();
        UpdateChecker updateChecker2 = updateChecker = new UpdateChecker(pluginContext28, "glAntiElytraTarget", version);
        PluginContext pluginContext29 = pluginContext;
        UpdateChecker updateChecker3 = updateChecker2;
        pluginContext29.setUpdateChecker(updateChecker3);
        PacketRouter packetRouter = new PacketRouter();
        CheckRegistry checkRegistry4 = checkRegistry2;
        PacketRouter packetRouter2 = packetRouter;
        checkRegistry4.registerHandlers(packetRouter2);
        PluginContext pluginContext30 = pluginContext;
        PacketRouter packetRouter3 = packetRouter;
        PacketDispatcher packetDispatcher3 = packetDispatcher2 = (packetDispatcher = new PacketDispatcher(pluginContext30, packetRouter3));
        glAntiElytraTarget2.setPacketDispatcher(packetDispatcher3);
        PluginContext pluginContext31 = pluginContext;
        long l19 = RuntimeGate.bootReady(pluginContext31);
        long l20 = 0L;
        if (l19 != l20) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        long l21 = runtimeAuthority._wire0(39667) ^ 0x9AF3;
        long l22 = l21 | (l13 = runtimeAuthority._co0());
        if (l22 != (l12 = 0L)) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        long l23 = runtimeAuthority._wire0(25289) ^ 0x62C9;
        long l24 = l23 | (l11 = runtimeAuthority._co0());
        if (l24 != (l10 = 0L)) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        PluginContext pluginContext32 = pluginContext;
        long l25 = RuntimeGate.bootReady(pluginContext32);
        if (l25 != (l9 = 0L)) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        EventManager eventManager = PacketEvents.getAPI().getEventManager();
        PacketDispatcher packetDispatcher4 = packetDispatcher2;
        PacketListenerCommon packetListenerCommon = eventManager.registerListener(packetDispatcher4);
        if (glAntiElytraTarget2.supportCommandPacketGuard() == null) {
            SupportCommandPacketGuard supportCommandPacketGuard;
            SupportCommandPacketGuard supportCommandPacketGuard2;
            PluginContext pluginContext33 = pluginContext;
            glAntiElytraTarget glAntiElytraTarget3 = Objects.requireNonNull(glAntiElytraTarget2);
            Supplier<SupportCommand> supplier = glAntiElytraTarget2::supportCommand;
            SupportCommandPacketGuard supportCommandPacketGuard3 = supportCommandPacketGuard2 = (supportCommandPacketGuard = new SupportCommandPacketGuard(pluginContext33, supplier));
            glAntiElytraTarget2.setSupportCommandPacketGuard(supportCommandPacketGuard3);
            EventManager eventManager2 = PacketEvents.getAPI().getEventManager();
            SupportCommandPacketGuard supportCommandPacketGuard4 = supportCommandPacketGuard2;
            PacketListenerCommon packetListenerCommon2 = eventManager2.registerListener(supportCommandPacketGuard4);
        }
        if ((serviceRegistry2 = (serviceRegistry = glAntiElytraTarget2.services())) == null) {
            ServiceRegistry serviceRegistry3;
            Logger logger = glAntiElytraTarget2.getLogger();
            ServiceRegistry serviceRegistry4 = serviceRegistry = (serviceRegistry3 = new ServiceRegistry(logger));
            glAntiElytraTarget2.setServices(serviceRegistry4);
        }
        ServiceRegistry serviceRegistry5 = serviceRegistry;
        PlatformScheduler platformScheduler4 = platformScheduler;
        PlatformScheduler platformScheduler5 = Objects.requireNonNull(platformScheduler4);
        Runnable runnable = platformScheduler4::shutdown;
        ServiceRegistry serviceRegistry6 = serviceRegistry5.registerMainThread("scheduler", runnable);
        RuntimeAuthority runtimeAuthority2 = Objects.requireNonNull(runtimeAuthority);
        Runnable runnable2 = runtimeAuthority::shutdown;
        ServiceRegistry serviceRegistry7 = serviceRegistry6.register("runtime", runnable2);
        Database database5 = database;
        Runnable runnable3 = () -> {
            Duration duration = Duration.ofSeconds(2L);
            CompletableFuture<Boolean> completableFuture = database5.closeAsync(duration);
            BiConsumer<Boolean, Throwable> biConsumer = (bl, e) -> {
                if (e != null) {
                    Logger logger = glAntiElytraTarget2.getLogger();
                    String string = "Database shutdown failed: " + e.getMessage();
                    logger.warning(string);
                } else if (!bl.booleanValue()) {
                    glAntiElytraTarget2.getLogger().warning("Database shutdown exceeded the 2 second deadline.");
                }
            };
            CompletionStage completionStage = completableFuture.whenComplete((BiConsumer)biConsumer);
        };
        ServiceRegistry serviceRegistry8 = serviceRegistry7.registerBounded("database", runnable3);
        FlagRepository flagRepository4 = flagRepository2;
        FlagRepository flagRepository5 = Objects.requireNonNull(flagRepository4);
        Runnable runnable4 = flagRepository4::close;
        ServiceRegistry serviceRegistry9 = serviceRegistry8.register("flagRepository", runnable4);
        ViolationRepository violationRepository5 = violationRepository2;
        ViolationRepository violationRepository6 = Objects.requireNonNull(violationRepository5);
        Runnable runnable5 = violationRepository5::close;
        ServiceRegistry serviceRegistry10 = serviceRegistry9.register("violationRepository", runnable5);
        MenuManager menuManager4 = menuManager2;
        MenuManager menuManager5 = Objects.requireNonNull(menuManager4);
        Runnable runnable6 = menuManager4::shutdown;
        ServiceRegistry serviceRegistry11 = serviceRegistry10.registerMainThread("menus", runnable6);
        PlayerSessionCache playerSessionCache3 = playerSessionCache;
        PlayerSessionCache playerSessionCache4 = Objects.requireNonNull(playerSessionCache3);
        Runnable runnable7 = playerSessionCache3::clear;
        ServiceRegistry serviceRegistry12 = serviceRegistry11.register("sessionCache", runnable7);
        TargetTracker targetTracker4 = targetTracker;
        TargetTracker targetTracker5 = Objects.requireNonNull(targetTracker4);
        Runnable runnable8 = targetTracker4::clear;
        ServiceRegistry serviceRegistry13 = serviceRegistry12.register("targetTracker", runnable8);
        RegionProtectionPolicy regionProtectionPolicy4 = regionProtectionPolicy;
        RegionProtectionPolicy regionProtectionPolicy5 = Objects.requireNonNull(regionProtectionPolicy4);
        Runnable runnable9 = regionProtectionPolicy4::shutdown;
        ServiceRegistry serviceRegistry14 = serviceRegistry13.registerMainThread("regionProtection", runnable9);
        CheckRegistry checkRegistry5 = checkRegistry2;
        CheckRegistry checkRegistry6 = Objects.requireNonNull(checkRegistry5);
        Runnable runnable10 = checkRegistry5::shutdown;
        ServiceRegistry serviceRegistry15 = serviceRegistry14.register("checks", runnable10);
        DiscordWebhookService discordWebhookService4 = discordWebhookService2;
        DiscordWebhookService discordWebhookService5 = Objects.requireNonNull(discordWebhookService4);
        Runnable runnable11 = discordWebhookService4::shutdown;
        ServiceRegistry serviceRegistry16 = serviceRegistry15.register("discord", runnable11);
        AlertService alertService4 = alertService2;
        AlertService alertService5 = Objects.requireNonNull(alertService4);
        Runnable runnable12 = alertService4::clearAllAlerts;
        ServiceRegistry serviceRegistry17 = serviceRegistry16.register("alerts", runnable12);
        ViolationService violationService4 = violationService2;
        ViolationService violationService5 = Objects.requireNonNull(violationService4);
        Runnable runnable13 = violationService4::clearAllViolations;
        ServiceRegistry serviceRegistry18 = serviceRegistry17.register("violations", runnable13);
        runtimeAuthority._sh0();
        long l26 = runtimeAuthority._wire0(29140) ^ 0x71D4;
        long l27 = runtimeAuthority._co0();
        long l28 = l26 | l27;
        long l29 = 0L;
        if (l28 != l29) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        long l30 = runtimeAuthority._wire0(42689) ^ 0xA6C1;
        long l31 = l30 | (l8 = runtimeAuthority._co0());
        if (l31 != (l7 = 0L)) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        if (glAntiElytraTarget2.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PluginContext pluginContext34 = pluginContext;
            AETPlaceholderExpansion aETPlaceholderExpansion = new AETPlaceholderExpansion(pluginContext34);
            boolean bl = aETPlaceholderExpansion.register();
        }
        if ((l6 = (l5 = runtimeAuthority._epoch0()) ^ (l4 = 3237998146L)) != (l3 = 0L)) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        long l32 = runtimeAuthority._seal0();
        long l33 = l32;
        long l34 = l33 ^ (l2 = 3237998146L);
        if (l34 != (l = 0L)) {
            return LicensedServiceWiring.abortFold(1274989907);
        }
        long l35 = l32;
        return l35;
    }

    private static long abortFold(int n) {
        int n2 = 0x3EE379B9 ^ (0x92F67F1 ^ (0xC7CE343 ^ n));
        n2 = 0xAA560D2 ^ n2;
        long l = ThreadLocalRandom.current().nextLong();
        long l2 = 1L;
        return l | l2;
    }
}

