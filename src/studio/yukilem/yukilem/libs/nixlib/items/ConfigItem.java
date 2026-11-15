package studio.yukilem.yukilem.libs.nixlib.items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.UnaryOperator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import studio.yukilem.yukilem.libs.nixlib.items.HeadDatabaseSupport;
import studio.yukilem.yukilem.libs.nixlib.items.ItemMetaReflect;
import studio.yukilem.yukilem.libs.nixlib.items.ItemStackComponents;
import studio.yukilem.yukilem.libs.nixlib.items.SkullBuilder;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;

public final class ConfigItem {
    private ConfigItem() {
    }

    public static ItemStack build(Map<String, Object> map) {
        return ConfigItem.build(map, UnaryOperator.identity());
    }

    public static ItemStack build(Map<String, Object> map, UnaryOperator<String> unaryOperator) {
        String string;
        boolean bl;
        String string2;
        String string3;
        Serializable serializable;
        List<String> list;
        if (map == null) {
            return new ItemStack(Material.STONE);
        }
        Material material = ConfigItem.resolveMaterial(ConfigItem.asString(map.get("material")));
        ItemStack itemStack = ConfigItem.createBase(map, material, unaryOperator);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        String string32 = ConfigItem.asString(map.get("name"));
        if (string32 != null) {
            itemMeta.displayName(ConfigItem.translate((String)unaryOperator.apply(string32)));
        }
        if ((list = ConfigItem.asStringList(map.get("lore"))) != null) {
            List<Component> loreLines = new ArrayList<>(list.size());
            for (String loreLine : list) {
                loreLines.add(ConfigItem.translate((String)unaryOperator.apply(loreLine)));
            }
            itemMeta.lore(loreLines);
        }
        Integer amount = ConfigItem.asInt(map.get("amount"));
        if (amount != null) {
            itemStack.setAmount(Math.max(1, Math.min(amount, itemStack.getMaxStackSize())));
        }
        if (ConfigItem.asBool(map.get("unbreakable"), false)) {
            itemMeta.setUnbreakable(true);
        }
        if (ConfigItem.asBool(map.get("glow"), false)) {
            itemMeta.addEnchant(Enchantment.MENDING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        ConfigItem.applyEnchants(itemMeta, ConfigItem.asStringList(map.get("enchants")));
        ConfigItem.applyItemFlags(itemMeta, ConfigItem.asStringList(ConfigItem.firstPresent(map, "item-flags", "itemflags", "flags")));
        ConfigItem.applyCustomModelData(itemMeta, map.get("custom-model-data"));
        ConfigItem.applyColor(itemMeta, ConfigItem.asString(map.get("color")));
        Integer n = ConfigItem.asInt(map.get("damage"));
        if (n != null && itemMeta instanceof Damageable) {
            Damageable damageable = (Damageable)itemMeta;
            damageable.setDamage(n);
        }
        if ((string3 = ConfigItem.asString(map.get("item-model"))) != null) {
            ItemMetaReflect.itemModel(itemMeta, string3);
        }
        if ((string2 = ConfigItem.asString(map.get("tooltip-style"))) != null) {
            ItemMetaReflect.tooltipStyle(itemMeta, string2);
        }
        Object object = ConfigItem.firstValue(map, "hide-tooltip", "hidetooltip", "hide_tooltip");
        Boolean bl2 = null;
        if (object != null && !ItemMetaReflect.hideTooltip(itemMeta, bl = ConfigItem.asBool(object, false))) {
            bl2 = bl;
        }
        itemStack.setItemMeta(itemMeta);
        if (bl2 != null) {
            ItemStackComponents.hideTooltip(itemStack, bl2);
        }
        if ((string = ConfigItem.asString(map.get("snbt"))) != null && !string.isBlank()) {
            itemStack = ConfigItem.applySnbt(itemStack, string);
        }
        return itemStack;
    }

    private static ItemStack createBase(Map<String, Object> map, Material material, UnaryOperator<String> unaryOperator) {
        ItemStack itemStack;
        String string = ConfigItem.asString(map.get("texture"));
        if (string != null && !string.isBlank()) {
            return SkullBuilder.head().texture(string.trim()).build();
        }
        String string2 = ConfigItem.asString(map.get("skull"));
        if (string2 == null) {
            string2 = ConfigItem.asString(map.get("head"));
        }
        if (string2 != null && !string2.isBlank()) {
            return SkullBuilder.head().ownerName((String)unaryOperator.apply(string2)).build();
        }
        String string3 = ConfigItem.asString(map.get("hdb"));
        if (string3 != null && !string3.isBlank() && (itemStack = HeadDatabaseSupport.head(string3)) != null) {
            return itemStack;
        }
        return new ItemStack(material);
    }

    private static Material resolveMaterial(String string) {
        if (string == null || string.isBlank()) {
            return Material.STONE;
        }
        Material material = Material.matchMaterial(string.trim());
        return material == null ? Material.STONE : material;
    }

    private static void applyEnchants(ItemMeta itemMeta, List<String> list) {
        if (list == null) {
            return;
        }
        for (String string : list) {
            Object object;
            int n = string.lastIndexOf(58);
            String string2 = n > 0 ? string.substring(0, n) : string;
            int n2 = 1;
            if (n > 0 && (object = ConfigItem.asInt(string.substring(n + 1).trim())) != null) {
                n2 = (Integer)object;
            }
            if ((object = ConfigItem.resolveEnchant(string2.trim())) == null) continue;
            itemMeta.addEnchant((Enchantment)object, Math.max(1, n2), true);
        }
    }

    private static Enchantment resolveEnchant(String string) {
        try {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(string.toLowerCase(Locale.ROOT)));
            if (enchantment != null) {
                return enchantment;
            }
        }
        catch (Throwable e) {
            // empty catch block
        }
        try {
            return Enchantment.getByName(string.toUpperCase(Locale.ROOT));
        }
        catch (Throwable e) {
            return null;
        }
    }

    private static void applyItemFlags(ItemMeta itemMeta, List<String> list) {
        if (list == null) {
            return;
        }
        for (String string : list) {
            try {
                itemMeta.addItemFlags(ItemFlag.valueOf(string.trim().toUpperCase(Locale.ROOT)));
            }
            catch (IllegalArgumentException e) {}
        }
    }

    private static void applyCustomModelData(ItemMeta itemMeta, Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof Map) {
            Integer n;
            Map map = (Map)object;
            List<String> list = ConfigItem.asStringList(map.get("floats"));
            if (list != null && !list.isEmpty() && (n = ConfigItem.asInt(list.get(0))) != null) {
                itemMeta.setCustomModelData(n);
            }
            return;
        }
        Integer n = ConfigItem.asInt(object);
        if (n != null) {
            itemMeta.setCustomModelData(n);
        }
    }

    private static void applyColor(ItemMeta itemMeta, String string) {
        Color color = ConfigItem.parseColor(string);
        if (color == null) {
            return;
        }
        if (itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta)itemMeta).setColor(color);
        } else if (itemMeta instanceof PotionMeta) {
            ((PotionMeta)itemMeta).setColor(color);
        }
    }

    private static Color parseColor(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        String string2 = string.trim();
        if (string2.startsWith("#") && string2.length() == 7) {
            try {
                return Color.fromRGB(Integer.parseInt(string2.substring(1), 16));
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
        String[] stringArray = string2.split(",");
        if (stringArray.length != 3) {
            return null;
        }
        Integer n = ConfigItem.asInt(stringArray[0].trim());
        Integer n2 = ConfigItem.asInt(stringArray[1].trim());
        Integer n3 = ConfigItem.asInt(stringArray[2].trim());
        if (n == null || n2 == null || n3 == null) {
            return null;
        }
        return Color.fromRGB(ConfigItem.clamp(n), ConfigItem.clamp(n2), ConfigItem.clamp(n3));
    }

    private static ItemStack applySnbt(ItemStack itemStack, String string) {
        try {
            Object object = Bukkit.class.getMethod("getUnsafe", new Class[0]).invoke(null, new Object[0]);
            object.getClass().getMethod("modifyItemStack", ItemStack.class, String.class).invoke(object, itemStack, string);
        }
        catch (Throwable e) {
            // empty catch block
        }
        return itemStack;
    }

    private static int clamp(int n) {
        return Math.max(0, Math.min(255, n));
    }

    private static Component translate(String string) {
        return ColorUtils.translate(string).decoration(TextDecoration.ITALIC, false);
    }

    private static String asString(Object object) {
        return object == null ? null : String.valueOf(object);
    }

    private static Object firstValue(Map<String, Object> map, String ... stringArray) {
        for (String string : stringArray) {
            if (!map.containsKey(string)) continue;
            return map.get(string);
        }
        return null;
    }

    private static Object firstPresent(Map<String, Object> map, String ... stringArray) {
        for (String string : stringArray) {
            Object object = map.get(string);
            if (object == null) continue;
            return object;
        }
        return null;
    }

    private static Integer asInt(Object object) {
        if (object instanceof Number) {
            Number number = (Number)object;
            return number.intValue();
        }
        if (object == null) {
            return null;
        }
        try {
            return Integer.parseInt(object.toString().trim());
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean asBool(Object object, boolean bl) {
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        if (object == null) {
            return bl;
        }
        return Boolean.parseBoolean(object.toString().trim());
    }

    private static List<String> asStringList(Object object) {
        if (object instanceof List) {
            List list = (List)object;
            ArrayList<String> arrayList = new ArrayList<String>(list.size());
            for (Object e : list) {
                if (e == null) continue;
                arrayList.add(String.valueOf(e));
            }
            return arrayList;
        }
        if (object instanceof String) {
            String string = (String)object;
            ArrayList<String> arrayList = new ArrayList<String>(1);
            arrayList.add(string);
            return arrayList;
        }
        return null;
    }
}

