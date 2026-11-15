package studio.yukilem.yukilem.platform;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.api.GeyserApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BedrockPlatformProbe {
    private static final boolean GEYSER_PRESENT;
    private static final boolean FLOODGATE_PRESENT;

    private BedrockPlatformProbe() {
    }

    @Nullable
    private static Plugin geyserPlugin() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        PluginManager pluginManager2 = pluginManager;
        Plugin plugin = pluginManager2.getPlugin("Geyser-Spigot");
        Plugin plugin2 = plugin;
        if (plugin2 != null) {
            Plugin plugin3 = plugin;
            return plugin3;
        }
        PluginManager pluginManager3 = pluginManager;
        return pluginManager3.getPlugin("Geyser");
    }

    public static boolean isGeyserAvailable(int n) {
        int n2 = 0xB2FAF1B ^ (0x2D717940 ^ (0x344F4F24 ^ n));
        n2 = 0x12B94F5C ^ n2;
        return GEYSER_PRESENT;
    }

    public static boolean isFloodgateAvailable(int n) {
        int n2 = 0x3457BDC9 ^ (0x75CD1E79 ^ (0x344F4F24 ^ n));
        n2 = 0x49CD4DBA ^ n2;
        return FLOODGATE_PRESENT;
    }

    public static boolean isBedrockPlayer(@Nullable Player player) {
        String string;
        String string2;
        String string3;
        if (player == null) {
            return false;
        }
        if (FLOODGATE_PRESENT) {
            try {
                UUID uUID;
                FloodgateApi floodgateApi;
                FloodgateApi floodgateApi2;
                FloodgateApi floodgateApi3 = floodgateApi2 = FloodgateApi.getInstance();
                if (floodgateApi3 != null && (floodgateApi = floodgateApi2).isFloodgatePlayer(uUID = player.getUniqueId())) {
                    return true;
                }
            }
            catch (Throwable e) {
                Throwable e2 = e;
            }
        }
        if (GEYSER_PRESENT) {
            try {
                UUID uUID;
                GeyserApi geyserApi;
                GeyserApi geyserApi2;
                GeyserApi geyserApi3 = geyserApi2 = GeyserApi.api();
                if (geyserApi3 != null && (geyserApi = geyserApi2).isBedrockPlayer(uUID = player.getUniqueId())) {
                    return true;
                }
            }
            catch (Throwable e) {
                Throwable e3 = e;
            }
        }
        String name = player.getName();
        boolean bl = FLOODGATE_PRESENT && (string3 = name) != null && ((string2 = name).startsWith(".") || (string = name).startsWith("*"));
        return bl;
    }

    public static boolean isBedrockName(@NotNull String string) {
        boolean bl = string.startsWith(".") || string.startsWith("*");
        return bl;
    }

    static {
        boolean bl = BedrockPlatformProbe.geyserPlugin() != null;
        GEYSER_PRESENT = bl;
        boolean bl2 = Bukkit.getPluginManager().getPlugin("floodgate") != null;
        FLOODGATE_PRESENT = bl2;
    }
}

