package studio.yukilem.yukilem.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.data.ViolationRecord;
import studio.yukilem.yukilem.libs.nixlib.gui.ClickContext;
import studio.yukilem.yukilem.libs.nixlib.gui.Gui;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;
import studio.yukilem.yukilem.menu.MenuActionService;
import studio.yukilem.yukilem.menu.MenuAudio;
import studio.yukilem.yukilem.menu.MenuItemFactory;
import studio.yukilem.yukilem.menu.MenuPlaceholders;
import studio.yukilem.yukilem.menu.Menus;

final class ProfileMenu {
    private final PluginContext ctx;

    ProfileMenu(@NotNull PluginContext pluginContext) {
        this.ctx = pluginContext;
    }

    void open(@NotNull Player player, @NotNull UUID uUID, @NotNull String string, @NotNull List<ViolationRecord> list) {
        Player player2;
        Player player3;
        ItemStack itemStack;
        Gui gui;
        Gui gui2;
        String string2;
        Map<String, Object> map;
        Map<String, Object> map2 = map = this.ctx.menuConfig().profileMenu();
        int n = Menus.clampRows(MenuItemFactory.asInt(map2.get("rows")), 3);
        Map<String, String> map3 = MenuPlaceholders.profile(this.ctx, uUID, string, list);
        String string3 = string2 = ColorUtils.toLegacySection(ColorUtils.translate(this.ctx.language().getRawMessage("menu.profile-title").replace("%player%", string)));
        int n2 = n;
        Gui gui3 = gui2 = (gui = new Gui(string3, n2));
        Gui gui4 = gui3.cancelByDefault(true);
        Map<String, Object> map4 = map;
        Map<String, Object> map5 = Menus.section(map4.get("filler"));
        Map<String, Object> map6 = map5;
        if (map6.isEmpty()) {
            itemStack = null;
        } else {
            Map<String, Object> map7 = map5;
            Map<String, Object> map8 = Menus.itemAppearance(map7);
            Map<String, String> map9 = map3;
            itemStack = MenuItemFactory.build(map8, map9);
        }
        ItemStack itemStack2 = itemStack;
        Gui gui5 = gui2;
        ItemStack itemStack3 = itemStack2;
        Map<String, Object> map10 = map5;
        List<Integer> list2 = Menus.readSlots(map10.get("slots"));
        Menus.applyFiller(gui5, itemStack3, list2);
        Player player4 = player3 = Bukkit.getPlayer(uUID);
        boolean bl = player4 != null && (player2 = player3).isOnline();
        boolean bl2 = bl;
        Gui gui6 = gui2;
        Map<String, Object> map11 = map;
        Map<String, String> map12 = map3;
        boolean bl3 = bl2;
        this.placeInfo(gui6, map11, map12, bl3);
        Map<String, Object> map13 = map;
        Map<String, Object> map14 = Menus.section(map13.get("items"));
        Gui gui7 = gui2;
        Map<String, Object> map15 = map14;
        Map<String, String> map16 = map3;
        this.placeButton(gui7, map15, "violations", 13, map16, player, "glantielytratarget.profile.violations");
        Gui gui8 = gui2;
        Map<String, Object> map17 = map14;
        Map<String, String> map18 = map3;
        this.placeButton(gui8, map17, "teleport", 11, map18, player, "glantielytratarget.profile.teleport");
        Gui gui9 = gui2;
        Map<String, Object> map19 = map14;
        Map<String, String> map20 = map3;
        this.placeButton(gui9, map19, "clear-vl", 15, map20, player, "glantielytratarget.profile.clear");
        Gui gui10 = gui2;
        Map<String, Object> map21 = map14;
        Map<String, String> map22 = map3;
        this.placeButton(gui10, map21, "close", 22, map22, player, null);
        Gui gui11 = gui2;
        MenuActionService menuActionService = this.ctx.menuActions();
        Map<String, Object> map23 = map;
        MenuAudio.attachClose(gui11, menuActionService, map23);
        MenuActionService menuActionService2 = this.ctx.menuActions();
        Map<String, Object> map24 = map;
        Gui gui12 = gui2;
        Runnable runnable = () -> gui12.open(player);
        MenuAudio.openAndSound(player, menuActionService2, map24, runnable);
    }

    private void placeInfo(@NotNull Gui gui, @NotNull Map<String, Object> map, @NotNull Map<String, String> map2, boolean bl) {
        HashMap<String, Object> hashMap;
        int n;
        int n2;
        Map<String, Object> map3 = Menus.section(map.get("player-info"));
        Map<String, Object> map4 = map3;
        if (map4.isEmpty()) {
            return;
        }
        Map<String, Object> map5 = map3;
        int n3 = MenuItemFactory.slot(map5, 4);
        int n4 = n3;
        if (n4 < 0 || (n2 = n3) >= (n = gui.size())) {
            return;
        }
        Map<String, Object> map6 = map3;
        Map<String, Object> map7 = Menus.itemAppearance(map6);
        HashMap<String, Object> hashMap2 = hashMap = new HashMap<String, Object>(map7);
        Map<String, Object> map8 = map3;
        String string = bl ? "online-lore" : "offline-lore";
        Object object = map8.get(string);
        Object object2 = object;
        if (object2 == null) {
            Map<String, Object> map9 = map3;
            object = map9.get("lore");
        }
        HashMap<String, Object> hashMap3 = hashMap2;
        Object v = hashMap3.remove("online-lore");
        HashMap<String, Object> hashMap4 = hashMap2;
        Object v2 = hashMap4.remove("offline-lore");
        Object object3 = object;
        if (object3 != null) {
            HashMap<String, Object> hashMap5 = hashMap2;
            Object object4 = object;
            Object object5 = hashMap5.put("lore", object4);
        }
        HashMap<String, Object> hashMap6 = hashMap2;
        ItemStack itemStack = MenuItemFactory.build(hashMap6, map2);
        Map<String, Object> map10 = map3;
        if (map10.containsKey("actions")) {
            int n5 = n3;
            ItemStack itemStack2 = itemStack;
            MenuActionService menuActionService = this.ctx.menuActions();
            Map<String, Object> map11 = map3;
            Consumer<ClickContext> consumer = menuActionService.staticHandler(map11, map2);
            Gui gui2 = gui.setItem(n5, itemStack2, consumer);
        } else {
            int n6 = n3;
            ItemStack itemStack3 = itemStack;
            Gui gui3 = gui.setItem(n6, itemStack3);
        }
    }

    private void placeButton(@NotNull Gui gui, @NotNull Map<String, Object> map, @NotNull String string, int n, @NotNull Map<String, String> map2, @NotNull Player player, @Nullable String string2) {
        int n2;
        int n3;
        Map<String, Object> map3 = Menus.section(map.get(string));
        Map<String, Object> map4 = map3;
        if (map4.isEmpty()) {
            return;
        }
        if (string2 != null && !player.hasPermission(string2)) {
            return;
        }
        Map<String, Object> map5 = map3;
        int n4 = MenuItemFactory.slot(map5, n);
        int n5 = n4;
        if (n5 < 0 || (n3 = n4) >= (n2 = gui.size())) {
            return;
        }
        int n6 = n4;
        Map<String, Object> map6 = map3;
        ItemStack itemStack = MenuItemFactory.build(Menus.itemAppearance(map6), map2);
        MenuActionService menuActionService = this.ctx.menuActions();
        Map<String, Object> map7 = map3;
        Consumer<ClickContext> consumer = menuActionService.staticHandler(map7, map2);
        Gui gui2 = gui.setItem(n6, itemStack, consumer);
    }
}

