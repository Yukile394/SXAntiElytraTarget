package studio.glace.glantielytratarget.libs.nixlib.menu;

import java.util.function.UnaryOperator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.libs.nixlib.gui.ClickContext;
import studio.glace.glantielytratarget.libs.nixlib.gui.Gui;

public final class ActionContext {
    private final Player player;
    private final ClickContext click;
    private final UnaryOperator<String> placeholders;

    public ActionContext(@NotNull Player player, @Nullable ClickContext clickContext, @Nullable UnaryOperator<String> unaryOperator) {
        this.player = player;
        this.click = clickContext;
        this.placeholders = unaryOperator;
    }

    @NotNull
    public static ActionContext of(@NotNull Player player) {
        return new ActionContext(player, null, null);
    }

    @NotNull
    public static ActionContext of(@NotNull Player player, @Nullable UnaryOperator<String> unaryOperator) {
        return new ActionContext(player, null, unaryOperator);
    }

    @NotNull
    public static ActionContext of(@NotNull ClickContext clickContext, @Nullable UnaryOperator<String> unaryOperator) {
        return new ActionContext(clickContext.player(), clickContext, unaryOperator);
    }

    @NotNull
    public Player player() {
        return this.player;
    }

    @Nullable
    public ClickContext click() {
        return this.click;
    }

    @Nullable
    public Gui gui() {
        return this.click == null ? null : this.click.gui();
    }

    @NotNull
    public String apply(@Nullable String string) {
        if (string == null) {
            return "";
        }
        return this.placeholders == null ? string : (String)this.placeholders.apply(string);
    }
}

