package studio.yukilem.yukilem.libs.nixlib.command;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.command.CommandContext;

@FunctionalInterface
public interface CommandAction {
    public void run(@NotNull CommandContext commandContext1);
}

