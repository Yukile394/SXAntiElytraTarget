package studio.glace.glantielytratarget.packet;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.packet.state.TickPacketState;
import studio.glace.glantielytratarget.platform.ClientVersionResolver;

public final class TickPacketUtil {
    private TickPacketUtil() {
    }

    @NotNull
    public static ClientVersion resolveClientVersion(@NotNull User user, @NotNull UUID uUID) {
        return ClientVersionResolver.resolve(user, uUID);
    }

    @NotNull
    public static ClientVersion resolveClientVersion(@NotNull User user) {
        UUID uUID = user.getUUID();
        UUID uUID2 = uUID;
        if (uUID2 != null) {
            UUID uUID3 = uUID;
            return TickPacketUtil.resolveClientVersion(user, uUID3);
        }
        return ClientVersionResolver.resolve(user, null);
    }

    public static boolean supportsEndTick(@NotNull User user, @NotNull UUID uUID) {
        ServerVersion serverVersion;
        ServerVersion serverVersion2;
        ClientVersion clientVersion;
        ClientVersion clientVersion2 = TickPacketUtil.resolveClientVersion(user, uUID);
        boolean bl = clientVersion2.isNewerThanOrEquals(clientVersion = ClientVersion.V_1_21_2) && (serverVersion2 = PacketEvents.getAPI().getServerManager().getVersion()).isNewerThanOrEquals(serverVersion = ServerVersion.V_1_21_2);
        return bl;
    }

    public static boolean canSkipTicks(@NotNull User user, @NotNull UUID uUID) {
        ClientVersion clientVersion;
        ClientVersion clientVersion2 = TickPacketUtil.resolveClientVersion(user, uUID);
        boolean bl = clientVersion2.isNewerThanOrEquals(clientVersion = ClientVersion.V_1_9) && !TickPacketUtil.supportsEndTick(user, uUID);
        return bl;
    }

    public static boolean isTickPacket(@NotNull PacketTypeCommon packetTypeCommon, @NotNull User user, @NotNull UUID uUID, @NotNull TickPacketState tickPacketState) {
        if (!TickPacketUtil.isTickPacketIncludingNonMovement(packetTypeCommon, user, uUID, tickPacketState)) return false;
        if (!WrapperPlayClientPlayerFlying.isFlying(packetTypeCommon)) return true;
        if (tickPacketState.isLastPacketWasTeleport()) return false;
        if (tickPacketState.isLastPacketWasOnePointSeventeenDuplicate()) return false;
        return true;
    }

    public static boolean isTickPacketIncludingNonMovement(@NotNull PacketTypeCommon packetTypeCommon, @NotNull User user, @NotNull UUID uUID, @NotNull TickPacketState tickPacketState) {
        PacketType.Play.Client client;
        ClientVersion clientVersion;
        ClientVersion clientVersion2 = TickPacketUtil.resolveClientVersion(user, uUID);
        if (clientVersion2.isNewerThanOrEquals(clientVersion = ClientVersion.V_1_21_2) && TickPacketUtil.supportsEndTick(user, uUID) && !tickPacketState.isDidSendMovementBeforeTickEnd() && packetTypeCommon == (client = PacketType.Play.Client.CLIENT_TICK_END)) {
            return true;
        }
        return WrapperPlayClientPlayerFlying.isFlying(packetTypeCommon);
    }

    public static void afterPacket(@NotNull PacketTypeCommon packetTypeCommon, @NotNull User user, @NotNull UUID uUID, @NotNull TickPacketState tickPacketState) {
        PacketType.Play.Client client;
        if (WrapperPlayClientPlayerFlying.isFlying(packetTypeCommon) && !tickPacketState.isLastPacketWasTeleport()) {
            tickPacketState.setDidSendMovementBeforeTickEnd(true);
        }
        if (packetTypeCommon == (client = PacketType.Play.Client.CLIENT_TICK_END) && TickPacketUtil.supportsEndTick(user, uUID)) {
            tickPacketState.setDidSendMovementBeforeTickEnd(false);
        }
        tickPacketState.endPacket();
    }
}

