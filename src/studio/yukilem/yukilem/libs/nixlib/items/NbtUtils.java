package studio.glace.glantielytratarget.libs.nixlib.items;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public final class NbtUtils {
    private NbtUtils() {
    }

    public static ItemStack setString(Plugin plugin, ItemStack itemStack, String string, String string2) {
        return NbtUtils.apply(itemStack, itemMeta -> itemMeta.getPersistentDataContainer().set(NbtUtils.keyOf(plugin, string), PersistentDataType.STRING, string2));
    }

    public static Optional<String> getString(Plugin plugin, ItemStack itemStack, String string) {
        return NbtUtils.read(itemStack, persistentDataContainer -> persistentDataContainer.get(NbtUtils.keyOf(plugin, string), PersistentDataType.STRING));
    }

    public static ItemStack setInt(Plugin plugin, ItemStack itemStack, String string, int n) {
        return NbtUtils.apply(itemStack, itemMeta -> itemMeta.getPersistentDataContainer().set(NbtUtils.keyOf(plugin, string), PersistentDataType.INTEGER, n));
    }

    public static Optional<Integer> getInt(Plugin plugin, ItemStack itemStack, String string) {
        return NbtUtils.read(itemStack, persistentDataContainer -> persistentDataContainer.get(NbtUtils.keyOf(plugin, string), PersistentDataType.INTEGER));
    }

    public static ItemStack setLong(Plugin plugin, ItemStack itemStack, String string, long l) {
        return NbtUtils.apply(itemStack, itemMeta -> itemMeta.getPersistentDataContainer().set(NbtUtils.keyOf(plugin, string), PersistentDataType.LONG, l));
    }

    public static Optional<Long> getLong(Plugin plugin, ItemStack itemStack, String string) {
        return NbtUtils.read(itemStack, persistentDataContainer -> persistentDataContainer.get(NbtUtils.keyOf(plugin, string), PersistentDataType.LONG));
    }

    public static ItemStack setDouble(Plugin plugin, ItemStack itemStack, String string, double d) {
        return NbtUtils.apply(itemStack, itemMeta -> itemMeta.getPersistentDataContainer().set(NbtUtils.keyOf(plugin, string), PersistentDataType.DOUBLE, d));
    }

    public static Optional<Double> getDouble(Plugin plugin, ItemStack itemStack, String string) {
        return NbtUtils.read(itemStack, persistentDataContainer -> persistentDataContainer.get(NbtUtils.keyOf(plugin, string), PersistentDataType.DOUBLE));
    }

    public static ItemStack setBoolean(Plugin plugin, ItemStack itemStack, String string, boolean bl) {
        return NbtUtils.setInt(plugin, itemStack, string, bl ? 1 : 0);
    }

    public static boolean getBoolean(Plugin plugin, ItemStack itemStack, String string) {
        return NbtUtils.getInt(plugin, itemStack, string).map(n -> n != 0).orElse(false);
    }

    public static ItemStack setUuid(Plugin plugin, ItemStack itemStack, String string, UUID uUID) {
        return NbtUtils.setString(plugin, itemStack, string, uUID.toString());
    }

    public static Optional<UUID> getUuid(Plugin plugin, ItemStack itemStack, String string2) {
        return NbtUtils.getString(plugin, itemStack, string2).map(string -> {
            try {
                return UUID.fromString(string);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        });
    }

    public static boolean has(Plugin plugin, ItemStack itemStack, String string) {
        NamespacedKey namespacedKey;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();
        return persistentDataContainer.has(namespacedKey = NbtUtils.keyOf(plugin, string), PersistentDataType.STRING) || persistentDataContainer.has(namespacedKey, PersistentDataType.INTEGER) || persistentDataContainer.has(namespacedKey, PersistentDataType.LONG) || persistentDataContainer.has(namespacedKey, PersistentDataType.DOUBLE);
    }

    public static ItemStack remove(Plugin plugin, ItemStack itemStack, String string) {
        return NbtUtils.apply(itemStack, itemMeta -> itemMeta.getPersistentDataContainer().remove(NbtUtils.keyOf(plugin, string)));
    }

    private static NamespacedKey keyOf(Plugin plugin, String string) {
        return new NamespacedKey(plugin, string);
    }

    private static ItemStack apply(ItemStack itemStack, Consumer<ItemMeta> consumer) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static <T> Optional<T> read(ItemStack itemStack, Function<PersistentDataContainer, T> function) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return Optional.empty();
        }
        return Optional.ofNullable(function.apply(itemStack.getItemMeta().getPersistentDataContainer()));
    }
}

