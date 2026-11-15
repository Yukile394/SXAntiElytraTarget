package studio.yukilem.yukilem.libs.nixlib.items;

import java.lang.reflect.Method;
import java.util.Collection;
import org.bukkit.inventory.ItemStack;

final class ItemStackComponents {
    private static final boolean AVAILABLE;
    private static final Object TOOLTIP_DISPLAY_TYPE;
    private static final Class<?> TOOLTIP_DISPLAY_CLASS;
    private static final Method SET_DATA;
    private static final Method GET_DATA;

    private ItemStackComponents() {
    }

    static boolean isAvailable() {
        return AVAILABLE;
    }

    static void hideTooltip(ItemStack itemStack, boolean bl) {
        if (!AVAILABLE || itemStack == null) {
            return;
        }
        try {
            Object object = TOOLTIP_DISPLAY_CLASS.getMethod("tooltipDisplay", new Class[0]).invoke(null, new Object[0]);
            Class<?> clazz = object.getClass();
            clazz.getMethod("hideTooltip", Boolean.TYPE).invoke(object, bl);
            ItemStackComponents.preserveHiddenComponents(itemStack, object, clazz);
            Object object2 = clazz.getMethod("build", new Class[0]).invoke(object, new Object[0]);
            SET_DATA.invoke((Object)itemStack, TOOLTIP_DISPLAY_TYPE, object2);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    private static void preserveHiddenComponents(ItemStack itemStack, Object object, Class<?> clazz) {
        if (GET_DATA == null) {
            return;
        }
        try {
            Object object2 = GET_DATA.invoke((Object)itemStack, TOOLTIP_DISPLAY_TYPE);
            if (object2 == null) {
                return;
            }
            Object object3 = TOOLTIP_DISPLAY_CLASS.getMethod("hiddenComponents", new Class[0]).invoke(object2, new Object[0]);
            if (object3 == null) {
                return;
            }
            clazz.getMethod("addHiddenComponents", Collection.class).invoke(object, object3);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    static {
        boolean bl = false;
        Object object = null;
        Class<?> clazz = null;
        Method method = null;
        Method method2 = null;
        try {
            Class<?> clazz2 = Class.forName("io.papermc.paper.datacomponent.DataComponentTypes");
            object = clazz2.getField("TOOLTIP_DISPLAY").get(null);
            clazz = Class.forName("io.papermc.paper.datacomponent.item.TooltipDisplay");
            Class<?> clazz3 = Class.forName("io.papermc.paper.datacomponent.DataComponentType$Valued");
            method = ItemStack.class.getMethod("setData", clazz3, Object.class);
            method2 = ItemStack.class.getMethod("getData", clazz3);
            bl = object != null;
        }
        catch (Throwable e) {
            // empty catch block
        }
        AVAILABLE = bl;
        TOOLTIP_DISPLAY_TYPE = object;
        TOOLTIP_DISPLAY_CLASS = clazz;
        SET_DATA = method;
        GET_DATA = method2;
    }
}

