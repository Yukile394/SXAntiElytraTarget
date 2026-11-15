package studio.yukilem.yukilem.menu;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.libs.nixlib.gui.ClickContext;
import studio.yukilem.yukilem.libs.nixlib.gui.Gui;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;
import studio.yukilem.yukilem.menu.ConfirmRequest;
import studio.yukilem.yukilem.menu.MenuActionService;
import studio.yukilem.yukilem.menu.MenuAudio;
import studio.yukilem.yukilem.menu.MenuItemFactory;
import studio.yukilem.yukilem.menu.Menus;

final class ConfirmMenu {
    private final PluginContext ctx;

    ConfirmMenu(@NotNull PluginContext pluginContext) {
        this.ctx = pluginContext;
    }

    void open(@NotNull Player player2, @NotNull ConfirmRequest confirmRequest) {
        ItemStack itemStack;
        Gui gui;
        Gui gui2;
        String string;
        Map<String, Object> map;
        Map<String, Object> map2 = map = this.ctx.menuConfig().confirmMenu();
        int n = Menus.clampRows(MenuItemFactory.asInt(map2.get("rows")), 3);
        String string2 = string = ColorUtils.toLegacySection(ColorUtils.translate(this.ctx.language().getRawMessage("menu.confirm-title")));
        int n2 = n;
        Gui gui3 = gui2 = (gui = new Gui(string2, n2));
        Gui gui4 = gui3.cancelByDefault(true);
        Gui gui5 = gui2;
        Consumer<Player> consumer = player -> {
            ConfirmRequest confirmRequest2;
            ConfirmRequest confirmRequest3 = this.ctx.menus() == null ? confirmRequest : this.ctx.menus().confirmRequest((Player)player);
            ConfirmRequest confirmRequest4 = confirmRequest2 = confirmRequest3;
            ConfirmRequest confirmRequest5 = confirmRequest4 == null ? confirmRequest : confirmRequest2;
            confirmRequest5.onCancel().accept((Player)player);
        };
        Gui gui6 = gui5.onBack(consumer);
        String string3 = confirmRequest.description();
        Map<String, String> map3 = Map.of("%action%", string3);
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
        Gui gui7 = gui2;
        ItemStack itemStack3 = itemStack2;
        Map<String, Object> map10 = map5;
        List<Integer> list = Menus.readSlots(map10.get("slots"));
        Menus.applyFiller(gui7, itemStack3, list);
        Gui gui8 = gui2;
        Map<String, Object> map11 = map;
        Map<String, Object> map12 = Menus.section(map11.get("confirm"));
        Map<String, String> map13 = map3;
        Map<String, Object> map14 = map;
        Map<String, String> map15 = map3;
        Consumer<ClickContext> consumer2 = clickContext -> {
            MenuActionService menuActionService = this.ctx.menuActions();
            Map<String, Object> confirmSection = Menus.section(map14.get("confirm"));
            menuActionService.staticHandler(confirmSection, map15).accept((ClickContext)clickContext);
        };
        this.placeButtons(gui8, map12, map13, consumer2);
        Gui gui9 = gui2;
        Map<String, Object> map16 = map;
        Map<String, Object> map17 = Menus.section(map16.get("cancel"));
        Map<String, String> map18 = map3;
        Map<String, Object> map19 = map;
        Map<String, String> map20 = map3;
        Consumer<ClickContext> consumer3 = clickContext -> {
            MenuActionService menuActionService = this.ctx.menuActions();
            Map<String, Object> cancelSection = Menus.section(map19.get("cancel"));
            menuActionService.staticHandler(cancelSection, map20).accept((ClickContext)clickContext);
        };
        this.placeButtons(gui9, map17, map18, consumer3);
        Gui gui10 = gui2;
        MenuActionService menuActionService = this.ctx.menuActions();
        Map<String, Object> map21 = map;
        MenuAudio.attachClose(gui10, menuActionService, map21);
        MenuActionService menuActionService2 = this.ctx.menuActions();
        Map<String, Object> map22 = map;
        Gui gui11 = gui2;
        Runnable runnable = () -> gui11.open(player2);
        MenuAudio.openAndSound(player2, menuActionService2, map22, runnable);
    }

    private void placeButtons(@NotNull Gui gui, @NotNull Map<String, Object> map, @NotNull Map<String, String> map2, @NotNull Consumer<ClickContext> consumer) {
        Iterator<Integer> iterator;
        if (map.isEmpty()) {
            return;
        }
        List<Integer> list = Menus.readSlots(map.get("slots"));
        List<Integer> list2 = list;
        if (list2.isEmpty()) {
            return;
        }
        ItemStack itemStack = MenuItemFactory.build(Menus.itemAppearance(map), map2);
        List<Integer> list3 = list;
        Iterator<Integer> iterator2 = list3.iterator();
        while ((iterator = iterator2).hasNext()) {
            int n;
            int n2;
            Iterator<Integer> iterator3 = iterator2;
            int n3 = iterator3.next();
            int n4 = n3;
            if (n4 < 0 || (n2 = n3) >= (n = gui.size())) continue;
            int n5 = n3;
            ItemStack itemStack2 = itemStack;
            ItemStack itemStack3 = itemStack2.clone();
            Gui gui2 = gui.setItem(n5, itemStack3, consumer);
        }
    }
}

