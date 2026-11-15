package studio.glace.glantielytratarget.checks.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.checks.Check;
import studio.glace.glantielytratarget.checks.CheckId;
import studio.glace.glantielytratarget.checks.CheckMetadata;
import studio.glace.glantielytratarget.checks.PlayerCheckData;
import studio.glace.glantielytratarget.checks.impl.pivot.PivotSettleCoordinator;
import studio.glace.glantielytratarget.checks.impl.pivot.PivotSettleSession;
import studio.glace.glantielytratarget.config.ChecksConfigurationService;
import studio.glace.glantielytratarget.packet.PacketContext;
import studio.glace.glantielytratarget.packet.PacketRouter;
import studio.glace.glantielytratarget.packet.handler.PacketHandler;
import studio.glace.glantielytratarget.packet.state.TickPacketState;

@CheckMetadata(id = CheckId.PIVOT, configRoot = "checks.pivot-check")
public final class PivotCheck extends Check {
   private static final List<PacketTypeCommon> ROTATION_PACKETS;
   private final Map<UUID, PivotSettleSession> sessions;
   private volatile PivotSettleCoordinator coordinator;

   public PivotCheck(@NotNull PluginContext pluginContext1) {
      super(pluginContext1);
      ConcurrentHashMap concurrentHashMap7 = new ConcurrentHashMap();
      this.sessions = concurrentHashMap7;
      this.reloadThresholds();
   }

   @Override
   public void refreshEnabled() {
      super.refreshEnabled();
      this.reloadThresholds();
   }

   private void reloadThresholds() {
      ChecksConfigurationService checksConfigurationService14 = this.ctx.checksConfig();
      ChecksConfigurationService checksConfigurationService5 = checksConfigurationService14;
      String string23 = "checks.pivot-check." + "min-yaw-d";
      double d7 = 50.0;
      float f22 = (float)checksConfigurationService5.getDouble(string23, d7);
      ChecksConfigurationService checksConfigurationService24 = checksConfigurationService14;
      String string29 = "checks.pivot-check." + "min-pitch-d";
      double d9 = 25.0;
      float f27 = (float)checksConfigurationService24.getDouble(string29, d9);
      ChecksConfigurationService checksConfigurationService30 = checksConfigurationService14;
      String string33 = "checks.pivot-check." + "req";
      int i31 = checksConfigurationService30.getInt(string33, 3);
      ChecksConfigurationService checksConfigurationService34 = checksConfigurationService14;
      String string38 = "checks.pivot-check." + "win-ms";
      long l12 = 15000L;
      long l35 = checksConfigurationService34.getLong(string38, l12);
      PivotSettleCoordinator.Thresholds thresholds4 = new PivotSettleCoordinator.Thresholds(f22, f27, i31, l35);
      PivotSettleCoordinator pivotSettleCoordinator3 = new PivotSettleCoordinator(thresholds4);
      this.coordinator = pivotSettleCoordinator3;
   }

   @Override
   public void register(@NotNull PacketRouter packetRouter1) {
      Client client4 = Client.INTERACT_ENTITY;
      PacketHandler packetHandler8 = this::onAttack;
      packetRouter1.subscribe(client4, packetHandler8);
      List list7 = ROTATION_PACKETS;
      PacketHandler packetHandler10 = this::onRotation;
      packetRouter1.subscribeAll(list7, packetHandler10);
   }

   private void onAttack(@NotNull PacketContext packetContext1) {
      Player player23 = packetContext1.player();
      if (this.isActiveFor(player23) && packetContext1.player().isGliding()) {
         Map map13 = this.sessions;
         UUID uuid25 = packetContext1.playerId();
         Function function4 = arg0 -> new PivotSettleSession();
         PivotSettleSession pivotSettleSession5 = (PivotSettleSession)map13.computeIfAbsent(uuid25, function4);
         PivotSettleSession pivotSettleSession16 = pivotSettleSession5;
         PivotSettleSession pivotSettleSession6 = pivotSettleSession16;
         synchronized (pivotSettleSession16) {
            PivotSettleCoordinator pivotSettleCoordinator18 = this.coordinator;
            PivotSettleSession pivotSettleSession26 = pivotSettleSession5;
            pivotSettleCoordinator18.onAttack(pivotSettleSession26);
            PivotSettleSession pivotSettleSession19 = pivotSettleSession6;
         }
      }
   }

   private void onRotation(@NotNull PacketContext packetContext1) {
      Player player3 = packetContext1.player();
      if (this.isActiveFor(player3)) {
         WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying8;
         try {
            PacketReceiveEvent packetReceiveEvent65 = packetContext1.event();
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying20 = new WrapperPlayClientPlayerFlying(packetReceiveEvent65);
            wrapperPlayClientPlayerFlying8 = wrapperPlayClientPlayerFlying20;
         } catch (Throwable throwable74) {
            return;
         }

         WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying22 = wrapperPlayClientPlayerFlying8;
         if (wrapperPlayClientPlayerFlying22.hasRotationChanged()) {
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying24 = wrapperPlayClientPlayerFlying8;
            float f72 = wrapperPlayClientPlayerFlying24.getLocation().getYaw();
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying27 = wrapperPlayClientPlayerFlying8;
            float f10 = wrapperPlayClientPlayerFlying27.getLocation().getPitch();
            TickPacketState tickPacketState11 = PlayerCheckData.get(packetContext1.playerId()).tickPacketState();
            Map map35 = this.sessions;
            UUID uuid59 = packetContext1.playerId();
            Function function66 = arg0 -> new PivotSettleSession();
            PivotSettleSession pivotSettleSession12 = (PivotSettleSession)map35.computeIfAbsent(uuid59, function66);
            PivotSettleCoordinator pivotSettleCoordinator13 = this.coordinator;
            PivotSettleSession pivotSettleSession40 = pivotSettleSession12;
            PivotSettleSession pivotSettleSession14 = pivotSettleSession40;
            synchronized (pivotSettleSession40) {
               TickPacketState tickPacketState41 = tickPacketState11;
               if (tickPacketState41.isLastPacketWasOnePointSeventeenDuplicate()) {
                  PivotSettleSession pivotSettleSession43 = pivotSettleSession14;
               } else {
                  TickPacketState tickPacketState47 = tickPacketState11;
                  if (tickPacketState47.isLastPacketWasTeleport()) {
                     PivotSettleCoordinator pivotSettleCoordinator55 = pivotSettleCoordinator13;
                     PivotSettleSession pivotSettleSession63 = pivotSettleSession12;
                     pivotSettleCoordinator55.resetTransient(pivotSettleSession63);
                     PivotSettleCoordinator pivotSettleCoordinator56 = pivotSettleCoordinator13;
                     PivotSettleSession pivotSettleSession64 = pivotSettleSession12;
                     float f68 = f72;
                     float f69 = f10;
                     pivotSettleCoordinator56.rebaseline(pivotSettleSession64, f68, f69);
                     PivotSettleSession pivotSettleSession57 = pivotSettleSession14;
                  } else {
                     PivotSettleCoordinator pivotSettleCoordinator49 = pivotSettleCoordinator13;
                     PivotSettleSession pivotSettleSession60 = pivotSettleSession12;
                     float f67 = f72;
                     float f5 = f10;
                     long l70 = packetContext1.timestamp();
                     if ((pivotSettleCoordinator49.onRotation(pivotSettleSession60, f67, f5, l70) & 1) != 0) {
                        PivotSettleSession pivotSettleSession53 = pivotSettleSession12;
                        pivotSettleSession53.pendingPivotFlag = true;
                     }

                     PivotSettleSession pivotSettleSession54 = pivotSettleSession14;
                  }
               }
            }
         }
      }
   }

   public void flushDeferredViolationFlags(@NotNull Player player1, @NotNull User user2, @NotNull UUID uuid3) {
      if (this.isActiveFor(player1)) {
         PivotSettleSession pivotSettleSession7 = this.sessions.get(uuid3);
         PivotSettleSession pivotSettleSession16 = pivotSettleSession7;
         if (pivotSettleSession16 != null) {
            PivotSettleSession pivotSettleSession17 = pivotSettleSession7;
            PivotSettleSession pivotSettleSession9 = pivotSettleSession17;
            synchronized (pivotSettleSession17) {
               PivotSettleSession pivotSettleSession18 = pivotSettleSession7;
               if (!pivotSettleSession18.pendingPivotFlag) {
                  PivotSettleSession pivotSettleSession27 = pivotSettleSession9;
                  return;
               }

               PivotSettleSession pivotSettleSession21 = pivotSettleSession7;
               pivotSettleSession21.pendingPivotFlag = false;
               PivotSettleSession pivotSettleSession22 = pivotSettleSession9;
            }

            this.flag(player1);
         }
      }
   }

   @Override
   public void seedPlayer(@NotNull UUID uuid1) {
      Map map6 = this.sessions;
      Function function5 = arg0 -> new PivotSettleSession();
      Object object7 = map6.computeIfAbsent(uuid1, function5);
   }

   @Override
   public void clearPlayer(@NotNull UUID uuid1) {
      Object object6 = this.sessions.remove(uuid1);
   }

   @Override
   public void shutdown() {
      this.sessions.clear();
   }

   static {
      Client client0 = Client.PLAYER_ROTATION;
      Client client1 = Client.PLAYER_POSITION_AND_ROTATION;
      ROTATION_PACKETS = List.of(client0, client1);
   }
}
