package studio.glace.glantielytratarget.libs.nixlib.gui;

import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import studio.glace.glantielytratarget.libs.nixlib.NixLib;
import studio.glace.glantielytratarget.libs.nixlib.gui.AnvilGui;
import studio.glace.glantielytratarget.libs.nixlib.gui.Gui;
import studio.glace.glantielytratarget.libs.nixlib.gui.GuiNavigation;

public final class GuiManager
implements Listener {
    private static GuiManager instance;
    private final Plugin plugin;

    private GuiManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public static synchronized GuiManager register(Plugin plugin) {
        if (instance != null) {
            return instance;
        }
        instance = new GuiManager(plugin);
        plugin.getServer().getPluginManager().registerEvents(instance, plugin);
        return instance;
    }

    public static synchronized void unregister() {
        if (instance == null) {
            return;
        }
        HandlerList.unregisterAll(instance);
        instance = null;
    }

    public Plugin plugin() {
        return this.plugin;
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=false)
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        AnvilGui anvilGui = AnvilGui.active(inventoryClickEvent.getWhoClicked().getUniqueId());
        if (anvilGui != null && anvilGui.matches(inventoryClickEvent.getInventory())) {
            anvilGui.handleClick(inventoryClickEvent);
            return;
        }
        Gui gui = this.resolve(inventoryClickEvent.getInventory());
        if (gui != null) {
            gui.handleClick(inventoryClickEvent);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=false)
    public void onDrag(InventoryDragEvent inventoryDragEvent) {
        AnvilGui anvilGui = AnvilGui.active(inventoryDragEvent.getWhoClicked().getUniqueId());
        if (anvilGui != null && anvilGui.matches(inventoryDragEvent.getInventory())) {
            inventoryDragEvent.setCancelled(true);
            return;
        }
        Gui gui = this.resolve(inventoryDragEvent.getInventory());
        if (gui != null) {
            gui.handleDrag(inventoryDragEvent);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=false)
    public void onPrepareAnvil(PrepareAnvilEvent prepareAnvilEvent) {
        AnvilGui anvilGui = AnvilGui.active(prepareAnvilEvent.getView().getPlayer().getUniqueId());
        if (anvilGui != null && anvilGui.matches(prepareAnvilEvent.getInventory())) {
            anvilGui.handlePrepare(prepareAnvilEvent);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {
        Gui gui = this.resolve(inventoryOpenEvent.getInventory());
        if (gui != null) {
            gui.handleOpen(inventoryOpenEvent);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {
        UUID uniqueId = inventoryCloseEvent.getPlayer().getUniqueId();
        AnvilGui anvilGui = AnvilGui.active(uniqueId);
        if (anvilGui != null && anvilGui.matches(inventoryCloseEvent.getInventory())) {
            AnvilGui.remove(uniqueId);
            anvilGui.handleClose(inventoryCloseEvent);
            return;
        }
        Gui gui = this.resolve(inventoryCloseEvent.getInventory());
        if (gui == null) {
            return;
        }
        gui.handleClose(inventoryCloseEvent);
        boolean bl = GuiNavigation.isTransitioning(uniqueId);
        boolean bl2 = GuiNavigation.consumeSuppressBack(uniqueId);
        Consumer<Player> consumer = gui.backHandler();
        if (bl || bl2 || consumer == null) {
            return;
        }
        HumanEntity player = inventoryCloseEvent.getPlayer();
        if (!(player instanceof Player)) {
            return;
        }
        Player player2 = (Player)player;
        this.scheduleBack(player2, consumer);
    }

    private void scheduleBack(Player player, Consumer<Player> consumer) {
        Runnable runnable = () -> {
            if (player.isOnline()) {
                consumer.accept(player);
            }
        };
        try {
            NixLib.get().scheduler().runForLater(player, runnable, 1L);
        }
        catch (Throwable e) {
            this.plugin.getServer().getScheduler().runTask(this.plugin, runnable);
        }
    }

    private Gui resolve(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryHolder holder = inventory.getHolder();
        return holder instanceof Gui ? (Gui)holder : null;
    }
}

