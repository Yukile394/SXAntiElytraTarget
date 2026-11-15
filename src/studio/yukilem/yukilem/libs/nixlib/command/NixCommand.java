package studio.yukilem.yukilem.libs.nixlib.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.command.CommandAction;
import studio.yukilem.yukilem.libs.nixlib.command.CommandContext;
import studio.yukilem.yukilem.libs.nixlib.command.CommandSpec;

public final class NixCommand
extends Command {
    private final CommandSpec spec;

    public NixCommand(@NotNull CommandSpec commandSpec) {
        super(commandSpec.label(), commandSpec.description(), "/" + commandSpec.label(), commandSpec.aliases());
        this.spec = commandSpec;
    }

    @NotNull
    public CommandSpec spec() {
        return this.spec;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String string, @NotNull String[] stringArray) {
        CommandContext commandContext = new CommandContext(commandSender, string, stringArray);
        if (!this.allowed(commandSender, this.spec.permission())) {
            this.deny(commandContext);
            return true;
        }
        if (stringArray.length == 0) {
            if (this.spec.rootAction() != null) {
                this.spec.rootAction().run(commandContext);
            }
            return true;
        }
        CommandSpec.Sub sub = this.spec.findSub(stringArray[0]);
        if (sub == null) {
            CommandAction commandAction;
            CommandAction commandAction2 = commandAction = this.spec.fallback() != null ? this.spec.fallback() : this.spec.rootAction();
            if (commandAction != null) {
                commandAction.run(commandContext);
            }
            return true;
        }
        if (!this.allowed(commandSender, sub.permission())) {
            this.deny(commandContext);
            return true;
        }
        if (sub.action() != null) {
            sub.action().run(commandContext);
        } else if (this.spec.rootAction() != null) {
            this.spec.rootAction().run(commandContext);
        }
        return true;
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String string, @NotNull String[] stringArray) {
        if (stringArray.length == 0 || !this.allowed(commandSender, this.spec.permission())) {
            return new ArrayList<String>();
        }
        if (stringArray.length == 1) {
            String lowerCase = stringArray[0].toLowerCase(Locale.ROOT);
            ArrayList<String> arrayList = new ArrayList<String>();
            for (CommandSpec.Sub sub : this.spec.subs()) {
                String string2;
                if (!this.allowed(commandSender, sub.permission()) || !(string2 = sub.primary()).toLowerCase(Locale.ROOT).startsWith(lowerCase)) continue;
                arrayList.add(string2);
            }
            return arrayList;
        }
        CommandSpec.Sub sub = this.spec.findSub(stringArray[0]);
        if (sub == null || !this.allowed(commandSender, sub.permission())) {
            return new ArrayList<String>();
        }
        int n = stringArray.length - 2;
        if (n < 0 || n >= sub.argSuggestions().size()) {
            return new ArrayList<String>();
        }
        List<String> list = sub.argSuggestions().get(n).suggest(new CommandContext(commandSender, string, stringArray));
        String lowerCase = stringArray[stringArray.length - 1].toLowerCase(Locale.ROOT);
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string3 : list) {
            if (string3 == null || !string3.toLowerCase(Locale.ROOT).startsWith(lowerCase)) continue;
            arrayList.add(string3);
        }
        return arrayList;
    }

    private boolean allowed(@NotNull CommandSender commandSender, String string) {
        return string == null || string.isBlank() || commandSender.hasPermission(string);
    }

    private void deny(@NotNull CommandContext commandContext) {
        if (this.spec.permissionDenied() != null) {
            this.spec.permissionDenied().run(commandContext);
        }
    }
}

