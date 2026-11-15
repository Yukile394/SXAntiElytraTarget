package studio.yukilem.yukilem.util.combat;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.scheduler.Scheduler;
import studio.yukilem.yukilem.libs.nixlib.world.CachedBlockRayTrace;
import studio.yukilem.yukilem.platform.PlatformScheduler;

public final class ElytraUseProbe {
    public static final double BLOCK_TARGET_REACH = 5.0;

    private ElytraUseProbe() {
    }

    public static boolean targetsBlockInReach(@NotNull Player player) {
        double d = 5.0;
        return CachedBlockRayTrace.solidBlockInReach(player, d);
    }

    public static void refreshLookTarget(@NotNull PlatformScheduler platformScheduler, @NotNull Player player) {
        Scheduler scheduler = platformScheduler.scheduler();
        double d = 5.0;
        CachedBlockRayTrace.refreshIfStale(scheduler, player, d);
    }

    public static void clearPlayer(@NotNull UUID uUID) {
        CachedBlockRayTrace.clear(uUID);
    }
}

