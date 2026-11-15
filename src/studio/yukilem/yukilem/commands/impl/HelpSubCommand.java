package studio.yukilem.yukilem.commands.impl;

import java.util.Iterator;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.commands.CommandPlaceholders;
import studio.yukilem.yukilem.commands.SubCommand;
import studio.yukilem.yukilem.config.LanguageService;

public final class HelpSubCommand
implements SubCommand {
    public static void sendHelp(@NotNull PluginContext pluginContext, @NotNull CommandSender commandSender) {
        Iterator<Component> iterator;
        LanguageService languageService = pluginContext.language();
        Map<String, String> map = CommandPlaceholders.command(pluginContext);
        Iterator<Component> iterator2 = languageService.messageList("commands.main.usage", map).iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Component> iterator3 = iterator2;
            Component component = iterator3.next();
            commandSender.sendMessage(component);
        }
    }

    @Override
    public boolean execute(@NotNull PluginContext pluginContext, @NotNull CommandSender commandSender, @NotNull String[] stringArray) {
        HelpSubCommand.sendHelp(pluginContext, commandSender);
        return true;
    }

    @Override
    @NotNull
    public String name() {
        return "help";
    }

    @Override
    @NotNull
    public String permission() {
        return "glantielytratarget.help";
    }

    @Override
    @NotNull
    public String description() {
        return "Show help message";
    }
}

