package studio.yukilem.yukilem.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.CheckRegistry;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.packet.PacketContext;
import studio.yukilem.yukilem.packet.PacketRouter;
import studio.yukilem.yukilem.packet.TickReliability;
import studio.yukilem.yukilem.packet.handler.PacketHandler;
import studio.yukilem.yukilem.packet.handler.PacketOrderHandler;
import studio.yukilem.yukilem.packet.state.MovementUncertaintyState;
import studio.yukilem.yukilem.packet.state.TickPacketState;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.platform.PlayerSessionCache;
import studio.yukilem.yukilem.platform.ServerHealthMonitor;
import studio.yukilem.yukilem.util.combat.ElytraUseProbe;

public final class PacketDispatcher
extends PacketListenerAbstract {
    private final PluginContext ctx;
    private final PacketRouter router;

    public PacketDispatcher(@NotNull PluginContext pluginContext, @NotNull PacketRouter packetRouter) {
        super(PacketListenerPriority.NORMAL);
        this.ctx = pluginContext;
        this.router = packetRouter;
    }

    @Override
    public void onPacketReceive(@NotNull PacketReceiveEvent packetReceiveEvent) {
        CheckRegistry checkRegistry;
        int n;
        int n2;
        PacketContext packetContext;
        PacketHandler[] packetHandlerArray;
        CheckRegistry checkRegistry2;
        CheckRegistry checkRegistry3;
        PacketTypeCommon packetTypeCommon;
        Player player;
        ServerHealthMonitor serverHealthMonitor;
        PlayerSessionCache.Session session;
        Player player2;
        Player player3;
        UUID uUID;
        User user;
        User user2;
        PacketHandler[] packetHandlerArray2;
        PacketTypeCommon packetTypeCommon2;
        block24: {
            PacketType.Play.Client client;
            PacketTypeCommon packetTypeCommon3;
            boolean bl;
            packetTypeCommon2 = packetReceiveEvent.getPacketType();
            PacketRouter packetRouter = this.router;
            PacketTypeCommon packetTypeCommon4 = packetTypeCommon2;
            packetHandlerArray2 = packetRouter.handlersFor(packetTypeCommon4);
            PacketTypeCommon packetTypeCommon5 = packetTypeCommon2;
            boolean bl2 = WrapperPlayClientPlayerFlying.isFlying(packetTypeCommon5);
            PacketHandler[] packetHandlerArray3 = packetHandlerArray2;
            if (packetHandlerArray3 == null && !(bl = bl2) && (packetTypeCommon3 = packetTypeCommon2) != (client = PacketType.Play.Client.TELEPORT_CONFIRM)) {
                return;
            }
            user2 = packetReceiveEvent.getUser();
            User user3 = user2;
            if (user3 == null) {
                return;
            }
            try {
                User user4 = user2;
                ConnectionState connectionState = user4.getConnectionState();
                ConnectionState connectionState2 = ConnectionState.PLAY;
                if (connectionState == connectionState2) break block24;
                return;
            }
            catch (IllegalStateException e) {
                IllegalStateException e2 = e;
                return;
            }
        }
        User user5 = user2;
        if (user5.getClientVersion() == null) {
            return;
        }
        User user6 = user2;
        UUID uUID2 = user6.getUUID();
        UUID uUID3 = uUID2;
        if (uUID3 == null && (user = user2).getProfile() != null) {
            User user7 = user2;
            uUID2 = user7.getProfile().getUUID();
        }
        if ((uUID = uUID2) == null) {
            return;
        }
        PlayerSessionCache playerSessionCache = this.ctx.sessionCache();
        PlayerSessionCache playerSessionCache2 = playerSessionCache;
        if (playerSessionCache2 == null) {
            return;
        }
        PlayerSessionCache playerSessionCache3 = playerSessionCache;
        UUID uUID4 = uUID2;
        PlayerSessionCache.Session session2 = playerSessionCache3.get(uUID4);
        PlayerSessionCache.Session session3 = session2;
        if (session3 != null) {
            PlayerSessionCache.Session session4 = session2;
            player3 = session4.player();
        } else {
            player3 = null;
        }
        Player player4 = player2 = player3;
        if (player4 == null) {
            Player player5;
            UUID uUID5 = uUID2;
            player2 = Bukkit.getPlayer(uUID5);
            Player player6 = player2;
            if (player6 == null || !(player5 = player2).isOnline()) {
                return;
            }
            PlayerSessionCache playerSessionCache4 = playerSessionCache;
            UUID uUID6 = uUID2;
            Player player7 = player2;
            User user8 = user2;
            session = playerSessionCache4.resolve(uUID6, player7, user8);
        } else {
            Player player8 = player2;
            if (!player8.isOnline()) {
                return;
            }
            PlayerSessionCache playerSessionCache5 = playerSessionCache;
            UUID uUID7 = uUID2;
            Player player9 = player2;
            User user9 = user2;
            session = playerSessionCache5.resolve(uUID7, player9, user9);
        }
        ServerHealthMonitor serverHealthMonitor2 = this.ctx.healthMonitor();
        CheckRegistry checkRegistry4 = this.ctx.checks();
        ServerHealthMonitor serverHealthMonitor3 = serverHealthMonitor2;
        if (serverHealthMonitor3 != null && (serverHealthMonitor = serverHealthMonitor2).shouldSkipDetection(player = player2)) {
            CheckRegistry checkRegistry5 = checkRegistry4;
            if (checkRegistry5 != null) {
                CheckRegistry checkRegistry6 = checkRegistry4;
                UUID uUID8 = uUID2;
                checkRegistry6.invalidatePacketContinuity(uUID8);
            }
            return;
        }
        CheckRegistry checkRegistry7 = checkRegistry4;
        if (checkRegistry7 != null) {
            CheckRegistry checkRegistry8 = checkRegistry4;
            UUID uUID9 = uUID2;
            checkRegistry8.restorePacketContinuity(uUID9);
        }
        UUID uUID10 = uUID2;
        PlayerCheckData playerCheckData = PlayerCheckData.get(uUID10);
        Player player10 = player2;
        User user10 = user2;
        UUID uUID11 = uUID2;
        PlayerCheckData playerCheckData2 = playerCheckData;
        MovementUncertaintyState movementUncertaintyState = playerCheckData2.movementUncertaintyState();
        PlayerCheckData playerCheckData3 = playerCheckData;
        TickPacketState tickPacketState = playerCheckData3.tickPacketState();
        TickReliability.observePacket(packetReceiveEvent, player10, user10, uUID11, movementUncertaintyState, tickPacketState);
        PlayerSessionCache.Session session5 = session;
        if (session5.bypass()) {
            return;
        }
        Player player11 = player2;
        if (player11.isGliding() && PacketDispatcher.isBlockLookRefreshPacket(packetTypeCommon = packetTypeCommon2) && (checkRegistry3 = checkRegistry4) != null && (checkRegistry2 = checkRegistry4).needsBlockLookProbe()) {
            PlatformScheduler platformScheduler = this.ctx.scheduler();
            Player player12 = player2;
            ElytraUseProbe.refreshLookTarget(platformScheduler, player12);
        }
        if ((packetHandlerArray = packetHandlerArray2) == null) {
            Player player13 = player2;
            PacketOrderHandler.finalizePacket(packetReceiveEvent, player13);
            return;
        }
        Player player14 = player2;
        User user11 = user2;
        UUID uUID12 = uUID2;
        long timestamp = packetReceiveEvent.getTimestamp();
        PacketContext packetContext2 = packetContext = new PacketContext(packetReceiveEvent, player14, user11, uUID12, timestamp);
        int n3 = 0;
        PacketHandler[] packetHandlerArray4 = packetHandlerArray2;
        int length = packetHandlerArray4.length;
        while ((n2 = ++n3) < (n = length)) {
            try {
                PacketHandler[] packetHandlerArray5 = packetHandlerArray2;
                int n4 = n3;
                PacketHandler packetHandler = packetHandlerArray5[n4];
                PacketContext packetContext3 = packetContext2;
                packetHandler.handle(packetContext3);
            }
            catch (Throwable e) {
                Throwable e3 = e;
                Logger logger = this.ctx.plugin().getLogger();
                PacketTypeCommon packetTypeCommon6 = packetTypeCommon2;
                String name = packetTypeCommon6.getName();
                Throwable e4 = e3;
                String message = e4.getMessage();
                String string = "Packet handler failure for " + name + ": " + message;
                logger.warning(string);
            }
        }
        PacketTypeCommon packetTypeCommon7 = packetTypeCommon2;
        Player player15 = player2;
        boolean bl = PacketOrderHandler.isTickPacket(packetTypeCommon7, packetReceiveEvent, player15);
        Player player16 = player2;
        PacketOrderHandler.finalizePacket(packetReceiveEvent, player16);
        boolean bl3 = bl;
        if (bl3 && (checkRegistry = checkRegistry4) != null) {
            CheckRegistry checkRegistry9 = checkRegistry4;
            Player player17 = player2;
            User user12 = user2;
            UUID uUID13 = uUID2;
            checkRegistry9.flushDeferredViolationFlags(player17, user12, uUID13);
        }
        return;
    }

    private static boolean isBlockLookRefreshPacket(@NotNull PacketTypeCommon packetTypeCommon) {
        PacketType.Play.Client client;
        PacketType.Play.Client client2 = PacketType.Play.Client.PLAYER_ROTATION;
        boolean bl = packetTypeCommon == client2 || packetTypeCommon == (client = PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION);
        return bl;
    }

    @Generated
    public PacketRouter getRouter() {
        return this.router;
    }
}

