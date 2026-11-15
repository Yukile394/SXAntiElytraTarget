package studio.glace.glantielytratarget.commands.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.commands.CommandMessages;
import studio.glace.glantielytratarget.commands.CommandPlaceholders;
import studio.glace.glantielytratarget.commands.SubCommand;
import studio.glace.glantielytratarget.config.LanguageService;
import studio.glace.glantielytratarget.config.settings.CommandSettings;
import studio.glace.glantielytratarget.data.FlagRepository;
import studio.glace.glantielytratarget.menu.ConfirmRequest;
import studio.glace.glantielytratarget.menu.MenuManager;

public final class DataSubCommand
implements SubCommand {
    @Override
    public boolean execute(@NotNull PluginContext pluginContext, @NotNull CommandSender commandSender, @NotNull String[] stringArray) {
        String string;
        CommandSettings commandSettings;
        String string2;
        boolean bl;
        String string3;
        String string4;
        CommandSettings commandSettings2;
        CommandSettings commandSettings3 = pluginContext.commandSettings();
        CommandSettings commandSettings4 = commandSettings3;
        if (commandSettings4 == null) {
            return true;
        }
        String string5 = this.permission();
        if (!commandSender.hasPermission(string5)) {
            Component message = pluginContext.language().getMessage("general.no-permission");
            commandSender.sendMessage(message);
            return true;
        }
        if (pluginContext.flagRepository() == null || pluginContext.violationRepository() == null) {
            Component message = pluginContext.language().getMessage("error", "&cPlugin is still initializing. Please try again shortly.");
            commandSender.sendMessage(message);
            return true;
        }
        if (stringArray.length < 4 || !(commandSettings2 = commandSettings3).isDataReset(string4 = stringArray[1])) {
            this.sendUsage(pluginContext, commandSender);
            return true;
        }
        CommandSettings commandSettings5 = commandSettings3;
        String string6 = stringArray[2];
        String string7 = commandSettings5.canonicalDataType(string6);
        String string8 = string7;
        if (string8 == null) {
            this.sendUsage(pluginContext, commandSender);
            return true;
        }
        String string9 = stringArray[2].replace('_', ' ');
        String string10 = stringArray[3];
        CommandSettings commandSettings6 = commandSettings3;
        String string11 = string10;
        boolean allTarget = commandSettings6.isAllTarget(string11);
        UUID uUID = null;
        String string12 = string3 = string10;
        boolean bl2 = allTarget;
        if (!bl2) {
            FlagRepository flagRepository = pluginContext.flagRepository();
            String string13 = string10;
            Player player2 = Bukkit.getPlayerExact(string13);
            Player player3 = player2;
            if (player3 != null) {
                Player player4 = player2;
                uUID = player4.getUniqueId();
                Player player5 = player2;
                string12 = player5.getName();
            } else {
                FlagRepository flagRepository2 = flagRepository;
                String string14 = string10;
                uUID = flagRepository2.uuidForName(string14);
                UUID uUID2 = uUID;
                if (uUID2 == null) {
                    LanguageService languageService = pluginContext.language();
                    String string15 = string10;
                    Map<String, String> map = Map.of("%player%", string15);
                    Component component = languageService.message("data-reset.no-data", map);
                    commandSender.sendMessage(component);
                    return true;
                }
                FlagRepository flagRepository3 = flagRepository;
                UUID uUID3 = uUID;
                String string16 = flagRepository3.nameForUuid(uUID3);
                String string17 = string16;
                if (string17 != null) {
                    String string18;
                    string12 = string18 = string16;
                }
            }
        }
        if (bl = allTarget) {
            String rawMessage = pluginContext.language().getRawMessage("data-reset.target-all");
            CommandSettings commandSettings7 = commandSettings3;
            String string19 = commandSettings7.dataSub().all();
            String string20 = rawMessage.replace("%all%", string19);
            CommandSettings commandSettings8 = commandSettings3;
            String string21 = commandSettings8.dataSub().allAlias();
            string2 = string20.replace("%all_alias%", string21);
        } else {
            string2 = string12;
        }
        String string22 = string2;
        if (commandSender instanceof Player) {
            ConfirmRequest confirmRequest;
            String string23;
            UUID uUID4;
            Player player6 = (Player)commandSender;
            if (pluginContext.menus() == null) {
                Component message = pluginContext.language().getMessage("error", "&cPlugin is still initializing. Please try again shortly.");
                commandSender.sendMessage(message);
                return true;
            }
            String rawMessage = pluginContext.language().getRawMessage("menu.confirm-data-reset");
            String string24 = string9;
            String string25 = rawMessage.replace("%type%", string24);
            String string26 = string22;
            String string27 = string25.replace("%target%", string26);
            UUID uUID5 = uUID4 = uUID;
            String string28 = string23 = string12;
            String string29 = string27;
            String string30 = string7;
            boolean bl3 = allTarget;
            UUID uUID6 = uUID5;
            String string31 = string9;
            String string32 = string28;
            Consumer<Player> consumer = player -> {
                player.closeInventory();
                this.applyReset(pluginContext, string30, bl3, uUID6);
                this.sendSuccess(pluginContext, (CommandSender)player, string31, bl3, string32);
            };
            Consumer<Player> consumer2 = HumanEntity::closeInventory;
            ConfirmRequest confirmRequest2 = confirmRequest = new ConfirmRequest(string29, consumer, consumer2);
            MenuManager menuManager = pluginContext.menus();
            Player player7 = player6;
            ConfirmRequest confirmRequest3 = confirmRequest2;
            menuManager.openConfirm(player7, confirmRequest3);
            return true;
        }
        boolean bl4 = stringArray.length >= 5 && (commandSettings = commandSettings3).isDataConfirm(string = stringArray[4]);
        boolean bl5 = bl4;
        boolean bl6 = bl5;
        if (!bl6) {
            HashMap<String, String> hashMap;
            HashMap<String, String> hashMap2;
            Map<String, String> map = CommandPlaceholders.command(pluginContext);
            HashMap<String, String> hashMap3 = hashMap2 = (hashMap = new HashMap<String, String>(map));
            String string33 = string9;
            String string34 = hashMap3.put("%type%", string33);
            HashMap<String, String> hashMap4 = hashMap2;
            String string35 = string10;
            String string36 = hashMap4.put("%target%", string35);
            LanguageService languageService = pluginContext.language();
            HashMap<String, String> hashMap5 = hashMap2;
            Component component = languageService.message("data-reset.console-confirm", hashMap5);
            commandSender.sendMessage(component);
            return true;
        }
        String string37 = string7;
        boolean bl7 = allTarget;
        UUID uUID7 = uUID;
        this.applyReset(pluginContext, string37, bl7, uUID7);
        String string38 = string9;
        boolean bl8 = allTarget;
        String string39 = string12;
        this.sendSuccess(pluginContext, commandSender, string38, bl8, string39);
        return true;
    }

    private void applyReset(@NotNull PluginContext pluginContext, @NotNull String string, boolean bl, @Nullable UUID uUID) {
        block5: {
            block4: {
                boolean bl2;
                block3: {
                    bl2 = "violations".equals(string);
                    if (!bl) break block3;
                    boolean bl3 = bl2;
                    if (!bl3) break block4;
                    pluginContext.violationRepository().clearAll();
                    break block5;
                }
                if (uUID == null) {
                    return;
                }
                boolean bl4 = bl2;
                if (bl4) {
                    pluginContext.violationRepository().clear(uUID);
                } else {
                    pluginContext.flagRepository().resetTotalViolations(uUID);
                }
                return;
            }
            pluginContext.flagRepository().resetAll();
        }
    }

    private void sendSuccess(@NotNull PluginContext pluginContext, @NotNull CommandSender commandSender, @NotNull String string, boolean bl, @NotNull String string2) {
        HashMap<String, String> hashMap;
        if (bl) {
            LanguageService languageService = pluginContext.language();
            Map<String, String> map = Map.of("%type%", string);
            Component component = languageService.message("data-reset.success-all", map);
            commandSender.sendMessage(component);
            return;
        }
        HashMap<String, String> hashMap2 = hashMap = new HashMap<String, String>();
        String string3 = hashMap2.put("%type%", string);
        HashMap<String, String> hashMap3 = hashMap;
        String string4 = hashMap3.put("%player%", string2);
        LanguageService languageService = pluginContext.language();
        HashMap<String, String> hashMap4 = hashMap;
        Component component = languageService.message("data-reset.success-player", hashMap4);
        commandSender.sendMessage(component);
    }

    private void sendUsage(@NotNull PluginContext pluginContext, @NotNull CommandSender commandSender) {
        CommandMessages.incorrectArgument(pluginContext, commandSender, "commands.usage.data-reset");
    }

    @Override
    @NotNull
    public String name() {
        return "data";
    }

    @Override
    @NotNull
    public String permission() {
        return "glantielytratarget.data.reset";
    }

    @Override
    @NotNull
    public String description() {
        return "Reset stored violation data";
    }
}

