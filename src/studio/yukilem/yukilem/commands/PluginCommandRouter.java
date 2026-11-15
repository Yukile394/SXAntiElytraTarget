package studio.yukilem.yukilem.commands;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.commands.SubCommand;
import studio.yukilem.yukilem.commands.impl.AlertSubCommand;
import studio.yukilem.yukilem.commands.impl.DataSubCommand;
import studio.yukilem.yukilem.commands.impl.HelpSubCommand;
import studio.yukilem.yukilem.commands.impl.ProfileSubCommand;
import studio.yukilem.yukilem.commands.impl.ReloadSubCommand;
import studio.yukilem.yukilem.config.settings.CommandSettings;
import studio.yukilem.yukilem.libs.nixlib.command.CommandAction;
import studio.yukilem.yukilem.libs.nixlib.command.CommandContext;
import studio.yukilem.yukilem.libs.nixlib.command.CommandSpec;
import studio.yukilem.yukilem.libs.nixlib.command.SuggestionProvider;
import studio.yukilem.yukilem.platform.runtime.RuntimeGate;

public final class PluginCommandRouter {
    private final PluginContext ctx;
    private final CommandSettings names;
    private final SubCommand alerts;
    private final SubCommand reload;
    private final SubCommand help;
    private final SubCommand profile;
    private final SubCommand data;

    public PluginCommandRouter(@NotNull PluginContext pluginContext) {
        CommandSettings commandSettings;
        AlertSubCommand alertSubCommand = new AlertSubCommand();
        this.alerts = alertSubCommand;
        ReloadSubCommand reloadSubCommand = new ReloadSubCommand();
        this.reload = reloadSubCommand;
        HelpSubCommand helpSubCommand = new HelpSubCommand();
        this.help = helpSubCommand;
        ProfileSubCommand profileSubCommand = new ProfileSubCommand();
        this.profile = profileSubCommand;
        DataSubCommand dataSubCommand = new DataSubCommand();
        this.data = dataSubCommand;
        this.ctx = pluginContext;
        this.names = commandSettings = pluginContext.commandSettings();
    }

    @NotNull
    public CommandSpec spec() {
        CommandSpec.Builder builder = CommandSpec.builder(this.names.name());
        List<String> list = this.names.aliases();
        CommandSpec.Builder builder2 = builder.aliases(list).description("Main command for glAntiElytraTarget");
        CommandAction commandAction = this::usage;
        CommandSpec.Builder builder3 = builder2.onRoot(commandAction);
        CommandAction commandAction2 = this::usage;
        CommandSpec.Builder builder4 = builder3.onUnknown(commandAction2);
        CommandAction commandAction3 = commandContext -> {
            CommandSender commandSender = commandContext.sender();
            Component message = this.ctx.language().getMessage("general.no-permission");
            commandSender.sendMessage(message);
        };
        CommandSpec.Builder builder5 = builder4.onPermissionDenied(commandAction3);
        CommandSpec.Sub.Builder builder6 = CommandSpec.sub(this.names.alerts());
        String string = this.alerts.permission();
        CommandSpec.Sub.Builder builder7 = builder6.permission(string);
        CommandAction commandAction4 = commandContext -> {
            SubCommand subCommand = this.alerts;
            this.dispatch(subCommand, commandContext);
        };
        CommandSpec.Sub.Builder builder8 = builder7.action(commandAction4);
        CommandSpec.Builder builder9 = builder5.sub(builder8);
        CommandSpec.Sub.Builder builder10 = CommandSpec.sub(this.names.reload());
        String string2 = this.reload.permission();
        CommandSpec.Sub.Builder builder11 = builder10.permission(string2);
        CommandAction commandAction5 = commandContext -> {
            SubCommand subCommand = this.reload;
            this.dispatch(subCommand, commandContext);
        };
        CommandSpec.Sub.Builder builder12 = builder11.action(commandAction5);
        CommandSpec.Builder builder13 = builder9.sub(builder12);
        CommandSpec.Sub.Builder builder14 = CommandSpec.sub(this.names.help());
        String string3 = this.help.permission();
        CommandSpec.Sub.Builder builder15 = builder14.permission(string3);
        CommandAction commandAction6 = commandContext -> {
            SubCommand subCommand = this.help;
            this.dispatch(subCommand, commandContext);
        };
        CommandSpec.Sub.Builder builder16 = builder15.action(commandAction6);
        CommandSpec.Builder builder17 = builder13.sub(builder16);
        CommandSpec.Sub.Builder builder18 = CommandSpec.sub(this.names.profile());
        String string4 = this.profile.permission();
        CommandSpec.Sub.Builder builder19 = builder18.permission(string4);
        SuggestionProvider suggestionProvider = commandContext -> this.flaggedPlayerNames();
        CommandSpec.Sub.Builder builder20 = builder19.arg(suggestionProvider);
        CommandAction commandAction7 = commandContext -> {
            SubCommand subCommand = this.profile;
            this.dispatch(subCommand, commandContext);
        };
        CommandSpec.Sub.Builder builder21 = builder20.action(commandAction7);
        CommandSpec.Builder builder22 = builder17.sub(builder21);
        CommandSpec.Sub.Builder builder23 = CommandSpec.sub(this.names.data());
        String string5 = this.data.permission();
        CommandSpec.Sub.Builder builder24 = builder23.permission(string5);
        List<String> list2 = List.of(this.names.dataSub().reset());
        CommandSpec.Sub.Builder builder25 = builder24.arg(list2);
        SuggestionProvider suggestionProvider2 = commandContext -> this.names.dataTypeSuggestions();
        CommandSpec.Sub.Builder builder26 = builder25.arg(suggestionProvider2);
        SuggestionProvider suggestionProvider3 = commandContext -> this.dataTargetSuggestions();
        CommandSpec.Sub.Builder builder27 = builder26.arg(suggestionProvider3);
        List<String> list3 = List.of(this.names.dataSub().confirm());
        CommandSpec.Sub.Builder builder28 = builder27.arg(list3);
        CommandAction commandAction8 = commandContext -> {
            SubCommand subCommand = this.data;
            this.dispatch(subCommand, commandContext);
        };
        CommandSpec.Sub.Builder builder29 = builder28.action(commandAction8);
        return builder22.sub(builder29).build();
    }

    @NotNull
    private List<String> flaggedPlayerNames() {
        if (this.ctx.flagRepository() == null) {
            return List.of();
        }
        return this.ctx.flagRepository().knownNames();
    }

    @NotNull
    private List<String> dataTargetSuggestions() {
        ArrayList<String> arrayList;
        ArrayList<String> arrayList2;
        List<String> list = this.names.allTargetSuggestions();
        ArrayList<String> arrayList3 = arrayList2 = (arrayList = new ArrayList<String>(list));
        List<String> list2 = this.flaggedPlayerNames();
        boolean bl = arrayList3.addAll(list2);
        ArrayList<String> arrayList4 = arrayList2;
        return arrayList4;
    }

    private void dispatch(@NotNull SubCommand subCommand, @NotNull CommandContext commandContext) {
        String string = subCommand.name();
        boolean bl = "reload".equalsIgnoreCase(string);
        if (this.blocked(bl)) {
            return;
        }
        PluginContext pluginContext = this.ctx;
        CommandSender commandSender = commandContext.sender();
        String[] stringArray = commandContext.args();
        boolean bl2 = subCommand.execute(pluginContext, commandSender, stringArray);
    }

    private void usage(@NotNull CommandContext commandContext) {
        if (this.blocked(false)) {
            return;
        }
        PluginContext pluginContext = this.ctx;
        CommandSender commandSender = commandContext.sender();
        HelpSubCommand.sendHelp(pluginContext, commandSender);
    }

    private boolean blocked(boolean bl) {
        long l;
        long l2;
        long l3;
        long l4;
        long l5 = 0L;
        if (bl) {
            long l6 = RuntimeGate.gracePending(this.ctx.runtime());
            long l7 = RuntimeGate.graceExpired(this.ctx);
            long l8 = l6;
            long l9 = 0L;
            if (l8 != l9) {
                long l10;
                l5 = l10 = l6;
            } else {
                long l11 = l7;
                long l12 = 0L;
                if (l11 != l12) {
                    long l13;
                    l5 = l13 = l7;
                }
            }
        }
        boolean bl2 = (l4 = RuntimeGate.detectionFold(this.ctx)) != (l3 = 0L) && (l2 = l5) == (l = 0L);
        return bl2;
    }
}

