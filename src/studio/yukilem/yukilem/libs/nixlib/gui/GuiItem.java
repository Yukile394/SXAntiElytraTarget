package studio.glace.glantielytratarget.libs.nixlib.gui;

import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;
import studio.glace.glantielytratarget.libs.nixlib.gui.ClickContext;

public final class GuiItem {
    private final ItemStack item;
    private final Consumer<ClickContext> handler;

    private GuiItem(ItemStack itemStack, Consumer<ClickContext> consumer) {
        this.item = itemStack;
        this.handler = consumer;
    }

    public static GuiItem of(ItemStack itemStack) {
        return new GuiItem(itemStack, clickContext -> {});
    }

    public static GuiItem of(ItemStack itemStack, Consumer<ClickContext> consumer) {
        return new GuiItem(itemStack, consumer == null ? clickContext -> {} : consumer);
    }

    public ItemStack stack() {
        return this.item;
    }

    public void onClick(ClickContext clickContext) {
        this.handler.accept(clickContext);
    }
}

