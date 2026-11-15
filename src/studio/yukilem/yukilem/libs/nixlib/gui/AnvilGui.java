package studio.glace.glantielytratarget.libs.nixlib.gui;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import studio.glace.glantielytratarget.libs.nixlib.NixLib;
import studio.glace.glantielytratarget.libs.nixlib.gui.GuiNavigation;

public final class AnvilGui {
    private static final int RESULT_SLOT = 2;
    private static final Map<UUID, AnvilGui> ACTIVE = new ConcurrentHashMap<UUID, AnvilGui>();
    private String title;
    private ItemStack inputTemplate;
    private String initialText = "";
    private Function<String, ItemStack> outputProvider;
    private BiConsumer<Player, String> confirmHandler;
    private BiConsumer<Player, String> closeHandler;
    private Player player;
    private AnvilInventory inventory;
    private String currentText = "";
    private boolean confirmed;

    static AnvilGui active(UUID uUID) {
        return ACTIVE.get(uUID);
    }

    static void remove(UUID uUID) {
        ACTIVE.remove(uUID);
    }

    public AnvilGui title(String string) {
        this.title = string;
        return this;
    }

    public AnvilGui initialText(String string) {
        this.initialText = string == null ? "" : string;
        return this;
    }

    public AnvilGui inputItem(ItemStack itemStack) {
        this.inputTemplate = itemStack;
        return this;
    }

    public AnvilGui output(Function<String, ItemStack> function) {
        this.outputProvider = function;
        return this;
    }

    public AnvilGui onConfirm(BiConsumer<Player, String> biConsumer) {
        this.confirmHandler = biConsumer;
        return this;
    }

    public AnvilGui onClose(BiConsumer<Player, String> biConsumer) {
        this.closeHandler = biConsumer;
        return this;
    }

    public String currentText() {
        return this.currentText;
    }

    public void open(Player player) {
        Inventory inventory;
        InventoryView inventoryView;
        this.player = player;
        this.currentText = this.initialText;
        this.confirmed = false;
        GuiNavigation.beginTransition(player.getUniqueId());
        try {
            inventoryView = player.openAnvil(player.getLocation(), true);
        }
        finally {
            GuiNavigation.endTransition(player.getUniqueId());
        }
        if (inventoryView == null || !((inventory = inventoryView.getTopInventory()) instanceof AnvilInventory)) {
            return;
        }
        AnvilInventory anvilInventory = (AnvilInventory)inventory;
        this.inventory = anvilInventory;
        if (this.title != null && !this.title.isEmpty()) {
            AnvilGui.applyTitle(inventoryView, this.title);
        }
        ACTIVE.put(player.getUniqueId(), this);
        this.applyItems();
        this.scheduleApply(player);
    }

    void handlePrepare(PrepareAnvilEvent prepareAnvilEvent) {
        String renameText = prepareAnvilEvent.getInventory().getRenameText();
        String string = this.currentText = renameText == null ? "" : renameText;
        if (this.outputProvider != null) {
            prepareAnvilEvent.setResult(this.outputProvider.apply(this.currentText));
        }
        prepareAnvilEvent.getInventory().setRepairCost(0);
    }

    void handleClick(InventoryClickEvent inventoryClickEvent) {
        inventoryClickEvent.setCancelled(true);
        if (inventoryClickEvent.getRawSlot() != 2) {
            return;
        }
        this.confirmed = true;
        if (this.confirmHandler != null) {
            this.confirmHandler.accept((Player)inventoryClickEvent.getWhoClicked(), this.currentText);
        }
    }

    void handleClose(InventoryCloseEvent inventoryCloseEvent) {
        this.clearItems();
        if (this.confirmed) {
            return;
        }
        if (this.closeHandler != null) {
            this.closeHandler.accept((Player)inventoryCloseEvent.getPlayer(), this.currentText);
        }
    }

    private void clearItems() {
        if (this.inventory == null) {
            return;
        }
        try {
            this.inventory.setItem(0, null);
            this.inventory.setItem(1, null);
            this.inventory.setItem(2, null);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    boolean matches(Inventory inventory) {
        return this.inventory != null && this.inventory == inventory;
    }

    private void applyItems() {
        if (this.inventory == null) {
            return;
        }
        this.inventory.setItem(0, this.buildInput());
        if (this.outputProvider != null) {
            this.inventory.setItem(2, this.outputProvider.apply(this.currentText));
        }
        try {
            this.inventory.setRepairCost(0);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    private void scheduleApply(Player player) {
        try {
            NixLib.get().scheduler().runForLater(player, this::applyItems, 1L);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }

    private ItemStack buildInput() {
        ItemStack itemStack = this.inputTemplate != null ? this.inputTemplate.clone() : new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.displayName(Component.text(this.initialText).decoration(TextDecoration.ITALIC, false));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    private static void applyTitle(InventoryView inventoryView, String string) {
        try {
            inventoryView.setTitle(string);
        }
        catch (Throwable e) {
            // empty catch block
        }
    }
}

