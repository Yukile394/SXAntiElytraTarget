package studio.yukilem.yukilem.packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.packet.MovementUncertaintyTracker;
import studio.yukilem.yukilem.packet.TickPacketUtil;
import studio.yukilem.yukilem.packet.state.MovementUncertaintyState;
import studio.yukilem.yukilem.packet.state.TickPacketState;

public final class TickReliability {
    static final int RELIABLE_TICK_WINDOW = 3;

    private TickReliability() {
    }

    public static boolean canSkipTicks(@NotNull User user, @NotNull UUID uUID) {
        return TickPacketUtil.canSkipTicks(user, uUID);
    }

    public static boolean isTickingReliablyFor(@NotNull Player player, @NotNull User user, @NotNull UUID uUID, @NotNull MovementUncertaintyState movementUncertaintyState, int n) {
        if (!TickReliability.canSkipTicks(user, uUID)) {
            return true;
        }
        boolean bl = (player.isInsideVehicle() || !movementUncertaintyState.getLastPointThree().hasOccurredSince(n)) && !movementUncertaintyState.getLastVehicleSwitch().hasOccurredSince(1);
        return bl;
    }

    public static boolean isTickingReliablyFor(@NotNull Player player, @NotNull User user, @NotNull UUID uUID, @NotNull MovementUncertaintyState movementUncertaintyState) {
        return TickReliability.isTickingReliablyFor(player, user, uUID, movementUncertaintyState, 3);
    }

    public static void observePacket(@NotNull PacketReceiveEvent packetReceiveEvent, @NotNull Player player, @NotNull User user, @NotNull UUID uUID, @NotNull MovementUncertaintyState movementUncertaintyState, @NotNull TickPacketState tickPacketState) {
        MovementUncertaintyTracker.onVehicleSwitch(player, movementUncertaintyState);
        PacketTypeCommon packetType = packetReceiveEvent.getPacketType();
        PacketType.Play.Client client = PacketType.Play.Client.TELEPORT_CONFIRM;
        if (packetType == client) {
            tickPacketState.setPendingTeleportConfirm(true);
        }
        if (WrapperPlayClientPlayerFlying.isFlying(packetReceiveEvent.getPacketType())) {
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying;
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying2;
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying3;
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying4 = new WrapperPlayClientPlayerFlying(packetReceiveEvent);
            if (tickPacketState.isPendingTeleportConfirm() && (wrapperPlayClientPlayerFlying3 = wrapperPlayClientPlayerFlying4).hasPositionChanged() && (wrapperPlayClientPlayerFlying2 = wrapperPlayClientPlayerFlying4).hasRotationChanged()) {
                tickPacketState.setLastPacketWasTeleport(true);
                tickPacketState.setPendingTeleportConfirm(false);
            }
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying5 = wrapperPlayClientPlayerFlying4;
            boolean onGround = wrapperPlayClientPlayerFlying5.isOnGround();
            movementUncertaintyState.setPacketOnGround(onGround);
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying6 = wrapperPlayClientPlayerFlying4;
            if (wrapperPlayClientPlayerFlying6.hasRotationChanged() && !(wrapperPlayClientPlayerFlying = wrapperPlayClientPlayerFlying4).hasPositionChanged()) {
                MovementUncertaintyTracker.onRotationFlying(movementUncertaintyState);
            }
            MovementUncertaintyTracker.onBeforePredictionFinish(packetReceiveEvent, user, uUID, movementUncertaintyState, tickPacketState);
        }
        if (TickPacketUtil.isTickPacket(packetReceiveEvent.getPacketType(), user, uUID, tickPacketState)) {
            MovementUncertaintyTracker.tickInstances(movementUncertaintyState);
            movementUncertaintyState.incrementClientFlyingSeq();
        }
    }
}

