package studio.yukilem.yukilem.checks;

import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.CheckMetadata;
import studio.yukilem.yukilem.checks.GlideAck;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.config.ChecksConfigurationService;
import studio.yukilem.yukilem.packet.PacketContext;
import studio.yukilem.yukilem.packet.PacketRouter;
import studio.yukilem.yukilem.platform.BypassPolicy;
import studio.yukilem.yukilem.platform.region.RegionProtectionPolicy;
import studio.yukilem.yukilem.punishments.ViolationService;
import studio.yukilem.yukilem.util.entity.EntityLookup;

public abstract class Check {
    protected final PluginContext ctx;
    private final CheckId id;
    private final String configRoot;
    private final String[] aliases;
    private final String displayName;
    private final boolean heuristic;
    private volatile boolean enabledCache = true;

    protected Check(@NotNull PluginContext pluginContext) {
        String string;
        String string2;
        CheckId checkId;
        CheckMetadata checkMetadata;
        this.ctx = pluginContext;
        Class<?> clazz = this.getClass();
        Class<CheckMetadata> clazz2 = CheckMetadata.class;
        CheckMetadata checkMetadata2 = checkMetadata = clazz.getAnnotation(clazz2);
        if (checkMetadata2 == null) {
            String string3 = "Check " + this.getClass().getName() + " is missing @CheckMetadata";
            IllegalStateException e = new IllegalStateException(string3);
            throw e;
        }
        CheckMetadata checkMetadata3 = checkMetadata;
        this.id = checkId = checkMetadata3.id();
        CheckMetadata checkMetadata4 = checkMetadata;
        this.configRoot = string2 = checkMetadata4.configRoot();
        CheckMetadata checkMetadata5 = checkMetadata;
        String[] stringArray = checkMetadata5.aliases();
        this.aliases = stringArray;
        this.displayName = string = this.id.displayName();
        CheckMetadata checkMetadata6 = checkMetadata;
        boolean bl = checkMetadata6.heuristic() || this.id.isHeuristic();
        this.heuristic = bl;
    }

    public boolean isEnabled() {
        return this.enabledCache;
    }

    public void refreshEnabled() {
        boolean bl;
        if (this.configRoot.isEmpty()) {
            this.enabledCache = true;
            return;
        }
        ChecksConfigurationService checksConfigurationService = this.ctx.checksConfig();
        String string = this.configRoot + ".enabled";
        this.enabledCache = bl = checksConfigurationService.getBoolean(string, true);
    }

    protected final boolean awaitingGlideAck(@NotNull Player player) {
        PlayerCheckData playerCheckData = PlayerCheckData.getIfPresent(player.getUniqueId());
        PlayerCheckData playerCheckData2 = playerCheckData;
        if (playerCheckData2 == null) {
            return false;
        }
        PlayerCheckData playerCheckData3 = playerCheckData;
        long l = playerCheckData3.glideStartMs();
        long l2 = System.currentTimeMillis();
        int ping = player.getPing();
        return GlideAck.awaiting(l, l2, ping);
    }

    protected final boolean clientOnGround(@NotNull Player player) {
        PlayerCheckData playerCheckData;
        PlayerCheckData playerCheckData2 = PlayerCheckData.getIfPresent(player.getUniqueId());
        PlayerCheckData playerCheckData3 = playerCheckData2;
        boolean bl = playerCheckData3 != null && (playerCheckData = playerCheckData2).movementUncertaintyState().isPacketOnGround();
        return bl;
    }

    protected final void flag(@NotNull Player player) {
        if (!this.isActiveFor(player) || BypassPolicy.shouldBypass(player)) {
            return;
        }
        if (this.ctx.warnings() != null) {
            ViolationService violationService = this.ctx.warnings();
            CheckId checkId = this.id;
            violationService.addViolation(player, checkId);
        }
    }

    @Nullable
    protected final Entity resolveTarget(@NotNull Player player, int n) {
        return EntityLookup.byNetworkId(player, n);
    }

    public abstract void register(@NotNull PacketRouter packetRouter1);

    public void seedPlayer(@NotNull UUID uUID) {
    }

    public void clearPlayer(@NotNull UUID uUID) {
    }

    public void shutdown() {
    }

    protected final boolean isInactivePacket(@NotNull PacketContext packetContext) {
        Player player = packetContext.player();
        boolean bl = !this.isActiveFor(player);
        return bl;
    }

    public final boolean isActiveFor(@NotNull Player player) {
        RegionProtectionPolicy regionProtectionPolicy;
        if (!this.isEnabled() || !player.isOnline()) {
            return false;
        }
        RegionProtectionPolicy regionProtectionPolicy2 = this.ctx.regionProtection();
        RegionProtectionPolicy regionProtectionPolicy3 = regionProtectionPolicy2;
        if (regionProtectionPolicy3 == null || !(regionProtectionPolicy = regionProtectionPolicy2).active()) {
            return true;
        }
        RegionProtectionPolicy regionProtectionPolicy4 = regionProtectionPolicy2;
        CheckId checkId = this.id;
        boolean bl = !regionProtectionPolicy4.disables(player, checkId);
        return bl;
    }

    protected final boolean isRegionCombatInactive(@NotNull Player player, @Nullable Entity entity) {
        RegionProtectionPolicy regionProtectionPolicy;
        RegionProtectionPolicy regionProtectionPolicy2 = this.ctx.regionProtection();
        RegionProtectionPolicy regionProtectionPolicy3 = regionProtectionPolicy2;
        if (regionProtectionPolicy3 == null || !(regionProtectionPolicy = regionProtectionPolicy2).active()) {
            boolean bl = !this.isActiveFor(player);
            return bl;
        }
        RegionProtectionPolicy regionProtectionPolicy4 = regionProtectionPolicy2;
        Location location = player.getLocation();
        regionProtectionPolicy4.refreshPlayer(player, location);
        if (!this.isActiveFor(player)) {
            return true;
        }
        if (entity == null) {
            return false;
        }
        RegionProtectionPolicy regionProtectionPolicy5 = regionProtectionPolicy2;
        Location location2 = entity.getLocation();
        CheckId checkId = this.id;
        return regionProtectionPolicy5.disablesLocation(player, location2, checkId);
    }

    @Generated
    public PluginContext getCtx() {
        return this.ctx;
    }

    @Generated
    public CheckId getId() {
        return this.id;
    }

    @Generated
    public String getConfigRoot() {
        return this.configRoot;
    }

    @Generated
    public String[] getAliases() {
        return this.aliases;
    }

    @Generated
    public String getDisplayName() {
        return this.displayName;
    }

    @Generated
    public boolean isHeuristic() {
        return this.heuristic;
    }
}

