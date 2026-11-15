package studio.yukilem.yukilem.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.libs.nixlib.items.ConfigItem;

public final class MenuItemFactory {
   private MenuItemFactory() {
   }

   @NotNull
   public static ItemStack build(@NotNull Map<String, Object> map0, @NotNull Map<String, String> map1) {
      UnaryOperator unaryOperator6 = transformer(map1);
      return ConfigItem.build(map0, unaryOperator6);
   }

   @NotNull
   public static UnaryOperator<String> transformer(@NotNull Map<String, String> map0) {
      return arg1 -> {
         if (arg1 != null && !arg1.isEmpty()) {
            String string5 = arg1;
            Iterator iterator6 = map0.entrySet().iterator();

            while (true) {
               Iterator iterator14 = iterator6;
               if (!iterator14.hasNext()) {
                  return string5;
               }

               Iterator iterator16 = iterator6;
               Entry entry7 = (Entry)iterator16.next();
               String string19 = string5;
               Entry entry3 = entry7;
               CharSequence charSequence24 = (CharSequence)entry3.getKey();
               Entry entry4 = entry7;
               CharSequence charSequence26 = (CharSequence)entry4.getValue();
               string5 = string19.replace(charSequence24, charSequence26);
            }
         } else {
            return arg1;
         }
      };
   }

   @NotNull
   public static List<Integer> slots(@Nullable Map<String, Object> map0) {
      ArrayList arrayList3 = new ArrayList();
      if (map0 == null) {
         return arrayList3;
      }

      Object object4 = map0.get("slots");
      Object object13 = object4;
      if (object13 instanceof List) {
         Object object15 = object4;
         Iterator iterator6 = ((List)object15).iterator();

         while (true) {
            Iterator iterator19 = iterator6;
            if (!iterator19.hasNext()) {
               break;
            }

            Iterator iterator21 = iterator6;
            Integer integer8 = asInt(iterator21.next());
            Integer integer25 = integer8;
            if (integer25 != null) {
               ArrayList arrayList26 = arrayList3;
               Integer integer36 = integer8;
               boolean flag27 = arrayList26.add(integer36);
            }
         }
      }

      Integer integer39 = asInt(map0.get("slot"));
      Integer integer31 = integer39;
      if (integer31 != null) {
         ArrayList arrayList32 = arrayList3;
         Integer integer38 = integer39;
         boolean flag33 = arrayList32.add(integer38);
      }

      return arrayList3;
   }

   public static int slot(@Nullable Map<String, Object> map0, int i1) {
      if (map0 == null) {
         return i1;
      }

      Integer integer5 = asInt(map0.get("slot"));
      Integer integer10 = integer5;
      if (integer10 != null) {
         Integer integer19 = integer5;
         return integer19;
      }

      List list6 = slots(map0);
      List list13 = list6;
      int i15;
      if (list13.isEmpty()) {
         i15 = i1;
      } else {
         List list16 = list6;
         i15 = (Integer)list16.get(0);
      }

      return i15;
   }

   @Nullable
   static Integer asInt(@Nullable Object object0) {
      if (object0 instanceof Number) {
         return ((Number)object0).intValue();
      }

      if (object0 == null) {
         return null;
      }

      try {
         return Integer.parseInt(object0.toString().trim());
      } catch (NumberFormatException numberFormatException13) {
         return null;
      }
   }
}
