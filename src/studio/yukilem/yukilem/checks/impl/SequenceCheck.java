package studio.glace.glantielytratarget.checks.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.checks.Check;
import studio.glace.glantielytratarget.checks.CheckId;
import studio.glace.glantielytratarget.checks.CheckMetadata;
import studio.glace.glantielytratarget.checks.PlayerCheckData;
import studio.glace.glantielytratarget.packet.PacketContext;
import studio.glace.glantielytratarget.packet.PacketRouter;
import studio.glace.glantielytratarget.packet.TickPacketUtil;
import studio.glace.glantielytratarget.packet.TickReliability;
import studio.glace.glantielytratarget.packet.handler.PacketHandler;
import studio.glace.glantielytratarget.packet.state.MovementUncertaintyState;
import studio.glace.glantielytratarget.platform.PlatformScheduler;
import studio.glace.glantielytratarget.util.combat.ElytraUseProbe;

@CheckMetadata(id = CheckId.SEQUENCE, configRoot = "checks.sequence-check")
public final class SequenceCheck extends Check {
   private final Map<UUID, SequenceCheck.State> stateByPlayer;

   public SequenceCheck(@NotNull PluginContext pluginContext1) {
      super(pluginContext1);
      ConcurrentHashMap concurrentHashMap6 = new ConcurrentHashMap();
      this.stateByPlayer = concurrentHashMap6;
   }

   @Override
   public void register(@NotNull PacketRouter packetRouter1) {
      Client client4 = Client.USE_ITEM;
      PacketHandler packetHandler10 = this::onUseItem;
      packetRouter1.subscribe(client4, packetHandler10);
      Client client8 = Client.HELD_ITEM_CHANGE;
      PacketHandler packetHandler12 = this::onHeldItemChange;
      packetRouter1.subscribe(client8, packetHandler12);
      Client client9 = Client.TELEPORT_CONFIRM;
      PacketHandler packetHandler14 = this::onTeleportConfirm;
      packetRouter1.subscribe(client9, packetHandler14);
   }

   private void onTeleportConfirm(@NotNull PacketContext packetContext1) {
      Map map7 = this.stateByPlayer;
      UUID uuid18 = packetContext1.playerId();
      SequenceCheck.State state4 = (SequenceCheck.State)map7.get(uuid18);
      SequenceCheck.State state10 = state4;
      if (state10 != null) {
         SequenceCheck.State state11 = state4;
         SequenceCheck.State state5 = state11;
         synchronized (state11) {
            SequenceCheck.State state12 = state4;
            state12.disarm();
            SequenceCheck.State state13 = state4;
            long l19 = 0L;
            state13.swapNs = l19;
            SequenceCheck.State state14 = state5;
         }
      }
   }

   private void onUseItem(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1)) {
         Player player7 = packetContext1.player();

         InteractionHand interactionHand8;
         try {
            PacketReceiveEvent packetReceiveEvent59 = packetContext1.event();
            WrapperPlayClientUseItem wrapperPlayClientUseItem17 = new WrapperPlayClientUseItem(packetReceiveEvent59);
            interactionHand8 = wrapperPlayClientUseItem17.getHand();
         } catch (Throwable throwable70) {
            return;
         }

         Map map21 = this.stateByPlayer;
         UUID uuid51 = packetContext1.playerId();
         Function function60 = arg0 -> new SequenceCheck.State();
         SequenceCheck.State state65 = (SequenceCheck.State)map21.computeIfAbsent(uuid51, function60);
         SequenceCheck.State state24 = state65;
         SequenceCheck.State state11 = state24;
         int i10;
         synchronized (state24) {
            SequenceCheck.State state25 = state65;
            i10 = state25.heldSlot;
            SequenceCheck.State state27 = state11;
         }

         Player player31 = player7;
         if (player31.isGliding()) {
            PlatformScheduler platformScheduler35 = this.ctx.scheduler();
            Player player52 = player7;
            ElytraUseProbe.refreshLookTarget(platformScheduler35, player52);
         }

         Player player36 = player7;
         boolean flag37 = player36.isGliding();
         Player player53 = player7;
         boolean flag54 = ElytraUseProbe.targetsBlockInReach(player53);
         Player player61 = player7;
         InteractionHand interactionHand5 = interactionHand8;
         int i6 = i10;
         boolean flag62 = isFireworkSelected(player61, interactionHand5, i6);
         if (SequenceLogic.shouldArm(flag37, flag54, flag62)) {
            int i66 = flyingSeq(packetContext1.playerId());
            SequenceCheck.State state42 = state65;
            SequenceCheck.State state67 = state42;
            synchronized (state42) {
               SequenceCheck.State state43 = state65;
               int i55 = i66;
               state43.armedSeq = i55;
               SequenceCheck.State state44 = state65;
               long l56 = System.nanoTime();
               state44.armedNs = l56;
               SequenceCheck.State state45 = state67;
            }

            Player player58 = player7;
            int i63 = i66;
            String string64 = "armed on rocket USE_ITEM seq=" + i63;
            this.debug(player58, string64);
         }
      }
   }

   private void onHeldItemChange(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1)) {
         Player player8 = packetContext1.player();

         int i9;
         try {
            PacketReceiveEvent packetReceiveEvent71 = packetContext1.event();
            WrapperPlayClientHeldItemChange wrapperPlayClientHeldItemChange20 = new WrapperPlayClientHeldItemChange(packetReceiveEvent71);
            i9 = wrapperPlayClientHeldItemChange20.getSlot();
         } catch (Throwable throwable89) {
            return;
         }

         long l87 = System.nanoTime();
         int i11 = flyingSeq(packetContext1.playerId());
         Map map29 = this.stateByPlayer;
         UUID uuid63 = packetContext1.playerId();
         Function function72 = arg0 -> new SequenceCheck.State();
         SequenceCheck.State state12 = (SequenceCheck.State)map29.computeIfAbsent(uuid63, function72);
         boolean flag13 = false;
         boolean flag14 = false;
         SequenceCheck.State state34 = state12;
         SequenceCheck.State state15 = state34;
         synchronized (state34) {
            Player player35 = player8;
            if (player35.isGliding()) {
               SequenceCheck.State state37 = state12;
               int i38 = state37.armedSeq;
               int i64 = i11;
               SequenceCheck.State state73 = state12;
               long l74 = state73.armedNs;
               long l6 = l87;
               if (SequenceLogic.swapCompletesPair(i38, i64, l74, l6)) {
                  SequenceCheck.State state40 = state12;
                  long l65 = l87;
                  state40.swapNs = l65;
                  flag13 = true;
                  SequenceCheck.State state42 = state12;
                  long l43 = state42.swapNs;
                  SequenceCheck.State state76 = state12;
                  long l77 = state76.boostNs;
                  flag14 = SequenceLogic.pairsWithBoost(l43, l77);
               }
            }

            SequenceCheck.State state46 = state12;
            long l47 = state46.armedNs;
            long l79 = l87;
            if (SequenceLogic.isArmExpired(l47, l79)) {
               SequenceCheck.State state50 = state12;
               state50.disarm();
            }

            int i51 = i9;
            if (i51 >= 0) {
               int i52 = i9;
               if (i52 <= 8) {
                  SequenceCheck.State state53 = state12;
                  int i68 = i9;
                  state53.heldSlot = i68;
               }
            }

            SequenceCheck.State state54 = state15;
         }

         boolean flag55 = flag13;
         if (flag55) {
            Player player69 = player8;
            boolean flag81 = flag14;
            String string82 = "HELD_ITEM_CHANGE inside same-tick use window, paired=" + flag81;
            this.debug(player69, string82);
         }

         boolean flag57 = flag14;
         if (flag57) {
            Player player70 = player8;
            User user84 = packetContext1.user();
            UUID uuid85 = packetContext1.playerId();
            SequenceCheck.State state86 = state12;
            this.deliverFlag(player70, user84, uuid85, state86);
         }
      }
   }

   public void onElytraBoost(@NotNull Player player1) {
      if (this.isActiveFor(player1)) {
         UUID uuid8 = player1.getUniqueId();
         Map map17 = this.stateByPlayer;
         UUID uuid35 = uuid8;
         Function function5 = arg0 -> new SequenceCheck.State();
         SequenceCheck.State state9 = (SequenceCheck.State)map17.computeIfAbsent(uuid35, function5);
         SequenceCheck.State state20 = state9;
         SequenceCheck.State state11 = state20;
         boolean flag10;
         synchronized (state20) {
            SequenceCheck.State state21 = state9;
            long l36 = System.nanoTime();
            state21.boostNs = l36;
            SequenceCheck.State state22 = state9;
            long l23 = state22.swapNs;
            SequenceCheck.State state39 = state9;
            long l40 = state39.boostNs;
            flag10 = SequenceLogic.pairsWithBoost(l23, l40);
            SequenceCheck.State state26 = state11;
         }

         boolean flag30 = flag10;
         if (flag30) {
            User user43 = resolveUser(player1);
            User user33 = user43;
            if (user33 != null) {
               User user42 = user43;
               UUID uuid6 = uuid8;
               SequenceCheck.State state7 = state9;
               this.deliverFlag(player1, user42, uuid6, state7);
            }
         }
      }
   }

   private void deliverFlag(@NotNull Player player1, @NotNull User user2, @NotNull UUID uuid3, @NotNull SequenceCheck.State state4) {
      if (TickPacketUtil.canSkipTicks(user2, uuid3)) {
         synchronized (state4) {
            state4.pendingFlag = true;
         }

         this.debug(player1, "flag deferred until reliable tick");
      } else {
         synchronized (state4) {
            state4.reset();
         }

         this.debug(player1, "FLAG (instant)");
         this.flag(player1);
      }
   }

   public void flushDeferredViolationFlags(@NotNull Player player1, @NotNull User user2, @NotNull UUID uuid3) {
      if (this.isActiveFor(player1)) {
         SequenceCheck.State state9 = this.stateByPlayer.get(uuid3);
         SequenceCheck.State state18 = state9;
         if (state18 != null) {
            SequenceCheck.State state19 = state9;
            SequenceCheck.State state11 = state19;
            boolean flag10;
            synchronized (state19) {
               SequenceCheck.State state20 = state9;
               if (!state20.pendingFlag) {
                  SequenceCheck.State state22 = state11;
                  return;
               }

               SequenceCheck.State state26 = state9;
               state26.pendingFlag = false;
               MovementUncertaintyState movementUncertaintyState42 = PlayerCheckData.get(uuid3).movementUncertaintyState();
               flag10 = TickReliability.isTickingReliablyFor(player1, user2, uuid3, movementUncertaintyState42);
               boolean flag29 = flag10;
               if (flag29) {
                  SequenceCheck.State state30 = state9;
                  state30.reset();
               }

               SequenceCheck.State state31 = state11;
            }

            boolean flag32 = flag10;
            if (flag32) {
               this.debug(player1, "FLAG (deferred)");
               this.flag(player1);
            }
         }
      }
   }

   private static boolean isFireworkSelected(@NotNull Player player0, @NotNull InteractionHand interactionHand1, int i2) {
      InteractionHand interactionHand5 = InteractionHand.MAIN_HAND;
      return interactionHand1 == interactionHand5 && i2 >= 0 && i2 <= 8 ? HotbarOrderLogic.isFireworkInHotbarSlot(player0, i2) : HotbarOrderLogic.isFireworkInHand(player0, interactionHand1);
   }

   private static int flyingSeq(@NotNull UUID uuid0) {
      return PlayerCheckData.get(uuid0).movementUncertaintyState().getClientFlyingSeq();
   }

   @Nullable
   private static User resolveUser(@NotNull Player player0) {
      try {
         return PacketEvents.getAPI().getPlayerManager().getUser(player0);
      } catch (Throwable throwable8) {
         return null;
      }
   }

   private void debug(@NotNull Player player1, @NotNull String string2) {
      if (this.ctx.config().isDebugEnabled()) {
         Logger logger13 = this.ctx.plugin().getLogger();
         String string15 = "[Sequence/" + player1.getName() + "] " + string2;
         logger13.info(string15);
      }
   }

   @Override
   public void seedPlayer(@NotNull UUID uuid1) {
      Map map6 = this.stateByPlayer;
      Function function5 = arg0 -> new SequenceCheck.State();
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
      int heldSlot = -1;
      int armedSeq = -1;
      long armedNs;
      long swapNs;
      long boostNs;
      boolean pendingFlag;

      private State() {
      }

      void disarm() {
         this.armedSeq = -1;
         long l5 = 0L;
         this.armedNs = l5;
      }

      void reset() {
         this.disarm();
         long l3 = 0L;
         this.swapNs = l3;
         long l8 = 0L;
         this.boostNs = l8;
         this.pendingFlag = false;
      }
   }
}
