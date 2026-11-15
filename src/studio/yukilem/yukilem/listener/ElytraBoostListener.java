package studio.glace.glantielytratarget.listener;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import lombok.Generated;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.checks.CheckRegistry;
import studio.glace.glantielytratarget.checks.PlayerCheckData;
import studio.glace.glantielytratarget.checks.impl.CombatCheck;
import studio.glace.glantielytratarget.checks.impl.ElytraPatternCheck;
import studio.glace.glantielytratarget.checks.impl.FWInputCheck;
import studio.glace.glantielytratarget.checks.impl.HotbarOrderCheck;
import studio.glace.glantielytratarget.checks.impl.SequenceCheck;
import studio.glace.glantielytratarget.checks.impl.ShootCheck;
import studio.glace.glantielytratarget.platform.BypassPolicy;
import studio.glace.glantielytratarget.platform.PlayerSessionCache;
import studio.glace.glantielytratarget.platform.ServerHealthMonitor;
import studio.glace.glantielytratarget.util.combat.ElytraUseProbe;

public final class ElytraBoostListener
implements Listener {
    private final PluginContext ctx;

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onElytraBoost(@NotNull PlayerElytraBoostEvent playerElytraBoostEvent) {
        CheckRegistry checkRegistry;
        ShootCheck shootCheck;
        ShootCheck shootCheck2;
        CheckRegistry checkRegistry2;
        SequenceCheck sequenceCheck;
        SequenceCheck sequenceCheck2;
        CheckRegistry checkRegistry3;
        HotbarOrderCheck hotbarOrderCheck;
        HotbarOrderCheck hotbarOrderCheck2;
        CheckRegistry checkRegistry4;
        FWInputCheck fWInputCheck;
        FWInputCheck fWInputCheck2;
        CheckRegistry checkRegistry5;
        ElytraPatternCheck elytraPatternCheck;
        ElytraPatternCheck elytraPatternCheck2;
        CheckRegistry checkRegistry6;
        Player player;
        ServerHealthMonitor serverHealthMonitor;
        PlayerSessionCache playerSessionCache;
        Player player2 = playerElytraBoostEvent.getPlayer();
        Player player3 = player2;
        if (BypassPolicy.shouldBypass(player3, playerSessionCache = this.ctx.sessionCache())) {
            return;
        }
        ServerHealthMonitor serverHealthMonitor2 = this.ctx.healthMonitor();
        ServerHealthMonitor serverHealthMonitor3 = serverHealthMonitor2;
        if (serverHealthMonitor3 != null && (serverHealthMonitor = serverHealthMonitor2).shouldSkipDetection(player = player2)) {
            return;
        }
        CheckRegistry checkRegistry7 = this.ctx.checks();
        CheckRegistry checkRegistry8 = checkRegistry7;
        if (checkRegistry8 == null || !(checkRegistry6 = checkRegistry7).anyBoostCheckEnabled()) {
            return;
        }
        Player player4 = player2;
        if (ElytraUseProbe.targetsBlockInReach(player4)) {
            return;
        }
        long l = System.currentTimeMillis();
        CheckRegistry checkRegistry9 = checkRegistry7;
        CombatCheck combatCheck = checkRegistry9.getCombatCheck();
        CombatCheck combatCheck2 = combatCheck;
        if (combatCheck2 != null) {
            CombatCheck combatCheck3 = combatCheck;
            Player player5 = player2;
            long l2 = l;
            combatCheck3.onRocketBoost(player5, l2);
        }
        if ((elytraPatternCheck2 = (elytraPatternCheck = (checkRegistry5 = checkRegistry7).getElytraPatternCheck())) != null) {
            ElytraPatternCheck elytraPatternCheck3 = elytraPatternCheck;
            Player player6 = player2;
            long l3 = l;
            elytraPatternCheck3.onRocketBoost(player6, l3);
        }
        if ((fWInputCheck2 = (fWInputCheck = (checkRegistry4 = checkRegistry7).getFwInputCheck())) != null) {
            FWInputCheck fWInputCheck3 = fWInputCheck;
            Player player7 = player2;
            fWInputCheck3.onElytraBoost(player7);
        }
        if ((hotbarOrderCheck2 = (hotbarOrderCheck = (checkRegistry3 = checkRegistry7).getHotbarOrderCheck())) != null) {
            HotbarOrderCheck hotbarOrderCheck3 = hotbarOrderCheck;
            Player player8 = player2;
            hotbarOrderCheck3.onElytraBoost(player8);
        }
        if ((sequenceCheck2 = (sequenceCheck = (checkRegistry2 = checkRegistry7).getSequenceCheck())) != null) {
            SequenceCheck sequenceCheck3 = sequenceCheck;
            Player player9 = player2;
            sequenceCheck3.onElytraBoost(player9);
        }
        if ((shootCheck2 = (shootCheck = (checkRegistry = checkRegistry7).getShootCheck())) != null) {
            ShootCheck shootCheck3 = shootCheck;
            Player player10 = player2;
            shootCheck3.onElytraBoost(player10);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onToggleGlide(@NotNull EntityToggleGlideEvent entityToggleGlideEvent) {
        double d;
        Player player;
        PlayerCheckData playerCheckData;
        PlayerCheckData playerCheckData2;
        CheckRegistry checkRegistry;
        Entity entity = entityToggleGlideEvent.getEntity();
        Entity entity2 = entity;
        if (!(entity2 instanceof Player)) {
            return;
        }
        Entity entity3 = entity;
        Player player2 = (Player)entity3;
        CheckRegistry checkRegistry2 = checkRegistry = this.ctx.checks();
        if (checkRegistry2 != null) {
            CheckRegistry checkRegistry3 = checkRegistry;
            ShootCheck shootCheck = checkRegistry3.getShootCheck();
            Player player3 = player2;
            shootCheck.onGlideTransition(player3);
        }
        if ((playerCheckData2 = (playerCheckData = PlayerCheckData.getIfPresent((player = player2).getUniqueId()))) == null) {
            return;
        }
        boolean gliding = entityToggleGlideEvent.isGliding();
        PlayerCheckData playerCheckData3 = playerCheckData;
        boolean bl = gliding;
        if (bl) {
            Player player4 = player2;
            d = player4.getLocation().getY();
        } else {
            d = Double.NaN;
        }
        playerCheckData3.setGlideBaselineY(d);
        PlayerCheckData playerCheckData4 = playerCheckData;
        boolean bl2 = gliding;
        long l = bl2 ? System.currentTimeMillis() : 0L;
        playerCheckData4.setGlideStartMs(l);
        PlayerCheckData playerCheckData5 = playerCheckData;
        playerCheckData5.setCombatPhaseBActive(false);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onGlideMove(@NotNull PlayerMoveEvent playerMoveEvent) {
        if (!playerMoveEvent.hasChangedPosition()) {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        Player player2 = player;
        if (!player2.isGliding()) {
            return;
        }
        CombatCheck combatCheck = this.ctx.checks() != null ? this.ctx.checks().getCombatCheck() : null;
        CombatCheck combatCheck2 = combatCheck;
        CombatCheck combatCheck3 = combatCheck2;
        if (combatCheck3 != null) {
            CombatCheck combatCheck4 = combatCheck2;
            Player player3 = player;
            combatCheck4.onGlidePositionUpdate(player3);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onGlideTeleport(@NotNull PlayerTeleportEvent playerTeleportEvent) {
        Player player;
        Player player2 = playerTeleportEvent.getPlayer();
        CheckRegistry checkRegistry = this.ctx.checks();
        CheckRegistry checkRegistry2 = checkRegistry;
        if (checkRegistry2 != null) {
            CheckRegistry checkRegistry3 = checkRegistry;
            ShootCheck shootCheck = checkRegistry3.getShootCheck();
            Player player3 = player2;
            shootCheck.onGlideTransition(player3);
        }
        if (!(player = player2).isGliding()) {
            return;
        }
        CombatCheck combatCheck = this.ctx.checks() != null ? this.ctx.checks().getCombatCheck() : null;
        CombatCheck combatCheck2 = combatCheck;
        CombatCheck combatCheck3 = combatCheck2;
        if (combatCheck3 != null) {
            CombatCheck combatCheck4 = combatCheck2;
            Player player4 = player2;
            combatCheck4.onGlidePositionUpdate(player4);
        }
    }

    @Generated
    public ElytraBoostListener(PluginContext pluginContext) {
        this.ctx = pluginContext;
    }
}

