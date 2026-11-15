package studio.glace.glantielytratarget.libs.nixlib.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import studio.glace.glantielytratarget.libs.nixlib.gui.Gui;

public final class ClickContext {
    private final InventoryClickEvent event;
    private final Gui gui;

    ClickContext(Gui gui, InventoryClickEvent inventoryClickEvent) {
        this.gui = gui;
        this.event = inventoryClickEvent;
    }

    public Player player() {
        return (Player)this.event.getWhoClicked();
    }

    public Gui gui() {
        return this.gui;
    }

    public InventoryClickEvent event() {
        return this.event;
    }

    public ClickType clickType() {
        return this.event.getClick();
    }

    public InventoryAction action() {
        return this.event.getAction();
    }

    public int slot() {
        return this.event.getRawSlot();
    }

    public ItemStack cursor() {
        return this.event.getCursor();
    }

    public boolean isLeftClick() {
        return this.event.getClick() == ClickType.LEFT;
    }

    public boolean isRightClick() {
        return this.event.getClick() == ClickType.RIGHT;
    }

    public boolean isShiftClick() {
        return this.event.getClick().isShiftClick();
    }

    public void close() {
        this.player().closeInventory();
    }
}

