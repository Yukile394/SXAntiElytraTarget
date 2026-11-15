package studio.yukilem.yukilem.platform.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.CheckRegistry;
import studio.yukilem.yukilem.config.ConfigView;
import studio.yukilem.yukilem.glAntiElytraTarget;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.platform.region.RegionProtectionPolicy;
import studio.yukilem.yukilem.platform.region.RegionProtectionSettings;
import studio.yukilem.yukilem.platform.region.WorldGuardPvpBridgeListener;
import studio.yukilem.yukilem.platform.region.WorldGuardPvpRules;
import studio.yukilem.yukilem.util.combat.TargetTracker;

final class WorldGuardRegionPolicy
implements RegionProtectionPolicy {
    private static final long SAME_BLOCK_REFRESH_NS = 1000000000L;
    private final PluginContext ctx;
    private final RegionQuery regionQuery;
    private final Map<UUID, Snapshot> snapshots;
    private final WorldGuardPvpBridgeListener pvpBridgeListener;
    private volatile RegionProtectionSettings settings;

    WorldGuardRegionPolicy(@NotNull PluginContext pluginContext) {
        glAntiElytraTarget glAntiElytraTarget2;
        WorldGuardPvpBridgeListener worldGuardPvpBridgeListener;
        RegionProtectionSettings regionProtectionSettings;
        RegionQuery regionQuery;
        ConcurrentHashMap<UUID, Snapshot> concurrentHashMap = new ConcurrentHashMap<UUID, Snapshot>();
        this.snapshots = concurrentHashMap;
        this.ctx = pluginContext;
        this.regionQuery = regionQuery = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        this.settings = regionProtectionSettings = this.loadSettings();
        this.pvpBridgeListener = worldGuardPvpBridgeListener = new WorldGuardPvpBridgeListener(this);
        glAntiElytraTarget glAntiElytraTarget3 = glAntiElytraTarget2 = pluginContext.plugin();
        PluginManager pluginManager = glAntiElytraTarget3.getServer().getPluginManager();
        WorldGuardPvpBridgeListener worldGuardPvpBridgeListener2 = this.pvpBridgeListener;
        glAntiElytraTarget glAntiElytraTarget4 = glAntiElytraTarget2;
        pluginManager.registerEvents(worldGuardPvpBridgeListener2, glAntiElytraTarget4);
    }

    @Override
    public boolean active() {
        return this.settings.anyGateEnabled();
    }

    @Override
    public boolean disables(@NotNull Player player, @NotNull CheckId checkId) {
        if (!this.settings.anyGateEnabled()) {
            return false;
        }
        Map<UUID, Snapshot> map = this.snapshots;
        UUID uniqueId = player.getUniqueId();
        Snapshot snapshot = map.get(uniqueId);
        Snapshot snapshot2 = snapshot;
        if (snapshot2 == null) return false;
        Snapshot snapshot3 = snapshot;
        if (!snapshot3.disabledChecks.contains((Object)checkId)) return false;
        return true;
    }

    @Override
    public boolean disablesLocation(@NotNull Player player, @NotNull Location location, @NotNull CheckId checkId) {
        RegionProtectionSettings regionProtectionSettings = this.settings;
        RegionProtectionSettings regionProtectionSettings2 = regionProtectionSettings;
        if (!regionProtectionSettings2.pvpDisabledRegionsEnabled()) {
            return false;
        }
        RegionProtectionSettings regionProtectionSettings3 = regionProtectionSettings;
        if (!regionProtectionSettings3.pvpDisabledRegionChecks().contains((Object)checkId)) {
            return false;
        }
        return this.isPvpDenied(player, location);
    }

    @Override
    public void refreshSettings() {
        Iterator<? extends Player> iterator;
        RegionProtectionSettings regionProtectionSettings;
        this.settings = regionProtectionSettings = this.loadSettings();
        this.snapshots.clear();
        if (!this.settings.anyGateEnabled()) {
            return;
        }
        Iterator<? extends Player> iterator2 = Bukkit.getOnlinePlayers().iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<? extends Player> iterator3 = iterator2;
            Player player = iterator3.next();
            PlatformScheduler platformScheduler = this.ctx.scheduler();
            Player player2 = player;
            Player player3 = player;
            Runnable runnable = () -> {
                if (player3.isOnline()) {
                    Location location = player3.getLocation();
                    this.refreshPlayer(player3, location);
                }
            };
            platformScheduler.runEntity(player2, runnable);
        }
    }

    @Override
    public void refreshPlayer(@NotNull Player player, @NotNull Location location) {
        CheckId checkId;
        EnumSet<CheckId> enumSet;
        CheckId checkId2;
        EnumSet<CheckId> enumSet2;
        EnumSet<CheckId> enumSet3;
        EnumSet<CheckId> enumSet4;
        World world;
        UUID uUID;
        Snapshot snapshot;
        RegionProtectionSettings regionProtectionSettings = this.settings;
        UUID uniqueId = player.getUniqueId();
        RegionProtectionSettings regionProtectionSettings2 = regionProtectionSettings;
        if (!regionProtectionSettings2.anyGateEnabled()) {
            Map<UUID, Snapshot> map = this.snapshots;
            UUID uUID2 = uniqueId;
            Snapshot snapshot2 = map.remove(uUID2);
            return;
        }
        World world2 = location.getWorld();
        World world3 = world2;
        if (world3 == null) {
            Map<UUID, Snapshot> map = this.snapshots;
            UUID uUID3 = uniqueId;
            Snapshot snapshot3 = map.remove(uUID3);
            return;
        }
        long l = System.nanoTime();
        Map<UUID, Snapshot> map = this.snapshots;
        UUID uUID4 = uniqueId;
        Snapshot snapshot4 = map.get(uUID4);
        Snapshot snapshot5 = snapshot4;
        if (snapshot5 != null && (snapshot = snapshot4).matches(uUID = (world = world2).getUID(), location)) {
            long l2 = l;
            Snapshot snapshot6 = snapshot4;
            long l3 = snapshot6.refreshedNs;
            long l4 = l2 - l3;
            long l5 = 1000000000L;
            if (l4 < l5) {
                return;
            }
        }
        RegionProtectionSettings regionProtectionSettings3 = regionProtectionSettings;
        EnumSet<CheckId> enumSet5 = this.resolveDisabled(regionProtectionSettings3, player, location);
        Map<UUID, Snapshot> map2 = this.snapshots;
        UUID uUID5 = uniqueId;
        World world4 = world2;
        UUID uID = world4.getUID();
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();
        long l6 = l;
        EnumSet<CheckId> enumSet6 = enumSet5;
        Snapshot snapshot7 = new Snapshot(uID, blockX, blockY, blockZ, l6, enumSet6);
        Snapshot snapshot8 = map2.put(uUID5, snapshot7);
        EnumSet<CheckId> enumSet7 = enumSet5;
        EnumSet<CheckId> enumSet8 = EnumSet.copyOf(enumSet7);
        Snapshot snapshot9 = snapshot4;
        if (snapshot9 != null) {
            EnumSet<CheckId> enumSet9 = enumSet8;
            Snapshot snapshot10 = snapshot4;
            EnumSet<CheckId> enumSet10 = snapshot10.disabledChecks;
            boolean bl = enumSet9.removeAll(enumSet10);
        }
        if (!(enumSet4 = enumSet8).isEmpty() && this.ctx.checks() != null) {
            CheckRegistry checkRegistry = this.ctx.checks();
            UUID uUID6 = uniqueId;
            EnumSet<CheckId> enumSet11 = enumSet8;
            checkRegistry.clearRegionDisabledPlayer(uUID6, enumSet11);
        }
        if (!(enumSet3 = enumSet8).isEmpty() && (enumSet2 = enumSet5).contains((Object)(checkId2 = CheckId.COMBAT)) && (enumSet = enumSet5).contains((Object)(checkId = CheckId.ELYTRA_PATTERN)) && this.ctx.targetTracker() != null) {
            TargetTracker targetTracker = this.ctx.targetTracker();
            UUID uUID7 = uniqueId;
            targetTracker.removePlayer(uUID7);
        }
    }

    void onDisallowedPvp(@NotNull Player player, @NotNull Player player2) {
        CheckId checkId;
        EnumSet<CheckId> enumSet;
        CheckId checkId2;
        EnumSet<CheckId> enumSet2;
        RegionProtectionSettings regionProtectionSettings = this.settings;
        RegionProtectionSettings regionProtectionSettings2 = regionProtectionSettings;
        if (!regionProtectionSettings2.pvpDisabledRegionsEnabled()) {
            return;
        }
        RegionProtectionSettings regionProtectionSettings3 = regionProtectionSettings;
        EnumSet<CheckId> enumSet3 = regionProtectionSettings3.pvpDisabledRegionChecks();
        EnumSet<CheckId> enumSet4 = enumSet3;
        if (enumSet4.isEmpty()) {
            return;
        }
        Location location = player2.getLocation();
        this.refreshPlayer(player2, location);
        EnumSet<CheckId> enumSet5 = enumSet3;
        this.mergePvpDisabled(player, enumSet5);
        if (this.ctx.checks() != null) {
            CheckRegistry checkRegistry = this.ctx.checks();
            UUID uniqueId = player.getUniqueId();
            EnumSet<CheckId> enumSet6 = enumSet3;
            checkRegistry.clearRegionDisabledPlayer(uniqueId, enumSet6);
        }
        if ((enumSet2 = enumSet3).contains((Object)(checkId2 = CheckId.COMBAT)) && (enumSet = enumSet3).contains((Object)(checkId = CheckId.ELYTRA_PATTERN)) && this.ctx.targetTracker() != null) {
            TargetTracker targetTracker = this.ctx.targetTracker();
            UUID uniqueId = player.getUniqueId();
            targetTracker.removePlayer(uniqueId);
        }
    }

    @Override
    public void clearPlayer(@NotNull UUID uUID) {
        Snapshot snapshot = this.snapshots.remove(uUID);
    }

    @Override
    public void shutdown() {
        HandlerList.unregisterAll(this.pvpBridgeListener);
        this.snapshots.clear();
    }

    private void mergePvpDisabled(@NotNull Player player, @NotNull EnumSet<CheckId> enumSet) {
        Location location = player.getLocation();
        Location location2 = location;
        World world = location2.getWorld();
        World world2 = world;
        if (world2 == null) {
            return;
        }
        long l = System.nanoTime();
        Map<UUID, Snapshot> map = this.snapshots;
        UUID uniqueId = player.getUniqueId();
        World world3 = world;
        Location location3 = location;
        long l2 = l;
        BiFunction<UUID, Snapshot, Snapshot> biFunction = (uUID, snapshot) -> {
            EnumSet<CheckId> enumSet2;
            EnumSet<CheckId> enumSet3 = snapshot != null ? EnumSet.copyOf(snapshot.disabledChecks) : EnumSet.noneOf(CheckId.class);
            EnumSet<CheckId> enumSet4 = enumSet2 = enumSet3;
            boolean bl = enumSet4.addAll(enumSet);
            UUID uID = world3.getUID();
            int blockX = location3.getBlockX();
            int blockY = location3.getBlockY();
            int blockZ = location3.getBlockZ();
            EnumSet<CheckId> enumSet5 = enumSet2;
            Snapshot snapshot2 = new Snapshot(uID, blockX, blockY, blockZ, l2, enumSet5);
            return snapshot2;
        };
        Snapshot snapshot2 = map.compute(uniqueId, biFunction);
    }

    @NotNull
    private EnumSet<CheckId> resolveDisabled(@NotNull RegionProtectionSettings regionProtectionSettings, @NotNull Player player, @NotNull Location location) {
        ApplicableRegionSet applicableRegionSet;
        World world;
        ApplicableRegionSet applicableRegionSet2;
        ApplicableRegionSet applicableRegionSet3;
        World world2 = location.getWorld();
        World world3 = world2;
        if (world3 == null) {
            return EnumSet.noneOf(CheckId.class);
        }
        ApplicableRegionSet applicableRegionSet4 = null;
        if (regionProtectionSettings.enabled() || regionProtectionSettings.pvpDisabledRegionsEnabled()) {
            RegionQuery regionQuery = this.regionQuery;
            com.sk89q.worldedit.util.Location location2 = BukkitAdapter.adapt(location);
            applicableRegionSet4 = regionQuery.getApplicableRegions(location2);
        }
        boolean bl = false;
        if (regionProtectionSettings.enabled() && (applicableRegionSet3 = applicableRegionSet4) != null) {
            Iterator iterator;
            ApplicableRegionSet applicableRegionSet5 = applicableRegionSet4;
            Iterator iterator2 = applicableRegionSet5.iterator();
            while ((iterator = iterator2).hasNext()) {
                ProtectedRegion protectedRegion;
                String string;
                Iterator iterator3 = iterator2;
                ProtectedRegion protectedRegion2 = (ProtectedRegion)iterator3.next();
                World world4 = world2;
                String name = world4.getName();
                if (!regionProtectionSettings.matchesRegion(name, string = (protectedRegion = protectedRegion2).getId())) continue;
                bl = true;
                break;
            }
        }
        boolean bl2 = regionProtectionSettings.pvpDisabledRegionsEnabled() && (applicableRegionSet2 = applicableRegionSet4) != null && this.isPvpDenied(player, world = world2, applicableRegionSet = applicableRegionSet4);
        boolean bl3 = bl2;
        boolean bl4 = bl;
        boolean bl5 = bl3;
        return regionProtectionSettings.disabledChecks(bl4, bl5);
    }

    private boolean isPvpDenied(@NotNull Player player, @NotNull Location location) {
        World world = location.getWorld();
        World world2 = world;
        if (world2 == null) {
            return false;
        }
        RegionQuery regionQuery = this.regionQuery;
        com.sk89q.worldedit.util.Location location2 = BukkitAdapter.adapt(location);
        ApplicableRegionSet applicableRegions = regionQuery.getApplicableRegions(location2);
        World world3 = world;
        ApplicableRegionSet applicableRegionSet = applicableRegions;
        return this.isPvpDenied(player, world3, applicableRegionSet);
    }

    private boolean isPvpDenied(@NotNull Player player, @NotNull World world, @NotNull ApplicableRegionSet applicableRegionSet) {
        LocalPlayer localPlayer;
        LocalPlayer localPlayer2 = localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        StateFlag stateFlag = Flags.PVP;
        StateFlag[] stateFlagArray = new StateFlag[]{stateFlag};
        StateFlag.State state = applicableRegionSet.queryState(localPlayer2, stateFlagArray);
        LocalPlayer localPlayer3 = localPlayer;
        StateFlag stateFlag2 = Flags.INVINCIBILITY;
        StateFlag[] stateFlagArray2 = new StateFlag[]{stateFlag2};
        StateFlag.State state2 = applicableRegionSet.queryState(localPlayer3, stateFlagArray2);
        boolean pVP = world.getPVP();
        boolean bl = WorldGuardRegionPolicy.hasProtectedRegion(applicableRegionSet);
        StateFlag.State state3 = state;
        Boolean bl2 = WorldGuardRegionPolicy.mapPvpAllow(state3);
        StateFlag.State state4 = state2;
        StateFlag.State state5 = StateFlag.State.ALLOW;
        boolean bl3 = state4 == state5;
        return WorldGuardPvpRules.pvpDenied(pVP, bl, bl2, bl3);
    }

    @Nullable
    private static Boolean mapPvpAllow(@Nullable StateFlag.State state) {
        StateFlag.State state2 = StateFlag.State.ALLOW;
        if (state == state2) {
            return Boolean.TRUE;
        }
        StateFlag.State state3 = StateFlag.State.DENY;
        if (state == state3) {
            return Boolean.FALSE;
        }
        return null;
    }

    private static boolean hasProtectedRegion(@NotNull ApplicableRegionSet applicableRegionSet) {
        Iterator iterator;
        Iterator iterator2 = applicableRegionSet.iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator iterator3 = iterator2;
            String id = ((ProtectedRegion)iterator3.next()).getId();
            if ("__global__".equalsIgnoreCase(id)) continue;
            return true;
        }
        return false;
    }

    @NotNull
    private RegionProtectionSettings loadSettings() {
        ConfigView configView = this.ctx.config().view();
        Consumer<String> consumer = string -> {
            Logger logger = this.ctx.plugin().getLogger();
            String string2 = "[Config] " + string;
            logger.warning(string2);
        };
        return RegionProtectionSettings.load(configView, consumer);
    }

    private static final class Snapshot {
        final UUID worldId;
        final int x;
        final int y;
        final int z;
        final long refreshedNs;
        final EnumSet<CheckId> disabledChecks;

        Snapshot(@NotNull UUID uUID, int n, int n2, int n3, long l, @NotNull EnumSet<CheckId> enumSet) {
            this.worldId = uUID;
            this.x = n;
            this.y = n2;
            this.z = n3;
            this.refreshedNs = l;
            EnumSet<CheckId> enumSet2 = EnumSet.copyOf(enumSet);
            this.disabledChecks = enumSet2;
        }

        boolean matches(@NotNull UUID uUID, @NotNull Location location) {
            int n;
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            boolean bl = this.worldId.equals(uUID) && (n6 = this.x) == (n5 = location.getBlockX()) && (n4 = this.y) == (n3 = location.getBlockY()) && (n2 = this.z) == (n = location.getBlockZ());
            return bl;
        }
    }
}

