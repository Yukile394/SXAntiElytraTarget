package studio.glace.glantielytratarget.platform;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.platform.BedrockPlatformProbe;
import studio.glace.glantielytratarget.platform.PlayerSessionCache;
import studio.glace.glantielytratarget.platform.ServerHealthMonitor;

public final class BypassPolicy {
    public static final String BYPASS_PERMISSION = "glantielytratarget.bypass";

    private BypassPolicy() {
    }

    public static boolean shouldBypass(@Nullable Player player) {
        if (player == null) {
            return true;
        }
        if (player.hasPermission(BYPASS_PERMISSION)) {
            return true;
        }
        return BedrockPlatformProbe.isBedrockPlayer(player);
    }

    public static boolean shouldBypass(@NotNull Player player, @Nullable ServerHealthMonitor serverHealthMonitor) {
        if (BypassPolicy.shouldBypass(player)) {
            return true;
        }
        boolean bl = serverHealthMonitor != null && serverHealthMonitor.shouldSkipDetection(player);
        return bl;
    }

    public static boolean shouldBypass(@Nullable Player player, @Nullable PlayerSessionCache playerSessionCache) {
        UUID uUID;
        PlayerSessionCache.Session session;
        PlayerSessionCache.Session session2;
        if (player == null) {
            return true;
        }
        if (playerSessionCache != null && (session2 = (session = playerSessionCache.get(uUID = player.getUniqueId()))) != null) {
            PlayerSessionCache.Session session3 = session;
            return session3.bypass();
        }
        return BypassPolicy.shouldBypass(player);
    }
}

