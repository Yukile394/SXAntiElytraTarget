package studio.glace.glantielytratarget.checks.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.checks.Check;
import studio.glace.glantielytratarget.checks.CheckId;
import studio.glace.glantielytratarget.checks.CheckMetadata;
import studio.glace.glantielytratarget.checks.PlayerCheckData;
import studio.glace.glantielytratarget.config.ChecksConfigurationService;
import studio.glace.glantielytratarget.packet.PacketContext;
import studio.glace.glantielytratarget.packet.PacketRouter;
import studio.glace.glantielytratarget.packet.handler.PacketHandler;
import studio.glace.glantielytratarget.platform.PlatformScheduler;
import studio.glace.glantielytratarget.platform.runtime.RuntimeAuthority;
import studio.glace.glantielytratarget.platform.runtime.RuntimeProfile;
import studio.glace.glantielytratarget.util.combat.CombatEnvironment;
import studio.glace.glantielytratarget.util.combat.CombatSession;
import studio.glace.glantielytratarget.util.combat.CombatSessionManager;
import studio.glace.glantielytratarget.util.combat.TargetTracker;

@CheckMetadata(id = CheckId.COMBAT, configRoot = "heuristic-checks.combat-check", heuristic = true)
public final class CombatCheck extends Check {
   private final CombatSessionManager sessionManager;
   private volatile CombatCheck.CombatThresholds thresholds;
   private volatile CombatCheck.CombatThresholds altThresholds;
   private volatile int phaseSliceB;

   public CombatCheck(@NotNull PluginContext pluginContext1) {
      super(pluginContext1);
      CombatSessionManager combatSessionManager7 = new CombatSessionManager();
      this.sessionManager = combatSessionManager7;
      this.reloadThresholds(pluginContext1);
   }

   @Override
   public void refreshEnabled() {
      super.refreshEnabled();
      PluginContext pluginContext5 = this.ctx;
      this.reloadThresholds(pluginContext5);
   }

   private void reloadThresholds(@NotNull PluginContext pluginContext1) {
      CombatCheck.CombatThresholds combatThresholds10 = loadThresholds(pluginContext1);
      this.thresholds = combatThresholds10;
      CombatCheck.CombatThresholds combatThresholds12 = loadAltThresholds(pluginContext1);
      this.altThresholds = combatThresholds12;
      int i16 = pluginContext1.checksConfig().getInt("heuristic-checks.combat-check.phase-b.slice-b", 20);
      int i14 = Math.max(0, i16);
      this.phaseSliceB = i14;
   }

   @NotNull
   private static CombatCheck.CombatThresholds loadThresholds(@NotNull PluginContext pluginContext0) {
      ChecksConfigurationService checksConfigurationService16 = pluginContext0.checksConfig();
      RuntimeAuthority runtimeAuthority18 = pluginContext0.runtime();
      ChecksConfigurationService checksConfigurationService27 = checksConfigurationService16;
      String string35 = "heuristic-checks.combat-check." + "reqh";
      int i19 = checksConfigurationService27.getInt(string35, 4);
      ChecksConfigurationService checksConfigurationService29 = checksConfigurationService16;
      String string37 = "heuristic-checks.combat-check." + "reqf";
      int i20 = checksConfigurationService29.getInt(string37, 8);
      ChecksConfigurationService checksConfigurationService31 = checksConfigurationService16;
      String string39 = "heuristic-checks.combat-check." + "win-ms";
      long l41 = 6500L;
      long l21 = checksConfigurationService31.getLong(string39, l41);
      RuntimeAuthority runtimeAuthority43 = runtimeAuthority18;
      int i6 = i19;
      int i8 = i19;
      int i54 = i8 - 1;
      int i51 = Math.max(1, i54);
      int i44 = RuntimeProfile.foldInt(runtimeAuthority43, 0, i6, i51);
      RuntimeAuthority runtimeAuthority45 = runtimeAuthority18;
      int i52 = i20;
      int i62 = i20;
      int i63 = i62 - 2;
      int i56 = Math.max(1, i63);
      int i46 = RuntimeProfile.foldInt(runtimeAuthority45, 1, i52, i56);
      RuntimeAuthority runtimeAuthority48 = runtimeAuthority18;
      long l57 = l21;
      long l66 = 1000L;
      long l12 = l21;
      long l14 = 500L;
      long l76 = l12 - l14;
      long l68 = Math.max(l66, l76);
      long l49 = RuntimeProfile.foldLong(runtimeAuthority48, 2, l57, l68);
      ChecksConfigurationService checksConfigurationService59 = checksConfigurationService16;
      String string65 = "heuristic-checks.combat-check." + "max-h-d";
      double d70 = 20.0;
      double d60 = checksConfigurationService59.getDouble(string65, d70);
      ChecksConfigurationService checksConfigurationService72 = checksConfigurationService16;
      String string75 = "heuristic-checks.combat-check." + "max-v-d";
      double d78 = 10.0;
      double d73 = checksConfigurationService72.getDouble(string75, d78);
      return new CombatCheck.CombatThresholds(i44, i46, l49, d60, d73);
   }

   @NotNull
   private static CombatCheck.CombatThresholds loadAltThresholds(@NotNull PluginContext pluginContext0) {
      ChecksConfigurationService checksConfigurationService16 = pluginContext0.checksConfig();
      RuntimeAuthority runtimeAuthority18 = pluginContext0.runtime();
      ChecksConfigurationService checksConfigurationService27 = checksConfigurationService16;
      String string35 = "heuristic-checks.combat-check.phase-b." + "reqh";
      int i19 = checksConfigurationService27.getInt(string35, 3);
      ChecksConfigurationService checksConfigurationService29 = checksConfigurationService16;
      String string37 = "heuristic-checks.combat-check.phase-b." + "reqf";
      int i20 = checksConfigurationService29.getInt(string37, 6);
      ChecksConfigurationService checksConfigurationService31 = checksConfigurationService16;
      String string39 = "heuristic-checks.combat-check.phase-b." + "win-ms";
      long l41 = 7000L;
      long l21 = checksConfigurationService31.getLong(string39, l41);
      RuntimeAuthority runtimeAuthority43 = runtimeAuthority18;
      int i6 = i19;
      int i8 = i19;
      int i54 = i8 - 1;
      int i51 = Math.max(1, i54);
      int i44 = RuntimeProfile.foldInt(runtimeAuthority43, 3, i6, i51);
      RuntimeAuthority runtimeAuthority45 = runtimeAuthority18;
      int i52 = i20;
      int i62 = i20;
      int i63 = i62 - 2;
      int i56 = Math.max(1, i63);
      int i46 = RuntimeProfile.foldInt(runtimeAuthority45, 4, i52, i56);
      RuntimeAuthority runtimeAuthority48 = runtimeAuthority18;
      long l57 = l21;
      long l66 = 1000L;
      long l12 = l21;
      long l14 = 500L;
      long l76 = l12 - l14;
      long l68 = Math.max(l66, l76);
      long l49 = RuntimeProfile.foldLong(runtimeAuthority48, 5, l57, l68);
      ChecksConfigurationService checksConfigurationService59 = checksConfigurationService16;
      String string65 = "heuristic-checks.combat-check.phase-b." + "max-h-d";
      double d70 = 18.0;
      double d60 = checksConfigurationService59.getDouble(string65, d70);
      ChecksConfigurationService checksConfigurationService72 = checksConfigurationService16;
      String string75 = "heuristic-checks.combat-check.phase-b." + "max-v-d";
      double d78 = 10.0;
      double d73 = checksConfigurationService72.getDouble(string75, d78);
      return new CombatCheck.CombatThresholds(i44, i46, l49, d60, d73);
   }

   @NotNull
   private CombatCheck.CombatThresholds selectThresholds(@NotNull Player player1, double d2) {
      PlayerCheckData playerCheckData8 = PlayerCheckData.getIfPresent(player1.getUniqueId());
      PlayerCheckData playerCheckData13 = playerCheckData8;
      if (playerCheckData13 != null) {
         PlayerCheckData playerCheckData14 = playerCheckData8;
         double d9 = playerCheckData14.glideBaselineY();
         double d17 = d9;
         if (!Double.isNaN(d17)) {
            double d6 = d9;
            double d22 = d2 - d6;
            double d31 = this.phaseSliceB;
            if (d22 >= d31) {
               return this.altThresholds;
            }
         }
      }

      return this.thresholds;
   }

   public void onGlidePositionUpdate(@NotNull Player player1) {
      if (this.isActiveFor(player1) && player1.isGliding()) {
         this.trackAltitudePhase(player1, null);
      }
   }

   private void trackAltitudePhase(@NotNull Player player1, @Nullable CombatSession combatSession2) {
      PlayerCheckData playerCheckData12 = PlayerCheckData.getIfPresent(player1.getUniqueId());
      PlayerCheckData playerCheckData25 = playerCheckData12;
      if (playerCheckData25 != null) {
         PlayerCheckData playerCheckData26 = playerCheckData12;
         double d13 = playerCheckData26.glideBaselineY();
         double d29 = d13;
         if (!Double.isNaN(d29)) {
            double d36 = player1.getLocation().getY();
            double d6 = d13;
            int i43 = (int)Math.floor(d36 - d6);
            int i64 = this.phaseSliceB;
            boolean flag63;
            if (i43 >= i64) {
               flag63 = true;
            } else {
               flag63 = false;
            }

            boolean flag18 = flag63;
            PlayerCheckData playerCheckData44 = playerCheckData12;
            boolean flag19 = playerCheckData44.combatPhaseBActive();
            CombatSession combatSession20 = combatSession2;
            CombatSession combatSession47 = combatSession20;
            if (combatSession47 == null) {
               CombatSessionManager combatSessionManager49 = this.sessionManager;
               UUID uuid66 = player1.getUniqueId();
               combatSession20 = combatSessionManager49.getSessionIfPresent(uuid66);
            }

            boolean flag51 = flag19;
            if (!flag51) {
               boolean flag52 = flag18;
               if (flag52) {
                  PlayerCheckData playerCheckData53 = playerCheckData12;
                  playerCheckData53.setCombatPhaseBActive(true);
                  CombatSession combatSession54 = combatSession20;
                  if (combatSession54 != null) {
                     CombatSession combatSession55 = combatSession20;
                     long l56 = combatSession55.getWindowStart();
                     long l70 = 0L;
                     if (l56 != l70) {
                        long l21 = System.currentTimeMillis();
                        CombatSession combatSession72 = combatSession20;
                        CombatCheck.CombatThresholds combatThresholds73 = this.altThresholds;
                        long l9 = l21;
                        this.evaluate(player1, combatSession72, combatThresholds73, l9, null);
                     }
                  }

                  return;
               }
            }

            PlayerCheckData playerCheckData62 = playerCheckData12;
            boolean flag69 = flag18;
            playerCheckData62.setCombatPhaseBActive(flag69);
         }
      }
   }

   @Override
   public void register(@NotNull PacketRouter packetRouter1) {
      Client client4 = Client.INTERACT_ENTITY;
      PacketHandler packetHandler6 = this::onAttack;
      packetRouter1.subscribe(client4, packetHandler6);
   }

   private void onAttack(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1)) {
         Player player10 = packetContext1.player();
         Player player19 = player10;
         if (player19.isGliding()) {
            PacketReceiveEvent packetReceiveEvent36 = packetContext1.event();
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity21 = new WrapperPlayClientInteractEntity(packetReceiveEvent36);
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity11 = wrapperPlayClientInteractEntity21;
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity22 = wrapperPlayClientInteractEntity11;
            InteractAction interactAction23 = wrapperPlayClientInteractEntity22.getAction();
            InteractAction interactAction34 = InteractAction.ATTACK;
            if (interactAction23 == interactAction34) {
               WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity24 = wrapperPlayClientInteractEntity11;
               int i12 = wrapperPlayClientInteractEntity24.getEntityId();
               UUID uuid13 = packetContext1.playerId();
               long l14 = packetContext1.timestamp();
               PlatformScheduler platformScheduler33 = this.ctx.scheduler();
               Player player35 = player10;
               Player player5 = player10;
               int i6 = i12;
               UUID uuid7 = uuid13;
               long l8 = l14;
               Runnable runnable38 = () -> {
                  if (player5.isGliding()) {
                     Entity entity13x = this.resolveTarget(player5, i6);
                     Entity entity54 = entity13x;
                     if (!this.isRegionCombatInactive(player5, entity54)) {
                        Entity entity22x = entity13x;
                        if (entity22x != null) {
                           TargetTracker targetTracker25 = this.ctx.targetTracker();
                           Entity entity55 = entity13x;
                           targetTracker25.recordAttack(player5, entity55);
                        } else {
                           this.ctx.targetTracker().recordUnresolvedAttack(player5);
                        }

                        CombatCheck.CombatThresholds combatThresholds14x;
                        CombatSession combatSession15;
                        boolean flag45;
                        label24: {
                           double d58 = player5.getLocation().getY();
                           combatThresholds14x = this.selectThresholds(player5, d58);
                           combatSession15 = this.sessionManager.getSession(uuid7);
                           CombatSession combatSession31 = combatSession15;
                           long l32 = combatSession31.getWindowStart();
                           long l60 = 0L;
                           if (l32 != l60) {
                              CombatSession combatSession62 = combatSession15;
                              long l63 = combatSession62.getWindowStart();
                              long l37 = l8 - l63;
                              CombatCheck.CombatThresholds combatThresholds65 = combatThresholds14x;
                              long l66 = combatThresholds65.winMs;
                              if (l37 <= l66) {
                                 flag45 = false;
                                 break label24;
                              }
                           }

                           flag45 = true;
                        }

                        boolean flag16 = flag45;
                        boolean flag40 = flag16;
                        if (flag40) {
                           CombatSession combatSession41 = combatSession15;
                           combatSession41.begin(l8);
                        }

                        CombatSession combatSession42 = combatSession15;
                        combatSession42.addHit();
                        CombatSession combatSession68 = combatSession15;
                        this.trackAltitudePhase(player5, combatSession68);
                        CombatSession combatSession69 = combatSession15;
                        CombatCheck.CombatThresholds combatThresholds9 = combatThresholds14x;
                        Entity entity12x = entity13x;
                        this.evaluate(player5, combatSession69, combatThresholds9, l8, entity12x);
                     }
                  }
               };
               platformScheduler33.runEntity(player35, runnable38);
            }
         }
      }
   }

   public void onRocketBoost(@NotNull Player player1, long l2) {
      if (this.isActiveFor(player1)) {
         if (player1.isOnline() && player1.isGliding()) {
            UUID uuid13 = player1.getUniqueId();
            CombatSessionManager combatSessionManager35 = this.sessionManager;
            UUID uuid101 = uuid13;
            CombatSession combatSession14 = combatSessionManager35.getSessionIfPresent(uuid101);
            CombatSession combatSession37 = combatSession14;
            if (combatSession37 != null) {
               CombatSession combatSession38 = combatSession14;
               long l39 = combatSession38.getWindowStart();
               long l7 = 0L;
               if (l39 != l7) {
                  Location location15 = player1.getLocation();
                  Location location109 = location15;
                  double d110 = location109.getY();
                  CombatCheck.CombatThresholds combatThresholds16 = this.selectThresholds(player1, d110);
                  CombatSession combatSession112 = combatSession14;
                  long l113 = combatSession112.getWindowStart();
                  long l48 = l2 - l113;
                  CombatCheck.CombatThresholds combatThresholds115 = combatThresholds16;
                  long l116 = combatThresholds115.winMs;
                  if (l48 > l116) {
                     CombatSessionManager combatSessionManager100 = this.sessionManager;
                     UUID uuid108 = uuid13;
                     combatSessionManager100.removePlayer(uuid108);
                     return;
                  }

                  TargetTracker targetTracker53 = this.ctx.targetTracker();
                  UUID uuid103 = uuid13;
                  Entity entity17 = targetTracker53.getTarget(uuid103);
                  if (entity17 == null && targetTracker53.hasUnresolvedTarget(uuid103)) {
                     // packet based NPC: no position to range check
                     if (CombatEnvironment.intersectsCobweb(player1)) {
                        return;
                     }
                     combatSession14.addFirework();
                     this.trackAltitudePhase(player1, combatSession14);
                     this.evaluate(player1, combatSession14, combatThresholds16, l2, null);
                     return;
                  }
                  Entity entity55 = entity17;
                  if (entity55 != null) {
                     Entity entity56 = entity17;
                     if (entity56.isValid()) {
                        Entity entity58 = entity17;
                        Location location18 = entity58.getLocation();
                        Location location60 = location15;
                        if (location60.getWorld() != null) {
                           Location location62 = location15;
                           World world63 = location62.getWorld();
                           Location location104 = location18;
                           World world105 = location104.getWorld();
                           if (!world63.equals(world105)) {
                              return;
                           }
                        }

                        Location location65 = location15;
                        double d66 = location65.getX();
                        Location location118 = location18;
                        double d119 = location118.getX();
                        double d19 = d66 - d119;
                        Location location70 = location15;
                        double d71 = location70.getZ();
                        Location location121 = location18;
                        double d122 = location121.getZ();
                        double d21 = d71 - d122;
                        double d75 = d19;
                        double d124 = d19;
                        double d77 = d75 * d124;
                        double d126 = d21;
                        double d10 = d21;
                        double d128 = d126 * d10;
                        double d23 = d77 + d128;
                        Location location81 = location15;
                        double d82 = location81.getY();
                        Location location130 = location18;
                        double d131 = location130.getY();
                        double d25 = Math.abs(d82 - d131);
                        double d88 = d23;
                        CombatCheck.CombatThresholds combatThresholds133 = combatThresholds16;
                        double d134 = combatThresholds133.maxHSq;
                        if (!(d88 > d134)) {
                           double d91 = d25;
                           CombatCheck.CombatThresholds combatThresholds136 = combatThresholds16;
                           double d137 = combatThresholds136.maxV;
                           if (!(d91 > d137)) {
                              if (CombatEnvironment.intersectsCobweb(player1)) {
                                 return;
                              }

                              CombatSession combatSession96 = combatSession14;
                              combatSession96.addFirework();
                              CombatSession combatSession139 = combatSession14;
                              this.trackAltitudePhase(player1, combatSession139);
                              CombatSession combatSession140 = combatSession14;
                              CombatCheck.CombatThresholds combatThresholds9 = combatThresholds16;
                              Entity entity12 = entity17;
                              this.evaluate(player1, combatSession140, combatThresholds9, l2, entity12);
                              return;
                           }
                        }

                        return;
                     }
                  }

                  return;
               }
            }
         }
      }
   }

   private void evaluate(@NotNull Player player1, @NotNull CombatSession combatSession2, @NotNull CombatCheck.CombatThresholds combatThresholds3, long l4, @Nullable Entity entity6) {
      int i10 = combatSession2.getHitCount();
      int i11 = combatSession2.getFireworkCount();
      int i16 = i10;
      int i31 = combatThresholds3.reqh;
      if (i16 >= i31) {
         int i17 = i11;
         int i33 = combatThresholds3.reqf;
         if (i17 >= i33) {
            Entity entity12;
            label20: {
               entity12 = entity6;
               Entity entity19 = entity12;
               if (entity19 != null) {
                  Entity entity20 = entity12;
                  if (entity20.isValid()) {
                     break label20;
                  }
               }

               TargetTracker targetTracker24 = this.ctx.targetTracker();
               UUID uuid35 = player1.getUniqueId();
               entity12 = targetTracker24.getTarget(uuid35);
            }

            Entity entity26 = entity12;
            if (entity26 == null && this.ctx.targetTracker().hasUnresolvedTarget(player1.getUniqueId())) {
               this.flag(player1);
               long now = System.currentTimeMillis();
               combatSession2.begin(now);
               return;
            }
            if (entity26 != null) {
               Entity entity27 = entity12;
               if (entity27.isValid()) {
                  this.flag(player1);
                  long l37 = System.currentTimeMillis();
                  combatSession2.begin(l37);
                  return;
               }
            }

            return;
         }
      }
   }

   @Override
   public void clearPlayer(@NotNull UUID uuid1) {
      this.sessionManager.removePlayer(uuid1);
   }

   @Override
   public void shutdown() {
      this.sessionManager.clearAll();
   }

   private static final class CombatThresholds {
      final int reqh;
      final int reqf;
      final long winMs;
      final double maxHSq;
      final double maxV;

      CombatThresholds(int i1, int i2, long l3, double d5, double d7) {
         int i26 = Math.max(1, i1);
         this.reqh = i26;
         int i28 = Math.max(1, i2);
         this.reqf = i28;
         long l31 = Math.max(1L, l3);
         this.winMs = l31;
         double d15 = Math.max(1.0, d5);
         double d33 = d15;
         double d44 = d15;
         double d35 = d33 * d44;
         this.maxHSq = d35;
         double d39 = Math.max(0.5, d7);
         this.maxV = d39;
      }
   }
}
