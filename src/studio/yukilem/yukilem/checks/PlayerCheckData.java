package studio.glace.glantielytratarget.checks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.packet.state.MovementUncertaintyState;
import studio.glace.glantielytratarget.packet.state.PacketOrderState;
import studio.glace.glantielytratarget.packet.state.RotationState;
import studio.glace.glantielytratarget.packet.state.TickPacketState;

public final class PlayerCheckData {
    private static final Map<UUID, PlayerCheckData> DATA = new ConcurrentHashMap<UUID, PlayerCheckData>();
    private final PacketOrderState packetOrderState;
    private final TickPacketState tickPacketState;
    private final MovementUncertaintyState movementUncertaintyState;
    private final RotationState rotationState;
    private volatile double glideBaselineY;
    private volatile boolean combatPhaseBActive;
    private volatile long glideStartMs;

    public PlayerCheckData() {
        double d;
        RotationState rotationState;
        MovementUncertaintyState movementUncertaintyState;
        TickPacketState tickPacketState;
        PacketOrderState packetOrderState;
        this.packetOrderState = packetOrderState = new PacketOrderState();
        this.tickPacketState = tickPacketState = new TickPacketState();
        this.movementUncertaintyState = movementUncertaintyState = new MovementUncertaintyState();
        this.rotationState = rotationState = new RotationState();
        this.glideBaselineY = d = Double.NaN;
    }

    @NotNull
    public PacketOrderState packetOrderState() {
        return this.packetOrderState;
    }

    public double glideBaselineY() {
        return this.glideBaselineY;
    }

    public void setGlideBaselineY(double d) {
        this.glideBaselineY = d;
    }

    public long glideStartMs() {
        return this.glideStartMs;
    }

    public void setGlideStartMs(long l) {
        this.glideStartMs = l;
    }

    public boolean combatPhaseBActive() {
        return this.combatPhaseBActive;
    }

    public void setCombatPhaseBActive(boolean bl) {
        this.combatPhaseBActive = bl;
    }

    @NotNull
    public TickPacketState tickPacketState() {
        return this.tickPacketState;
    }

    @NotNull
    public MovementUncertaintyState movementUncertaintyState() {
        return this.movementUncertaintyState;
    }

    @NotNull
    public RotationState rotationState() {
        return this.rotationState;
    }

    @NotNull
    public static PlayerCheckData get(@NotNull UUID uUID2) {
        Map<UUID, PlayerCheckData> map = DATA;
        Function<UUID, PlayerCheckData> function = uUID -> new PlayerCheckData();
        return map.computeIfAbsent(uUID2, function);
    }

    @Nullable
    public static PlayerCheckData getIfPresent(@NotNull UUID uUID) {
        return DATA.get(uUID);
    }

    public static void clear(@NotNull UUID uUID) {
        PlayerCheckData playerCheckData = DATA.remove(uUID);
        PlayerCheckData playerCheckData2 = playerCheckData;
        if (playerCheckData2 != null) {
            PlayerCheckData playerCheckData3 = playerCheckData;
            playerCheckData3.packetOrderState.clearAll();
        }
    }

    public static void invalidatePacketContinuity(@NotNull UUID uUID) {
        PlayerCheckData playerCheckData = DATA.get(uUID);
        PlayerCheckData playerCheckData2 = playerCheckData;
        if (playerCheckData2 == null) {
            return;
        }
        PlayerCheckData playerCheckData3 = playerCheckData;
        playerCheckData3.packetOrderState.clearAll();
        PlayerCheckData playerCheckData4 = playerCheckData;
        playerCheckData4.tickPacketState.invalidatePacketContinuity();
        PlayerCheckData playerCheckData5 = playerCheckData;
        playerCheckData5.movementUncertaintyState.invalidatePacketContinuity();
    }

    public static void clearAll() {
        DATA.clear();
    }
}

