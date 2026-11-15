package studio.glace.glantielytratarget.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.libs.nixlib.gui.Gui;
import studio.glace.glantielytratarget.libs.nixlib.gui.GuiNavigation;
import studio.glace.glantielytratarget.menu.AetSounds;
import studio.glace.glantielytratarget.menu.MenuActionService;

final class MenuAudio {
    private static final Set<UUID> SUPPRESS_CLOSE = ConcurrentHashMap.newKeySet();

    private MenuAudio() {
    }

    static void suppressNextClose(@NotNull UUID uUID) {
        boolean bl = SUPPRESS_CLOSE.add(uUID);
    }

    static void attachClose(@NotNull Gui gui, @NotNull MenuActionService menuActionService, @NotNull Map<String, Object> map) {
        List<String> list = AetSounds.DEFAULT_CLOSE_ACTIONS;
        List<String> list2 = MenuAudio.resolveActions(map, "close-actions", list);
        Consumer<InventoryCloseEvent> consumer = inventoryCloseEvent -> {
            HumanEntity player = inventoryCloseEvent.getPlayer();
            HumanEntity humanEntity = player;
            if (!(humanEntity instanceof Player)) {
                return;
            }
            HumanEntity humanEntity2 = player;
            Player player2 = (Player)humanEntity2;
            Player player3 = player2;
            if (GuiNavigation.isTransitioning(player3.getUniqueId())) {
                return;
            }
            Set<UUID> set = SUPPRESS_CLOSE;
            Player player4 = player2;
            UUID uniqueId = player4.getUniqueId();
            if (set.remove(uniqueId)) {
                return;
            }
            Player player5 = player2;
            menuActionService.runActions(player5, list2);
        };
        Gui gui2 = gui.onClose(consumer);
    }

    static void openAndSound(@NotNull Player player, @NotNull MenuActionService menuActionService, @NotNull Map<String, Object> map, @NotNull Runnable runnable) {
        boolean bl = MenuAudio.hadMenuOpen(player);
        runnable.run();
        boolean bl2 = bl;
        if (!bl2) {
            List<String> list = AetSounds.DEFAULT_OPEN_ACTIONS;
            List<String> list2 = MenuAudio.resolveActions(map, "open-actions", list);
            menuActionService.runActions(player, list2);
        }
    }

    static boolean hadMenuOpen(@NotNull Player player) {
        InventoryType inventoryType;
        InventoryType type = player.getOpenInventory().getType();
        if (type == (inventoryType = InventoryType.ANVIL)) {
            return true;
        }
        return player.getOpenInventory().getTopInventory().getHolder() instanceof Gui;
    }

    @NotNull
    static List<String> resolveActions(@NotNull Map<String, Object> map, @NotNull String string, @NotNull List<String> list) {
        List<String> list2;
        Iterator iterator;
        Object object = map.get(string);
        Object object2 = object;
        if (!(object2 instanceof List)) {
            return list;
        }
        Object object3 = object;
        List list3 = (List)object3;
        ArrayList<String> arrayList = new ArrayList<String>();
        List list4 = list3;
        Iterator iterator2 = list4.iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator iterator3 = iterator2;
            Object e = iterator3.next();
            Object e2 = e;
            if (e2 == null) continue;
            ArrayList<String> arrayList2 = arrayList;
            Object e3 = e;
            String string2 = String.valueOf(e3);
            boolean bl = arrayList2.add(string2);
        }
        ArrayList<String> arrayList3 = arrayList;
        if (arrayList3.isEmpty()) {
            list2 = list;
        } else {
            ArrayList<String> arrayList4 = arrayList;
            list2 = List.copyOf(arrayList4);
        }
        return list2;
    }
}

