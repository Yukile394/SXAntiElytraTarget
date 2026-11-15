package studio.yukilem.yukilem.listener;

import lombok.Generated;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.platform.BypassPolicy;
import studio.yukilem.yukilem.platform.PlayerSessionCache;

public final class FireworkListener
implements Listener {
    private final PluginContext ctx;

    @EventHandler(priority=EventPriority.HIGH)
    public void onOffhandRocketUse(PlayerInteractEvent playerInteractEvent) {
        Material material;
        PlayerSessionCache playerSessionCache;
        Action action;
        Action action2;
        Action action3;
        EquipmentSlot equipmentSlot;
        EquipmentSlot hand = playerInteractEvent.getHand();
        if (hand != (equipmentSlot = EquipmentSlot.OFF_HAND)) {
            return;
        }
        Action action4 = playerInteractEvent.getAction();
        Action action5 = action4;
        if (action5 != (action3 = Action.RIGHT_CLICK_AIR) && (action2 = action4) != (action = Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!this.ctx.config().getOffhand().enabled()) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        Player player2 = player;
        if (BypassPolicy.shouldBypass(player2, playerSessionCache = this.ctx.sessionCache())) {
            return;
        }
        Player player3 = player;
        Material type = player3.getInventory().getItemInOffHand().getType();
        if (type != (material = Material.FIREWORK_ROCKET)) {
            return;
        }
        playerInteractEvent.setCancelled(true);
        Player player4 = player;
        Component message = this.ctx.language().getMessage("offhand.warning");
        player4.sendMessage(message);
    }

    @Generated
    public FireworkListener(PluginContext pluginContext) {
        this.ctx = pluginContext;
    }
}

