package studio.glace.glantielytratarget.packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.packet.state.RotationState;
import studio.glace.glantielytratarget.packet.state.TickPacketState;

public final class RotationTracker {
    private RotationTracker() {
    }

    public static void applyFlying(@NotNull PacketReceiveEvent packetReceiveEvent, @NotNull RotationState rotationState, @NotNull TickPacketState tickPacketState) {
        float f;
        float f2;
        float f3;
        float f4;
        WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying = new WrapperPlayClientPlayerFlying(packetReceiveEvent);
        WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying2 = wrapperPlayClientPlayerFlying;
        if (!wrapperPlayClientPlayerFlying2.hasRotationChanged()) {
            return;
        }
        WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying3 = wrapperPlayClientPlayerFlying;
        float yaw = wrapperPlayClientPlayerFlying3.getLocation().getYaw();
        WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying4 = wrapperPlayClientPlayerFlying;
        float pitch = wrapperPlayClientPlayerFlying4.getLocation().getPitch();
        if (!tickPacketState.isLastPacketWasOnePointSeventeenDuplicate() || (f4 = rotationState.getYaw()) != (f3 = yaw) || (f2 = rotationState.getPitch()) != (f = pitch)) {
            float yaw2 = rotationState.getYaw();
            rotationState.setLastYaw(yaw2);
            float pitch2 = rotationState.getPitch();
            rotationState.setLastPitch(pitch2);
        }
        float f5 = yaw;
        rotationState.setYaw(f5);
        float f6 = pitch;
        rotationState.setPitch(f6);
    }
}

