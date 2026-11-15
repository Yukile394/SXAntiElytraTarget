package studio.yukilem.yukilem.libs.nixlib.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import studio.yukilem.yukilem.libs.nixlib.items.ItemMetaReflect;
import studio.yukilem.yukilem.libs.nixlib.items.ItemStackComponents;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;

public final class ItemBuilder {
    private final ItemStack item;

    private ItemBuilder(ItemStack itemStack) {
        this.item = itemStack;
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    public static ItemBuilder of(Material material, int n) {
        return new ItemBuilder(new ItemStack(material, n));
    }

    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack.clone());
    }

    public ItemBuilder amount(int n) {
        this.item.setAmount(Math.max(1, Math.min(n, this.item.getMaxStackSize())));
        return this;
    }

    public ItemBuilder name(String string) {
        return this.mutate(itemMeta -> itemMeta.displayName(ColorUtils.parse(string)));
    }

    public ItemBuilder name(Component component) {
        return this.mutate(itemMeta -> itemMeta.displayName(component));
    }

    public ItemBuilder lore(String ... stringArray) {
        return this.lore(Arrays.asList(stringArray));
    }

    public ItemBuilder lore(List<String> list) {
        return this.mutate(itemMeta -> itemMeta.lore(ColorUtils.parseAll(list)));
    }

    public ItemBuilder loreComponents(List<Component> list) {
        return this.mutate(itemMeta -> itemMeta.lore(list));
    }

    public ItemBuilder appendLore(String ... stringArray) {
        return this.mutate(itemMeta -> {
            List<Component> list = itemMeta.lore();
            ArrayList<Component> arrayList = list == null ? new ArrayList<Component>() : new ArrayList<Component>(list);
            for (String string : stringArray) {
                arrayList.add(ColorUtils.parse(string));
            }
            itemMeta.lore(arrayList);
        });
    }

    public ItemBuilder enchant(Enchantment enchantment, int n) {
        return this.mutate(itemMeta -> itemMeta.addEnchant(enchantment, n, true));
    }

    public ItemBuilder unsafeEnchant(Enchantment enchantment, int n) {
        this.item.addUnsafeEnchantment(enchantment, n);
        return this;
    }

    public ItemBuilder flags(ItemFlag ... itemFlagArray) {
        return this.mutate(itemMeta -> itemMeta.addItemFlags(itemFlagArray));
    }

    public ItemBuilder hideAllFlags() {
        return this.flags(ItemFlag.values());
    }

    public ItemBuilder unbreakable() {
        return this.mutate(itemMeta -> itemMeta.setUnbreakable(true));
    }

    public ItemBuilder damage(int n) {
        return this.mutate(itemMeta -> {
            if (itemMeta instanceof Damageable) {
                ((Damageable)itemMeta).setDamage(n);
            }
        });
    }

    public ItemBuilder customModelData(Integer n) {
        return this.mutate(itemMeta -> itemMeta.setCustomModelData(n));
    }

    public ItemBuilder glow() {
        return this.mutate(itemMeta -> {
            itemMeta.addEnchant(Enchantment.MENDING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
    }

    public ItemBuilder glow(boolean bl) {
        return bl ? this.glow() : this;
    }

    public ItemBuilder unbreakable(boolean bl) {
        return this.mutate(itemMeta -> itemMeta.setUnbreakable(bl));
    }

    public ItemBuilder itemModel(String string) {
        return this.mutate(itemMeta -> ItemMetaReflect.itemModel(itemMeta, string));
    }

    public ItemBuilder tooltipStyle(String string) {
        return this.mutate(itemMeta -> ItemMetaReflect.tooltipStyle(itemMeta, string));
    }

    public ItemBuilder hideTooltip(boolean bl) {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (itemMeta == null) {
            return this;
        }
        boolean bl2 = ItemMetaReflect.hideTooltip(itemMeta, bl);
        this.item.setItemMeta(itemMeta);
        if (!bl2) {
            ItemStackComponents.hideTooltip(this.item, bl);
        }
        return this;
    }

    public ItemBuilder color(Color color) {
        return this.mutate(itemMeta -> {
            if (itemMeta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta)itemMeta).setColor(color);
            } else if (itemMeta instanceof PotionMeta) {
                ((PotionMeta)itemMeta).setColor(color);
            }
        });
    }

    public ItemBuilder attribute(Attribute attribute, AttributeModifier attributeModifier) {
        return this.mutate(itemMeta -> itemMeta.addAttributeModifier(attribute, attributeModifier));
    }

    public ItemBuilder pdcString(Plugin plugin, String string, String string2) {
        return this.mutate(itemMeta -> itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, string), PersistentDataType.STRING, string2));
    }

    public ItemBuilder pdcInt(Plugin plugin, String string, int n) {
        return this.mutate(itemMeta -> itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, string), PersistentDataType.INTEGER, n));
    }

    public ItemBuilder modify(Consumer<ItemMeta> consumer) {
        return this.mutate(consumer);
    }

    public ItemStack build() {
        return this.item;
    }

    public ItemStack cloneStack() {
        return this.item.clone();
    }

    private ItemBuilder mutate(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (itemMeta != null) {
            consumer.accept(itemMeta);
            this.item.setItemMeta(itemMeta);
        }
        return this;
    }
}

