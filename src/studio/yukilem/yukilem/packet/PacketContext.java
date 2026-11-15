package studio.yukilem.yukilem.packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record PacketContext(@NotNull PacketReceiveEvent event, @NotNull Player player, @NotNull User user, @NotNull UUID playerId, long timestamp) {
}

