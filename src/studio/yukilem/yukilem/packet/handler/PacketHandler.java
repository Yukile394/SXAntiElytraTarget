package studio.yukilem.yukilem.packet.handler;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.packet.PacketContext;

@FunctionalInterface
public interface PacketHandler {
    public void handle(@NotNull PacketContext packetContext1);
}

