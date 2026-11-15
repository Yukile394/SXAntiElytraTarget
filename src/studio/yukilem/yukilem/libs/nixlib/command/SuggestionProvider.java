package studio.yukilem.yukilem.libs.nixlib.command;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.command.CommandContext;

@FunctionalInterface
public interface SuggestionProvider {
    @NotNull
    public List<String> suggest(@NotNull CommandContext commandContext1);
}

