package studio.glace.glantielytratarget.packet.handler;

import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.packet.PacketContext;

@FunctionalInterface
public interface PacketHandler {
    public void handle(@NotNull PacketContext packetContext1);
}

