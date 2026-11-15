package studio.glace.glantielytratarget.libs.nixlib.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import studio.glace.glantielytratarget.libs.nixlib.gui.ClickContext;
import studio.glace.glantielytratarget.libs.nixlib.gui.GuiItem;
import studio.glace.glantielytratarget.libs.nixlib.gui.GuiNavigation;

public class Gui
implements InventoryHolder {
    private final String title;
    private final int rows;
    private final Map<Integer, GuiItem> items = new HashMap<Integer, GuiItem>();
    private final Inventory inventory;
    private boolean cancelByDefault = true;
    private Consumer<ClickContext> globalClickHandler;
    private Consumer<InventoryOpenEvent> openHandler;
    private Consumer<InventoryCloseEvent> closeHandler;
    private Consumer<Player> backHandler;

    public Gui(String string, int n) {
        if (n < 1 || n > 6) {
            throw new IllegalArgumentException("rows must be between 1 and 6, got " + n);
        }
        this.title = string;
        this.rows = n;
        this.inventory = Bukkit.createInventory((InventoryHolder)this, n * 9, string);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public int size() {
        return this.rows * 9;
    }

    public int rows() {
        return this.rows;
    }

    public String title() {
        return this.title;
    }

    public Inventory inventory() {
        return this.inventory;
    }

    public Gui setItem(int n, GuiItem guiItem) {
        this.ensureSlot(n);
        this.items.put(n, guiItem);
        this.inventory.setItem(n, guiItem.stack());
        return this;
    }

    public Gui setItem(int n, ItemStack itemStack) {
        return this.setItem(n, GuiItem.of(itemStack));
    }

    public Gui setItem(int n, ItemStack itemStack, Consumer<ClickContext> consumer) {
        return this.setItem(n, GuiItem.of(itemStack, consumer));
    }

    public Gui fill(ItemStack itemStack) {
        for (int n = 0; n < this.size(); ++n) {
            if (this.items.containsKey(n)) continue;
            this.setItem(n, itemStack);
        }
        return this;
    }

    public Gui border(ItemStack itemStack) {
        int n;
        for (n = 0; n < 9; ++n) {
            this.setItem(n, itemStack);
        }
        for (n = this.size() - 9; n < this.size(); ++n) {
            this.setItem(n, itemStack);
        }
        for (n = 1; n < this.rows - 1; ++n) {
            this.setItem(n * 9, itemStack);
            this.setItem(n * 9 + 8, itemStack);
        }
        return this;
    }

    public Gui clear(int n) {
        this.ensureSlot(n);
        this.items.remove(n);
        this.inventory.setItem(n, null);
        return this;
    }

    public Gui clearAll() {
        this.items.clear();
        this.inventory.clear();
        return this;
    }

    public Gui cancelByDefault(boolean bl) {
        this.cancelByDefault = bl;
        return this;
    }

    public Gui onAnyClick(Consumer<ClickContext> consumer) {
        this.globalClickHandler = consumer;
        return this;
    }

    public Gui onOpen(Consumer<InventoryOpenEvent> consumer) {
        this.openHandler = consumer;
        return this;
    }

    public Gui onClose(Consumer<InventoryCloseEvent> consumer) {
        this.closeHandler = consumer;
        return this;
    }

    public Gui onBack(Consumer<Player> consumer) {
        this.backHandler = consumer;
        return this;
    }

    Consumer<Player> backHandler() {
        return this.backHandler;
    }

    public void open(Player player) {
        GuiNavigation.beginTransition(player.getUniqueId());
        try {
            player.openInventory(this.inventory);
        }
        finally {
            GuiNavigation.endTransition(player.getUniqueId());
        }
    }

    void handleClick(InventoryClickEvent inventoryClickEvent) {
        GuiItem guiItem;
        if (this.cancelByDefault) {
            inventoryClickEvent.setCancelled(true);
        }
        ClickContext clickContext = new ClickContext(this, inventoryClickEvent);
        if (this.globalClickHandler != null) {
            this.globalClickHandler.accept(clickContext);
        }
        if ((guiItem = this.items.get(inventoryClickEvent.getRawSlot())) != null) {
            guiItem.onClick(clickContext);
        }
    }

    void handleDrag(InventoryDragEvent inventoryDragEvent) {
        if (this.cancelByDefault) {
            inventoryDragEvent.setCancelled(true);
        }
    }

    void handleOpen(InventoryOpenEvent inventoryOpenEvent) {
        if (this.openHandler != null) {
            this.openHandler.accept(inventoryOpenEvent);
        }
    }

    void handleClose(InventoryCloseEvent inventoryCloseEvent) {
        if (this.closeHandler != null) {
            this.closeHandler.accept(inventoryCloseEvent);
        }
    }

    public boolean isViewing(HumanEntity humanEntity) {
        return this.inventory.getViewers().contains(humanEntity);
    }

    private void ensureSlot(int n) {
        if (n < 0 || n >= this.size()) {
            throw new IndexOutOfBoundsException("slot " + n + " not in [0, " + this.size() + ")");
        }
    }
}

