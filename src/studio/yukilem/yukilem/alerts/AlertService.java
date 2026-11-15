package studio.glace.glantielytratarget.alerts;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.libs.caffeine.cache.Cache;
import studio.glace.glantielytratarget.libs.caffeine.cache.Caffeine;

public final class AlertService {
    private final PluginContext ctx;
    private final Cache<UUID, Boolean> alertsEnabled;

    public void toggleAlerts(@NotNull UUID uUID) {
        Boolean bl = Boolean.TRUE;
        Boolean ifPresent = this.alertsEnabled.getIfPresent(uUID);
        if (bl.equals(ifPresent)) {
            this.alertsEnabled.invalidate(uUID);
        } else {
            Cache<UUID, Boolean> cache = this.alertsEnabled;
            Boolean bl2 = Boolean.TRUE;
            cache.put(uUID, bl2);
        }
    }

    public boolean hasAlertsEnabled(@NotNull UUID uUID) {
        Boolean bl = Boolean.TRUE;
        Boolean ifPresent = this.alertsEnabled.getIfPresent(uUID);
        return bl.equals(ifPresent);
    }

    public void enableAlerts(@NotNull UUID uUID) {
        Cache<UUID, Boolean> cache = this.alertsEnabled;
        Boolean bl = Boolean.TRUE;
        cache.put(uUID, bl);
    }

    public void disableAlerts(@NotNull UUID uUID) {
        this.alertsEnabled.invalidate(uUID);
    }

    public void clearAllAlerts() {
        this.alertsEnabled.invalidateAll();
    }

    public long getEnabledAlertsCount() {
        return this.alertsEnabled.estimatedSize();
    }

    public void handlePlayerJoin(@NotNull Player player) {
        if (!this.ctx.config().getAlerts().enableOnJoin()) {
            return;
        }
        if (player.hasPermission("glantielytratarget.alerts") && player.hasPermission("glantielytratarget.alerts.join")) {
            UUID uniqueId = player.getUniqueId();
            this.enableAlerts(uniqueId);
        }
    }

    @Generated
    public AlertService(PluginContext pluginContext) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        long l = 30L;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        Caffeine<Object, Object> caffeine2 = caffeine.expireAfterAccess(l, timeUnit);
        long l2 = 2000L;
        Cache cache = caffeine2.maximumSize(l2).build();
        this.alertsEnabled = cache;
        this.ctx = pluginContext;
    }
}

