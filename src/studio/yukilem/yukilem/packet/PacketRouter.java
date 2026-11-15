package studio.glace.glantielytratarget.packet;

import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.packet.handler.PacketHandler;

public final class PacketRouter {
   private static final PacketHandler[] EMPTY = new PacketHandler[0];
   private final Map<PacketTypeCommon, List<PacketHandler>> staging;
   private volatile Map<PacketTypeCommon, PacketHandler[]> dispatch;

   public PacketRouter() {
      HashMap hashMap2 = new HashMap();
      this.staging = hashMap2;
      Map map5 = Map.of();
      this.dispatch = map5;
   }

   public void subscribe(@NotNull PacketTypeCommon packetTypeCommon1, @NotNull PacketHandler packetHandler2) {
      Map map7 = this.staging;
      Function function6 = arg0 -> new ArrayList();
      boolean flag10 = ((List)map7.computeIfAbsent(packetTypeCommon1, function6)).add(packetHandler2);
      this.rebuild();
   }

   public void subscribeAll(@NotNull Iterable<? extends PacketTypeCommon> iterable1, @NotNull PacketHandler packetHandler2) {
      Iterator iterator7 = iterable1.iterator();

      while (true) {
         Iterator iterator10 = iterator7;
         if (!iterator10.hasNext()) {
            this.rebuild();
            return;
         }

         Iterator iterator12 = iterator7;
         PacketTypeCommon packetTypeCommon8 = (PacketTypeCommon)iterator12.next();
         Map map16 = this.staging;
         PacketTypeCommon packetTypeCommon5 = packetTypeCommon8;
         Function function6 = arg0 -> new ArrayList();
         boolean flag19 = ((List)map16.computeIfAbsent(packetTypeCommon5, function6)).add(packetHandler2);
      }
   }

   @Nullable
   public PacketHandler[] handlersFor(@NotNull PacketTypeCommon packetTypeCommon1) {
      return this.dispatch.get(packetTypeCommon1);
   }

   public void clear() {
      this.staging.clear();
      Map map2 = Map.of();
      this.dispatch = map2;
   }

   private void rebuild() {
      int i30 = this.staging.size() * 2;
      HashMap hashMap2 = new HashMap(i30);
      HashMap hashMap5 = hashMap2;
      Iterator iterator6 = this.staging.entrySet().iterator();

      while (true) {
         Iterator iterator13 = iterator6;
         if (!iterator13.hasNext()) {
            HashMap hashMap24 = hashMap5;
            this.dispatch = hashMap24;
            return;
         }

         Iterator iterator16 = iterator6;
         Entry entry7 = (Entry)iterator16.next();
         Entry entry19 = entry7;
         List list8 = (List)entry19.getValue();
         HashMap hashMap22 = hashMap5;
         Entry entry25 = entry7;
         PacketTypeCommon packetTypeCommon27 = (PacketTypeCommon)entry25.getKey();
         List list31 = list8;
         PacketHandler[] packetHandlers35;
         if (list31.isEmpty()) {
            packetHandlers35 = EMPTY;
         } else {
            List list33 = list8;
            PacketHandler[] packetHandlers36 = EMPTY;
            packetHandlers35 = (PacketHandler[])list33.toArray(packetHandlers36);
         }

         Object object23 = hashMap22.put(packetTypeCommon27, packetHandlers35);
      }
   }
}
