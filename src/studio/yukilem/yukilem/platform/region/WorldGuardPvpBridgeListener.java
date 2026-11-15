package studio.glace.glantielytratarget.platform.region;

import com.sk89q.worldguard.bukkit.protection.events.DisallowedPVPEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.platform.region.WorldGuardRegionPolicy;

final class WorldGuardPvpBridgeListener
implements Listener {
    private final WorldGuardRegionPolicy policy;

    WorldGuardPvpBridgeListener(@NotNull WorldGuardRegionPolicy worldGuardRegionPolicy) {
        this.policy = worldGuardRegionPolicy;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDisallowedPvp(@NotNull DisallowedPVPEvent disallowedPVPEvent) {
        Player player;
        Player attacker = disallowedPVPEvent.getAttacker();
        Player defender = disallowedPVPEvent.getDefender();
        Player player2 = attacker;
        if (player2 != null && (player = defender) != null) {
            WorldGuardRegionPolicy worldGuardRegionPolicy = this.policy;
            Player player3 = attacker;
            Player player4 = defender;
            worldGuardRegionPolicy.onDisallowedPvp(player3, player4);
        }
    }
}

