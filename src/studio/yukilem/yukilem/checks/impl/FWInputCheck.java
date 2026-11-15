package studio.glace.glantielytratarget.checks.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.checks.Check;
import studio.glace.glantielytratarget.checks.CheckId;
import studio.glace.glantielytratarget.checks.CheckMetadata;
import studio.glace.glantielytratarget.checks.PlayerCheckData;
import studio.glace.glantielytratarget.checks.impl.LegitSwapLogic;
import studio.glace.glantielytratarget.checks.impl.fwinput.FWInputFlagEvaluator;
import studio.glace.glantielytratarget.checks.impl.fwinput.FWInputSession;
import studio.glace.glantielytratarget.checks.impl.fwinput.FWInputSessionLifecycle;
import studio.glace.glantielytratarget.packet.PacketContext;
import studio.glace.glantielytratarget.packet.PacketRouter;
import studio.glace.glantielytratarget.packet.TickReliability;
import studio.glace.glantielytratarget.packet.handler.PacketHandler;
import studio.glace.glantielytratarget.packet.state.MovementUncertaintyState;

@CheckMetadata(id=CheckId.FW_INPUT, configRoot="checks.fw-input-check", aliases={"TestCheck", "SilentSwapCheck"})
public final class FWInputCheck
extends Check {
    private final Map<UUID, FWInputSession> sessions;
    private final FWInputFlagEvaluator flagEvaluator;

    public FWInputCheck(@NotNull PluginContext pluginContext) {
        super(pluginContext);
        FWInputFlagEvaluator fWInputFlagEvaluator;
        ConcurrentHashMap<UUID, FWInputSession> concurrentHashMap = new ConcurrentHashMap<UUID, FWInputSession>();
        this.sessions = concurrentHashMap;
        BiPredicate<Player, User> biPredicate = this::isTickReliable;
        Consumer<Player> consumer = player -> this.flag((Player)player);
        this.flagEvaluator = fWInputFlagEvaluator = new FWInputFlagEvaluator(biPredicate, consumer);
    }

    @Override
    public void register(@NotNull PacketRouter packetRouter) {
        PacketType.Play.Client client = PacketType.Play.Client.CLICK_WINDOW;
        PacketHandler packetHandler = this::onClickWindow;
        packetRouter.subscribe(client, packetHandler);
        PacketType.Play.Client client2 = PacketType.Play.Client.CLOSE_WINDOW;
        PacketHandler packetHandler2 = this::onCloseWindow;
        packetRouter.subscribe(client2, packetHandler2);
        PacketType.Play.Client client3 = PacketType.Play.Client.PLAYER_DIGGING;
        PacketHandler packetHandler3 = this::onPlayerDigging;
        packetRouter.subscribe(client3, packetHandler3);
    }

    private void onClickWindow(@NotNull PacketContext packetContext) {
        FWInputSession fWInputSession;
        FWInputSession fWInputSession2;
        UUID uUID2;
        Map<UUID, FWInputSession> map;
        Player player;
        WrapperPlayClientClickWindow wrapperPlayClientClickWindow;
        if (this.isInactivePacket(packetContext) || !packetContext.player().isGliding()) {
            return;
        }
        try {
            WrapperPlayClientClickWindow wrapperPlayClientClickWindow2;
            PacketReceiveEvent packetReceiveEvent = packetContext.event();
            WrapperPlayClientClickWindow wrapperPlayClientClickWindow3 = wrapperPlayClientClickWindow = (wrapperPlayClientClickWindow2 = new WrapperPlayClientClickWindow(packetReceiveEvent));
            if (wrapperPlayClientClickWindow3.getWindowId() != 0) {
                return;
            }
            player = packetContext.player();
            map = this.sessions;
            uUID2 = packetContext.playerId();
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return;
        }
        Function<UUID, FWInputSession> function = uUID -> new FWInputSession();
        FWInputSession fWInputSession3 = fWInputSession2 = (fWInputSession = map.computeIfAbsent(uUID2, function));
        synchronized (fWInputSession2) {
            try {
                long l;
                int n;
                int n2;
                Material material;
                long l2;
                int n3;
                int n4;
                int n5;
                FWInputSession fWInputSession4 = fWInputSession;
                if (!fWInputSession4.valid) {
                    FWInputSession fWInputSession5 = fWInputSession3;
                    // ** MonitorExit[arg74_14] (shouldn't be in output)
                    return;
                }
                Player player2 = player;
                int heldItemSlot = player2.getInventory().getHeldItemSlot();
                FWInputSession fWInputSession6 = fWInputSession;
                fWInputSession6.heldSlot = n5 = heldItemSlot;
                long l3 = System.nanoTime();
                FWInputSession fWInputSession7 = fWInputSession;
                long l4 = l3;
                FWInputSessionLifecycle.expireStale(fWInputSession7, l4);
                WrapperPlayClientClickWindow wrapperPlayClientClickWindow4 = wrapperPlayClientClickWindow;
                WrapperPlayClientClickWindow.WindowClickType windowClickType = wrapperPlayClientClickWindow4.getWindowClickType();
                WrapperPlayClientClickWindow.WindowClickType windowClickType2 = WrapperPlayClientClickWindow.WindowClickType.SWAP;
                if (windowClickType != windowClickType2) {
                    FWInputSession fWInputSession8 = fWInputSession;
                    FWInputSessionLifecycle.clearSwap(fWInputSession8);
                    FWInputSession fWInputSession9 = fWInputSession3;
                    // ** MonitorExit[arg73_26] (shouldn't be in output)
                    return;
                }
                WrapperPlayClientClickWindow wrapperPlayClientClickWindow5 = wrapperPlayClientClickWindow;
                int button = wrapperPlayClientClickWindow5.getButton();
                WrapperPlayClientClickWindow wrapperPlayClientClickWindow6 = wrapperPlayClientClickWindow;
                int slot = wrapperPlayClientClickWindow6.getSlot();
                FWInputSession fWInputSession10 = fWInputSession;
                int n6 = heldItemSlot;
                LegitSwapLogic.SwapTrack swapTrack = fWInputSession10.track(n6);
                if (LegitSwapLogic.isMatchingReturnSwap(swapTrack, n4 = slot, n3 = button, l2 = l3)) {
                    FWInputSession fWInputSession11 = fWInputSession;
                    fWInputSession11.returnSwapSeen = true;
                    FWInputFlagEvaluator fWInputFlagEvaluator = this.flagEvaluator;
                    Player player3 = player;
                    User user = packetContext.user();
                    FWInputSession fWInputSession12 = fWInputSession;
                    fWInputFlagEvaluator.trySwapFlag(player3, user, fWInputSession12);
                    FWInputSession fWInputSession13 = fWInputSession3;
                    // ** MonitorExit[arg71_42] (shouldn't be in output)
                    return;
                }
                int n7 = slot;
                int n8 = button;
                int n9 = heldItemSlot;
                Player player4 = player;
                int n10 = button;
                Material material2 = FWInputSessionLifecycle.hotbarMaterial(player4, n10);
                Material material3 = material2;
                boolean bl = material3 == (material = Material.FIREWORK_ROCKET);
                if (!LegitSwapLogic.canArmFirstSwap(n7, n8, n9, bl)) {
                    FWInputSession fWInputSession14 = fWInputSession;
                    FWInputSessionLifecycle.clearSwap(fWInputSession14);
                    FWInputSession fWInputSession15 = fWInputSession3;
                    // ** MonitorExit[arg67_53] (shouldn't be in output)
                    return;
                }
                FWInputSession fWInputSession16 = fWInputSession;
                fWInputSession16.swapInvSlot = n2 = slot;
                FWInputSession fWInputSession17 = fWInputSession;
                fWInputSession17.swapHotbarButton = n = button;
                FWInputSession fWInputSession18 = fWInputSession;
                fWInputSession18.swapFirstNs = l = l3;
                FWInputSession fWInputSession19 = fWInputSession;
                fWInputSession19.swapBaselineNonRocket = true;
                FWInputSession fWInputSession20 = fWInputSession;
                fWInputSession20.returnSwapSeen = false;
                FWInputFlagEvaluator fWInputFlagEvaluator = this.flagEvaluator;
                Player player5 = player;
                User user = packetContext.user();
                FWInputSession fWInputSession21 = fWInputSession;
                fWInputFlagEvaluator.trySwapFlag(player5, user, fWInputSession21);
                FWInputSession fWInputSession22 = fWInputSession3;
                // ** MonitorExit[arg62_66] (shouldn't be in output)
                return;
            }
            catch (Throwable e) {
                Throwable e3 = e;
                FWInputSession fWInputSession23 = fWInputSession3;
                // ** MonitorExit[arg64_69] (shouldn't be in output)
                throw e;
            }
        }
    }

    private void onCloseWindow(@NotNull PacketContext packetContext) {
        FWInputSession fWInputSession;
        FWInputSession fWInputSession2;
        FWInputSession fWInputSession3;
        if (this.isInactivePacket(packetContext)) {
            return;
        }
        try {
            int n;
            PacketReceiveEvent packetReceiveEvent = packetContext.event();
            WrapperPlayClientCloseWindow wrapperPlayClientCloseWindow = new WrapperPlayClientCloseWindow(packetReceiveEvent);
            int n2 = n = wrapperPlayClientCloseWindow.getWindowId();
            if (n2 != 0) {
                return;
            }
            Map<UUID, FWInputSession> map = this.sessions;
            UUID uUID = packetContext.playerId();
            fWInputSession3 = map.get(uUID);
            FWInputSession fWInputSession4 = fWInputSession3;
            if (fWInputSession4 == null) {
                return;
            }
            fWInputSession = fWInputSession2 = fWInputSession3;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return;
        }
        synchronized (fWInputSession2) {
            try {
                FWInputSession fWInputSession5 = fWInputSession3;
                if (!fWInputSession5.valid) {
                    FWInputSession fWInputSession6 = fWInputSession;
                    // ** MonitorExit[arg26_13] (shouldn't be in output)
                    return;
                }
                FWInputSession fWInputSession7 = fWInputSession3;
                FWInputSessionLifecycle.clearSwap(fWInputSession7);
                FWInputSession fWInputSession8 = fWInputSession;
                // ** MonitorExit[arg22_15] (shouldn't be in output)
                return;
            }
            catch (Throwable e) {
                Throwable e3 = e;
                FWInputSession fWInputSession9 = fWInputSession;
                // ** MonitorExit[arg24_18] (shouldn't be in output)
                throw e;
            }
        }
    }

    private void onPlayerDigging(@NotNull PacketContext packetContext) {
        FWInputSession fWInputSession;
        DiggingAction diggingAction;
        if (this.isInactivePacket(packetContext)) {
            return;
        }
        try {
            PacketReceiveEvent packetReceiveEvent = packetContext.event();
            WrapperPlayClientPlayerDigging wrapperPlayClientPlayerDigging = new WrapperPlayClientPlayerDigging(packetReceiveEvent);
            diggingAction = wrapperPlayClientPlayerDigging.getAction();
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return;
        }
        DiggingAction diggingAction2 = diggingAction;
        DiggingAction diggingAction3 = DiggingAction.SWAP_ITEM_WITH_OFFHAND;
        if (diggingAction2 != diggingAction3) {
            return;
        }
        Map<UUID, FWInputSession> map = this.sessions;
        UUID uUID = packetContext.playerId();
        FWInputSession fWInputSession2 = map.get(uUID);
        FWInputSession fWInputSession3 = fWInputSession2;
        if (fWInputSession3 == null) {
            return;
        }
        FWInputSession fWInputSession4 = fWInputSession = fWInputSession2;
        synchronized (fWInputSession) {
            try {
                FWInputSession fWInputSession5 = fWInputSession2;
                if (!fWInputSession5.valid) {
                    FWInputSession fWInputSession6 = fWInputSession4;
                    // ** MonitorExit[arg21_16] (shouldn't be in output)
                    return;
                }
                FWInputSession fWInputSession7 = fWInputSession2;
                FWInputSessionLifecycle.clearSwap(fWInputSession7);
                FWInputSession fWInputSession8 = fWInputSession4;
                // ** MonitorExit[arg26_18] (shouldn't be in output)
            }
            catch (Throwable e) {
                Throwable e3 = e;
                FWInputSession fWInputSession9 = fWInputSession4;
                // ** MonitorExit[arg23_21] (shouldn't be in output)
                throw e;
            }
            return;
        }
    }

    public void flushDeferredViolationFlags(@NotNull Player player, @NotNull User user, @NotNull UUID uUID) {
        FWInputSession fWInputSession;
        if (!this.isActiveFor(player)) {
            return;
        }
        FWInputSession fWInputSession2 = this.sessions.get(uUID);
        FWInputSession fWInputSession3 = fWInputSession2;
        if (fWInputSession3 == null) {
            return;
        }
        FWInputSession fWInputSession4 = fWInputSession = fWInputSession2;
        synchronized (fWInputSession) {
            try {
                FWInputSession fWInputSession5 = fWInputSession2;
                if (!fWInputSession5.valid) {
                    FWInputSession fWInputSession6 = fWInputSession4;
                    // ** MonitorExit[arg24_9] (shouldn't be in output)
                    return;
                }
                FWInputSession fWInputSession7 = fWInputSession2;
                long l = System.nanoTime();
                FWInputSessionLifecycle.expireStale(fWInputSession7, l);
                FWInputSession fWInputSession8 = fWInputSession4;
                // ** MonitorExit[arg20_12] (shouldn't be in output)
                return;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                FWInputSession fWInputSession9 = fWInputSession4;
                // ** MonitorExit[arg22_15] (shouldn't be in output)
                throw e;
            }
        }
    }

    public void onElytraBoost(@NotNull Player player) {
        FWInputSession fWInputSession;
        if (!this.isActiveFor(player) || !player.isGliding()) {
            return;
        }
        User user = FWInputCheck.resolveUser(player);
        User user2 = user;
        if (user2 == null) {
            return;
        }
        Map<UUID, FWInputSession> map = this.sessions;
        UUID uniqueId = player.getUniqueId();
        UUID uUID = uniqueId;
        FWInputSession fWInputSession2 = map.get(uUID);
        FWInputSession fWInputSession3 = fWInputSession2;
        if (fWInputSession3 == null) {
            return;
        }
        FWInputSession fWInputSession4 = fWInputSession = fWInputSession2;
        synchronized (fWInputSession) {
            try {
                int n;
                FWInputSession fWInputSession5 = fWInputSession2;
                if (!fWInputSession5.valid) {
                    FWInputSession fWInputSession6 = fWInputSession4;
                    // ** MonitorExit[arg30_12] (shouldn't be in output)
                    return;
                }
                FWInputSession fWInputSession7 = fWInputSession2;
                fWInputSession7.heldSlot = n = player.getInventory().getHeldItemSlot();
                long l = System.nanoTime();
                FWInputSession fWInputSession8 = fWInputSession2;
                long l2 = l;
                FWInputSessionLifecycle.expireStale(fWInputSession8, l2);
                FWInputSession fWInputSession9 = fWInputSession2;
                if (fWInputSession9.swapHotbarButton >= 0) {
                    long l3;
                    FWInputSession fWInputSession10 = fWInputSession2;
                    fWInputSession10.elytraBoostNs = l3 = l;
                    FWInputFlagEvaluator fWInputFlagEvaluator = this.flagEvaluator;
                    User user3 = user;
                    FWInputSession fWInputSession11 = fWInputSession2;
                    fWInputFlagEvaluator.trySwapFlag(player, user3, fWInputSession11);
                }
                FWInputSession fWInputSession12 = fWInputSession4;
                // ** MonitorExit[arg43_24] (shouldn't be in output)
                return;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                FWInputSession fWInputSession13 = fWInputSession4;
                // ** MonitorExit[arg32_27] (shouldn't be in output)
                throw e;
            }
        }
    }

    private boolean isTickReliable(@NotNull Player player, @NotNull User user) {
        UUID uniqueId = player.getUniqueId();
        MovementUncertaintyState movementUncertaintyState = PlayerCheckData.get(player.getUniqueId()).movementUncertaintyState();
        return TickReliability.isTickingReliablyFor(player, user, uniqueId, movementUncertaintyState);
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

    @Override
    public void seedPlayer(@NotNull UUID uUID2) {
        Map<UUID, FWInputSession> map = this.sessions;
        Function<UUID, FWInputSession> function = uUID -> new FWInputSession();
        FWInputSession fWInputSession = map.computeIfAbsent(uUID2, function);
    }

    @Override
    public void clearPlayer(@NotNull UUID uUID2) {
        Map<UUID, FWInputSession> map = this.sessions;
        BiFunction<UUID, FWInputSession, FWInputSession> biFunction = (uUID, fWInputSession) -> {
            synchronized (fWInputSession) {
                fWInputSession.valid = false;
                FWInputSessionLifecycle.clearSwap(fWInputSession);
                return null;
            }
        };
        map.computeIfPresent(uUID2, biFunction);
    }

    @Override
    public void shutdown() {
        for (FWInputSession session : this.sessions.values()) {
            synchronized (session) {
                session.valid = false;
                FWInputSessionLifecycle.clearSwap(session);
            }
        }
        this.sessions.clear();
    }
}

