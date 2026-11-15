package studio.glace.glantielytratarget.packet;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.packet.TickPacketUtil;
import studio.glace.glantielytratarget.packet.state.MovementUncertaintyState;
import studio.glace.glantielytratarget.packet.state.TickPacketState;

public final class MovementUncertaintyTracker {
    private MovementUncertaintyTracker() {
    }

    public static double movementThreshold(@NotNull User user, @NotNull UUID uUID) {
        ClientVersion clientVersion;
        ClientVersion clientVersion2 = TickPacketUtil.resolveClientVersion(user, uUID);
        if (clientVersion2.isOlderThan(clientVersion = ClientVersion.V_1_18_2)) {
            return 0.03;
        }
        return 2.0E-4;
    }

    public static void onRotationFlying(@NotNull MovementUncertaintyState movementUncertaintyState) {
        movementUncertaintyState.getLastPointThree().reset();
    }

    public static void onBeforePredictionFinish(@NotNull PacketReceiveEvent packetReceiveEvent, @NotNull User user, @NotNull UUID uUID, @NotNull MovementUncertaintyState movementUncertaintyState, @NotNull TickPacketState tickPacketState) {
        boolean bl;
        boolean bl2;
        WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying;
        WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying2;
        boolean bl3 = !tickPacketState.isLastPacketWasTeleport() && !tickPacketState.isLastPacketWasOnePointSeventeenDuplicate();
        boolean bl4 = bl3;
        boolean bl5 = false;
        if (WrapperPlayClientPlayerFlying.isFlying(packetReceiveEvent.getPacketType()) && (wrapperPlayClientPlayerFlying2 = (wrapperPlayClientPlayerFlying = new WrapperPlayClientPlayerFlying(packetReceiveEvent))).hasPositionChanged()) {
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying3 = wrapperPlayClientPlayerFlying;
            double x = wrapperPlayClientPlayerFlying3.getLocation().getX();
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying4 = wrapperPlayClientPlayerFlying;
            double y = wrapperPlayClientPlayerFlying4.getLocation().getY();
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying5 = wrapperPlayClientPlayerFlying;
            double z = wrapperPlayClientPlayerFlying5.getLocation().getZ();
            if (!Double.isNaN(movementUncertaintyState.getLastX())) {
                double d;
                double d2;
                double d3;
                double d4;
                double d5;
                double d6;
                double d7;
                double d8;
                double d9;
                double d10;
                double d11;
                double d12;
                double d13;
                double d14;
                double d15;
                double d16;
                double d17;
                double d18 = x;
                double lastX = movementUncertaintyState.getLastX();
                double d19 = d18 - lastX;
                double d20 = d19;
                double d21 = d20 * (d17 = d19);
                double d22 = d21 + (d16 = (d15 = (d14 = (d13 = y) - (d12 = movementUncertaintyState.getLastY()))) * (d11 = d14));
                double d23 = d22 + (d10 = (d9 = (d8 = (d7 = z) - (d6 = movementUncertaintyState.getLastZ()))) * (d5 = d8));
                boolean bl6 = d23 < (d4 = (d3 = (d2 = MovementUncertaintyTracker.movementThreshold(user, uUID))) * (d = d2));
                bl5 = bl6;
                boolean bl7 = bl4;
                if (bl7) {
                    double d24 = d19;
                    double d25 = d19;
                    double d26 = d24 * d25;
                    double d27 = d8;
                    double d28 = d8;
                    double d29 = d27 * d28;
                    double d30 = Math.sqrt(d26 + d29);
                    movementUncertaintyState.recordHorizontalDelta(d30);
                }
            }
            double d = x;
            movementUncertaintyState.setLastX(d);
            double d31 = y;
            movementUncertaintyState.setLastY(d31);
            double d32 = z;
            movementUncertaintyState.setLastZ(d32);
        }
        if ((bl2 = bl5) || !(bl = bl4)) {
            movementUncertaintyState.getLastPointThree().reset();
        }
    }

    public static void onVehicleSwitch(@NotNull Player player, @NotNull MovementUncertaintyState movementUncertaintyState) {
        boolean bl;
        boolean insideVehicle = player.isInsideVehicle();
        boolean bl2 = insideVehicle;
        if (bl2 != (bl = movementUncertaintyState.isWasInVehicle())) {
            movementUncertaintyState.getLastVehicleSwitch().reset();
        }
        boolean bl3 = insideVehicle;
        movementUncertaintyState.setWasInVehicle(bl3);
    }

    public static void tickInstances(@NotNull MovementUncertaintyState movementUncertaintyState) {
        movementUncertaintyState.getLastPointThree().tick();
        movementUncertaintyState.getLastVehicleSwitch().tick();
    }
}

