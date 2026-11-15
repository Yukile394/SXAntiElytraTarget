package studio.glace.glantielytratarget.libs.nixlib.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CommandContext {
    private final CommandSender sender;
    private final String label;
    private final String[] args;

    public CommandContext(@NotNull CommandSender commandSender, @NotNull String string, @NotNull String[] stringArray) {
        this.sender = commandSender;
        this.label = string;
        this.args = stringArray;
    }

    @NotNull
    public CommandSender sender() {
        return this.sender;
    }

    @NotNull
    public String label() {
        return this.label;
    }

    @NotNull
    public String[] args() {
        return this.args;
    }

    public int size() {
        return this.args.length;
    }

    public boolean isPlayer() {
        return this.sender instanceof Player;
    }

    @Nullable
    public Player player() {
        CommandSender commandSender = this.sender;
        return commandSender instanceof Player ? (Player)commandSender : null;
    }

    @Nullable
    public String arg(int n) {
        return n >= 0 && n < this.args.length ? this.args[n] : null;
    }

    @NotNull
    public String argOr(int n, @NotNull String string) {
        String string2 = this.arg(n);
        return string2 == null ? string : string2;
    }
}

