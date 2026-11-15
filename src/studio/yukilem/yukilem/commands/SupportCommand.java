package studio.glace.glantielytratarget.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.libs.nixlib.utils.ColorUtils;

public final class SupportCommand
extends Command {
    private static final String COMMAND_NAME = "elytratarget";
    private static final String ALIAS = "elytraac";
    private static final String[] MESSAGE_LINES;
    private final PluginContext ctx;
    private boolean registered;

    public SupportCommand(@NotNull PluginContext pluginContext) {
        super(COMMAND_NAME, "glAntiElytraTarget plugin information", "/elytratarget", Collections.singletonList(ALIAS));
        this.ctx = pluginContext;
    }

    public void register() {
        if (this.registered) {
            return;
        }
        try {
            CommandMap commandMap;
            CommandMap commandMap2 = commandMap = this.resolveCommandMap();
            if (commandMap2 == null) {
                return;
            }
            CommandMap commandMap3 = commandMap;
            boolean bl = commandMap3.register(COMMAND_NAME, this);
            this.registered = true;
        }
        catch (Exception e) {
            Exception e2 = e;
            Logger logger = this.ctx.plugin().getLogger();
            Exception e3 = e2;
            String string = "Failed to register support command: " + e3.getMessage();
            logger.warning(string);
        }
    }

    public void unregister() {
        if (!this.registered) {
            return;
        }
        try {
            CommandMap commandMap;
            CommandMap commandMap2 = commandMap = this.resolveCommandMap();
            if (commandMap2 == null) {
                return;
            }
            try {
                Field field;
                CommandMap commandMap3 = commandMap;
                Field field2 = field = commandMap3.getClass().getDeclaredField("knownCommands");
                field2.setAccessible(true);
                Field field3 = field;
                CommandMap commandMap4 = commandMap;
                Collection collection = ((Map)field3.get(commandMap4)).values();
                Predicate<Command> predicate = command -> {
                    boolean bl = command == this;
                    return bl;
                };
                boolean bl = collection.removeIf(predicate);
            }
            catch (NoSuchFieldException e) {
                NoSuchFieldException e2 = e;
            }
            CommandMap commandMap5 = commandMap;
            boolean bl = this.unregister(commandMap5);
            this.registered = false;
        }
        catch (Exception e) {
            Exception e3 = e;
            Logger logger = this.ctx.plugin().getLogger();
            Exception e4 = e3;
            String string = "Failed to unregister support command: " + e4.getMessage();
            logger.warning(string);
        }
    }

    @Override
    public boolean isRegistered() {
        return this.registered;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String string, @NotNull String[] stringArray) {
        int n;
        int n2;
        String[] stringArray2;
        String version = this.ctx.plugin().getDescription().getVersion();
        String[] stringArray3 = stringArray2 = MESSAGE_LINES;
        int length = stringArray3.length;
        int n3 = 0;
        while ((n2 = n3) < (n = length)) {
            String[] stringArray4 = stringArray2;
            int n4 = n3++;
            String string2 = stringArray4[n4];
            String string3 = version;
            Component component = ColorUtils.translate(string2.replace("%version%", string3));
            commandSender.sendMessage(component);
        }
        return true;
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String string, @NotNull String[] stringArray) {
        return Collections.emptyList();
    }

    private CommandMap resolveCommandMap() {
        try {
            Object object;
            Class<Bukkit> clazz = Bukkit.class;
            Class[] clazzArray = new Class[]{};
            Method method = clazz.getMethod("getCommandMap", clazzArray);
            Object[] objectArray = new Object[]{};
            Object object2 = object = method.invoke(null, objectArray);
            if (object2 instanceof CommandMap) {
                Object object3 = object;
                CommandMap commandMap = (CommandMap)object3;
                return commandMap;
            }
        }
        catch (NoSuchMethodException e) {
            NoSuchMethodException e2 = e;
        }
        catch (Exception e) {
            Exception e3 = e;
            Logger logger = this.ctx.plugin().getLogger();
            Exception e4 = e3;
            String string = "Bukkit#getCommandMap failed: " + e4.getMessage();
            logger.warning(string);
        }
        try {
            Object object;
            Field field;
            Field field2 = field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field2.setAccessible(true);
            Field field3 = field;
            Server server = Bukkit.getServer();
            Object object4 = object = field3.get(server);
            if (object4 instanceof CommandMap) {
                Object object5 = object;
                CommandMap commandMap = (CommandMap)object5;
                return commandMap;
            }
        }
        catch (Exception e) {
            Exception e5 = e;
            Logger logger = this.ctx.plugin().getLogger();
            Exception e6 = e5;
            String string = "Reflective commandMap fallback failed: " + e6.getMessage();
            logger.warning(string);
        }
        return null;
    }

    static {
        String[] stringArray = new String[]{"", "<dark_gray><strikethrough>-----------------------------------------------------</strikethrough>", "", "  <gradient:#FF3333:#FF5555><bold>glAntiElytraTarget</bold></gradient>", "  <gray>The world's most advanced Elytra Target protection.", "", "  <#6A6A6A>\u25aa <gray>Version: <#BA2525>%version%", "  <#6A6A6A>\u25aa <gray>Author: <#FF3333>Nixoly", "", "  <dark_gray>Links: (Clickable):", "  <#6A6A6A>\u25aa <gray>Purchase: <aqua><underlined><click:open_url:https://builtbybit.com/resources/75799>https://builtbybit.com/resources/75799</click></underlined>", "  <#6A6A6A>\u25aa <gray>Discord: <aqua><underlined><click:open_url:https://discord.gg/mcgnMUqxnD>https://discord.gg/mcgnMUqxnD</click></underlined><aqua>", "", "  <#FF3333>Protecting your server from elytra cheats.", "", "<dark_gray><strikethrough>-----------------------------------------------------</strikethrough>", ""};
        MESSAGE_LINES = stringArray;
    }
}

