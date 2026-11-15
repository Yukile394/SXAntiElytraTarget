package studio.glace.glantielytratarget.commands.impl;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.alerts.AlertService;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.commands.SubCommand;
import studio.glace.glantielytratarget.config.LanguageService;

public final class AlertSubCommand
implements SubCommand {
    @Override
    public boolean execute(@NotNull PluginContext pluginContext, @NotNull CommandSender commandSender, @NotNull String[] stringArray) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            Player player2 = player;
            String string = this.permission();
            if (!player2.hasPermission(string)) {
                Player player3 = player;
                Component message = pluginContext.language().getMessage("general.no-permission");
                player3.sendMessage(message);
                return true;
            }
            if (pluginContext.alerts() == null) {
                Player player4 = player;
                Component message = pluginContext.language().getMessage("error", "&cPlugin is still initializing. Please try again shortly.");
                player4.sendMessage(message);
                return true;
            }
            try {
                AlertService alertService = pluginContext.alerts();
                Player player5 = player;
                UUID uniqueId = player5.getUniqueId();
                alertService.toggleAlerts(uniqueId);
                AlertService alertService2 = pluginContext.alerts();
                Player player6 = player;
                UUID uniqueId2 = player6.getUniqueId();
                if (alertService2.hasAlertsEnabled(uniqueId2)) {
                    Player player7 = player;
                    Component message = pluginContext.language().getMessage("alerts.enabled");
                    player7.sendMessage(message);
                } else {
                    Player player8 = player;
                    Component message = pluginContext.language().getMessage("alerts.disabled");
                    player8.sendMessage(message);
                }
            }
            catch (Exception e) {
                Exception e2 = e;
                Player player9 = player;
                LanguageService languageService = pluginContext.language();
                Exception e3 = e2;
                String string2 = "&cAn error occurred: " + e3.getMessage();
                Component message = languageService.getMessage("error", string2);
                player9.sendMessage(message);
            }
            return true;
        }
        Component message = pluginContext.language().getMessage("general.players-only");
        commandSender.sendMessage(message);
        return true;
    }

    @Override
    @NotNull
    public String name() {
        return "alerts";
    }

    @Override
    @NotNull
    public String permission() {
        return "glantielytratarget.alerts";
    }

    @Override
    @NotNull
    public String description() {
        return "Toggle alerts on/off";
    }
}

