package studio.yukilem.yukilem.punishments;

import com.github.retrooper.packetevents.protocol.player.User;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.config.PunishmentsConfigurationService;
import studio.yukilem.yukilem.config.settings.AutoPunishSettings;
import studio.yukilem.yukilem.config.settings.DamageReductionSettings;
import studio.yukilem.yukilem.data.FlagRepository;
import studio.yukilem.yukilem.data.ViolationRecord;
import studio.yukilem.yukilem.data.ViolationRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import studio.yukilem.yukilem.platform.BypassPolicy;
import studio.yukilem.yukilem.platform.ClientVersionResolver;
import studio.yukilem.yukilem.platform.PlayerSessionCache;
import studio.yukilem.yukilem.platform.ServerHealthMonitor;
import studio.yukilem.yukilem.punishments.PlaceholderContext;
import studio.yukilem.yukilem.punishments.PunishmentCommand;
import studio.yukilem.yukilem.punishments.PunishmentProfile;
import studio.yukilem.yukilem.punishments.ViolationAlertDispatcher;
import studio.yukilem.yukilem.punishments.ViolationCommandSink;

public final class ViolationService {
    private final PluginContext ctx;
    private final Map<UUID, Map<String, Integer>> violationCounts;
    private final Map<UUID, Long> damageReductionUntil;
    private final ViolationAlertDispatcher alertDispatcher;
    private final ViolationCommandSink commandSink;
    private Cache<UUID, Long> violationDecay;
    private Cache<UUID, Integer> damageFlagCounts;

    public ViolationService(@NotNull PluginContext pluginContext) {
        ViolationCommandSink violationCommandSink;
        ViolationAlertDispatcher violationAlertDispatcher;
        ConcurrentHashMap<UUID, Map<String, Integer>> concurrentHashMap = new ConcurrentHashMap<UUID, Map<String, Integer>>();
        this.violationCounts = concurrentHashMap;
        ConcurrentHashMap<UUID, Long> concurrentHashMap2 = new ConcurrentHashMap<UUID, Long>();
        this.damageReductionUntil = concurrentHashMap2;
        this.ctx = pluginContext;
        ViolationAlertDispatcher violationAlertDispatcher2 = this.alertDispatcher = (violationAlertDispatcher = new ViolationAlertDispatcher(pluginContext));
        this.commandSink = violationCommandSink = new ViolationCommandSink(pluginContext, violationAlertDispatcher2);
        this.rebuildCaches();
    }

    public void rebuildCaches() {
        long l;
        long l2;
        PunishmentsConfigurationService punishmentsConfigurationService = this.ctx.punishments();
        PunishmentsConfigurationService punishmentsConfigurationService2 = punishmentsConfigurationService;
        if (punishmentsConfigurationService2 != null) {
            PunishmentsConfigurationService punishmentsConfigurationService3 = punishmentsConfigurationService;
            l2 = punishmentsConfigurationService3.removeViolationsAfterSeconds();
        } else {
            l2 = 600L;
        }
        long l3 = l = l2;
        long l4 = 0L;
        if (l3 <= l4) {
            l = 2592000L;
        }
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        long l5 = l;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        Caffeine<Object, Object> caffeine2 = caffeine.expireAfterWrite(l5, timeUnit).initialCapacity(16);
        long l6 = 5000L;
        Caffeine<Object, Object> caffeine3 = caffeine2.maximumSize(l6);
        RemovalListener<Object, Object> removalListener = (object, object2, removalCause) -> {
            if (removalCause.wasEvicted() && object instanceof UUID) {
                UUID uUID = (UUID)object;
                Map<UUID, Map<String, Integer>> map = this.violationCounts;
                UUID uUID2 = uUID;
                Map<String, Integer> map2 = map.remove(uUID2);
            }
        };
        Cache cache = caffeine3.removalListener(removalListener).build();
        this.violationDecay = cache;
        Caffeine<Object, Object> caffeine4 = Caffeine.newBuilder();
        long l7 = l;
        TimeUnit timeUnit2 = TimeUnit.SECONDS;
        Caffeine<Object, Object> caffeine5 = caffeine4.expireAfterWrite(l7, timeUnit2).initialCapacity(16);
        long l8 = 5000L;
        Cache cache2 = caffeine5.maximumSize(l8).build();
        this.damageFlagCounts = cache2;
        this.alertDispatcher.clearCaches();
    }

    public void addViolation(@NotNull Player player, @NotNull CheckId checkId) {
        String string = checkId.displayName();
        this.report(player, string);
    }

    public void report(@NotNull Player player, @NotNull String string) {
        Iterator<PunishmentProfile> iterator;
        if (BypassPolicy.shouldBypass(player)) {
            return;
        }
        UUID uniqueId = player.getUniqueId();
        PunishmentsConfigurationService punishmentsConfigurationService = this.ctx.punishments();
        PunishmentsConfigurationService punishmentsConfigurationService2 = punishmentsConfigurationService;
        if (punishmentsConfigurationService2 == null) {
            return;
        }
        PunishmentsConfigurationService punishmentsConfigurationService3 = punishmentsConfigurationService;
        List<PunishmentProfile> list = punishmentsConfigurationService3.profiles();
        List<PunishmentProfile> list2 = list;
        if (list2.isEmpty()) {
            return;
        }
        DamageReductionSettings damageReduction = this.ctx.config().getDamageReduction();
        DamageReductionSettings damageReductionSettings = damageReduction;
        if (damageReductionSettings.enabled()) {
            int n;
            Cache<UUID, Integer> cache = this.damageFlagCounts;
            UUID uUID2 = uniqueId;
            Integer ifPresent = cache.getIfPresent(uUID2);
            Integer n2 = ifPresent;
            if (n2 == null) {
                n = 0;
            } else {
                Integer n3 = ifPresent;
                n = n3;
            }
            int n4 = n + 1;
            Cache<UUID, Integer> cache2 = this.damageFlagCounts;
            UUID uUID3 = uniqueId;
            int n5 = n4;
            Integer n6 = n5;
            cache2.put(uUID3, n6);
            int n7 = n4;
            DamageReductionSettings damageReductionSettings2 = damageReduction;
            int n8 = damageReductionSettings2.violations();
            if (n7 >= n8) {
                Map<UUID, Long> map = this.damageReductionUntil;
                UUID uUID4 = uniqueId;
                long l = System.currentTimeMillis();
                DamageReductionSettings damageReductionSettings3 = damageReduction;
                long l2 = damageReductionSettings3.durationMillis();
                Long l3 = l + l2;
                Long l4 = map.put(uUID4, l3);
            }
        }
        Map<UUID, Map<String, Integer>> map = this.violationCounts;
        UUID uUID5 = uniqueId;
        Function<UUID, Map<String, Integer>> function = uUID -> new ConcurrentHashMap<>();
        Map<String, Integer> map2 = map.computeIfAbsent(uUID5, function);
        List<PunishmentProfile> list3 = list;
        Iterator<PunishmentProfile> iterator2 = list3.iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<PunishmentCommand> iterator3;
            PlaceholderContext placeholderContext;
            Iterator<PunishmentProfile> iterator4 = iterator2;
            PunishmentProfile punishmentProfile = iterator4.next();
            PunishmentProfile punishmentProfile2 = punishmentProfile;
            if (!punishmentProfile2.matcher().matches(string)) continue;
            Map<String, Integer> map3 = map2;
            PunishmentProfile punishmentProfile3 = punishmentProfile;
            String string2 = punishmentProfile3.name();
            Integer n = 1;
            BiFunction<Integer, Integer, Integer> biFunction = Integer::sum;
            int n9 = map3.merge(string2, n, biFunction);
            Cache<UUID, Long> cache = this.violationDecay;
            UUID uUID6 = uniqueId;
            Long l = System.currentTimeMillis();
            cache.put(uUID6, l);
            String name = player.getName();
            int n10 = n9;
            int n11 = this.resolvePing(player);
            String string3 = this.resolveTps();
            PlaceholderContext placeholderContext2 = placeholderContext = new PlaceholderContext(name, string, n10, n11, string3);
            PunishmentProfile punishmentProfile4 = punishmentProfile;
            Iterator<PunishmentCommand> iterator5 = punishmentProfile4.commands().iterator();
            while ((iterator3 = iterator5).hasNext()) {
                int n12;
                Iterator<PunishmentCommand> iterator6 = iterator5;
                PunishmentCommand punishmentCommand = iterator6.next();
                PunishmentCommand punishmentCommand2 = punishmentCommand;
                if (!punishmentCommand2.matchesViolation(n12 = n9)) continue;
                ViolationCommandSink violationCommandSink = this.commandSink;
                PunishmentCommand punishmentCommand3 = punishmentCommand;
                PlaceholderContext placeholderContext3 = placeholderContext2;
                violationCommandSink.execute(player, punishmentCommand3, placeholderContext3);
            }
            AutoPunishSettings autoPunish = this.ctx.config().getAutoPunish();
            if (autoPunish.enabled() && n9 >= autoPunish.violations()) {
                String autoCommand = autoPunish.command();
                this.commandSink.execute(player, new PunishmentCommand(autoPunish.violations(), 0, PunishmentAction.CONSOLE_COMMAND, autoCommand, autoCommand), placeholderContext2);
            }
        }
        if (this.ctx.flagRepository() != null) {
            FlagRepository flagRepository = this.ctx.flagRepository();
            UUID uUID7 = uniqueId;
            String name = player.getName();
            flagRepository.incrementViolations(uUID7, name);
        }
        this.recordHistory(player, string);
    }

    private void recordHistory(@NotNull Player player, @NotNull String string) {
        ViolationRecord violationRecord;
        double d;
        PlayerCheckData playerCheckData;
        String string2;
        Location location;
        int n;
        if (this.ctx.violationRepository() == null) {
            return;
        }
        UUID uniqueId = player.getUniqueId();
        if (this.ctx.flagRepository() != null) {
            FlagRepository flagRepository = this.ctx.flagRepository();
            UUID uUID = uniqueId;
            n = flagRepository.getTotalViolations(uUID);
        } else {
            n = 0;
        }
        int n2 = n;
        Location location2 = location = player.getLocation();
        if (location2.getWorld() != null) {
            Location location3 = location;
            string2 = location3.getWorld().getName();
        } else {
            string2 = "unknown";
        }
        String string3 = string2;
        UUID uUID = uniqueId;
        PlayerCheckData playerCheckData2 = playerCheckData = PlayerCheckData.getIfPresent(uUID);
        if (playerCheckData2 == null) {
            d = 0.0;
        } else {
            PlayerCheckData playerCheckData3 = playerCheckData;
            d = playerCheckData3.movementUncertaintyState().getSmoothedBps();
        }
        double d2 = d;
        long l = 0L;
        UUID uUID2 = uniqueId;
        String name = player.getName();
        int n3 = n2;
        long l2 = System.currentTimeMillis();
        double d3 = d2;
        String string4 = this.resolveClientVersion(player);
        int n4 = this.resolvePing(player);
        String string5 = this.resolveTps();
        String string6 = string3;
        Location location4 = location;
        double x = location4.getX();
        Location location5 = location;
        double y = location5.getY();
        Location location6 = location;
        double z = location6.getZ();
        Location location7 = location;
        float yaw = location7.getYaw();
        Location location8 = location;
        float pitch = location8.getPitch();
        String string7 = player.getGameMode().name();
        boolean gliding = player.isGliding();
        boolean onGround = player.isOnGround();
        boolean sprinting = player.isSprinting();
        ViolationRecord violationRecord2 = violationRecord = new ViolationRecord(l, uUID2, name, string, n3, l2, d3, string4, n4, string5, string6, x, y, z, yaw, pitch, string7, gliding, onGround, sprinting);
        ViolationRepository violationRepository = this.ctx.violationRepository();
        ViolationRecord violationRecord3 = violationRecord2;
        violationRepository.record(violationRecord3);
    }

    @NotNull
    private String resolveClientVersion(@NotNull Player player) {
        User user;
        block5: {
            PlayerSessionCache playerSessionCache;
            block4: {
                try {
                    PlayerSessionCache playerSessionCache2 = playerSessionCache = this.ctx.sessionCache();
                    if (playerSessionCache2 != null) break block4;
                    return "Unknown";
                }
                catch (Throwable e) {
                    Throwable e2 = e;
                    return "Unknown";
                }
            }
            PlayerSessionCache playerSessionCache3 = playerSessionCache;
            User user2 = user = playerSessionCache3.userOf(player);
            if (user2 != null) break block5;
            return "Unknown";
        }
        User user3 = user;
        UUID uniqueId = player.getUniqueId();
        String releaseName = ClientVersionResolver.resolve(user3, uniqueId).getReleaseName();
        return releaseName;
    }

    private int resolvePing(@NotNull Player player) {
        ServerHealthMonitor serverHealthMonitor = this.ctx.healthMonitor();
        ServerHealthMonitor serverHealthMonitor2 = serverHealthMonitor;
        if (serverHealthMonitor2 != null) {
            ServerHealthMonitor serverHealthMonitor3 = serverHealthMonitor;
            return serverHealthMonitor3.getPlayerPing(player);
        }
        try {
            int ping = player.getPing();
            return ping;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            return 0;
        }
    }

    @NotNull
    private String resolveTps() {
        double d;
        ServerHealthMonitor serverHealthMonitor = this.ctx.healthMonitor();
        ServerHealthMonitor serverHealthMonitor2 = serverHealthMonitor;
        if (serverHealthMonitor2 != null) {
            ServerHealthMonitor serverHealthMonitor3 = serverHealthMonitor;
            d = serverHealthMonitor3.getCurrentTps();
        } else {
            d = 20.0;
        }
        double d2 = d;
        Locale locale = Locale.US;
        Object[] objectArray = new Object[1];
        double d3 = d2;
        Double d4 = d3;
        objectArray[0] = d4;
        return String.format(locale, "%.1f", objectArray);
    }

    public double damageReductionFactor(@NotNull UUID uUID) {
        Long l;
        long l2;
        DamageReductionSettings damageReduction = this.ctx.config().getDamageReduction();
        DamageReductionSettings damageReductionSettings = damageReduction;
        if (!damageReductionSettings.enabled()) {
            return 1.0;
        }
        Long l3 = this.damageReductionUntil.get(uUID);
        Long l4 = l3;
        if (l4 == null) {
            return 1.0;
        }
        long l5 = System.currentTimeMillis();
        if (l5 > (l2 = (l = l3).longValue())) {
            Long l6 = this.damageReductionUntil.remove(uUID);
            return 1.0;
        }
        DamageReductionSettings damageReductionSettings2 = damageReduction;
        return damageReductionSettings2.damageFactor();
    }

    public int getViolationCount(@NotNull UUID uUID, @NotNull String string) {
        int n;
        Map<String, Integer> map = this.violationCounts.get(uUID);
        Map<String, Integer> map2 = map;
        if (map2 == null) {
            return 0;
        }
        Map<String, Integer> map3 = map;
        Integer n2 = map3.get(string);
        Integer n3 = n2;
        if (n3 == null) {
            n = 0;
        } else {
            Integer n4 = n2;
            n = n4;
        }
        return n;
    }

    public void clearViolations(@NotNull UUID uUID) {
        Map<String, Integer> map = this.violationCounts.remove(uUID);
        if (this.violationDecay != null) {
            this.violationDecay.invalidate(uUID);
        }
        if (this.damageFlagCounts != null) {
            this.damageFlagCounts.invalidate(uUID);
        }
        Long l = this.damageReductionUntil.remove(uUID);
    }

    public void clearAllViolations() {
        this.violationCounts.clear();
        if (this.violationDecay != null) {
            this.violationDecay.invalidateAll();
        }
        if (this.damageFlagCounts != null) {
            this.damageFlagCounts.invalidateAll();
        }
        this.damageReductionUntil.clear();
        this.commandSink.clear();
    }
}

