package studio.yukilem.yukilem.packet.handler;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.packet.PacketContext;
import studio.yukilem.yukilem.packet.TickPacketUtil;
import studio.yukilem.yukilem.packet.handler.PacketHandler;
import studio.yukilem.yukilem.packet.state.PacketOrderState;
import studio.yukilem.yukilem.packet.state.TickPacketState;

public final class PacketOrderHandler
implements PacketHandler {
    private final Function<PacketContext, PacketOrderState> stateResolver;
    @Nullable
    private final BiConsumer<PacketContext, PacketOrderState> beforeTickClear;

    public PacketOrderHandler(@NotNull Function<PacketContext, PacketOrderState> function) {
        this(function, null);
    }

    public PacketOrderHandler(@NotNull Function<PacketContext, PacketOrderState> function, @Nullable BiConsumer<PacketContext, PacketOrderState> biConsumer) {
        this.stateResolver = function;
        this.beforeTickClear = biConsumer;
    }

    @Override
    public void handle(@NotNull PacketContext packetContext) {
        PacketOrderState packetOrderState = this.stateResolver.apply(packetContext);
        PacketOrderState packetOrderState2 = packetOrderState;
        if (packetOrderState2 == null) {
            return;
        }
        PacketReceiveEvent packetReceiveEvent = packetContext.event();
        PacketOrderState packetOrderState3 = packetOrderState;
        Player player = packetContext.player();
        BiConsumer<PacketContext, PacketOrderState> biConsumer = this.beforeTickClear;
        PacketOrderHandler.update(packetReceiveEvent, packetOrderState3, player, packetContext, biConsumer);
    }

    public static void update(@NotNull PacketReceiveEvent packetReceiveEvent, @NotNull PacketOrderState packetOrderState, @NotNull Player player) {
        PacketOrderHandler.update(packetReceiveEvent, packetOrderState, player, null, null);
    }

    public static void update(@NotNull PacketReceiveEvent packetReceiveEvent, @NotNull PacketOrderState packetOrderState, @NotNull Player player, @Nullable PacketContext packetContext, @Nullable BiConsumer<PacketContext, PacketOrderState> biConsumer) {
        TickPacketState tickPacketState;
        User user;
        PacketTypeCommon packetTypeCommon;
        User user2;
        PacketType.Play.Client client;
        PacketTypeCommon packetTypeCommon2;
        PacketType.Play.Client client2;
        PacketTypeCommon packetTypeCommon3;
        PacketType.Play.Client client3;
        PacketTypeCommon packetTypeCommon4;
        PacketType.Play.Client client4;
        PacketTypeCommon packetTypeCommon5;
        PacketType.Play.Client client5;
        PacketTypeCommon packetTypeCommon6;
        PacketType.Play.Client client6;
        PacketTypeCommon packetTypeCommon7;
        PacketType.Play.Client client7;
        PacketTypeCommon packetTypeCommon8;
        PacketType.Play.Client client8;
        PacketTypeCommon packetTypeCommon9;
        PacketType.Play.Client client9;
        PacketTypeCommon packetTypeCommon10;
        PacketType.Play.Client client10;
        PacketTypeCommon packetTypeCommon11;
        WrapperPlayClientClientStatus.Action action;
        WrapperPlayClientClientStatus.Action action2;
        PacketTypeCommon packetType = packetReceiveEvent.getPacketType();
        User user3 = packetReceiveEvent.getUser();
        TickPacketState tickPacketState2 = PlayerCheckData.get(player.getUniqueId()).tickPacketState();
        if (!PacketOrderHandler.isCameraSelf(player)) {
            packetOrderState.clearAll();
            return;
        }
        PacketTypeCommon packetTypeCommon12 = packetType;
        PacketType.Play.Client client11 = PacketType.Play.Client.CLIENT_STATUS;
        if (packetTypeCommon12 == client11 && (action2 = new WrapperPlayClientClientStatus(packetReceiveEvent).getAction()) == (action = WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT)) {
            packetOrderState.setOpeningInventory(true);
        }
        if ((packetTypeCommon11 = packetType) == (client10 = PacketType.Play.Client.INTERACT_ENTITY)) {
            WrapperPlayClientInteractEntity.InteractAction interactAction;
            WrapperPlayClientInteractEntity.InteractAction action3 = new WrapperPlayClientInteractEntity(packetReceiveEvent).getAction();
            if (action3 == (interactAction = WrapperPlayClientInteractEntity.InteractAction.ATTACK)) {
                packetOrderState.setAttacking(true);
            } else {
                packetOrderState.setInteracting(true);
            }
        }
        if ((packetTypeCommon10 = packetType) == (client9 = PacketType.Play.Client.ATTACK) || (packetTypeCommon9 = packetType) == (client8 = PacketType.Play.Client.SPECTATE_ENTITY)) {
            packetOrderState.setAttacking(true);
        }
        if ((packetTypeCommon8 = packetType) == (client7 = PacketType.Play.Client.PLAYER_DIGGING)) {
            DiggingAction action4 = new WrapperPlayClientPlayerDigging(packetReceiveEvent).getAction();
            switch (action4) {
                case RELEASE_USE_ITEM: {
                    packetOrderState.setReleasing(true);
                    break;
                }
                case DROP_ITEM:
                case DROP_ITEM_STACK: {
                    packetOrderState.setDropping(true);
                    break;
                }
                case SWAP_ITEM_WITH_OFFHAND: {
                    packetOrderState.setSwapping(true);
                    break;
                }
                case FINISHED_DIGGING:
                case CANCELLED_DIGGING:
                case START_DIGGING: {
                    packetOrderState.setDigging(true);
                    break;
                }
                default: {
                    if (action4 != DiggingAction.STAB) break;
                    packetOrderState.setStabbing(true);
                }
            }
        }
        if ((packetTypeCommon7 = packetType) == (client6 = PacketType.Play.Client.ENTITY_ACTION)) {
            WrapperPlayClientEntityAction.Action action5 = new WrapperPlayClientEntityAction(packetReceiveEvent).getAction();
            switch (action5) {
                case START_SPRINTING:
                case STOP_SPRINTING: {
                    if (player.isInsideVehicle()) break;
                    packetOrderState.setSprinting(true);
                    break;
                }
                case START_FLYING_WITH_ELYTRA: {
                    packetOrderState.setStartingToGlide(true);
                    break;
                }
                case LEAVE_BED: {
                    packetOrderState.setLeavingBed(true);
                    break;
                }
                case START_JUMPING_WITH_HORSE:
                case STOP_JUMPING_WITH_HORSE: {
                    packetOrderState.setJumpingWithMount(true);
                    break;
                }
                case OPEN_HORSE_INVENTORY: {
                    packetOrderState.setOpeningInventory(true);
                    break;
                }
                case STOP_SNEAKING:
                case START_SNEAKING: {
                    packetOrderState.setSneaking(true);
                }
            }
        }
        if ((packetTypeCommon6 = packetType) == (client5 = PacketType.Play.Client.USE_ITEM)) {
            packetOrderState.setUsing(true);
        }
        if ((packetTypeCommon5 = packetType) == (client4 = PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT)) {
            BlockFace blockFace;
            BlockFace face = new WrapperPlayClientPlayerBlockPlacement(packetReceiveEvent).getFace();
            if (face == (blockFace = BlockFace.OTHER)) {
                packetOrderState.setUsing(true);
            } else {
                packetOrderState.setPlacing(true);
            }
        }
        if ((packetTypeCommon4 = packetType) == (client3 = PacketType.Play.Client.PICK_ITEM)) {
            packetOrderState.setPicking(true);
        }
        if ((packetTypeCommon3 = packetType) == (client2 = PacketType.Play.Client.CLICK_WINDOW)) {
            packetOrderState.setClickingInInventory(true);
            WrapperPlayClientClickWindow wrapperPlayClientClickWindow = new WrapperPlayClientClickWindow(packetReceiveEvent);
            switch (wrapperPlayClientClickWindow.getWindowClickType()) {
                case QUICK_MOVE: {
                    packetOrderState.setQuickMoveClicking(true);
                    break;
                }
                case PICKUP:
                case PICKUP_ALL: {
                    packetOrderState.setPickUpClicking(true);
                }
            }
        }
        if ((packetTypeCommon2 = packetType) == (client = PacketType.Play.Client.CLOSE_WINDOW)) {
            packetOrderState.setClosingInventory(true);
        }
        if ((user2 = user3) != null && PacketOrderHandler.shouldClearFlags(packetReceiveEvent, packetTypeCommon = packetType, player, user = user3, tickPacketState = tickPacketState2)) {
            if (biConsumer != null && packetContext != null) {
                biConsumer.accept(packetContext, packetOrderState);
            }
            packetOrderState.clearAll();
        }
    }

    public static void finalizePacket(@NotNull PacketReceiveEvent packetReceiveEvent, @NotNull Player player) {
        User user = packetReceiveEvent.getUser();
        User user2 = user;
        if (user2 == null) {
            return;
        }
        TickPacketState tickPacketState = PlayerCheckData.get(player.getUniqueId()).tickPacketState();
        PacketTypeCommon packetType = packetReceiveEvent.getPacketType();
        User user3 = user;
        UUID uniqueId = player.getUniqueId();
        TickPacketState tickPacketState2 = tickPacketState;
        TickPacketUtil.afterPacket(packetType, user3, uniqueId, tickPacketState2);
    }

    public static boolean isTickPacket(@NotNull PacketTypeCommon packetTypeCommon, @NotNull PacketReceiveEvent packetReceiveEvent, @NotNull Player player) {
        User user = packetReceiveEvent.getUser();
        User user2 = user;
        if (user2 == null) {
            return false;
        }
        TickPacketState tickPacketState = PlayerCheckData.get(player.getUniqueId()).tickPacketState();
        User user3 = user;
        UUID uniqueId = player.getUniqueId();
        TickPacketState tickPacketState2 = tickPacketState;
        return TickPacketUtil.isTickPacket(packetTypeCommon, user3, uniqueId, tickPacketState2);
    }

    private static boolean shouldClearFlags(@NotNull PacketReceiveEvent packetReceiveEvent, @NotNull PacketTypeCommon packetTypeCommon, @NotNull Player player, @NotNull User user, @NotNull TickPacketState tickPacketState) {
        ClientVersion clientVersion;
        UUID uniqueId = player.getUniqueId();
        if (TickPacketUtil.isTickPacket(packetTypeCommon, user, uniqueId, tickPacketState)) {
            return true;
        }
        UUID uniqueId2 = player.getUniqueId();
        ClientVersion clientVersion2 = TickPacketUtil.resolveClientVersion(user, uniqueId2);
        return clientVersion2.isOlderThan(clientVersion = ClientVersion.V_1_21_2) && !PacketOrderHandler.isChunkLoaded(player);
    }

    private static boolean isChunkLoaded(@NotNull Player player) {
        Location location = player.getLocation();
        Location location2 = location;
        World world = location2.getWorld();
        World world2 = world;
        if (world2 == null) {
            return false;
        }
        World world3 = world;
        Location location3 = location;
        int n = location3.getBlockX() >> 4;
        Location location4 = location;
        int n2 = location4.getBlockZ() >> 4;
        return world3.isChunkLoaded(n, n2);
    }

    private static boolean isCameraSelf(@NotNull Player player) {
        GameMode gameMode;
        GameMode gameMode2 = player.getGameMode();
        if (gameMode2 != (gameMode = GameMode.SPECTATOR)) {
            return true;
        }
        boolean bl = player.getSpectatorTarget() == null;
        return bl;
    }
}

