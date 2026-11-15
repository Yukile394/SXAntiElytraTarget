package studio.yukilem.yukilem.checks.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;
import java.util.List;
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
import studio.yukilem.yukilem.packet.RotationTracker;
import studio.yukilem.yukilem.packet.TickPacketUtil;
import studio.yukilem.yukilem.packet.handler.PacketHandler;
import studio.yukilem.yukilem.packet.state.RotationState;
import studio.yukilem.yukilem.packet.state.TickPacketState;

@CheckMetadata(id = CheckId.CONSISTENCY, configRoot = "checks.consistency-check")
public final class ConsistencyCheck extends Check {
   private static final List<PacketTypeCommon> TICK_PACKETS;
   private static final int MAX_TRACKED_ROTATIONS = 16;
   private final Map<UUID, ConsistencyCheck.Session> sessions;
   private Boolean serverSupportsCache;

   public ConsistencyCheck(@NotNull PluginContext pluginContext1) {
      super(pluginContext1);
      ConcurrentHashMap concurrentHashMap6 = new ConcurrentHashMap();
      this.sessions = concurrentHashMap6;
   }

   @Override
   public void register(@NotNull PacketRouter packetRouter1) {
      Client client4 = Client.USE_ITEM;
      PacketHandler packetHandler18 = this::onUseItem;
      packetRouter1.subscribe(client4, packetHandler18);
      Client client12 = Client.HELD_ITEM_CHANGE;
      PacketHandler packetHandler20 = this::onHeldItemChange;
      packetRouter1.subscribe(client12, packetHandler20);
      Client client13 = Client.CLICK_WINDOW;
      PacketHandler packetHandler22 = this::onInventoryTransition;
      packetRouter1.subscribe(client13, packetHandler22);
      Client client14 = Client.CLOSE_WINDOW;
      PacketHandler packetHandler24 = this::onInventoryTransition;
      packetRouter1.subscribe(client14, packetHandler24);
      Client client15 = Client.PLAYER_DIGGING;
      PacketHandler packetHandler26 = this::onPlayerDigging;
      packetRouter1.subscribe(client15, packetHandler26);
      Client client16 = Client.ENTITY_ACTION;
      PacketHandler packetHandler28 = this::onEntityAction;
      packetRouter1.subscribe(client16, packetHandler28);
      List list17 = TICK_PACKETS;
      PacketHandler packetHandler30 = this::onTickPacket;
      packetRouter1.subscribeAll(list17, packetHandler30);
   }

   private boolean serverSupports() {
      Boolean booleanValue4 = this.serverSupportsCache;
      Boolean booleanValue7 = booleanValue4;
      if (booleanValue7 != null) {
         Boolean booleanValue14 = booleanValue4;
         return booleanValue14;
      } else {
         ServerVersion serverVersion10 = PacketEvents.getAPI().getServerManager().getVersion();
         ServerVersion serverVersion3 = ServerVersion.V_1_21;
         boolean flag5 = serverVersion10.isNewerThanOrEquals(serverVersion3);
         boolean flag16 = flag5;
         Boolean booleanValue17 = flag16;
         this.serverSupportsCache = booleanValue17;
         return flag5;
      }
   }

   private void onUseItem(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1) && this.serverSupports()) {
         User user17 = packetContext1.user();
         UUID uuid43 = packetContext1.playerId();
         ClientVersion clientVersion18 = TickPacketUtil.resolveClientVersion(user17, uuid43);
         ClientVersion clientVersion44 = ClientVersion.V_1_21;
         boolean flag6 = clientVersion18.isNewerThanOrEquals(clientVersion44);
         boolean flag22 = packetContext1.player().isGliding();
         boolean flag45 = flag6;
         if (ConsistencyLogic.shouldTrackUseItem(flag22, flag45)) {
            PacketReceiveEvent packetReceiveEvent53 = packetContext1.event();
            WrapperPlayClientUseItem wrapperPlayClientUseItem24 = new WrapperPlayClientUseItem(packetReceiveEvent53);
            WrapperPlayClientUseItem wrapperPlayClientUseItem7 = wrapperPlayClientUseItem24;
            Map map26 = this.sessions;
            UUID uuid47 = packetContext1.playerId();
            Function function54 = arg0 -> new ConsistencyCheck.Session();
            ConsistencyCheck.Session session8 = (ConsistencyCheck.Session)map26.computeIfAbsent(uuid47, function54);
            int i9 = flyingSeq(packetContext1.playerId());
            ConsistencyCheck.Session session32 = session8;
            ConsistencyCheck.Session session10 = session32;
            synchronized (session32) {
               int i33 = i9;
               ConsistencyCheck.Session session48 = session8;
               int i49 = session48.resumeUseAtSeq;
               if (ConsistencyLogic.shouldIgnoreTransitionUse(i33, i49)) {
                  ConsistencyCheck.Session session35 = session10;
               } else {
                  ConsistencyCheck.Session session39 = session8;
                  session39.resumeUseAtSeq = -1;
                  ConsistencyCheck.Session session40 = session8;
                  WrapperPlayClientUseItem wrapperPlayClientUseItem51 = wrapperPlayClientUseItem7;
                  float f52 = wrapperPlayClientUseItem51.getYaw();
                  WrapperPlayClientUseItem wrapperPlayClientUseItem55 = wrapperPlayClientUseItem7;
                  float f56 = wrapperPlayClientUseItem55.getPitch();
                  session40.add(f52, f56);
                  ConsistencyCheck.Session session41 = session10;
               }
            }
         }
      }
   }

   private void onHeldItemChange(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1) && this.serverSupports()) {
         int i5;
         try {
            PacketReceiveEvent packetReceiveEvent39 = packetContext1.event();
            WrapperPlayClientHeldItemChange wrapperPlayClientHeldItemChange12 = new WrapperPlayClientHeldItemChange(packetReceiveEvent39);
            i5 = wrapperPlayClientHeldItemChange12.getSlot();
         } catch (Throwable throwable43) {
            return;
         }

         int i14 = i5;
         if (i14 >= 0) {
            int i15 = i5;
            if (i15 <= 8) {
               Map map17 = this.sessions;
               UUID uuid33 = packetContext1.playerId();
               Function function40 = arg0 -> new ConsistencyCheck.Session();
               ConsistencyCheck.Session session6 = (ConsistencyCheck.Session)map17.computeIfAbsent(uuid33, function40);
               ConsistencyCheck.Session session20 = session6;
               ConsistencyCheck.Session session7 = session20;
               synchronized (session20) {
                  ConsistencyCheck.Session session21 = session6;
                  int i22 = session21.heldSlot;
                  int i34 = i5;
                  if (i22 == i34) {
                     ConsistencyCheck.Session session23 = session7;
                     return;
                  }

                  ConsistencyCheck.Session session27 = session6;
                  int i35 = i5;
                  session27.heldSlot = i35;
                  ConsistencyCheck.Session session28 = session6;
                  int i38 = flyingSeq(packetContext1.playerId());
                  session28.resetForTransition(i38);
                  ConsistencyCheck.Session session29 = session7;
                  return;
               }
            }
         }
      }
   }

   private void onInventoryTransition(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1) && this.serverSupports()) {
         Map map12 = this.sessions;
         UUID uuid22 = packetContext1.playerId();
         Function function4 = arg0 -> new ConsistencyCheck.Session();
         ConsistencyCheck.Session session5 = (ConsistencyCheck.Session)map12.computeIfAbsent(uuid22, function4);
         ConsistencyCheck.Session session15 = session5;
         ConsistencyCheck.Session session6 = session15;
         synchronized (session15) {
            ConsistencyCheck.Session session16 = session5;
            int i25 = flyingSeq(packetContext1.playerId());
            session16.resetForTransition(i25);
            ConsistencyCheck.Session session17 = session6;
         }
      }
   }

   private void onPlayerDigging(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1) && this.serverSupports()) {
         DiggingAction diggingAction5;
         try {
            PacketReceiveEvent packetReceiveEvent33 = packetContext1.event();
            WrapperPlayClientPlayerDigging wrapperPlayClientPlayerDigging12 = new WrapperPlayClientPlayerDigging(packetReceiveEvent33);
            diggingAction5 = wrapperPlayClientPlayerDigging12.getAction();
         } catch (Throwable throwable36) {
            return;
         }

         DiggingAction diggingAction14 = diggingAction5;
         DiggingAction diggingAction27 = DiggingAction.RELEASE_USE_ITEM;
         if (diggingAction14 == diggingAction27) {
            Map map16 = this.sessions;
            UUID uuid29 = packetContext1.playerId();
            ConsistencyCheck.Session session6 = (ConsistencyCheck.Session)map16.get(uuid29);
            ConsistencyCheck.Session session19 = session6;
            if (session19 != null) {
               ConsistencyCheck.Session session20 = session6;
               ConsistencyCheck.Session session7 = session20;
               synchronized (session20) {
                  ConsistencyCheck.Session session21 = session6;
                  int i32 = flyingSeq(packetContext1.playerId());
                  session21.resetForTransition(i32);
                  ConsistencyCheck.Session session22 = session7;
               }
            }
         }
      }
   }

   private void onEntityAction(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1) && this.serverSupports()) {
         Action action5;
         try {
            PacketReceiveEvent packetReceiveEvent33 = packetContext1.event();
            WrapperPlayClientEntityAction wrapperPlayClientEntityAction12 = new WrapperPlayClientEntityAction(packetReceiveEvent33);
            action5 = wrapperPlayClientEntityAction12.getAction();
         } catch (Throwable throwable36) {
            return;
         }

         Action action14 = action5;
         Action action27 = Action.START_FLYING_WITH_ELYTRA;
         if (action14 == action27) {
            Map map16 = this.sessions;
            UUID uuid29 = packetContext1.playerId();
            ConsistencyCheck.Session session6 = (ConsistencyCheck.Session)map16.get(uuid29);
            ConsistencyCheck.Session session19 = session6;
            if (session19 != null) {
               ConsistencyCheck.Session session20 = session6;
               ConsistencyCheck.Session session7 = session20;
               synchronized (session20) {
                  ConsistencyCheck.Session session21 = session6;
                  int i32 = flyingSeq(packetContext1.playerId());
                  session21.resetForTransition(i32);
                  ConsistencyCheck.Session session22 = session7;
               }
            }
         }
      }
   }

   private void onTickPacket(@NotNull PacketContext packetContext1) {
      if (!this.isInactivePacket(packetContext1) && this.serverSupports()) {
         User user7 = packetContext1.user();
         UUID uuid8 = packetContext1.playerId();
         User user30 = user7;
         UUID uuid99 = uuid8;
         ClientVersion clientVersion31 = TickPacketUtil.resolveClientVersion(user30, uuid99);
         ClientVersion clientVersion100 = ClientVersion.V_1_21;
         if (clientVersion31.isNewerThanOrEquals(clientVersion100)) {
            UUID uuid33 = uuid8;
            PlayerCheckData playerCheckData9 = PlayerCheckData.get(uuid33);
            PlayerCheckData playerCheckData35 = playerCheckData9;
            RotationState rotationState10 = playerCheckData35.rotationState();
            PlayerCheckData playerCheckData37 = playerCheckData9;
            TickPacketState tickPacketState11 = playerCheckData37.tickPacketState();
            PacketTypeCommon packetTypeCommon12 = packetContext1.event().getPacketType();
            PacketTypeCommon packetTypeCommon42 = packetTypeCommon12;
            if (WrapperPlayClientPlayerFlying.isFlying(packetTypeCommon42)) {
               PacketReceiveEvent packetReceiveEvent45 = packetContext1.event();
               RotationState rotationState101 = rotationState10;
               TickPacketState tickPacketState4 = tickPacketState11;
               RotationTracker.applyFlying(packetReceiveEvent45, rotationState101, tickPacketState4);
            }

            Map map47 = this.sessions;
            UUID uuid102 = uuid8;
            ConsistencyCheck.Session session13 = (ConsistencyCheck.Session)map47.get(uuid102);
            ConsistencyCheck.Session session50 = session13;
            if (session50 != null) {
               TickPacketState tickPacketState51 = tickPacketState11;
               if (tickPacketState51.isLastPacketWasTeleport()) {
                  ConsistencyCheck.Session session53 = session13;
                  ConsistencyCheck.Session session14 = session53;
                  synchronized (session53) {
                     ConsistencyCheck.Session session54 = session13;
                     UUID uuid103 = uuid8;
                     int i104 = flyingSeq(uuid103);
                     session54.resetForTransition(i104);
                     ConsistencyCheck.Session session55 = session14;
                  }
               } else {
                  PacketTypeCommon packetTypeCommon59 = packetTypeCommon12;
                  User user105 = user7;
                  UUID uuid118 = uuid8;
                  TickPacketState tickPacketState5 = tickPacketState11;
                  if (TickPacketUtil.isTickPacket(packetTypeCommon59, user105, uuid118, tickPacketState5)) {
                     Player player127 = packetContext1.player();
                     Player player63 = player127;
                     boolean flag128 = player63.isGliding();
                     boolean flag16 = false;
                     ConsistencyCheck.Session session66 = session13;
                     ConsistencyCheck.Session session17 = session66;
                     synchronized (session66) {
                        boolean flag67 = flag128;
                        if (!flag67) {
                           ConsistencyCheck.Session session96 = session13;
                           session96.count = 0;
                           ConsistencyCheck.Session session97 = session17;
                           return;
                        }

                        ConsistencyCheck.Session session68 = session13;
                        if (session68.count == 0) {
                           ConsistencyCheck.Session session70 = session17;
                           return;
                        }

                        boolean flag98;
                        label75: {
                           label74: {
                              User user74 = user7;
                              UUID uuid106 = uuid8;
                              if (TickPacketUtil.canSkipTicks(user74, uuid106)) {
                                 PacketTypeCommon packetTypeCommon76 = packetTypeCommon12;
                                 Client client107 = Client.PLAYER_POSITION_AND_ROTATION;
                                 if (packetTypeCommon76 == client107) {
                                    break label74;
                                 }

                                 PacketTypeCommon packetTypeCommon77 = packetTypeCommon12;
                                 Client client108 = Client.PLAYER_ROTATION;
                                 if (packetTypeCommon77 == client108) {
                                    break label74;
                                 }
                              }

                              flag98 = false;
                              break label75;
                           }

                           flag98 = true;
                        }

                        boolean flag18 = flag98;
                        int i19 = 0;

                        while (true) {
                           int i79 = i19;
                           ConsistencyCheck.Session session109 = session13;
                           int i110 = session109.count;
                           if (i79 >= i110) {
                              ConsistencyCheck.Session session92 = session13;
                              session92.count = 0;
                              ConsistencyCheck.Session session93 = session17;
                              break;
                           }

                           ConsistencyCheck.Session session80 = session13;
                           float[] fs81 = session80.yaw;
                           int i111 = i19;
                           float f20 = fs81[i111];
                           ConsistencyCheck.Session session83 = session13;
                           float[] fs84 = session83.pitch;
                           int i112 = i19;
                           float f21 = fs84[i112];
                           float f86 = f20;
                           float f113 = f21;
                           RotationState rotationState119 = rotationState10;
                           float f120 = rotationState119.getYaw();
                           RotationState rotationState123 = rotationState10;
                           float f124 = rotationState123.getPitch();
                           if (ConsistencyLogic.matchesCurrent(f86, f113, f120, f124)) {
                              flag18 = false;
                           } else {
                              float f89 = f20;
                              float f114 = f21;
                              RotationState rotationState121 = rotationState10;
                              float f122 = rotationState121.getLastYaw();
                              RotationState rotationState125 = rotationState10;
                              float f126 = rotationState125.getLastPitch();
                              boolean flag6 = flag18;
                              if (!ConsistencyLogic.matchesPrevious(f89, f114, f122, f126, flag6)) {
                                 flag16 = true;
                              }
                           }

                           i19++;
                        }
                     }

                     boolean flag94 = flag16;
                     if (flag94) {
                        Player player116 = player127;
                        this.flag(player116);
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void seedPlayer(@NotNull UUID uuid1) {
      Map map6 = this.sessions;
      Function function5 = arg0 -> new ConsistencyCheck.Session();
      Object object7 = map6.computeIfAbsent(uuid1, function5);
   }

   @Override
   public void clearPlayer(@NotNull UUID uuid1) {
      Object object6 = this.sessions.remove(uuid1);
   }

   public void onPacketContinuityRestored(@NotNull UUID uuid1) {
      Map map9 = this.sessions;
      Function function5 = arg0 -> new ConsistencyCheck.Session();
      ConsistencyCheck.Session session6 = (ConsistencyCheck.Session)map9.computeIfAbsent(uuid1, function5);
      ConsistencyCheck.Session session12 = session6;
      ConsistencyCheck.Session session7 = session12;
      synchronized (session12) {
         ConsistencyCheck.Session session13 = session6;
         int i19 = flyingSeq(uuid1);
         session13.resetForTransition(i19);
         ConsistencyCheck.Session session14 = session7;
      }
   }

   @Override
   public void shutdown() {
      this.sessions.clear();
   }

   private static int flyingSeq(@NotNull UUID uuid0) {
      return PlayerCheckData.get(uuid0).movementUncertaintyState().getClientFlyingSeq();
   }

   static {
      Client client0 = Client.CLIENT_TICK_END;
      Client client1 = Client.PLAYER_FLYING;
      Client client2 = Client.PLAYER_POSITION;
      Client client3 = Client.PLAYER_ROTATION;
      Client client4 = Client.PLAYER_POSITION_AND_ROTATION;
      TICK_PACKETS = List.of(client0, client1, client2, client3, client4);
   }

   private static final class Session {
      final float[] yaw;
      final float[] pitch;
      int count;
      int heldSlot;
      int resumeUseAtSeq;

      private Session() {
         float[] local7 = new float[16];
         this.yaw = local7;
         float[] fs9 = new float[16];
         this.pitch = fs9;
         this.heldSlot = -1;
         this.resumeUseAtSeq = -1;
      }

      void add(float local1, float f2) {
         if (this.count < 16) {
            float[] fs9 = this.yaw;
            int i14 = this.count;
            fs9[i14] = local1;
            float[] fs11 = this.pitch;
            int i16 = this.count;
            fs11[i16] = f2;
            int i18 = this.count + 1;
            this.count = i18;
         }
      }

      void resetForTransition(int local1) {
         this.count = 0;
         int local7 = ConsistencyLogic.resumeUseAtSeq(local1);
         this.resumeUseAtSeq = local7;
      }
   }
}
