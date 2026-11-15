package studio.glace.glantielytratarget.libs.nixlib.command;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.libs.nixlib.command.CommandSpec;
import studio.glace.glantielytratarget.libs.nixlib.command.NixCommand;

public final class CommandRegistrar {
    private CommandRegistrar() {
    }

    public static boolean register(@NotNull Plugin plugin, @NotNull CommandSpec commandSpec) {
        CommandMap commandMap = CommandRegistrar.commandMap(plugin);
        if (commandMap == null) {
            plugin.getLogger().warning("Could not resolve the server command map; /" + commandSpec.label() + " was not registered.");
            return false;
        }
        CommandRegistrar.unregister(plugin, commandSpec.label(), commandSpec.aliases());
        NixCommand nixCommand = new NixCommand(commandSpec);
        if (!commandMap.register(CommandRegistrar.fallbackPrefix(plugin), nixCommand)) {
            plugin.getLogger().info("/" + commandSpec.label() + " was already taken; it is also reachable as /" + CommandRegistrar.fallbackPrefix(plugin) + ":" + commandSpec.label() + ".");
        }
        CommandRegistrar.syncCommands();
        return true;
    }

    public static void unregister(@NotNull Plugin plugin, @NotNull CommandSpec commandSpec) {
        CommandRegistrar.unregister(plugin, commandSpec.label(), commandSpec.aliases());
    }

    public static void unregister(@NotNull Plugin plugin, @NotNull String string, @NotNull List<String> list) {
        CommandMap commandMap = CommandRegistrar.commandMap(plugin);
        if (commandMap == null) {
            return;
        }
        Map<String, Command> map = CommandRegistrar.knownCommands(commandMap);
        if (map == null) {
            return;
        }
        String string2 = CommandRegistrar.fallbackPrefix(plugin);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(string);
        arrayList.addAll(list);
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String lowerCase = ((String)iterator.next()).toLowerCase(Locale.ROOT);
            CommandRegistrar.removeIfOurs(map, lowerCase);
            CommandRegistrar.removeIfOurs(map, string2 + ":" + lowerCase);
        }
        CommandRegistrar.syncCommands();
    }

    private static void removeIfOurs(@NotNull Map<String, Command> map, @NotNull String string) {
        Command command = map.get(string);
        if (command instanceof NixCommand) {
            map.remove(string);
            command.unregister(null);
        }
    }

    private static String fallbackPrefix(@NotNull Plugin plugin) {
        return plugin.getName().toLowerCase(Locale.ROOT);
    }

    private static CommandMap commandMap(@NotNull Plugin plugin) {
        Object object;
        AccessibleObject accessibleObject;
        try {
            accessibleObject = Bukkit.getServer().getClass().getMethod("getCommandMap", new Class[0]);
            object = ((Method)accessibleObject).invoke(Bukkit.getServer(), new Object[0]);
            if (object instanceof CommandMap) {
                CommandMap commandMap = (CommandMap)object;
                return commandMap;
            }
        }
        catch (Throwable e) {
            // empty catch block
        }
        try {
            accessibleObject = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            ((Field)accessibleObject).setAccessible(true);
            object = ((Field)accessibleObject).get(Bukkit.getPluginManager());
            if (object instanceof CommandMap) {
                CommandMap commandMap = (CommandMap)object;
                return commandMap;
            }
        }
        catch (Throwable e) {
            plugin.getLogger().warning("Command map lookup failed: " + e.getMessage());
        }
        return null;
    }

    private static Map<String, Command> knownCommands(@NotNull CommandMap commandMap) {
        Class<?> clazz = commandMap.getClass();
        while (clazz != null) {
            try {
                Field declaredField = clazz.getDeclaredField("knownCommands");
                declaredField.setAccessible(true);
                Object object = declaredField.get(commandMap);
                if (!(object instanceof Map)) continue;
                return (Map)object;
            }
            catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
            catch (Throwable e) {
                return null;
            }
        }
        return null;
    }

    private static void syncCommands() {
        try {
            Bukkit.getServer().getClass().getMethod("syncCommands", new Class[0]).invoke((Object)Bukkit.getServer(), new Object[0]);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }
}

