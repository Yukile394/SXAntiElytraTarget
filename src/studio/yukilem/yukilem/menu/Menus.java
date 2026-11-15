package studio.yukilem.yukilem.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.libs.nixlib.gui.Gui;
import studio.yukilem.yukilem.menu.MenuItemFactory;

final class Menus {
    private Menus() {
    }

    @NotNull
    static Map<String, Object> section(@Nullable Object object) {
        Map map = object instanceof Map ? (Map)object : Map.of();
        return map;
    }

    @NotNull
    static Map<String, Object> itemAppearance(@NotNull Map<String, Object> map) {
        HashMap<String, Object> hashMap;
        HashMap<String, Object> hashMap2 = hashMap = new HashMap<String, Object>(map);
        Object v = hashMap2.remove("actions");
        HashMap<String, Object> hashMap3 = hashMap;
        Object v2 = hashMap3.remove("slot");
        HashMap<String, Object> hashMap4 = hashMap;
        Object v3 = hashMap4.remove("slots");
        HashMap<String, Object> hashMap5 = hashMap;
        return hashMap5;
    }

    static int clampRows(@Nullable Integer n, int n2) {
        if (n == null) {
            return n2;
        }
        int n3 = n;
        int n4 = Math.min(6, n3);
        return Math.max(1, n4);
    }

    @NotNull
    static List<Integer> readSlots(@Nullable Object object) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        if (object instanceof List) {
            Iterator iterator;
            Iterator iterator2 = ((List)object).iterator();
            while ((iterator = iterator2).hasNext()) {
                Iterator iterator3 = iterator2;
                Integer n = MenuItemFactory.asInt(iterator3.next());
                Integer n2 = n;
                if (n2 == null) continue;
                ArrayList<Integer> arrayList2 = arrayList;
                Integer n3 = n;
                boolean bl = arrayList2.add(n3);
            }
        }
        ArrayList<Integer> arrayList3 = arrayList;
        return arrayList3;
    }

    static void applyFiller(@NotNull Gui gui, @Nullable ItemStack itemStack, @NotNull List<Integer> list) {
        Iterator<Integer> iterator;
        if (itemStack == null) {
            return;
        }
        if (list.isEmpty()) {
            Gui gui2 = gui.fill(itemStack);
            return;
        }
        Iterator<Integer> iterator2 = list.iterator();
        while ((iterator = iterator2).hasNext()) {
            int n;
            int n2;
            Iterator<Integer> iterator3 = iterator2;
            int n3 = iterator3.next();
            int n4 = n3;
            if (n4 < 0 || (n2 = n3) >= (n = gui.size())) continue;
            int n5 = n3;
            ItemStack itemStack2 = itemStack.clone();
            Gui gui3 = gui.setItem(n5, itemStack2);
        }
    }
}

