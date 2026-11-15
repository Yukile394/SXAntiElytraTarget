package studio.yukilem.yukilem.bootstrap;

import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.alerts.AlertService;
import studio.yukilem.yukilem.checks.CheckRegistry;
import studio.yukilem.yukilem.config.ChecksConfigurationService;
import studio.yukilem.yukilem.config.ConfigurationService;
import studio.yukilem.yukilem.config.LanguageService;
import studio.yukilem.yukilem.config.MenuConfigurationService;
import studio.yukilem.yukilem.config.PunishmentsConfigurationService;
import studio.yukilem.yukilem.config.WebhookConfigurationService;
import studio.yukilem.yukilem.config.settings.CommandSettings;
import studio.yukilem.yukilem.data.FlagRepository;
import studio.yukilem.yukilem.data.ViolationRepository;
import studio.yukilem.yukilem.discord.DiscordWebhookService;
import studio.yukilem.yukilem.glAntiElytraTarget;
import studio.yukilem.yukilem.menu.MenuActionService;
import studio.yukilem.yukilem.menu.MenuManager;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.platform.PlayerSessionCache;
import studio.yukilem.yukilem.platform.ServerHealthMonitor;
import studio.yukilem.yukilem.platform.region.RegionProtectionPolicy;
import studio.yukilem.yukilem.platform.runtime.RuntimeAuthority;
import studio.yukilem.yukilem.punishments.ViolationService;
import studio.yukilem.yukilem.update.UpdateChecker;
import studio.yukilem.yukilem.util.combat.TargetTracker;

public final class PluginContext {
    private final glAntiElytraTarget plugin;
    private ConfigurationService config;
    private CommandSettings commandSettings;
    private ChecksConfigurationService checksConfig;
    private PunishmentsConfigurationService punishments;
    private WebhookConfigurationService webhookConfig;
    private LanguageService language;
    private PlatformScheduler scheduler;
    private PlayerSessionCache sessionCache;
    private ServerHealthMonitor healthMonitor;
    private RegionProtectionPolicy regionProtection;
    private ViolationService warnings;
    private AlertService alerts;
    private DiscordWebhookService discord;
    private CheckRegistry checks;
    private TargetTracker targetTracker;
    private UpdateChecker updateChecker;
    private RuntimeAuthority runtime;
    private FlagRepository flagRepository;
    private ViolationRepository violationRepository;
    private MenuConfigurationService menuConfig;
    private MenuActionService menuActions;
    private MenuManager menus;

    @NotNull
    public glAntiElytraTarget plugin() {
        return this.plugin;
    }

    @NotNull
    public ConfigurationService config() {
        return this.config;
    }

    @Nullable
    public CommandSettings commandSettings() {
        return this.commandSettings;
    }

    @NotNull
    public ChecksConfigurationService checksConfig() {
        return this.checksConfig;
    }

    @NotNull
    public PunishmentsConfigurationService punishments() {
        return this.punishments;
    }

    @NotNull
    public WebhookConfigurationService webhookConfig() {
        return this.webhookConfig;
    }

    @NotNull
    public LanguageService language() {
        return this.language;
    }

    @NotNull
    public PlatformScheduler scheduler() {
        return this.scheduler;
    }

    @Nullable
    public PlayerSessionCache sessionCache() {
        return this.sessionCache;
    }

    @Nullable
    public ServerHealthMonitor healthMonitor() {
        return this.healthMonitor;
    }

    @Nullable
    public RegionProtectionPolicy regionProtection() {
        return this.regionProtection;
    }

    @Nullable
    public ViolationService warnings() {
        return this.warnings;
    }

    @Nullable
    public AlertService alerts() {
        return this.alerts;
    }

    @Nullable
    public DiscordWebhookService discord() {
        return this.discord;
    }

    @Nullable
    public CheckRegistry checks() {
        return this.checks;
    }

    @Nullable
    public TargetTracker targetTracker() {
        return this.targetTracker;
    }

    @Nullable
    public UpdateChecker updateChecker() {
        return this.updateChecker;
    }

    @Nullable
    public RuntimeAuthority runtime() {
        return this.runtime;
    }

    @Nullable
    public FlagRepository flagRepository() {
        return this.flagRepository;
    }

    @Nullable
    public ViolationRepository violationRepository() {
        return this.violationRepository;
    }

    @Nullable
    public MenuConfigurationService menuConfig() {
        return this.menuConfig;
    }

    @Nullable
    public MenuActionService menuActions() {
        return this.menuActions;
    }

    @Nullable
    public MenuManager menus() {
        return this.menus;
    }

    @Generated
    public glAntiElytraTarget getPlugin() {
        return this.plugin;
    }

    @Generated
    public ConfigurationService getConfig() {
        return this.config;
    }

    @Generated
    public CommandSettings getCommandSettings() {
        return this.commandSettings;
    }

    @Generated
    public ChecksConfigurationService getChecksConfig() {
        return this.checksConfig;
    }

    @Generated
    public PunishmentsConfigurationService getPunishments() {
        return this.punishments;
    }

    @Generated
    public WebhookConfigurationService getWebhookConfig() {
        return this.webhookConfig;
    }

    @Generated
    public LanguageService getLanguage() {
        return this.language;
    }

    @Generated
    public PlatformScheduler getScheduler() {
        return this.scheduler;
    }

    @Generated
    public PlayerSessionCache getSessionCache() {
        return this.sessionCache;
    }

    @Generated
    public ServerHealthMonitor getHealthMonitor() {
        return this.healthMonitor;
    }

    @Generated
    public RegionProtectionPolicy getRegionProtection() {
        return this.regionProtection;
    }

    @Generated
    public ViolationService getWarnings() {
        return this.warnings;
    }

    @Generated
    public AlertService getAlerts() {
        return this.alerts;
    }

    @Generated
    public DiscordWebhookService getDiscord() {
        return this.discord;
    }

    @Generated
    public CheckRegistry getChecks() {
        return this.checks;
    }

    @Generated
    public TargetTracker getTargetTracker() {
        return this.targetTracker;
    }

    @Generated
    public UpdateChecker getUpdateChecker() {
        return this.updateChecker;
    }

    @Generated
    public RuntimeAuthority getRuntime() {
        return this.runtime;
    }

    @Generated
    public FlagRepository getFlagRepository() {
        return this.flagRepository;
    }

    @Generated
    public ViolationRepository getViolationRepository() {
        return this.violationRepository;
    }

    @Generated
    public MenuConfigurationService getMenuConfig() {
        return this.menuConfig;
    }

    @Generated
    public MenuActionService getMenuActions() {
        return this.menuActions;
    }

    @Generated
    public MenuManager getMenus() {
        return this.menus;
    }

    @Generated
    public void setConfig(ConfigurationService configurationService) {
        this.config = configurationService;
    }

    @Generated
    public void setCommandSettings(CommandSettings commandSettings) {
        this.commandSettings = commandSettings;
    }

    @Generated
    public void setChecksConfig(ChecksConfigurationService checksConfigurationService) {
        this.checksConfig = checksConfigurationService;
    }

    @Generated
    public void setPunishments(PunishmentsConfigurationService punishmentsConfigurationService) {
        this.punishments = punishmentsConfigurationService;
    }

    @Generated
    public void setWebhookConfig(WebhookConfigurationService webhookConfigurationService) {
        this.webhookConfig = webhookConfigurationService;
    }

    @Generated
    public void setLanguage(LanguageService languageService) {
        this.language = languageService;
    }

    @Generated
    public void setScheduler(PlatformScheduler platformScheduler) {
        this.scheduler = platformScheduler;
    }

    @Generated
    public void setSessionCache(PlayerSessionCache playerSessionCache) {
        this.sessionCache = playerSessionCache;
    }

    @Generated
    public void setHealthMonitor(ServerHealthMonitor serverHealthMonitor) {
        this.healthMonitor = serverHealthMonitor;
    }

    @Generated
    public void setRegionProtection(RegionProtectionPolicy regionProtectionPolicy) {
        this.regionProtection = regionProtectionPolicy;
    }

    @Generated
    public void setWarnings(ViolationService violationService) {
        this.warnings = violationService;
    }

    @Generated
    public void setAlerts(AlertService alertService) {
        this.alerts = alertService;
    }

    @Generated
    public void setDiscord(DiscordWebhookService discordWebhookService) {
        this.discord = discordWebhookService;
    }

    @Generated
    public void setChecks(CheckRegistry checkRegistry) {
        this.checks = checkRegistry;
    }

    @Generated
    public void setTargetTracker(TargetTracker targetTracker) {
        this.targetTracker = targetTracker;
    }

    @Generated
    public void setUpdateChecker(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @Generated
    public void setRuntime(RuntimeAuthority runtimeAuthority) {
        this.runtime = runtimeAuthority;
    }

    @Generated
    public void setFlagRepository(FlagRepository flagRepository) {
        this.flagRepository = flagRepository;
    }

    @Generated
    public void setViolationRepository(ViolationRepository violationRepository) {
        this.violationRepository = violationRepository;
    }

    @Generated
    public void setMenuConfig(MenuConfigurationService menuConfigurationService) {
        this.menuConfig = menuConfigurationService;
    }

    @Generated
    public void setMenuActions(MenuActionService menuActionService) {
        this.menuActions = menuActionService;
    }

    @Generated
    public void setMenus(MenuManager menuManager) {
        this.menus = menuManager;
    }

    @Generated
    public PluginContext(glAntiElytraTarget glAntiElytraTarget2) {
        this.plugin = glAntiElytraTarget2;
    }
}

