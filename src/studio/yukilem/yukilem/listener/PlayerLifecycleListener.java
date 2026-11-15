package studio.yukilem.yukilem.listener;

import java.util.Iterator;
import java.util.UUID;
import java.util.function.BiConsumer;
import lombok.Generated;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import studio.yukilem.yukilem.alerts.AlertService;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.CheckRegistry;
import studio.yukilem.yukilem.config.settings.UpdateCheckerSettings;
import studio.yukilem.yukilem.data.FlagRepository;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;
import studio.yukilem.yukilem.menu.MenuManager;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.platform.PlayerSessionCache;
import studio.yukilem.yukilem.platform.ServerHealthMonitor;
import studio.yukilem.yukilem.platform.region.RegionProtectionPolicy;
import studio.yukilem.yukilem.punishments.ViolationService;
import studio.yukilem.yukilem.update.UpdateChecker;
import studio.yukilem.yukilem.util.combat.ElytraUseProbe;
import studio.yukilem.yukilem.util.combat.TargetTracker;

public final class PlayerLifecycleListener
implements Listener {
    private static final String UPDATE_PERMISSION = "glantielytratarget.updatechecker.notify";
    private final PluginContext ctx;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player;
        UpdateCheckerSettings updateCheckerSettings;
        UpdateCheckerSettings updateCheckerSettings2;
        UpdateCheckerSettings updateCheckerSettings3;
        Player player2 = playerJoinEvent.getPlayer();
        if (this.ctx.sessionCache() != null) {
            PlayerSessionCache playerSessionCache = this.ctx.sessionCache();
            Player player3 = player2;
            playerSessionCache.onJoin(player3);
        }
        if (this.ctx.checks() != null) {
            CheckRegistry checkRegistry = this.ctx.checks();
            Player player4 = player2;
            UUID uniqueId = player4.getUniqueId();
            checkRegistry.seedPlayer(uniqueId);
        }
        if (this.ctx.alerts() != null) {
            AlertService alertService = this.ctx.alerts();
            Player player5 = player2;
            alertService.handlePlayerJoin(player5);
        }
        if (this.ctx.flagRepository() != null) {
            FlagRepository flagRepository = this.ctx.flagRepository();
            Player player6 = player2;
            UUID uniqueId = player6.getUniqueId();
            flagRepository.loadPlayerAsync(uniqueId);
        }
        if (this.ctx.warnings() != null) {
            ViolationService violationService = this.ctx.warnings();
            Player player7 = player2;
            UUID uniqueId = player7.getUniqueId();
            violationService.clearViolations(uniqueId);
        }
        if ((updateCheckerSettings3 = (updateCheckerSettings2 = this.ctx.config().getUpdateChecker())).enabled() && (updateCheckerSettings = updateCheckerSettings2).notifyOnJoin() && (player = player2).hasPermission(UPDATE_PERMISSION)) {
            PlatformScheduler platformScheduler = this.ctx.scheduler();
            Player player8 = player2;
            Player player9 = player2;
            Runnable runnable = () -> this.notifyUpdate(player9);
            long l = 40L;
            platformScheduler.runEntityLater(player8, runnable, l);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        UUID uniqueId = playerQuitEvent.getPlayer().getUniqueId();
        if (this.ctx.checks() != null) {
            CheckRegistry checkRegistry = this.ctx.checks();
            UUID uUID = uniqueId;
            checkRegistry.clearPlayer(uUID);
        }
        if (this.ctx.regionProtection() != null) {
            RegionProtectionPolicy regionProtectionPolicy = this.ctx.regionProtection();
            UUID uUID = uniqueId;
            regionProtectionPolicy.clearPlayer(uUID);
        }
        if (this.ctx.warnings() != null) {
            ViolationService violationService = this.ctx.warnings();
            UUID uUID = uniqueId;
            violationService.clearViolations(uUID);
        }
        if (this.ctx.targetTracker() != null) {
            TargetTracker targetTracker = this.ctx.targetTracker();
            UUID uUID = uniqueId;
            targetTracker.removePlayer(uUID);
        }
        if (this.ctx.healthMonitor() != null) {
            ServerHealthMonitor serverHealthMonitor = this.ctx.healthMonitor();
            UUID uUID = uniqueId;
            serverHealthMonitor.onPlayerQuit(uUID);
        }
        if (this.ctx.sessionCache() != null) {
            PlayerSessionCache playerSessionCache = this.ctx.sessionCache();
            UUID uUID = uniqueId;
            playerSessionCache.onQuit(uUID);
        }
        if (this.ctx.flagRepository() != null) {
            FlagRepository flagRepository = this.ctx.flagRepository();
            UUID uUID = uniqueId;
            flagRepository.evictPlayer(uUID);
        }
        if (this.ctx.menus() != null) {
            MenuManager menuManager = this.ctx.menus();
            UUID uUID = uniqueId;
            menuManager.evict(uUID);
        }
        UUID uUID = uniqueId;
        ElytraUseProbe.clearPlayer(uUID);
    }

    private void notifyUpdate(Player player) {
        if (!player.isOnline()) {
            return;
        }
        UpdateChecker updateChecker = this.ctx.updateChecker();
        UpdateChecker updateChecker2 = updateChecker;
        if (updateChecker2 == null) {
            return;
        }
        UpdateChecker updateChecker3 = updateChecker;
        if (updateChecker3.isUpdateAvailable()) {
            UpdateChecker updateChecker4 = updateChecker;
            this.sendUpdateMessage(player, updateChecker4);
        } else {
            UpdateChecker updateChecker5 = updateChecker;
            UpdateChecker updateChecker6 = updateChecker;
            BiConsumer<Boolean, String> biConsumer = (bl, string) -> {
                if (bl.booleanValue() && player.isOnline()) {
                    PlatformScheduler platformScheduler = this.ctx.scheduler();
                    Runnable runnable = () -> this.sendUpdateMessage(player, updateChecker6);
                    platformScheduler.runEntity(player, runnable);
                }
            };
            updateChecker5.checkForUpdates(biConsumer);
        }
    }

    private void sendUpdateMessage(Player player, UpdateChecker updateChecker) {
        Iterator<String> iterator;
        String currentVersion = updateChecker.getCurrentVersion();
        String latestVersion = updateChecker.getLatestVersion();
        Iterator<String> iterator2 = this.ctx.language().getRawMessageList("update.message").iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<String> iterator3 = iterator2;
            String string = iterator3.next();
            String string2 = currentVersion;
            String string3 = string.replace("%current%", string2);
            String string4 = latestVersion;
            Component component = ColorUtils.translate(string3.replace("%latest%", string4));
            player.sendMessage(component);
        }
    }

    @Generated
    public PlayerLifecycleListener(PluginContext pluginContext) {
        this.ctx = pluginContext;
    }
}

