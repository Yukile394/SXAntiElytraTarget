package studio.glace.glantielytratarget.libs.nixlib.menu;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.messaging.Messenger;
import studio.glace.glantielytratarget.libs.nixlib.menu.Warnings;

final class Integrations {
    private static final String BUNGEE_CHANNEL = "BungeeCord";

    private Integrations() {
    }

    static void registerBungee(Plugin plugin) {
        try {
            Messenger messenger = plugin.getServer().getMessenger();
            if (!messenger.isOutgoingChannelRegistered(plugin, BUNGEE_CHANNEL)) {
                messenger.registerOutgoingPluginChannel(plugin, BUNGEE_CHANNEL);
            }
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    static void connect(Plugin plugin, Player player, String string) {
        if (string == null || string.isBlank()) {
            return;
        }
        try {
            Integrations.registerBungee(plugin);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(string.trim());
            player.sendPluginMessage(plugin, BUNGEE_CHANNEL, byteArrayOutputStream.toByteArray());
        }
        catch (Throwable e) {
            Warnings.once("bungee", "Could not send BungeeCord connect message: " + e.getMessage());
        }
    }

    static String parsePlaceholders(Player player, String string) {
        Object object = Integrations.papiClass();
        if (object == null) {
            Warnings.once("papi", "PlaceholderAPI is not installed; placeholder action skipped.");
            return string;
        }
        try {
            Method method = ((Class)object).getMethod("setPlaceholders", Player.class, String.class);
            Object object2 = method.invoke(null, player, string);
            return object2 == null ? string : object2.toString();
        }
        catch (Throwable e) {
            Warnings.once("papi-error", "PlaceholderAPI parsing failed: " + e.getMessage());
            return string;
        }
    }

    private static Object papiClass() {
        try {
            return Class.forName("me.clip.placeholderapi.PlaceholderAPI");
        }
        catch (Throwable e) {
            return null;
        }
    }

    static boolean money(Player player, double d, boolean bl) {
        Object object = Integrations.economy();
        if (object == null) {
            Warnings.once("vault-economy", "Vault economy not found; money action skipped.");
            return false;
        }
        try {
            String string = bl ? "depositPlayer" : "withdrawPlayer";
            object.getClass().getMethod(string, OfflinePlayer.class, Double.TYPE).invoke(object, player, d);
            return true;
        }
        catch (Throwable e) {
            Warnings.once("vault-economy-error", "Vault economy action failed: " + e.getMessage());
            return false;
        }
    }

    static boolean permission(Player player, String string, boolean bl) {
        Object object = Integrations.permissions();
        if (object == null) {
            Warnings.once("vault-permission", "Vault permission provider not found; permission action skipped.");
            return false;
        }
        try {
            String string2 = bl ? "playerAdd" : "playerRemove";
            object.getClass().getMethod(string2, Player.class, String.class).invoke(object, player, string);
            return true;
        }
        catch (Throwable e) {
            Warnings.once("vault-permission-error", "Vault permission action failed: " + e.getMessage());
            return false;
        }
    }

    private static Object economy() {
        return Integrations.service("net.milkbowl.vault.economy.Economy");
    }

    private static Object permissions() {
        return Integrations.service("net.milkbowl.vault.permission.Permission");
    }

    private static Object service(String string) {
        try {
            Class<?> clazz = Class.forName(string);
            RegisteredServiceProvider<Object> registration = Bukkit.getServicesManager().getRegistration(clazz.asSubclass(Object.class));
            if (registration == null) {
                return null;
            }
            return registration.getClass().getMethod("getProvider", new Class[0]).invoke(registration, new Object[0]);
        }
        catch (Throwable e) {
            return null;
        }
    }
}

