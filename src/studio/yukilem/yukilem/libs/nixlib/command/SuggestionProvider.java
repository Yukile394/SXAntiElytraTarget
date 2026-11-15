package studio.glace.glantielytratarget.libs.nixlib.command;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.libs.nixlib.command.CommandContext;

@FunctionalInterface
public interface SuggestionProvider {
    @NotNull
    public List<String> suggest(@NotNull CommandContext commandContext1);
}

