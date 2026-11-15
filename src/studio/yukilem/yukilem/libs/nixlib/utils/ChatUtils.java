package studio.glace.glantielytratarget.libs.nixlib.utils;

import java.time.Duration;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import studio.glace.glantielytratarget.libs.nixlib.utils.ColorUtils;
import studio.glace.glantielytratarget.libs.nixlib.utils.StringUtils;

public final class ChatUtils {
    private ChatUtils() {
    }

    public static void send(CommandSender commandSender, String string) {
        if (commandSender == null || string == null) {
            return;
        }
        if (string.isBlank()) {
            commandSender.sendMessage(Component.empty());
            return;
        }
        commandSender.sendMessage(ColorUtils.parse(string));
    }

    public static void send(CommandSender commandSender, Component component) {
        if (commandSender == null || component == null) {
            return;
        }
        commandSender.sendMessage(component);
    }

    public static void send(CommandSender commandSender, List<String> list) {
        if (commandSender == null || list == null) {
            return;
        }
        for (String string : list) {
            ChatUtils.send(commandSender, string);
        }
    }

    public static boolean isDisabled(String string) {
        return string == null || string.isEmpty();
    }

    public static void sendIfPresent(CommandSender commandSender, String string) {
        if (commandSender == null || ChatUtils.isDisabled(string)) {
            return;
        }
        commandSender.sendMessage(ColorUtils.parse(string));
    }

    public static void sendTranslatedIfPresent(CommandSender commandSender, String string) {
        if (commandSender == null || ChatUtils.isDisabled(string)) {
            return;
        }
        commandSender.sendMessage(ColorUtils.translate(string));
    }

    public static void sendTranslatedIfPresent(CommandSender commandSender, List<String> list) {
        if (commandSender == null || list == null) {
            return;
        }
        for (String string : list) {
            ChatUtils.sendTranslatedIfPresent(commandSender, string);
        }
    }

    public static void sendTranslated(CommandSender commandSender, String string) {
        if (commandSender == null || string == null) {
            return;
        }
        if (string.isBlank()) {
            commandSender.sendMessage(Component.empty());
            return;
        }
        commandSender.sendMessage(ColorUtils.translate(string));
    }

    public static void sendTranslatedAll(CommandSender commandSender, List<String> list) {
        if (commandSender == null || list == null) {
            return;
        }
        for (String string : list) {
            ChatUtils.sendTranslated(commandSender, string);
        }
    }

    public static void broadcast(String string) {
        if (string == null) {
            return;
        }
        ChatUtils.broadcast(string.isBlank() ? Component.empty() : ColorUtils.parse(string));
    }

    public static void broadcast(Component component) {
        if (component == null) {
            return;
        }
        Bukkit.getServer().sendMessage(component);
    }

    public static void broadcastTranslated(String string) {
        if (string == null) {
            return;
        }
        ChatUtils.broadcast(string.isBlank() ? Component.empty() : ColorUtils.translate(string));
    }

    public static void broadcastWithPermission(String string, String string2) {
        if (string2 == null) {
            return;
        }
        Component component = string2.isBlank() ? Component.empty() : ColorUtils.parse(string2);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(string)) continue;
            player.sendMessage(component);
        }
    }

    public static void broadcastTranslatedWithPermission(String string, String string2) {
        if (string2 == null) {
            return;
        }
        Component component = string2.isBlank() ? Component.empty() : ColorUtils.translate(string2);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(string)) continue;
            player.sendMessage(component);
        }
    }

    public static void title(Player player, String string, String string2, int n, int n2, int n3) {
        if (player == null) {
            return;
        }
        Component component = ColorUtils.parse(string == null ? "" : string);
        Component component2 = ColorUtils.parse(string2 == null ? "" : string2);
        Title.Times times = Title.Times.times(ChatUtils.ticksToDuration(n), ChatUtils.ticksToDuration(n2), ChatUtils.ticksToDuration(n3));
        player.showTitle(Title.title(component, component2, times));
    }

    public static void title(Player player, Component component, Component component2, int n, int n2, int n3) {
        if (player == null) {
            return;
        }
        Title.Times times = Title.Times.times(ChatUtils.ticksToDuration(n), ChatUtils.ticksToDuration(n2), ChatUtils.ticksToDuration(n3));
        player.showTitle(Title.title(component == null ? Component.empty() : component, component2 == null ? Component.empty() : component2, times));
    }

    public static void actionBar(Player player, String string) {
        if (player == null || StringUtils.isBlank(string)) {
            return;
        }
        player.sendActionBar(ColorUtils.parse(string));
    }

    public static void actionBar(Player player, Component component) {
        if (player == null || component == null) {
            return;
        }
        player.sendActionBar(component);
    }

    private static Duration ticksToDuration(int n) {
        return Duration.ofMillis((long)Math.max(0, n) * 50L);
    }
}

