package studio.yukilem.yukilem.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;

public interface SubCommand {
    public boolean execute(@NotNull PluginContext pluginContext1, @NotNull CommandSender commandSender2, @NotNull String[] strings3);

    @NotNull
    public String name();

    @NotNull
    public String permission();

    @NotNull
    public String description();
}

