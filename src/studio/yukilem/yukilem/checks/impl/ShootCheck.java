package studio.yukilem.yukilem.checks.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAnimation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.checks.Check;
import studio.yukilem.yukilem.checks.CheckId;
import studio.yukilem.yukilem.checks.CheckMetadata;
import studio.yukilem.yukilem.checks.PlayerCheckData;
import studio.yukilem.yukilem.packet.PacketContext;
import studio.yukilem.yukilem.packet.PacketRouter;
import studio.yukilem.yukilem.packet.TickPacketUtil;
import studio.yukilem.yukilem.packet.TickReliability;
import studio.yukilem.yukilem.packet.handler.PacketHandler;
import studio.yukilem.yukilem.packet.state.MovementUncertaintyState;
import studio.yukilem.yukilem.platform.PlatformScheduler;
import studio.yukilem.yukilem.util.combat.ElytraUseProbe;

@CheckMetadata(id = CheckId.SHOOT, configRoot = "checks.shoot-check")
public final class ShootCheck extends Check {
   private final Map<UUID, ShootCheck.State> stateByPlayer;

   public ShootCheck(@NotNull PluginContext pluginContext1) {
      super(pluginContext1);
      ConcurrentHashMap concurrentHashMap6 = new ConcurrentHashMap();
      this.stateByPlayer = concurrentHashMap6;
   }

   @Override
   public void register(@NotNull PacketRouter packetRouter1) {
      Client client4 = Client.USE_ITEM;
      PacketHandler packetHandler14 = this::onUseItem;
      packetRouter1.subscribe(client4, packetHandler14);
      Client client10 = Client.ANIMATION;
      PacketHandler packetHandler16 = this::onAnimation;
      packetRouter1.subscribe(client10, packetHandler16);
      Client client11 = Client.HELD_ITEM_CHANGE;
      PacketHandler packetHandler18 = this::onHeldItemChange;
      packetRouter1.subscribe(client11, packetHandler18);
      Client client12 = Client.CLICK_WINDOW;
      PacketHandler packetHandler20 = this::onInventoryTransition;
      packetRouter1.subscribe(client12, packetHandler20);
      Client client13 = Client.CLOSE_WINDOW;
      PacketHandler packetHandler22 = this::onInventoryTransition;
      packetRouter1.subscribe(client13, packetHandler22);
   }

   private void onInventoryTransition(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1)) {
         Map map10 = this.stateByPlayer;
         UUID uuid20 = packetContext1.playerId();
         Function function4 = arg0 -> new ShootCheck.State();
         ShootCheck.State state5 = (ShootCheck.State)map10.computeIfAbsent(uuid20, function4);
         ShootCheck.State state13 = state5;
         ShootCheck.State state6 = state13;
         synchronized (state13) {
            ShootCheck.State state14 = state5;
            long l21 = System.nanoTime();
            state14.suppressInventoryUse(l21);
            ShootCheck.State state15 = state6;
         }
      }
   }

   private void onHeldItemChange(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1)) {
         int i5;
         try {
            PacketReceiveEvent packetReceiveEvent29 = packetContext1.event();
            WrapperPlayClientHeldItemChange wrapperPlayClientHeldItemChange10 = new WrapperPlayClientHeldItemChange(packetReceiveEvent29);
            i5 = wrapperPlayClientHeldItemChange10.getSlot();
         } catch (Throwable throwable33) {
            return;
         }

         int i12 = i5;
         if (i12 >= 0) {
            int i13 = i5;
            if (i13 <= 8) {
               Map map15 = this.stateByPlayer;
               UUID uuid27 = packetContext1.playerId();
               Function function30 = arg0 -> new ShootCheck.State();
               ShootCheck.State state6 = (ShootCheck.State)map15.computeIfAbsent(uuid27, function30);
               ShootCheck.State state18 = state6;
               ShootCheck.State state7 = state18;
               synchronized (state18) {
                  ShootCheck.State state19 = state6;
                  int i28 = i5;
                  state19.heldSlot = i28;
                  ShootCheck.State state20 = state7;
                  return;
               }
            }
         }
      }
   }

   private void onUseItem(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1)) {
         Player player7 = packetContext1.player();
         User user20 = packetContext1.user();
         UUID uuid73 = packetContext1.playerId();
         if (isSupportedClient(user20, uuid73)) {
            InteractionHand interactionHand8;
            try {
               PacketReceiveEvent packetReceiveEvent87 = packetContext1.event();
               WrapperPlayClientUseItem wrapperPlayClientUseItem22 = new WrapperPlayClientUseItem(packetReceiveEvent87);
               interactionHand8 = wrapperPlayClientUseItem22.getHand();
            } catch (Throwable throwable108) {
               return;
            }

            InteractionHand interactionHand24 = interactionHand8;
            InteractionHand interactionHand74 = InteractionHand.MAIN_HAND;
            if (interactionHand24 == interactionHand74) {
               Map map26 = this.stateByPlayer;
               UUID uuid76 = packetContext1.playerId();
               Function function88 = arg0 -> new ShootCheck.State();
               ShootCheck.State state9 = (ShootCheck.State)map26.computeIfAbsent(uuid76, function88);
               long l11 = System.nanoTime();
               ShootCheck.State state31 = state9;
               ShootCheck.State state13 = state31;
               int i10;
               synchronized (state31) {
                  ShootCheck.State state32 = state9;
                  long l33 = state32.suppressUseUntilNs;
                  long l89 = l11;
                  if (ShootCheckLogic.inventoryTransitionActive(l33, l89)) {
                     ShootCheck.State state36 = state13;
                     return;
                  }

                  ShootCheck.State state40 = state9;
                  i10 = state40.heldSlot;
                  ShootCheck.State state42 = state13;
               }

               Player player43 = player7;
               if (player43.isGliding()) {
                  PlatformScheduler platformScheduler47 = this.ctx.scheduler();
                  Player player77 = player7;
                  ElytraUseProbe.refreshLookTarget(platformScheduler47, player77);
               }

               Player player48 = player7;
               boolean flag49 = player48.isGliding();
               Player player78 = player7;
               boolean flag79 = ElytraUseProbe.targetsBlockInReach(player78);
               Player player91 = player7;
               InteractionHand interactionHand5 = interactionHand8;
               int i6 = i10;
               boolean flag92 = isFireworkSelected(player91, interactionHand5, i6);
               Player player102 = player7;
               boolean flag98 = this.clientOnGround(player102);
               Player player103 = player7;
               boolean flag104 = player103.isInWater();
               if (ShootCheckLogic.shouldArm(flag49, flag79, flag92, flag98, flag104)) {
                  Player player80 = player7;
                  if (!this.awaitingGlideAck(player80)) {
                     ShootCheck.State state53 = state9;
                     ShootCheck.State state106 = state53;
                     synchronized (state53) {
                        ShootCheck.State state54 = state9;
                        long l55 = state54.suppressUseUntilNs;
                        long l93 = l11;
                        if (ShootCheckLogic.inventoryTransitionActive(l55, l93)) {
                           ShootCheck.State state70 = state106;
                        } else {
                           ShootCheck.State state58 = state9;
                           long l59 = state58.armedUseNs;
                           long l95 = 0L;
                           if (l59 > l95) {
                              ShootCheck.State state62 = state9;
                              state62.clearEvidence();
                           }

                           ShootCheck.State state63 = state9;
                           long l81 = l11;
                           state63.armedUseNs = l81;
                           ShootCheck.State state64 = state9;
                           long l83 = l11;
                           ShootCheck.State state99 = state9;
                           long l100 = state99.lastSwingNs;
                           boolean flag85 = ShootCheckLogic.swingMatchesUse(l83, l100);
                           state64.swingSeen = flag85;
                           ShootCheck.State state65 = state9;
                           state65.boostSeen = false;
                           ShootCheck.State state66 = state106;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void onAnimation(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1)) {
         InteractionHand interactionHand5;
         try {
            PacketReceiveEvent packetReceiveEvent38 = packetContext1.event();
            WrapperPlayClientAnimation wrapperPlayClientAnimation11 = new WrapperPlayClientAnimation(packetReceiveEvent38);
            interactionHand5 = wrapperPlayClientAnimation11.getHand();
         } catch (Throwable throwable45) {
            return;
         }

         InteractionHand interactionHand14 = interactionHand5;
         InteractionHand interactionHand32 = InteractionHand.MAIN_HAND;
         if (interactionHand14 == interactionHand32) {
            long l42 = System.nanoTime();
            Map map18 = this.stateByPlayer;
            UUID uuid34 = packetContext1.playerId();
            Function function39 = arg0 -> new ShootCheck.State();
            ShootCheck.State state7 = (ShootCheck.State)map18.computeIfAbsent(uuid34, function39);
            ShootCheck.State state21 = state7;
            ShootCheck.State state8 = state21;
            synchronized (state21) {
               ShootCheck.State state22 = state7;
               long l35 = l42;
               state22.lastSwingNs = l35;
               ShootCheck.State state23 = state7;
               long l24 = state23.armedUseNs;
               long l40 = l42;
               if (ShootCheckLogic.swingMatchesUse(l24, l40)) {
                  ShootCheck.State state27 = state7;
                  state27.swingSeen = true;
               }

               ShootCheck.State state28 = state8;
            }
         }
      }
   }

   public void onElytraBoost(@NotNull Player player1) {
      if (this.isActiveFor(player1) && player1.isGliding()) {
         Map map16 = this.stateByPlayer;
         UUID uuid33 = player1.getUniqueId();
         ShootCheck.State state7 = (ShootCheck.State)map16.get(uuid33);
         ShootCheck.State state19 = state7;
         if (state19 != null) {
            long l8 = System.nanoTime();
            ShootCheck.State state22 = state7;
            ShootCheck.State state10 = state22;
            synchronized (state22) {
               ShootCheck.State state23 = state7;
               long l24 = state23.armedUseNs;
               long l5 = l8;
               if (ShootCheckLogic.boostMatchesUse(l24, l5)) {
                  ShootCheck.State state27 = state7;
                  state27.boostSeen = true;
               }

               ShootCheck.State state28 = state10;
            }
         }
      }
   }

   public void onGlideTransition(@NotNull Player player1) {
      Map map8 = this.stateByPlayer;
      UUID uuid18 = player1.getUniqueId();
      ShootCheck.State state5 = (ShootCheck.State)map8.get(uuid18);
      ShootCheck.State state11 = state5;
      if (state11 != null) {
         ShootCheck.State state12 = state5;
         ShootCheck.State state6 = state12;
         synchronized (state12) {
            ShootCheck.State state13 = state5;
            state13.resetGlideSession();
            ShootCheck.State state14 = state6;
         }
      }
   }

   public void flushDeferredViolationFlags(@NotNull Player player1, @NotNull User user2, @NotNull UUID uuid3) {
      if (this.isActiveFor(player1)) {
         ShootCheck.State state13 = this.stateByPlayer.get(uuid3);
         ShootCheck.State state24 = state13;
         if (state24 != null) {
            long l14 = System.nanoTime();
            ShootCheck.State state28 = state13;
            ShootCheck.State state17 = state28;
            boolean flag98;
            synchronized (state28) {
               ShootCheck.State state29 = state13;
               long l30 = state29.armedUseNs;
               long l7 = 0L;
               if (l30 <= l7) {
                  ShootCheck.State state33 = state17;
                  return;
               }

               ShootCheck.State state37 = state13;
               long l38 = state37.armedUseNs;
               long l82 = l14;
               if (ShootCheckLogic.isArmExpired(l38, l82)) {
                  ShootCheck.State state68 = state13;
                  state68.clearEvidence();
                  ShootCheck.State state69 = state17;
                  return;
               }

               ShootCheck.State state41 = state13;
               long l42 = state41.armedUseNs;
               long l84 = l14;
               if (!ShootCheckLogic.reconciliationComplete(l42, l84)) {
                  ShootCheck.State state45 = state17;
                  return;
               }

               MovementUncertaintyState movementUncertaintyState94 = PlayerCheckData.get(uuid3).movementUncertaintyState();
               if (!TickReliability.isTickingReliablyFor(player1, user2, uuid3, movementUncertaintyState94)) {
                  ShootCheck.State state66 = state13;
                  state66.clearEvidence();
                  ShootCheck.State state67 = state17;
                  return;
               }

               ShootCheck.State state48 = state13;
               if (state48.swingSeen) {
                  ShootCheck.State state64 = state13;
                  state64.swingCalibrated = true;
                  state64.clearEvidence();
                  ShootCheck.State state65 = state17;
                  return;
               }

               ShootCheck.State state50 = state13;
               if (!state50.boostSeen) {
                  ShootCheck.State state52 = state17;
                  return;
               }

               // Only count a missing swing once this client has been seen swinging on a firework use,
               // otherwise clients that never send the swing packet would be flagged on every rocket.
               if (!state13.swingCalibrated) {
                  state13.disarm();
                  return;
               }

               ShootCheck.State state53 = state13;
               ShootCheck.State state72 = state13;
               int i73 = state72.confirmedMisses;
               ShootCheck.State state87 = state13;
               long l88 = state87.lastConfirmedMissNs;
               long l10 = l14;
               int i74 = ShootCheckLogic.nextConfirmedMissCount(i73, l88, l10);
               state53.confirmedMisses = i74;
               ShootCheck.State state54 = state13;
               long l75 = l14;
               state54.lastConfirmedMissNs = l75;
               ShootCheck.State state90 = state13;
               long l91 = state90.armedUseNs;
               long l95 = l14;
               ShootCheck.State state12 = state13;
               int i97 = state12.confirmedMisses;
               flag98 = ShootCheckLogic.shouldFlag(false, true, l91, l95, i97);
               ShootCheck.State state57 = state13;
               state57.disarm();
               boolean flag58 = flag98;
               if (flag58) {
                  ShootCheck.State state59 = state13;
                  state59.confirmedMisses = 0;
                  ShootCheck.State state60 = state13;
                  long l79 = 0L;
                  state60.lastConfirmedMissNs = l79;
               }

               ShootCheck.State state61 = state17;
            }

            boolean flag62 = flag98;
            if (flag62) {
               this.flag(player1);
            }
         }
      }
   }

   private static boolean isFireworkSelected(@NotNull Player player0, @NotNull InteractionHand interactionHand1, int i2) {
      InteractionHand interactionHand5 = InteractionHand.MAIN_HAND;
      return interactionHand1 == interactionHand5 && i2 >= 0 && i2 <= 8 ? HotbarOrderLogic.isFireworkInHotbarSlot(player0, i2) : HotbarOrderLogic.isFireworkInHand(player0, interactionHand1);
   }

   private static boolean isSupportedClient(@NotNull User user0, @NotNull UUID uuid1) {
      // Bedrock (Floodgate) players do not send vanilla swing packets on item use
      if (uuid1.getMostSignificantBits() == 0L) {
         return false;
      }

      ClientVersion clientVersion5 = TickPacketUtil.resolveClientVersion(user0, uuid1);
      ClientVersion clientVersion7 = ClientVersion.V_1_15;
      return clientVersion5.isNewerThanOrEquals(clientVersion7);
   }

   @Override
   public void seedPlayer(@NotNull UUID uuid1) {
      Map map6 = this.stateByPlayer;
      Function function5 = arg0 -> new ShootCheck.State();
      Object object7 = map6.computeIfAbsent(uuid1, function5);
   }

   @Override
   public void clearPlayer(@NotNull UUID uuid1) {
      Object object6 = this.stateByPlayer.remove(uuid1);
   }

   @Override
   public void shutdown() {
      this.stateByPlayer.clear();
   }

   private static final class State {
      long armedUseNs;
      boolean swingSeen;
      boolean boostSeen;
      boolean swingCalibrated;
      long lastSwingNs;
      long lastConfirmedMissNs;
      long suppressUseUntilNs;
      int confirmedMisses;
      int heldSlot = -1;

      private State() {
      }

      void disarm() {
         long l3 = 0L;
         this.armedUseNs = l3;
         this.swingSeen = false;
         this.boostSeen = false;
      }

      void clearEvidence() {
         this.disarm();
         this.confirmedMisses = 0;
         long l6 = 0L;
         this.lastConfirmedMissNs = l6;
      }

      void suppressInventoryUse(long l1) {
         this.clearEvidence();
         long l11 = this.suppressUseUntilNs;
         long l8 = 250000000L;
         long l15 = l1 + l8;
         long l13 = Math.max(l11, l15);
         this.suppressUseUntilNs = l13;
      }

      void resetGlideSession() {
         this.clearEvidence();
         long l3 = 0L;
         this.lastSwingNs = l3;
         long l7 = 0L;
         this.suppressUseUntilNs = l7;
      }
   }
}
