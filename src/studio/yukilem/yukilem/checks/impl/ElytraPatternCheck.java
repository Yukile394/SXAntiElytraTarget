package studio.yukilem.yukilem.checks.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.Check;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.CheckMetadata;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternEvaluator;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternSession;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternTargetGate;
import studio.yukilem.yukilem.checks.impl.pattern.ElytraPatternThresholds;
import studio.yukilem.yukilem.libs.nixlib.collection.LongSampleQueue;
import studio.yukilem.yukilem.packet.PacketContext;
import studio.yukilem.yukilem.packet.PacketRouter;
import studio.yukilem.yukilem.packet.handler.PacketHandler;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.util.combat.TargetTracker;

@CheckMetadata(id=CheckId.ELYTRA_PATTERN, configRoot="heuristic-checks.elytra-pattern-check", heuristic=true)
public final class ElytraPatternCheck
extends Check {
    private final Map<UUID, ElytraPatternSession> sessions;
    private volatile ElytraPatternThresholds.PatternThresholds thresholds;

    public ElytraPatternCheck(@NotNull PluginContext pluginContext) {
        super(pluginContext);
        ElytraPatternThresholds.PatternThresholds patternThresholds;
        ConcurrentHashMap<UUID, ElytraPatternSession> concurrentHashMap = new ConcurrentHashMap<UUID, ElytraPatternSession>();
        this.sessions = concurrentHashMap;
        this.thresholds = patternThresholds = ElytraPatternThresholds.load(pluginContext);
    }

    @Override
    public void refreshEnabled() {
        Iterator<ElytraPatternSession> iterator;
        ElytraPatternThresholds.PatternThresholds patternThresholds;
        ElytraPatternThresholds.PatternThresholds patternThresholds2;
        super.refreshEnabled();
        this.thresholds = patternThresholds2 = (patternThresholds = ElytraPatternThresholds.load(this.ctx));
        Iterator<ElytraPatternSession> iterator2 = this.sessions.values().iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<ElytraPatternSession> iterator3 = iterator2;
            ElytraPatternSession elytraPatternSession = iterator3.next();
            ElytraPatternThresholds.PatternThresholds patternThresholds3 = patternThresholds;
            int n = patternThresholds3.sampCount();
            ElytraPatternSession.resize(elytraPatternSession, n);
        }
    }

    @Override
    public void register(@NotNull PacketRouter packetRouter) {
        PacketType.Play.Client client = PacketType.Play.Client.INTERACT_ENTITY;
        PacketHandler packetHandler = this::onAttack;
        packetRouter.subscribe(client, packetHandler);
    }

    private void onAttack(@NotNull PacketContext packetContext) {
        WrapperPlayClientInteractEntity.InteractAction interactAction;
        if (this.isInactivePacket(packetContext)) {
            return;
        }
        Player player = packetContext.player();
        Player player2 = player;
        if (!player2.isGliding()) {
            return;
        }
        PacketReceiveEvent packetReceiveEvent = packetContext.event();
        WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(packetReceiveEvent);
        WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity2 = wrapperPlayClientInteractEntity;
        WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity3 = wrapperPlayClientInteractEntity2;
        WrapperPlayClientInteractEntity.InteractAction action = wrapperPlayClientInteractEntity3.getAction();
        if (action != (interactAction = WrapperPlayClientInteractEntity.InteractAction.ATTACK)) {
            return;
        }
        WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity4 = wrapperPlayClientInteractEntity2;
        int entityId = wrapperPlayClientInteractEntity4.getEntityId();
        UUID uUID = packetContext.playerId();
        long l = packetContext.timestamp();
        PlatformScheduler platformScheduler = this.ctx.scheduler();
        Player player3 = player;
        Player player4 = player;
        int n = entityId;
        UUID uUID2 = uUID;
        long l2 = l;
        Runnable runnable = () -> {
            ElytraPatternSession elytraPatternSession;
            Entity entity;
            Entity entity2 = this.resolveTarget(player4, n);
            Entity entity3 = entity2;
            if (entity3 == null || this.isRegionCombatInactive(player4, entity = entity2)) {
                return;
            }
            TargetTracker targetTracker = this.ctx.targetTracker();
            Entity entity4 = entity2;
            targetTracker.recordAttack(player4, entity4);
            ElytraPatternSession elytraPatternSession2 = elytraPatternSession = this.sessions.get(uUID2);
            if (elytraPatternSession2 != null) {
                ElytraPatternSession elytraPatternSession3 = elytraPatternSession;
                elytraPatternSession3.lastHitTime = l2;
            }
        };
        platformScheduler.runEntity(player3, runnable);
    }

    public void onRocketBoost(@NotNull Player player2, long l) {
        ElytraPatternThresholds.PatternThresholds patternThresholds;
        ElytraPatternSession elytraPatternSession;
        UUID uUID2;
        TargetTracker targetTracker;
        Entity entity;
        ElytraPatternSession elytraPatternSession2;
        if (!this.isActiveFor(player2)) {
            return;
        }
        if (!player2.isOnline() || !player2.isGliding()) {
            return;
        }
        UUID uniqueId = player2.getUniqueId();
        ElytraPatternThresholds.PatternThresholds patternThresholds2 = this.thresholds;
        Map<UUID, ElytraPatternSession> map = this.sessions;
        UUID uUID3 = uniqueId;
        ElytraPatternThresholds.PatternThresholds patternThresholds3 = patternThresholds2;
        Function<UUID, ElytraPatternSession> function = uUID -> ElytraPatternSession.create(patternThresholds3.sampCount());
        ElytraPatternSession elytraPatternSession3 = elytraPatternSession2 = map.computeIfAbsent(uUID3, function);
        ElytraPatternThresholds.PatternThresholds patternThresholds4 = patternThresholds2;
        int n = patternThresholds4.sampCount();
        ElytraPatternSession.resize(elytraPatternSession3, n);
        ElytraPatternThresholds.PatternThresholds patternThresholds5 = patternThresholds2;
        if (patternThresholds5.trEnabled() && ElytraPatternTargetGate.skipBoostReason(player2, entity = (targetTracker = this.ctx.targetTracker()).getTarget(uUID2 = uniqueId), elytraPatternSession = elytraPatternSession2, patternThresholds = patternThresholds2, l) != null) {
            return;
        }
        ElytraPatternSession elytraPatternSession4 = elytraPatternSession2;
        long l2 = elytraPatternSession4.lastFireworkTime;
        ElytraPatternSession elytraPatternSession5 = elytraPatternSession2;
        elytraPatternSession5.lastFireworkTime = l;
        long l3 = l2;
        long l4 = 0L;
        if (l3 == l4) {
            return;
        }
        long l5 = l2;
        long l6 = l - l5;
        long l7 = l6;
        ElytraPatternThresholds.PatternThresholds patternThresholds6 = patternThresholds2;
        long l8 = patternThresholds6.winMs();
        if (l7 > l8) {
            ElytraPatternSession elytraPatternSession6 = elytraPatternSession2;
            elytraPatternSession6.intervals.clear();
            return;
        }
        ElytraPatternSession elytraPatternSession7 = elytraPatternSession2;
        LongSampleQueue longSampleQueue = elytraPatternSession7.intervals;
        long l9 = l6;
        longSampleQueue.add(l9);
        ElytraPatternSession elytraPatternSession8 = elytraPatternSession2;
        if (!elytraPatternSession8.intervals.isFull()) {
            return;
        }
        ElytraPatternSession elytraPatternSession9 = elytraPatternSession2;
        ElytraPatternThresholds.PatternThresholds patternThresholds7 = patternThresholds2;
        Consumer<Player> consumer = player -> this.flag((Player)player);
        ElytraPatternEvaluator.evaluate(player2, elytraPatternSession9, patternThresholds7, consumer);
    }

    @Override
    public void seedPlayer(@NotNull UUID uUID2) {
        Map<UUID, ElytraPatternSession> map = this.sessions;
        Function<UUID, ElytraPatternSession> function = uUID -> ElytraPatternSession.create(this.thresholds.sampCount());
        ElytraPatternSession elytraPatternSession = map.computeIfAbsent(uUID2, function);
    }

    @Override
    public void clearPlayer(@NotNull UUID uUID) {
        ElytraPatternSession elytraPatternSession = this.sessions.remove(uUID);
    }

    @Override
    public void shutdown() {
        this.sessions.clear();
    }
}

