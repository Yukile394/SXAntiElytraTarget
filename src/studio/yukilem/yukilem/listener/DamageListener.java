package studio.yukilem.yukilem.listener;

import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.CheckRegistry;
import studio.yukilem.yukilem.checks.impl.CombatCheck;
import studio.yukilem.yukilem.checks.impl.ElytraPatternCheck;
import studio.yukilem.yukilem.platform.BypassPolicy;
import studio.yukilem.yukilem.platform.PlayerSessionCache;
import studio.yukilem.yukilem.platform.region.RegionProtectionPolicy;
import studio.yukilem.yukilem.punishments.ViolationService;
import studio.yukilem.yukilem.util.combat.TargetTracker;

public final class DamageListener
implements Listener {
    private final PluginContext ctx;

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onDamageReduction(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity damager = entityDamageByEntityEvent.getDamager();
        Entity entity = damager;
        if (entity instanceof Player) {
            double d;
            Entity entity2 = damager;
            Player player = (Player)entity2;
            Player player2 = player;
            PlayerSessionCache playerSessionCache = this.ctx.sessionCache();
            if (BypassPolicy.shouldBypass(player2, playerSessionCache)) {
                return;
            }
            ViolationService violationService = this.ctx.warnings();
            ViolationService violationService2 = violationService;
            if (violationService2 == null) {
                return;
            }
            ViolationService violationService3 = violationService;
            Player player3 = player;
            UUID uniqueId = player3.getUniqueId();
            double d2 = violationService3.damageReductionFactor(uniqueId);
            double d3 = d2;
            if (d3 < (d = 1.0)) {
                double damage = entityDamageByEntityEvent.getDamage();
                double d4 = d2;
                double d5 = damage * d4;
                entityDamageByEntityEvent.setDamage(d5);
            }
            return;
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        boolean bl;
        CheckId checkId;
        Entity entity;
        Location location;
        Player player;
        RegionProtectionPolicy regionProtectionPolicy;
        RegionProtectionPolicy regionProtectionPolicy2;
        RegionProtectionPolicy regionProtectionPolicy3;
        CheckId checkId2;
        Entity entity2;
        Location location2;
        Player player2;
        RegionProtectionPolicy regionProtectionPolicy4;
        RegionProtectionPolicy regionProtectionPolicy5;
        RegionProtectionPolicy regionProtectionPolicy6;
        Player player3;
        CheckRegistry checkRegistry;
        CombatCheck combatCheck;
        RegionProtectionPolicy regionProtectionPolicy7;
        CheckRegistry checkRegistry2;
        Entity damager = entityDamageByEntityEvent.getDamager();
        Entity entity3 = damager;
        if (!(entity3 instanceof Player)) {
            return;
        }
        Entity entity4 = damager;
        Player player4 = (Player)entity4;
        CheckRegistry checkRegistry3 = checkRegistry2 = this.ctx.checks();
        if (checkRegistry3 == null) {
            return;
        }
        RegionProtectionPolicy regionProtectionPolicy8 = this.ctx.regionProtection();
        Entity entity5 = entityDamageByEntityEvent.getEntity();
        RegionProtectionPolicy regionProtectionPolicy9 = regionProtectionPolicy8;
        if (regionProtectionPolicy9 != null && (regionProtectionPolicy7 = regionProtectionPolicy8).active()) {
            RegionProtectionPolicy regionProtectionPolicy10 = regionProtectionPolicy8;
            Player player5 = player4;
            Player player6 = player4;
            Location location3 = player6.getLocation();
            regionProtectionPolicy10.refreshPlayer(player5, location3);
        }
        boolean bl2 = (combatCheck = (checkRegistry = checkRegistry2).getCombatCheck()).isActiveFor(player3 = player4) && ((regionProtectionPolicy6 = regionProtectionPolicy8) == null || !(regionProtectionPolicy5 = regionProtectionPolicy8).active() || !(regionProtectionPolicy4 = regionProtectionPolicy8).disablesLocation(player2 = player4, location2 = (entity2 = entity5).getLocation(), checkId2 = CheckId.COMBAT));
        boolean bl3 = bl2;
        CheckRegistry checkRegistry4 = checkRegistry2;
        ElytraPatternCheck elytraPatternCheck = checkRegistry4.getElytraPatternCheck();
        Player player7 = player4;
        boolean bl4 = elytraPatternCheck.isActiveFor(player7) && ((regionProtectionPolicy3 = regionProtectionPolicy8) == null || !(regionProtectionPolicy2 = regionProtectionPolicy8).active() || !(regionProtectionPolicy = regionProtectionPolicy8).disablesLocation(player = player4, location = (entity = entity5).getLocation(), checkId = CheckId.ELYTRA_PATTERN));
        boolean bl5 = bl4;
        boolean bl6 = bl3;
        if (!bl6 && !(bl = bl5)) {
            return;
        }
        TargetTracker targetTracker = this.ctx.targetTracker();
        Player player8 = player4;
        Entity entity6 = entity5;
        targetTracker.recordAttack(player8, entity6);
    }

    @Generated
    public DamageListener(PluginContext pluginContext) {
        this.ctx = pluginContext;
    }
}

