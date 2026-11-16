package studio.yukilem.yukilem.punishments;

import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.punishments.ConsoleCommandDeduplicator;
import studio.yukilem.yukilem.punishments.PlaceholderContext;
import studio.yukilem.yukilem.punishments.PunishmentCommand;
import studio.yukilem.yukilem.punishments.ViolationAlertDispatcher;

final class ViolationCommandSink {
    private final PluginContext ctx;
    private final ViolationAlertDispatcher alerts;
    private final ConsoleCommandDeduplicator consoleCommands;

    ViolationCommandSink(@NotNull PluginContext pluginContext, @NotNull ViolationAlertDispatcher violationAlertDispatcher) {
        ConsoleCommandDeduplicator consoleCommandDeduplicator;
        this.consoleCommands = consoleCommandDeduplicator = new ConsoleCommandDeduplicator();
        this.ctx = pluginContext;
        this.alerts = violationAlertDispatcher;
    }

    void execute(@NotNull Player player, @NotNull PunishmentCommand punishmentCommand, @NotNull PlaceholderContext placeholderContext) {
        String string = punishmentCommand.payload();
        String string2 = placeholderContext.render(string);
        switch (punishmentCommand.action()) {
            case ALERT: {
                this.alerts.dispatch(player, placeholderContext);
                break;
            }
            case WEBHOOK: {
                this.dispatchWebhook(placeholderContext);
                break;
            }
            case CONSOLE_COMMAND: {
                String string3 = string2;
                this.dispatchConsole(player, string3, placeholderContext);
            }
        }
    }

    void clear() {
        this.consoleCommands.clear();
    }

    private void dispatchConsole(@NotNull Player player, @NotNull String string, @NotNull PlaceholderContext placeholderContext) {
        ConsoleCommandDeduplicator consoleCommandDeduplicator = this.consoleCommands;
        UUID uniqueId = player.getUniqueId();
        if (!consoleCommandDeduplicator.claim(uniqueId, string)) {
            return;
        }
        PlatformScheduler platformScheduler = this.ctx.scheduler();
        Runnable runnable = () -> {
            try {
                boolean bl = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
                if (bl && ViolationCommandSink.isRemovalCommand(string)) {
                    this.broadcastRemoval(placeholderContext);
                }
            }
            catch (Exception e) {
                Exception e2 = e;
                Logger logger = this.ctx.plugin().getLogger();
                Exception e3 = e2;
                String message = e3.getMessage();
                String string2 = "Failed to execute command: " + string + " (" + message + ")";
                logger.warning(string2);
            }
        };
        if (PunishAnimation.shouldAnimate(this.ctx, string) && player.isOnline()) {
            platformScheduler.runGlobal(() -> {
                if (!player.isOnline()) {
                    runnable.run();
                    return;
                }
                PunishAnimation.begin(this.ctx, player, placeholderContext, runnable);
            });
            return;
        }
        platformScheduler.runGlobal(runnable);
    }

    private static boolean isRemovalCommand(@NotNull String command) {
        String name = command.trim().toLowerCase(java.util.Locale.ROOT).split("[ \t]+", 2)[0];
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        int colon = name.indexOf(':');
        if (colon >= 0) {
            name = name.substring(colon + 1);
        }
        return name.equals("kick") || name.equals("ban") || name.equals("tempban") || name.equals("ban-ip") || name.equals("banip");
    }

    private void broadcastRemoval(@NotNull PlaceholderContext placeholderContext) {
        studio.yukilem.yukilem.config.settings.AutoPunishSettings settings = this.ctx.config().getAutoPunish();
        if (!settings.broadcastEnabled()) {
            return;
        }
        net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer serializer = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.builder().character('&').hexColors().build();
        for (String line : settings.broadcastLines()) {
            Bukkit.broadcast(serializer.deserialize(placeholderContext.render(line)));
        }
    }

    private void dispatchWebhook(@NotNull PlaceholderContext placeholderContext) {
        if (this.ctx.discord() == null || !this.ctx.discord().isEnabled()) {
            return;
        }
        PlatformScheduler platformScheduler = this.ctx.scheduler();
        Runnable runnable = () -> this.ctx.discord().sendViolation(placeholderContext);
        platformScheduler.runAsync(runnable);
    }
}

