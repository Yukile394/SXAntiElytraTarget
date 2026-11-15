package studio.yukilem.yukilem.checks;

import com.github.retrooper.packetevents.protocol.player.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.Check;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.checks.impl.CombatCheck;
import studio.yukilem.yukilem.checks.impl.ConsistencyCheck;
import studio.yukilem.yukilem.checks.impl.ElytraPatternCheck;
import studio.yukilem.yukilem.checks.impl.FWInputCheck;
import studio.yukilem.yukilem.checks.impl.HotbarOrderCheck;
import studio.yukilem.yukilem.checks.impl.PivotCheck;
import studio.yukilem.yukilem.checks.impl.SequenceCheck;
import studio.yukilem.yukilem.checks.impl.ShootCheck;
import studio.yukilem.yukilem.packet.PacketRouter;

public final class CheckRegistry {
    private final List<Check> checks;
    private final Map<CheckId, Check> byId;
    private final Set<UUID> brokenPacketContinuity;
    private final HotbarOrderCheck hotbarOrderCheck;
    private final SequenceCheck sequenceCheck;
    private final ConsistencyCheck consistencyCheck;
    private final FWInputCheck fwInputCheck;
    private final ShootCheck shootCheck;
    private final PivotCheck pivotCheck;
    private final CombatCheck combatCheck;
    private final ElytraPatternCheck elytraPatternCheck;
    private volatile boolean needsBlockLookProbe;
    private volatile boolean anyBoostCheckEnabled;

    public CheckRegistry(@NotNull PluginContext pluginContext) {
        ElytraPatternCheck elytraPatternCheck;
        CombatCheck combatCheck;
        PivotCheck pivotCheck;
        ShootCheck shootCheck;
        FWInputCheck fWInputCheck;
        ConsistencyCheck consistencyCheck;
        SequenceCheck sequenceCheck;
        HotbarOrderCheck hotbarOrderCheck;
        ArrayList<Check> arrayList = new ArrayList<Check>();
        this.checks = arrayList;
        Class<CheckId> clazz = CheckId.class;
        EnumMap<CheckId, Check> enumMap = new EnumMap<CheckId, Check>(clazz);
        this.byId = enumMap;
        ConcurrentHashMap.KeySetView<UUID, Boolean> keySetView = ConcurrentHashMap.newKeySet();
        this.brokenPacketContinuity = keySetView;
        this.needsBlockLookProbe = true;
        this.anyBoostCheckEnabled = true;
        HotbarOrderCheck hotbarOrderCheck2 = new HotbarOrderCheck(pluginContext);
        this.hotbarOrderCheck = hotbarOrderCheck = this.register(hotbarOrderCheck2);
        SequenceCheck sequenceCheck2 = new SequenceCheck(pluginContext);
        this.sequenceCheck = sequenceCheck = this.register(sequenceCheck2);
        ConsistencyCheck consistencyCheck2 = new ConsistencyCheck(pluginContext);
        this.consistencyCheck = consistencyCheck = this.register(consistencyCheck2);
        FWInputCheck fWInputCheck2 = new FWInputCheck(pluginContext);
        this.fwInputCheck = fWInputCheck = this.register(fWInputCheck2);
        ShootCheck shootCheck2 = new ShootCheck(pluginContext);
        this.shootCheck = shootCheck = this.register(shootCheck2);
        PivotCheck pivotCheck2 = new PivotCheck(pluginContext);
        this.pivotCheck = pivotCheck = this.register(pivotCheck2);
        CombatCheck combatCheck2 = new CombatCheck(pluginContext);
        this.combatCheck = combatCheck = this.register(combatCheck2);
        ElytraPatternCheck elytraPatternCheck2 = new ElytraPatternCheck(pluginContext);
        this.elytraPatternCheck = elytraPatternCheck = this.register(elytraPatternCheck2);
        this.refreshEnabled();
    }

    private <T extends Check> T register(@NotNull T t) {
        boolean bl = this.checks.add(t);
        Map<CheckId, Check> map = this.byId;
        CheckId id = t.getId();
        Check check = map.put(id, t);
        return t;
    }

    public void registerHandlers(@NotNull PacketRouter packetRouter) {
        int n;
        int n2;
        int n3 = 0;
        int size = this.checks.size();
        while ((n2 = n3) < (n = size)) {
            List<Check> list = this.checks;
            int n4 = n3++;
            list.get(n4).register(packetRouter);
        }
    }

    public void refreshEnabled() {
        boolean bl;
        boolean bl2;
        boolean bl3;
        int n;
        int n2;
        int n3 = 0;
        int size = this.checks.size();
        while ((n2 = n3) < (n = size)) {
            List<Check> list = this.checks;
            int n4 = n3++;
            list.get(n4).refreshEnabled();
        }
        boolean bl4 = this.combatCheck.isEnabled() || this.elytraPatternCheck.isEnabled() || this.fwInputCheck.isEnabled() || this.hotbarOrderCheck.isEnabled() || this.sequenceCheck.isEnabled() || this.shootCheck.isEnabled();
        this.anyBoostCheckEnabled = bl3 = (bl2 = bl4);
        boolean bl5 = this.hotbarOrderCheck.isEnabled() || this.sequenceCheck.isEnabled() || this.shootCheck.isEnabled() || (bl = bl2);
        this.needsBlockLookProbe = bl5;
    }

    public boolean needsBlockLookProbe() {
        return this.needsBlockLookProbe;
    }

    public boolean anyBoostCheckEnabled() {
        return this.anyBoostCheckEnabled;
    }

    @NotNull
    public List<Check> getChecks() {
        return Collections.unmodifiableList(this.checks);
    }

    public Check getById(@NotNull CheckId checkId) {
        return this.byId.get((Object)checkId);
    }

    public void seedPlayer(@NotNull UUID uUID) {
        int n;
        int n2;
        int n3 = 0;
        int size = this.checks.size();
        while ((n2 = n3) < (n = size)) {
            List<Check> list = this.checks;
            int n4 = n3++;
            list.get(n4).seedPlayer(uUID);
        }
    }

    public void clearPlayer(@NotNull UUID uUID) {
        boolean bl = this.brokenPacketContinuity.remove(uUID);
        this.clearCheckSessions(uUID);
        PlayerCheckData.clear(uUID);
    }

    public void invalidatePacketContinuity(@NotNull UUID uUID) {
        if (!this.brokenPacketContinuity.add(uUID)) {
            return;
        }
        this.clearCheckSessions(uUID);
        PlayerCheckData.invalidatePacketContinuity(uUID);
    }

    public void restorePacketContinuity(@NotNull UUID uUID) {
        if (this.brokenPacketContinuity.remove(uUID)) {
            this.consistencyCheck.onPacketContinuityRestored(uUID);
        }
    }

    private void clearCheckSessions(@NotNull UUID uUID) {
        int n;
        int n2;
        int n3 = 0;
        int size = this.checks.size();
        while ((n2 = n3) < (n = size)) {
            List<Check> list = this.checks;
            int n4 = n3++;
            list.get(n4).clearPlayer(uUID);
        }
    }

    public void clearRegionDisabledPlayer(@NotNull UUID uUID, @NotNull Set<CheckId> set) {
        int n;
        int n2;
        int n3 = 0;
        int size = this.checks.size();
        while ((n2 = ++n3) < (n = size)) {
            List<Check> list = this.checks;
            int n4 = n3;
            Check check = list.get(n4);
            Check check2 = check;
            CheckId id = check2.getId();
            if (!set.contains((Object)id)) continue;
            Check check3 = check;
            check3.clearPlayer(uUID);
        }
    }

    public void shutdown() {
        int n;
        int n2;
        int n3 = 0;
        int size = this.checks.size();
        while ((n2 = n3) < (n = size)) {
            List<Check> list = this.checks;
            int n4 = n3++;
            list.get(n4).shutdown();
        }
        PlayerCheckData.clearAll();
        this.brokenPacketContinuity.clear();
    }

    public void flushDeferredViolationFlags(@NotNull Player player, @NotNull User user, @NotNull UUID uUID) {
        this.fwInputCheck.flushDeferredViolationFlags(player, user, uUID);
        this.shootCheck.flushDeferredViolationFlags(player, user, uUID);
        this.hotbarOrderCheck.flushDeferredViolationFlags(player, user, uUID);
        this.sequenceCheck.flushDeferredViolationFlags(player, user, uUID);
        this.pivotCheck.flushDeferredViolationFlags(player, user, uUID);
    }

    @Generated
    public HotbarOrderCheck getHotbarOrderCheck() {
        return this.hotbarOrderCheck;
    }

    @Generated
    public SequenceCheck getSequenceCheck() {
        return this.sequenceCheck;
    }

    @Generated
    public ConsistencyCheck getConsistencyCheck() {
        return this.consistencyCheck;
    }

    @Generated
    public FWInputCheck getFwInputCheck() {
        return this.fwInputCheck;
    }

    @Generated
    public ShootCheck getShootCheck() {
        return this.shootCheck;
    }

    @Generated
    public PivotCheck getPivotCheck() {
        return this.pivotCheck;
    }

    @Generated
    public CombatCheck getCombatCheck() {
        return this.combatCheck;
    }

    @Generated
    public ElytraPatternCheck getElytraPatternCheck() {
        return this.elytraPatternCheck;
    }
}

