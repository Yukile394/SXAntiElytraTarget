package studio.glace.glantielytratarget.libs.nixlib.items;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public final class HeadDatabaseSupport {
    private static volatile Object api;
    private static volatile boolean resolved;

    private HeadDatabaseSupport() {
    }

    public static boolean isAvailable() {
        return Bukkit.getPluginManager().getPlugin("HeadDatabase") != null;
    }

    public static ItemStack head(String string) {
        if (string == null || string.isBlank() || !HeadDatabaseSupport.isAvailable()) {
            return null;
        }
        try {
            Object object = HeadDatabaseSupport.api();
            if (object == null) {
                return null;
            }
            Object object2 = object.getClass().getMethod("getItemHead", String.class).invoke(object, string.trim());
            return object2 instanceof ItemStack ? (ItemStack)object2 : null;
        }
        catch (Throwable e) {
            return null;
        }
    }

    private static Object api() {
        if (resolved) {
            return api;
        }
        Class<HeadDatabaseSupport> clazz = HeadDatabaseSupport.class;
        synchronized (HeadDatabaseSupport.class) {
            if (resolved) {
                // ** MonitorExit[arg0] (shouldn't be in output)
                return api;
            }
            try {
                Class<?> clazz2 = Class.forName("me.arcaniax.hdb.api.HeadDatabaseAPI");
                api = clazz2.getConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (Throwable e) {
                api = null;
            }
            resolved = true;
            // ** MonitorExit[arg0] (shouldn't be in output)
            return api;
        }
    }
}

