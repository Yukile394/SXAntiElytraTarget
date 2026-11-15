package studio.yukilem.yukilem.menu;

import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class ConfirmRequest {
    private final String description;
    private final Consumer<Player> onConfirm;
    private final Consumer<Player> onCancel;

    public ConfirmRequest(@NotNull String string, @NotNull Consumer<Player> consumer, @NotNull Consumer<Player> consumer2) {
        this.description = string;
        this.onConfirm = consumer;
        this.onCancel = consumer2;
    }

    @NotNull
    public String description() {
        return this.description;
    }

    @NotNull
    public Consumer<Player> onConfirm() {
        return this.onConfirm;
    }

    @NotNull
    public Consumer<Player> onCancel() {
        return this.onCancel;
    }
}

