package studio.yukilem.yukilem.libs.nixlib.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.libs.nixlib.utils.NumberUtils;

public final class MapCoercion {
   private MapCoercion() {
   }

   public static double readSeconds(@Nullable Object object0, double d1) {
      return readSeconds(object0, d1, 0.0);
   }

   public static double readSeconds(@Nullable Object object0, double d1, double d3) {
      if (object0 instanceof Number number6) {
         return Math.max(d3, number6.doubleValue());
      } else {
         if (object0 != null) {
            Optional optional5 = NumberUtils.tryDouble(object0.toString().trim());
            if (optional5.isPresent()) {
               return Math.max(d3, (Double)optional5.get());
            }
         }

         return d1;
      }
   }

   public static int intValue(@Nullable Object object0, int i1) {
      if (object0 instanceof Number) {
         return ((Number)object0).intValue();
      } else {
         return object0 != null ? NumberUtils.tryInt(object0.toString().trim()).orElse(i1) : i1;
      }
   }

   public static boolean boolAt(@NotNull Map<String, Object> map0, @NotNull String string1, boolean flag2) {
      String[] strings3 = string1.split("\\.");
      Object object4 = map0;

      for (String string8 : strings3) {
         if (!(object4 instanceof Map map9)) {
            return flag2;
         }

         object4 = map9.get(string8);
         if (object4 == null) {
            return flag2;
         }
      }

      if (object4 instanceof Boolean booleanValue11) {
         return booleanValue11;
      } else {
         return object4 instanceof String arg10 ? Boolean.parseBoolean(arg10) : flag2;
      }
   }

   @NotNull
   public static Set<String> readWorldNames(@Nullable Object object0) {
      HashSet hashSet1 = new HashSet();
      if (object0 instanceof List list2) {
         addNames(hashSet1, list2, true);
      } else if (object0 instanceof Map) {
         for (Object object7 : ((Map)object0).keySet()) {
            if (object7 != null) {
               hashSet1.add(object7.toString().toLowerCase(Locale.ROOT));
            }
         }
      } else if (object0 instanceof Collection collection4) {
         addNames(hashSet1, collection4, true);
      } else if (object0 instanceof String arg5 && !arg5.isBlank()) {
         hashSet1.add(arg5.toLowerCase(Locale.ROOT));
      }

      return hashSet1.isEmpty() ? Set.of() : Set.copyOf(hashSet1);
   }

   @NotNull
   public static Set<String> readStringSet(@Nullable Object object0, boolean flag1) {
      if (object0 instanceof List list4) {
         HashSet hashSet5 = new HashSet();
         addNames(hashSet5, list4, flag1);
         return Set.copyOf(hashSet5);
      } else if (object0 instanceof Collection collection2) {
         HashSet hashSet3 = new HashSet();
         addNames(hashSet3, collection2, flag1);
         return Set.copyOf(hashSet3);
      } else {
         return Set.of();
      }
   }

   private static void addNames(@NotNull Set<String> set0, @NotNull Iterable<?> iterable1, boolean flag2) {
      for (Object object4 : iterable1) {
         if (object4 != null) {
            String string5 = object4.toString().trim();
            if (!string5.isEmpty()) {
               set0.add(flag2 ? string5.toLowerCase(Locale.ROOT) : string5.toUpperCase(Locale.ROOT));
            }
         }
      }
   }
}
