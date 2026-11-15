package studio.glace.glantielytratarget.commands.impl;

import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.LicenseBootstrap;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.commands.SubCommand;
import studio.glace.glantielytratarget.config.ConfigurationService;
import studio.glace.glantielytratarget.data.ViolationRepository;
import studio.glace.glantielytratarget.libs.nixlib.utils.ColorUtils;
import studio.glace.glantielytratarget.platform.PlatformScheduler;
import studio.glace.glantielytratarget.platform.runtime.RuntimeGate;
import studio.glace.glantielytratarget.util.combat.TargetTracker;

public final class ReloadSubCommand implements SubCommand {
   public ReloadSubCommand() {
   }

   @Override
   public boolean execute(@NotNull PluginContext pluginContext1, @NotNull CommandSender commandSender2, @NotNull String[] strings3) {
      String string24 = this.permission();
      if (!commandSender2.hasPermission(string24)) {
         Component component32 = pluginContext1.language().getMessage("general.no-permission");
         commandSender2.sendMessage(component32);
         return true;
      } else {
         long l11 = RuntimeGate.graceExpired(pluginContext1);
         long l7 = 0L;
         if (l11 != l7) {
            Component component28 = ColorUtils.translate("<#FF3333>Grace period expired. Restart the server after setting your license key.");
            commandSender2.sendMessage(component28);
            pluginContext1.plugin().getLogger().severe("Grace period expired. Restart the server after setting license-key in config.yml.");
            return true;
         } else {
            PlatformScheduler platformScheduler15 = pluginContext1.scheduler();
            Runnable runnable26 = () -> {
               try {
                  pluginContext1.plugin().reloadConfig();
                  pluginContext1.config().reload();
                  pluginContext1.checksConfig().reload();
                  pluginContext1.punishments().reload();
                  pluginContext1.webhookConfig().reload();
                  pluginContext1.language().reload();
                  if (pluginContext1.menuConfig() != null) {
                     pluginContext1.menuConfig().reload();
                  }

                  PlatformScheduler platformScheduler22 = pluginContext1.scheduler();
                  Runnable runnable30 = () -> {
                     try {
                        if (pluginContext1.warnings() != null) {
                           pluginContext1.warnings().rebuildCaches();
                        }

                        if (pluginContext1.violationRepository() != null) {
                           ViolationRepository violationRepository14 = pluginContext1.violationRepository();
                           int i56 = pluginContext1.config().getInt("violation-history.max-per-player", 250);
                           boolean flag79 = pluginContext1.config().getBoolean("violation-history.overwrite-oldest", true);
                           violationRepository14.configure(i56, flag79);
                        }

                        if (pluginContext1.checks() != null) {
                           pluginContext1.checks().refreshEnabled();
                        }

                        if (pluginContext1.regionProtection() != null) {
                           pluginContext1.regionProtection().refreshSettings();
                        }

                        if (pluginContext1.targetTracker() != null) {
                           TargetTracker targetTracker26x = pluginContext1.targetTracker();
                           ConfigurationService configurationService58 = pluginContext1.config();
                           long l88 = 8000L;
                           long l59 = configurationService58.getLong("combat.target-expiry-ms", l88);
                           targetTracker26x.setTargetExpiryMs(l59);
                        }

                        long tmp28xx = RuntimeGate.bootReady(pluginContext1);
                        long l81 = 0L;
                        if (tmp28xx != l81) {
                           if (LicenseBootstrap.completeIfGracePending(pluginContext1)) {
                              Component component75 = ColorUtils.translate("<yellow>License verification started. Detection will enable when verify completes.");
                              commandSender2.sendMessage(component75);
                              pluginContext1.plugin().getLogger().info("License verification started. Detection will enable when verify completes.");
                              return;
                           }

                           long l34 = RuntimeGate.bootReady(pluginContext1);
                           long l83 = 0L;
                           if (l34 != l83) {
                              Component component63 = pluginContext1.language().getMessage("general.reload-success");
                              commandSender2.sendMessage(component63);
                              pluginContext1.plugin()
                                 .getLogger()
                                 .info("Configuration reloaded. Save a license key in config.yml to verify automatically or run /aet reload again.");
                              return;
                           }
                        }

                        Component component72 = pluginContext1.language().getMessage("general.reload-success");
                        commandSender2.sendMessage(component72);
                        pluginContext1.plugin().getLogger().info("Configuration reloaded successfully!");
                     } catch (Exception exception90) {
                        Component component66 = ColorUtils.translate("<#FF3333>An error occurred during reload!");
                        commandSender2.sendMessage(component66);
                        Logger logger45 = pluginContext1.plugin().getLogger();
                        String string69 = "Reload error (sync): " + exception90.getMessage();
                        logger45.severe(string69);
                        exception90.printStackTrace();
                     }
                  };
                  platformScheduler22.runGlobal(runnable30);
               } catch (Exception exception36) {
                  PlatformScheduler platformScheduler25 = pluginContext1.scheduler();
                  Runnable runnable32x = () -> {
                     Component component6 = pluginContext1.language().getMessage("general.reload-failed");
                     commandSender2.sendMessage(component6);
                  };
                  platformScheduler25.runGlobal(runnable32x);
                  Logger logger28x = pluginContext1.plugin().getLogger();
                  String string35 = "Failed to reload configuration: " + exception36.getMessage();
                  logger28x.severe(string35);
                  exception36.printStackTrace();
               }
            };
            platformScheduler15.runAsync(runnable26);
            return true;
         }
      }
   }

   @NotNull
   @Override
   public String name() {
      return "reload";
   }

   @NotNull
   @Override
   public String permission() {
      return "glantielytratarget.reload";
   }

   @NotNull
   @Override
   public String description() {
      return "Reload configuration";
   }
}
