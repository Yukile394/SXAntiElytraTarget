package studio.yukilem.yukilem.listener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import lombok.Generated;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;

public final class CommandPriorityListener
implements Listener {
    private static final Set<String> SUPPORT_LABELS = Set.of("elytratarget", "elytraac");
    private final PluginContext ctx;

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onCommandSend(@NotNull PlayerCommandSendEvent playerCommandSendEvent) {
        Iterator<String> iterator;
        if (this.ctx.config() == null || !this.ctx.config().isSupportCommandEnabled()) {
            return;
        }
        Collection<String> commands = playerCommandSendEvent.getCommands();
        Iterator<String> iterator2 = SUPPORT_LABELS.iterator();
        while ((iterator = iterator2).hasNext()) {
            Collection<String> collection = commands;
            Iterator<String> iterator3 = iterator2;
            String string = iterator3.next();
            String string2 = string;
            if (collection.contains(string2)) continue;
            Collection<String> collection2 = commands;
            String string3 = string;
            boolean bl = collection2.add(string3);
        }
    }

    @Generated
    public CommandPriorityListener(PluginContext pluginContext) {
        this.ctx = pluginContext;
    }
}

