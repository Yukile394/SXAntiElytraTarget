package studio.yukilem.yukilem.menu;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.data.ViolationRecord;
import studio.yukilem.yukilem.libs.nixlib.gui.ClickContext;
import studio.yukilem.yukilem.libs.nixlib.gui.Gui;
import studio.yukilem.yukilem.libs.nixlib.gui.PagedGui;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;
import studio.yukilem.yukilem.menu.MenuActionService;
import studio.yukilem.yukilem.menu.MenuAudio;
import studio.yukilem.yukilem.menu.MenuItemFactory;
import studio.yukilem.yukilem.menu.MenuPlaceholders;
import studio.yukilem.yukilem.menu.Menus;

final class ViolationsMenu {
    private static final int[] CONTENT_SLOTS = ViolationsMenu.buildContentSlots();
    private static final int PREV_SLOT = 45;
    private static final int NEXT_SLOT = 53;
    private final PluginContext ctx;

    ViolationsMenu(@NotNull PluginContext pluginContext) {
        this.ctx = pluginContext;
    }

    /*
     * Ignored method signature, as it can't be verified against descriptor
     */
    void open(@NotNull Player player2, @NotNull String string, @NotNull UUID uUID, @NotNull List list, int n) {
        int n2;
        int n3;
        ItemStack itemStack;
        PagedGui pagedGui;
        PagedGui pagedGui2;
        String string2;
        int n4;
        Map<String, Object> map = this.ctx.menuConfig().violationsMenu();
        int length = CONTENT_SLOTS.length;
        double size = list.size();
        int n5 = length;
        double d = n5;
        int n6 = (int)Math.ceil(size / d);
        int n7 = n4 = Math.max(1, n6);
        int n8 = n7 - 1;
        int n9 = Math.min(n, n8);
        int n10 = Math.max(0, n9);
        String string3 = this.ctx.language().getRawMessage("menu.violations-title").replace("%player%", string);
        int n11 = n10;
        String string4 = String.valueOf(n11 + 1);
        String string5 = string3.replace("%page%", string4);
        int n12 = n4;
        String string6 = String.valueOf(n12);
        String string7 = string5.replace("%maxpage%", string6);
        String string8 = String.valueOf(list.size());
        String string9 = string2 = ColorUtils.toLegacySection(ColorUtils.translate(string7.replace("%total%", string8)));
        PagedGui pagedGui3 = pagedGui2 = (pagedGui = new PagedGui(string9, 6));
        Gui gui = pagedGui3.cancelByDefault(true);
        PagedGui pagedGui4 = pagedGui2;
        Consumer<Player> consumer = player -> {
            if (this.ctx.menus() != null) {
                this.ctx.menus().reopenProfile((Player)player);
            }
        };
        Gui gui2 = pagedGui4.onBack(consumer);
        PagedGui pagedGui5 = pagedGui2;
        int[] nArray = CONTENT_SLOTS;
        PagedGui pagedGui6 = pagedGui5.contentSlots(nArray);
        Map<String, Object> map2 = map;
        Map<String, Object> map3 = Menus.section(map2.get("filler"));
        Map<String, Object> map4 = map3;
        if (map4.isEmpty()) {
            itemStack = null;
        } else {
            Map<String, Object> map5 = map3;
            Map<String, Object> map6 = Menus.itemAppearance(map5);
            Map<String, String> map7 = Map.of("%player%", string);
            itemStack = MenuItemFactory.build(map6, map7);
        }
        ItemStack itemStack2 = itemStack;
        PagedGui pagedGui7 = pagedGui2;
        ItemStack itemStack3 = itemStack2;
        Map<String, Object> map8 = map3;
        List<Integer> list2 = Menus.readSlots(map8.get("slots"));
        Menus.applyFiller(pagedGui7, itemStack3, list2);
        SimpleDateFormat simpleDateFormat = this.ctx.config().getMenuTime().newDateFormat();
        Map<String, Object> map9 = map;
        Map<String, Object> map10 = Menus.section(map9.get("violation-item"));
        int n13 = 0;
        while ((n3 = ++n13) < (n2 = list.size())) {
            int n14 = n13;
            ViolationRecord violationRecord = (ViolationRecord)list.get(n14);
            PluginContext pluginContext = this.ctx;
            ViolationRecord violationRecord2 = violationRecord;
            int n15 = n13;
            int n16 = n15 + 1;
            int size2 = list.size();
            SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
            Map<String, String> map11 = MenuPlaceholders.violation(pluginContext, violationRecord2, n16, size2, simpleDateFormat2);
            Map<String, Object> map12 = map10;
            Map<String, Object> map13 = Menus.itemAppearance(map12);
            Map<String, String> map14 = map11;
            ItemStack itemStack4 = MenuItemFactory.build(map13, map14);
            Map<String, Object> map15 = map10;
            if (map15.containsKey("actions")) {
                PagedGui pagedGui8 = pagedGui2;
                ItemStack itemStack5 = itemStack4;
                MenuActionService menuActionService = this.ctx.menuActions();
                Map<String, Object> map16 = map10;
                Map<String, String> map17 = map11;
                Consumer<ClickContext> consumer2 = menuActionService.entryHandler(map16, map17);
                PagedGui pagedGui9 = pagedGui8.addEntry(itemStack5, consumer2);
                continue;
            }
            PagedGui pagedGui10 = pagedGui2;
            ItemStack itemStack6 = itemStack4;
            PagedGui pagedGui11 = pagedGui10.addEntry(itemStack6);
        }
        Map<String, Object> map18 = map;
        Map<String, Object> map19 = Menus.section(map18.get("items"));
        int n17 = n10;
        String string10 = String.valueOf(n17 + 1);
        int n18 = n4;
        String string11 = String.valueOf(n18);
        Map<String, String> map20 = Map.of("%player%", string, "%page%", string10, "%maxpage%", string11);
        PagedGui pagedGui12 = pagedGui2;
        Map<String, Object> map21 = map19;
        Map<String, String> map22 = map20;
        this.applyStaticItems(pagedGui12, map21, map22);
        PagedGui pagedGui13 = pagedGui2;
        Map<String, Object> map23 = map19;
        int n19 = n10;
        int n20 = n4;
        ItemStack itemStack7 = itemStack2;
        this.applyPagination(pagedGui13, map23, n19, n20, itemStack7, string);
        PagedGui pagedGui14 = pagedGui2;
        MenuActionService menuActionService = this.ctx.menuActions();
        Map<String, Object> map24 = map;
        MenuAudio.attachClose(pagedGui14, menuActionService, map24);
        MenuActionService menuActionService2 = this.ctx.menuActions();
        Map<String, Object> map25 = map;
        PagedGui pagedGui15 = pagedGui2;
        int n21 = n10;
        Runnable runnable = () -> {
            pagedGui15.open(player2, n21);
        };
        MenuAudio.openAndSound(player2, menuActionService2, map25, runnable);
    }

    private void applyStaticItems(@NotNull PagedGui pagedGui, @NotNull Map<String, Object> map, @NotNull Map<String, String> map2) {
        Iterator<Map.Entry<String, Object>> iterator;
        Iterator<Map.Entry<String, Object>> iterator2 = map.entrySet().iterator();
        while ((iterator = iterator2).hasNext()) {
            int n;
            int n2;
            Map<String, Object> map3;
            int n3;
            int n4;
            Map.Entry<String, Object> entry;
            Map<String, Object> map4;
            Map<String, Object> map5;
            String string;
            Iterator<Map.Entry<String, Object>> iterator3 = iterator2;
            Map.Entry<String, Object> entry2 = iterator3.next();
            Map.Entry<String, Object> entry3 = entry2;
            String key = entry3.getKey();
            String string2 = key;
            if ("previous-page".equals(string2) || "next-page".equals(string = key) || (map5 = (map4 = Menus.section((entry = entry2).getValue()))).isEmpty() || (n4 = (n3 = MenuItemFactory.slot(map3 = map4, -1))) < 0 || (n2 = n3) >= (n = pagedGui.size())) continue;
            int n5 = n3;
            Map<String, Object> map6 = map4;
            ItemStack itemStack = MenuItemFactory.build(Menus.itemAppearance(map6), map2);
            MenuActionService menuActionService = this.ctx.menuActions();
            Map<String, Object> map7 = map4;
            Consumer<ClickContext> consumer = menuActionService.staticHandler(map7, map2);
            Gui gui = pagedGui.setItem(n5, itemStack, consumer);
        }
    }

    private void applyPagination(@NotNull PagedGui pagedGui, @NotNull Map<String, Object> map, int n, int n2, @Nullable ItemStack itemStack, @NotNull String string) {
        int n3;
        String string2 = String.valueOf(n + 1);
        String string3 = String.valueOf(n2);
        Map<String, String> map2 = Map.of("%player%", string, "%page%", string2, "%maxpage%", string3);
        Map<String, Object> map3 = Menus.section(map.get("previous-page"));
        Map<String, Object> map4 = Menus.section(map.get("next-page"));
        Map<String, Object> map5 = map3;
        int n4 = MenuItemFactory.slot(map5, 45);
        Map<String, Object> map6 = map4;
        int n5 = MenuItemFactory.slot(map6, 53);
        Map<String, Object> map7 = map3;
        if (!map7.isEmpty() && n > 0) {
            int n6 = n4;
            Map<String, Object> map8 = map3;
            Map<String, Object> map9 = Menus.itemAppearance(map8);
            Map<String, String> map10 = map2;
            ItemStack itemStack2 = MenuItemFactory.build(map9, map10);
            MenuActionService menuActionService = this.ctx.menuActions();
            Map<String, Object> map11 = map3;
            Map<String, String> map12 = map2;
            Consumer<ClickContext> consumer = menuActionService.staticHandler(map11, map12);
            Gui gui = pagedGui.setItem(n6, itemStack2, consumer);
        } else {
            int n7 = n4;
            ViolationsMenu.fillSlot(pagedGui, n7, itemStack);
        }
        Map<String, Object> map13 = map4;
        if (!map13.isEmpty() && n < (n3 = n2 - 1)) {
            int n8 = n5;
            Map<String, Object> map14 = map4;
            Map<String, Object> map15 = Menus.itemAppearance(map14);
            Map<String, String> map16 = map2;
            ItemStack itemStack3 = MenuItemFactory.build(map15, map16);
            MenuActionService menuActionService = this.ctx.menuActions();
            Map<String, Object> map17 = map4;
            Map<String, String> map18 = map2;
            Consumer<ClickContext> consumer = menuActionService.staticHandler(map17, map18);
            Gui gui = pagedGui.setItem(n8, itemStack3, consumer);
        } else {
            int n9 = n5;
            ViolationsMenu.fillSlot(pagedGui, n9, itemStack);
        }
    }

    private static void fillSlot(@NotNull PagedGui pagedGui, int n, @Nullable ItemStack itemStack) {
        int n2;
        if (itemStack != null && n >= 0 && n < (n2 = pagedGui.size())) {
            ItemStack itemStack2 = itemStack.clone();
            Gui gui = pagedGui.setItem(n, itemStack2);
        }
    }

    private static int[] buildContentSlots() {
        int n;
        int[] nArray = new int[45];
        int n2 = 0;
        while ((n = n2) < 45) {
            int n3;
            int[] nArray2 = nArray;
            int n4 = n2;
            nArray2[n4] = n3 = n2++;
        }
        int[] nArray3 = nArray;
        return nArray3;
    }
}

