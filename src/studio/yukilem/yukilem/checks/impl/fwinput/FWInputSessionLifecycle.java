package studio.yukilem.yukilem.checks.impl.fwinput;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.checks.impl.fwinput.FWInputSession;

public final class FWInputSessionLifecycle {
    private FWInputSessionLifecycle() {
    }

    public static void expireStale(@NotNull FWInputSession fWInputSession, long l) {
        long l2 = fWInputSession.swapFirstNs;
        long l3 = 0L;
        if (l2 <= l3) {
            return;
        }
        long l4 = fWInputSession.swapFirstNs;
        long l5 = l - l4;
        long l6 = 800000000L;
        if (l5 > l6) {
            FWInputSessionLifecycle.clearSwap(fWInputSession);
        }
    }

    public static void clearSwap(@NotNull FWInputSession fWInputSession) {
        long l;
        long l2;
        fWInputSession.swapInvSlot = -1;
        fWInputSession.swapHotbarButton = -1;
        fWInputSession.swapFirstNs = l2 = 0L;
        fWInputSession.swapBaselineNonRocket = false;
        fWInputSession.elytraBoostNs = l = 0L;
        fWInputSession.returnSwapSeen = false;
    }

    public static Material hotbarMaterial(@NotNull Player player, int n) {
        ItemStack itemStack;
        ItemStack itemStack2 = FWInputSessionLifecycle.safeGet(player.getInventory(), n);
        ItemStack itemStack3 = itemStack2;
        if (itemStack3 == null || (itemStack = itemStack2).getType().isAir()) {
            return Material.AIR;
        }
        ItemStack itemStack4 = itemStack2;
        return itemStack4.getType();
    }

    @Nullable
    private static ItemStack safeGet(@NotNull PlayerInventory playerInventory, int n) {
        if (n < 0 || n > 8) {
            return null;
        }
        try {
            ItemStack item = playerInventory.getItem(n);
            return item;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return null;
        }
    }
}

