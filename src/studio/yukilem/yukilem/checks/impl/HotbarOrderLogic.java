package studio.yukilem.yukilem.checks.impl;

import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

final class HotbarOrderLogic {
    static final long BOOST_BEFORE_CONFLICT_MAX_NS = 500000000L;
    static final long BOOST_AFTER_CONFLICT_MAX_NS = 200000000L;

    private HotbarOrderLogic() {
    }

    static boolean isFireworkInHand(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack;
        InteractionHand interactionHand2 = InteractionHand.OFF_HAND;
        ItemStack itemStack2 = interactionHand == interactionHand2 ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();
        ItemStack itemStack3 = itemStack = itemStack2;
        Material type = itemStack3.getType();
        Material material = Material.FIREWORK_ROCKET;
        boolean bl = type == material;
        return bl;
    }

    static boolean isFireworkInHotbarSlot(@NotNull Player player, int n) {
        Material material;
        ItemStack itemStack;
        Material material2;
        if (n < 0 || n > 8) {
            return false;
        }
        ItemStack item = player.getInventory().getItem(n);
        ItemStack itemStack2 = item;
        boolean bl = itemStack2 != null && (material2 = (itemStack = item).getType()) == (material = Material.FIREWORK_ROCKET);
        return bl;
    }

    static boolean isConflict(boolean bl, boolean bl2) {
        boolean bl3 = bl && bl2;
        return bl3;
    }

    static boolean pairsWithBoost(long l, long l2) {
        long l3;
        long l4;
        long l5;
        long l6 = 0L;
        if (l <= l6 || l2 <= (l5 = 0L)) {
            return false;
        }
        long l7 = l - l2;
        long l8 = l7;
        long l9 = -200000000L;
        boolean bl = l8 >= l9 && (l4 = l7) <= (l3 = 500000000L);
        return bl;
    }
}

