package studio.glace.glantielytratarget.libs.nixlib.menu;

import java.util.Iterator;
import java.util.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import studio.glace.glantielytratarget.libs.nixlib.gui.Gui;
import studio.glace.glantielytratarget.libs.nixlib.gui.GuiNavigation;
import studio.glace.glantielytratarget.libs.nixlib.gui.PagedGui;
import studio.glace.glantielytratarget.libs.nixlib.menu.ActionRegistry;
import studio.glace.glantielytratarget.libs.nixlib.menu.Integrations;
import studio.glace.glantielytratarget.libs.nixlib.menu.Warnings;
import studio.glace.glantielytratarget.libs.nixlib.utils.ChatUtils;
import studio.glace.glantielytratarget.libs.nixlib.utils.ColorUtils;
import studio.glace.glantielytratarget.libs.nixlib.utils.SoundUtils;

final class DefaultActions {
    private DefaultActions() {
    }

    static void install(ActionRegistry actionRegistry, Plugin plugin) {
        Warnings.logger(plugin.getLogger());
        Integrations.registerBungee(plugin);
        actionRegistry.register("player", (actionContext, string) -> actionContext.player().performCommand(DefaultActions.stripSlash(string)));
        actionRegistry.register("commandevent", (actionContext, string) -> actionContext.player().performCommand(DefaultActions.stripSlash(string)));
        actionRegistry.register("console", (actionContext, string) -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), DefaultActions.stripSlash(string)));
        actionRegistry.register("chat", (actionContext, string) -> actionContext.player().chat(string));
        actionRegistry.register("message", (actionContext, string) -> actionContext.player().sendMessage(ColorUtils.translate(string)));
        actionRegistry.register("minimessage", (actionContext, string) -> actionContext.player().sendMessage(ColorUtils.translate(string)));
        actionRegistry.register("broadcast", (actionContext, string) -> ChatUtils.broadcast(ColorUtils.translate(string)));
        actionRegistry.register("minibroadcast", (actionContext, string) -> ChatUtils.broadcast(ColorUtils.translate(string)));
        actionRegistry.register("json", (actionContext, string) -> actionContext.player().sendMessage(DefaultActions.json(string)));
        actionRegistry.register("jsonbroadcast", (actionContext, string) -> Bukkit.getServer().sendMessage(DefaultActions.json(string)));
        actionRegistry.register("placeholder", (actionContext, string) -> Integrations.parsePlaceholders(actionContext.player(), string));
        actionRegistry.register("sound", (actionContext, string) -> DefaultActions.playSound(actionContext.player(), string));
        actionRegistry.register("broadcastsound", (actionContext, string) -> {
            Iterator<? extends Player> iterator = Bukkit.getOnlinePlayers().iterator();
            while (iterator.hasNext()) {
                DefaultActions.playSound(iterator.next(), string);
            }
        });
        actionRegistry.register("broadcastsoundworld", (actionContext, string) -> {
            Iterator<Player> iterator = actionContext.player().getWorld().getPlayers().iterator();
            while (iterator.hasNext()) {
                DefaultActions.playSound(iterator.next(), string);
            }
        });
        actionRegistry.register("close", (actionContext, string) -> {
            GuiNavigation.suppressBack(actionContext.player().getUniqueId());
            actionContext.player().closeInventory();
        });
        actionRegistry.register("refresh", (actionContext, string) -> {});
        actionRegistry.register("openguimenu", (actionContext, string) -> {});
        actionRegistry.register("nextpage", (actionContext, string) -> {
            Gui gui = actionContext.gui();
            if (gui instanceof PagedGui) {
                ((PagedGui)gui).nextPage();
            }
        });
        actionRegistry.register("prevpage", (actionContext, string) -> {
            Gui gui = actionContext.gui();
            if (gui instanceof PagedGui) {
                ((PagedGui)gui).previousPage();
            }
        });
        actionRegistry.register("previouspage", (actionContext, string) -> {
            Gui gui = actionContext.gui();
            if (gui instanceof PagedGui) {
                ((PagedGui)gui).previousPage();
            }
        });
        actionRegistry.register("connect", (actionContext, string) -> Integrations.connect(plugin, actionContext.player(), string));
        actionRegistry.register("givemoney", (actionContext, string) -> Integrations.money(actionContext.player(), DefaultActions.amount(string), true));
        actionRegistry.register("takemoney", (actionContext, string) -> Integrations.money(actionContext.player(), DefaultActions.amount(string), false));
        actionRegistry.register("givepermission", (actionContext, string) -> Integrations.permission(actionContext.player(), string.trim(), true));
        actionRegistry.register("takepermission", (actionContext, string) -> Integrations.permission(actionContext.player(), string.trim(), false));
        actionRegistry.register("giveexp", (actionContext, string) -> DefaultActions.exp(actionContext.player(), string, true));
        actionRegistry.register("takeexp", (actionContext, string) -> DefaultActions.exp(actionContext.player(), string, false));
        actionRegistry.register("meta", (actionContext, string) -> DefaultActions.meta(plugin, actionContext.player(), string));
    }

    private static String stripSlash(String string) {
        String string2 = string.trim();
        return string2.startsWith("/") ? string2.substring(1) : string2;
    }

    private static Component json(String string) {
        try {
            return GsonComponentSerializer.gson().deserialize(string);
        }
        catch (Throwable e) {
            return ColorUtils.translate(string);
        }
    }

    private static void playSound(Player player, String string) {
        String[] stringArray = string.trim().split("\\s+");
        if (stringArray.length == 0 || stringArray[0].isEmpty()) {
            return;
        }
        float f = stringArray.length > 1 ? DefaultActions.parseFloat(stringArray[1], 1.0f) : 1.0f;
        float f2 = stringArray.length > 2 ? DefaultActions.parseFloat(stringArray[2], 1.0f) : 1.0f;
        SoundUtils.playSound(player, stringArray[0], f, f2);
    }

    private static void exp(Player player, String string, boolean bl) {
        int n;
        int n2;
        String string2 = string.trim();
        boolean bl2 = string2.toLowerCase(Locale.ROOT).endsWith("l");
        if (bl2) {
            string2 = string2.substring(0, string2.length() - 1).trim();
        }
        if ((n2 = (int)DefaultActions.parseDouble(string2, 0.0)) == 0) {
            return;
        }
        int n3 = n = bl ? n2 : -n2;
        if (bl2) {
            player.giveExpLevels(n);
        } else {
            player.giveExp(n);
        }
    }

    private static void meta(Plugin plugin, Player player, String string) {
        String[] stringArray = string.trim().split("\\s+", 4);
        if (stringArray.length < 3) {
            return;
        }
        String lowerCase = stringArray[0].toLowerCase(Locale.ROOT);
        NamespacedKey namespacedKey = new NamespacedKey(plugin, DefaultActions.sanitizeKey(stringArray[1]));
        String lowerCase2 = stringArray[2].toLowerCase(Locale.ROOT);
        String string2 = stringArray.length > 3 ? stringArray[3] : "";
        PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();
        switch (lowerCase) {
            case "remove": {
                persistentDataContainer.remove(namespacedKey);
                break;
            }
            case "switch": {
                boolean bl = Boolean.TRUE.equals(persistentDataContainer.get(namespacedKey, PersistentDataType.BOOLEAN));
                persistentDataContainer.set(namespacedKey, PersistentDataType.BOOLEAN, !bl);
                break;
            }
            case "set": {
                DefaultActions.setMeta(persistentDataContainer, namespacedKey, lowerCase2, string2);
                break;
            }
            case "add": 
            case "subtract": {
                DefaultActions.adjustMeta(persistentDataContainer, namespacedKey, lowerCase2, string2, lowerCase.equals("subtract"));
            }
        }
    }

    private static void setMeta(PersistentDataContainer persistentDataContainer, NamespacedKey namespacedKey, String string, String string2) {
        int n = -1;
        switch (string.hashCode()) {
            case 104431: {
                if (!string.equals("int")) break;
                n = 0;
                break;
            }
            case 1958052158: {
                if (!string.equals("integer")) break;
                n = 1;
                break;
            }
            case 3327612: {
                if (!string.equals("long")) break;
                n = 2;
                break;
            }
            case -1325958191: {
                if (!string.equals("double")) break;
                n = 3;
                break;
            }
            case 64711720: {
                if (!string.equals("boolean")) break;
                n = 4;
                break;
            }
            case 3029738: {
                if (!string.equals("bool")) break;
                n = 5;
            }
        }
        switch (n) {
            case 0: 
            case 1: {
                persistentDataContainer.set(namespacedKey, PersistentDataType.INTEGER, (int)DefaultActions.parseDouble(string2, 0.0));
                break;
            }
            case 2: {
                persistentDataContainer.set(namespacedKey, PersistentDataType.LONG, (long)DefaultActions.parseDouble(string2, 0.0));
                break;
            }
            case 3: {
                persistentDataContainer.set(namespacedKey, PersistentDataType.DOUBLE, DefaultActions.parseDouble(string2, 0.0));
                break;
            }
            case 4: 
            case 5: {
                persistentDataContainer.set(namespacedKey, PersistentDataType.BOOLEAN, Boolean.parseBoolean(string2.trim()));
                break;
            }
            default: {
                persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, string2);
            }
        }
    }

    private static void adjustMeta(PersistentDataContainer persistentDataContainer, NamespacedKey namespacedKey, String string, String string2, boolean bl) {
        double d = DefaultActions.parseDouble(string2, 0.0) * (double)(bl ? -1 : 1);
        int n = -1;
        switch (string.hashCode()) {
            case 3327612: {
                if (!string.equals("long")) break;
                n = 0;
                break;
            }
            case -1325958191: {
                if (!string.equals("double")) break;
                n = 1;
            }
        }
        switch (n) {
            case 0: {
                long l = persistentDataContainer.has(namespacedKey, PersistentDataType.LONG) ? persistentDataContainer.get(namespacedKey, PersistentDataType.LONG) : 0L;
                persistentDataContainer.set(namespacedKey, PersistentDataType.LONG, l + (long)d);
                break;
            }
            case 1: {
                double d2 = persistentDataContainer.has(namespacedKey, PersistentDataType.DOUBLE) ? persistentDataContainer.get(namespacedKey, PersistentDataType.DOUBLE) : 0.0;
                persistentDataContainer.set(namespacedKey, PersistentDataType.DOUBLE, d2 + d);
                break;
            }
            default: {
                int n2 = persistentDataContainer.has(namespacedKey, PersistentDataType.INTEGER) ? persistentDataContainer.get(namespacedKey, PersistentDataType.INTEGER) : 0;
                persistentDataContainer.set(namespacedKey, PersistentDataType.INTEGER, n2 + (int)d);
            }
        }
    }

    private static String sanitizeKey(String string) {
        String string2 = string.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_./-]", "_");
        return string2.isEmpty() ? "meta" : string2;
    }

    private static double amount(String string) {
        return DefaultActions.parseDouble(string, 0.0);
    }

    private static float parseFloat(String string, float f) {
        try {
            return Float.parseFloat(string.trim());
        }
        catch (NumberFormatException e) {
            return f;
        }
    }

    private static double parseDouble(String string, double d) {
        try {
            return Double.parseDouble(string.trim());
        }
        catch (NumberFormatException e) {
            return d;
        }
    }
}

