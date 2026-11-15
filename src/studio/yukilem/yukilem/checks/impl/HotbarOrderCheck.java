package studio.yukilem.yukilem.checks.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.Check;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.CheckMetadata;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.checks.impl.HotbarOrderLogic;
import studio.yukilem.yukilem.packet.PacketContext;
import studio.yukilem.yukilem.packet.PacketRouter;
import studio.yukilem.yukilem.packet.TickPacketUtil;
import studio.yukilem.yukilem.packet.TickReliability;
import studio.yukilem.yukilem.packet.handler.PacketHandler;
import studio.yukilem.yukilem.packet.handler.PacketOrderHandler;
import studio.yukilem.yukilem.packet.state.MovementUncertaintyState;
import studio.yukilem.yukilem.packet.state.PacketOrderState;

@CheckMetadata(id=CheckId.HOTBAR_ORDER, configRoot="checks.hotbar-order-check")
public final class HotbarOrderCheck
extends Check {
    private static final List<PacketTypeCommon> TICK_PACKETS;
    private static final List<PacketTypeCommon> ACTION_PACKETS;
    private final Map<UUID, State> stateByPlayer;

    public HotbarOrderCheck(@NotNull PluginContext pluginContext) {
        super(pluginContext);
        ConcurrentHashMap<UUID, State> concurrentHashMap = new ConcurrentHashMap<UUID, State>();
        this.stateByPlayer = concurrentHashMap;
    }

    @Override
    public void register(@NotNull PacketRouter packetRouter) {
        PacketOrderHandler packetOrderHandler;
        Function<PacketContext, PacketOrderState> function = this::resolveOrderState;
        PacketOrderHandler packetOrderHandler2 = packetOrderHandler = new PacketOrderHandler(function);
        List<PacketTypeCommon> list = TICK_PACKETS;
        PacketOrderHandler packetOrderHandler3 = packetOrderHandler2;
        packetRouter.subscribeAll(list, packetOrderHandler3);
        List<PacketTypeCommon> list2 = ACTION_PACKETS;
        PacketOrderHandler packetOrderHandler4 = packetOrderHandler2;
        packetRouter.subscribeAll(list2, packetOrderHandler4);
        PacketType.Play.Client client = PacketType.Play.Client.HELD_ITEM_CHANGE;
        PacketHandler packetHandler = this::onHeldItemChange;
        packetRouter.subscribe(client, packetHandler);
        PacketType.Play.Client client2 = PacketType.Play.Client.TELEPORT_CONFIRM;
        PacketHandler packetHandler2 = this::onTeleportConfirm;
        packetRouter.subscribe(client2, packetHandler2);
    }

    private PacketOrderState resolveOrderState(@NotNull PacketContext packetContext) {
        return PlayerCheckData.get(packetContext.playerId()).packetOrderState();
    }

    private void onTeleportConfirm(@NotNull PacketContext packetContext) {
        State state;
        State state2;
        this.resolveOrderState(packetContext).clearAll();
        Map<UUID, State> map = this.stateByPlayer;
        UUID uUID = packetContext.playerId();
        State state3 = state2 = map.get(uUID);
        if (state3 == null) return;
        State state4 = state = state2;
        synchronized (state) {
            try {
                long l;
                State state5 = state2;
                state5.conflictNs = l = 0L;
                State state6 = state4;
                // ** MonitorExit[arg15_10] (shouldn't be in output)
                return;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                State state7 = state4;
                // ** MonitorExit[arg17_13] (shouldn't be in output)
                throw e;
            }
        }
    }

    private void onHeldItemChange(@NotNull PacketContext packetContext) {
        State state;
        State state2;
        PacketOrderState packetOrderState;
        boolean bl;
        if (this.isInactivePacket(packetContext)) {
            return;
        }
        Player player = packetContext.player();
        PacketOrderState packetOrderState2 = this.resolveOrderState(packetContext);
        Player player2 = player;
        boolean gliding = player2.isGliding();
        if (!HotbarOrderLogic.isConflict(gliding, bl = (packetOrderState = packetOrderState2).isRightClicking())) {
            return;
        }
        long l = System.nanoTime();
        Map<UUID, State> map = this.stateByPlayer;
        UUID uUID2 = packetContext.playerId();
        Function<UUID, State> function = uUID -> new State();
        State state3 = state2 = (state = map.computeIfAbsent(uUID2, function));
        synchronized (state2) {
            boolean bl2;
            Player player3;
            boolean bl3;
            try {
                long l2;
                State state4 = state;
                state4.conflictNs = l2 = l;
                State state5 = state;
                long l3 = state5.conflictNs;
                State state6 = state;
                long l4 = state6.boostNs;
                bl3 = HotbarOrderLogic.pairsWithBoost(l3, l4);
                State state7 = state3;
                // ** MonitorExit[arg35_22] (shouldn't be in output)
                player3 = player;
                bl2 = bl3;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                State state8 = state3;
                // ** MonitorExit[arg37_33] (shouldn't be in output)
                throw e;
            }
            String string = "HELD_ITEM_CHANGE during right-click conflict, paired=" + bl2;
            this.debug(player3, string);
            boolean bl4 = bl3;
            if (bl4) {
                Player player4 = player;
                User user = packetContext.user();
                UUID uUID3 = packetContext.playerId();
                State state9 = state;
                this.deliverFlag(player4, user, uUID3, state9);
            }
            return;
        }
    }

    public void onElytraBoost(@NotNull Player player) {
        State state;
        if (!this.isActiveFor(player)) {
            return;
        }
        UUID uniqueId = player.getUniqueId();
        Map<UUID, State> map = this.stateByPlayer;
        UUID uUID2 = uniqueId;
        Function<UUID, State> function = uUID -> new State();
        State state2 = map.computeIfAbsent(uUID2, function);
        long l = System.nanoTime();
        State state3 = state = state2;
        synchronized (state) {
            boolean bl;
            try {
                long l2;
                State state4 = state2;
                state4.boostNs = l2 = l;
                State state5 = state2;
                long l3 = state5.conflictNs;
                State state6 = state2;
                long l4 = state6.boostNs;
                bl = HotbarOrderLogic.pairsWithBoost(l3, l4);
                State state7 = state3;
                // ** MonitorExit[arg30_17] (shouldn't be in output)
            }
            catch (Throwable e) {
                Throwable e2 = e;
                State state8 = state3;
                // ** MonitorExit[arg37_20] (shouldn't be in output)
                throw e;
            }
            boolean bl2 = bl;
            if (!bl2) {
                return;
            }
            User user = HotbarOrderCheck.resolveUser(player);
            User user2 = user;
            if (user2 == null) {
                return;
            }
            User user3 = user;
            UUID uUID3 = uniqueId;
            State state9 = state2;
            this.deliverFlag(player, user3, uUID3, state9);
            return;
        }
    }

    private void deliverFlag(@NotNull Player player, @NotNull User user, @NotNull UUID uUID, @NotNull State state) {
        if (TickPacketUtil.canSkipTicks(user, uUID)) {
            synchronized (state) {
                state.pendingFlag = true;
            }
            this.debug(player, "flag deferred until reliable tick");
        } else {
            synchronized (state) {
                state.reset();
            }
            this.debug(player, "FLAG (instant)");
            this.flag(player);
        }
    }

    public void flushDeferredViolationFlags(@NotNull Player player, @NotNull User user, @NotNull UUID uUID) {
        State state;
        if (!this.isActiveFor(player)) {
            return;
        }
        State state2 = this.stateByPlayer.get(uUID);
        State state3 = state2;
        if (state3 == null) {
            return;
        }
        State state4 = state = state2;
        synchronized (state) {
            block7: {
                try {
                    State state5 = state2;
                    if (!state5.pendingFlag) {
                        State state6 = state4;
                        // ** MonitorExit[arg22_9] (shouldn't be in output)
                        return;
                    }
                    State state7 = state2;
                    state7.pendingFlag = false;
                    MovementUncertaintyState movementUncertaintyState = PlayerCheckData.get(uUID).movementUncertaintyState();
                    boolean bl = TickReliability.isTickingReliablyFor(player, user, uUID, movementUncertaintyState);
                    boolean bl2 = bl;
                    if (bl2) {
                        State state8 = state2;
                        state8.reset();
                    }
                    State state9 = state4;
                    // ** MonitorExit[arg31_15] (shouldn't be in output)
                    boolean bl3 = bl;
                    if (!bl3) break block7;
                    this.debug(player, "FLAG (deferred)");
                }
                catch (Throwable e) {
                    Throwable e2 = e;
                    State state10 = state4;
                    // ** MonitorExit[arg24_19] (shouldn't be in output)
                    throw e;
                }
                this.flag(player);
            }
            return;
        }
    }

    @Nullable
    private static User resolveUser(@NotNull Player player) {
        try {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            return user;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return null;
        }
    }

    private void debug(@NotNull Player player, @NotNull String string) {
        if (this.ctx.config().isDebugEnabled()) {
            Logger logger = this.ctx.plugin().getLogger();
            String string2 = "[HotbarOrder/" + player.getName() + "] " + string;
            logger.info(string2);
        }
    }

    @Override
    public void seedPlayer(@NotNull UUID uUID2) {
        Map<UUID, State> map = this.stateByPlayer;
        Function<UUID, State> function = uUID -> new State();
        State state = map.computeIfAbsent(uUID2, function);
    }

    @Override
    public void clearPlayer(@NotNull UUID uUID) {
        State state = this.stateByPlayer.remove(uUID);
    }

    @Override
    public void shutdown() {
        this.stateByPlayer.clear();
    }

    static {
        PacketType.Play.Client client = PacketType.Play.Client.CLIENT_TICK_END;
        PacketType.Play.Client client2 = PacketType.Play.Client.PLAYER_FLYING;
        PacketType.Play.Client client3 = PacketType.Play.Client.PLAYER_POSITION;
        PacketType.Play.Client client4 = PacketType.Play.Client.PLAYER_ROTATION;
        PacketType.Play.Client client5 = PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
        TICK_PACKETS = List.of(client, client2, client3, client4, client5);
        PacketType.Play.Client client6 = PacketType.Play.Client.USE_ITEM;
        PacketType.Play.Client client7 = PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT;
        PacketType.Play.Client client8 = PacketType.Play.Client.INTERACT_ENTITY;
        ACTION_PACKETS = List.of(client6, client7, client8);
    }

    private static final class State {
        long conflictNs;
        long boostNs;
        boolean pendingFlag;

        private State() {
        }

        void reset() {
            long l;
            long l2;
            this.conflictNs = l2 = 0L;
            this.boostNs = l = 0L;
            this.pendingFlag = false;
        }
    }
}

