package studio.yukilem.yukilem.libs.nixlib.items;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;

final class ItemMetaReflect {
    private static final Object MISSING = new Object();
    private static final Map<String, Object> RESOLVED = new ConcurrentHashMap<String, Object>();

    private ItemMetaReflect() {
    }

    static void itemModel(ItemMeta itemMeta, String string) {
        NamespacedKey namespacedKey = ItemMetaReflect.parseKey(string);
        if (namespacedKey != null) {
            ItemMetaReflect.invoke(itemMeta, "setItemModel", NamespacedKey.class, namespacedKey);
        }
    }

    static void tooltipStyle(ItemMeta itemMeta, String string) {
        NamespacedKey namespacedKey = ItemMetaReflect.parseKey(string);
        if (namespacedKey != null) {
            ItemMetaReflect.invoke(itemMeta, "setTooltipStyle", NamespacedKey.class, namespacedKey);
        }
    }

    static boolean hideTooltip(ItemMeta itemMeta, boolean bl) {
        Method method = ItemMetaReflect.resolve(itemMeta.getClass(), "setHideTooltip", Boolean.TYPE);
        if (method == null) {
            return false;
        }
        try {
            method.invoke((Object)itemMeta, bl);
        }
        catch (Throwable e) {
            return false;
        }
        Method method2 = ItemMetaReflect.resolveNoArg(itemMeta.getClass(), "isHideTooltip");
        if (method2 != null) {
            try {
                Object object = method2.invoke((Object)itemMeta, new Object[0]);
                if (object instanceof Boolean) {
                    return (Boolean)object == bl;
                }
            }
            catch (Throwable e) {
                // empty catch block
            }
        }
        return true;
    }

    private static NamespacedKey parseKey(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        try {
            return NamespacedKey.fromString(string.trim().toLowerCase());
        }
        catch (Throwable e) {
            return null;
        }
    }

    private static void invoke(ItemMeta itemMeta, String string, Class<?> clazz, Object object) {
        Method method = ItemMetaReflect.resolve(itemMeta.getClass(), string, clazz);
        if (method == null) {
            return;
        }
        try {
            method.invoke((Object)itemMeta, object);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    private static Method resolve(Class<?> clazz, String string, Class<?> clazz2) {
        String string2 = clazz.getName() + "#" + string;
        Object object = RESOLVED.get(string2);
        if (object == MISSING) {
            return null;
        }
        if (object instanceof Method) {
            Method method = (Method)object;
            return method;
        }
        try {
            Method method = clazz.getMethod(string, clazz2);
            RESOLVED.put(string2, method);
            return method;
        }
        catch (Throwable e) {
            RESOLVED.put(string2, MISSING);
            return null;
        }
    }

    private static Method resolveNoArg(Class<?> clazz, String string) {
        String string2 = clazz.getName() + "#" + string + "()";
        Object object = RESOLVED.get(string2);
        if (object == MISSING) {
            return null;
        }
        if (object instanceof Method) {
            Method method = (Method)object;
            return method;
        }
        try {
            Method method = clazz.getMethod(string, new Class[0]);
            RESOLVED.put(string2, method);
            return method;
        }
        catch (Throwable e) {
            RESOLVED.put(string2, MISSING);
            return null;
        }
    }
}

