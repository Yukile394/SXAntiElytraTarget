package studio.yukilem.yukilem.libs.nixlib.config.validation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public final class Validators {
   private Validators() {
   }

   public static void validate(Field field0, Object object1) {
      Range range2 = field0.getAnnotation(Range.class);
      if (range2 != null && object1 instanceof Number number3) {
         double d4 = number3.doubleValue();
         if (d4 < range2.min() || d4 > range2.max()) {
            throw new ValidationException("value " + d4 + " out of range [" + range2.min() + ", " + range2.max() + "]");
         }
      }

      Regex regex12 = field0.getAnnotation(Regex.class);
      if (regex12 != null && object1 != null) {
         String string13 = object1.toString();
         if (!Pattern.matches(regex12.value(), string13)) {
            throw new ValidationException("value '" + string13 + "' does not match pattern " + regex12.value());
         }
      }

      NotEmpty notEmpty14 = field0.getAnnotation(NotEmpty.class);
      if (notEmpty14 != null && isEmpty(object1)) {
         throw new ValidationException("value must not be empty");
      }

      OneOf oneOf5 = field0.getAnnotation(OneOf.class);
      if (oneOf5 != null && object1 != null) {
         String string6 = object1.toString();
         boolean flag7 = false;

         for (String string11 : oneOf5.value()) {
            if (oneOf5.caseSensitive() ? string11.equals(string6) : string11.equalsIgnoreCase(string6)) {
               flag7 = true;
               break;
            }
         }

         if (!flag7) {
            throw new ValidationException("value '" + string6 + "' not in allowed set " + String.join(",", oneOf5.value()));
         }
      }
   }

   private static boolean isEmpty(Object object0) {
      if (object0 == null) {
         return true;
      } else if (object0 instanceof CharSequence charSequence3) {
         return charSequence3.length() == 0;
      } else if (object0 instanceof Collection collection2) {
         return collection2.isEmpty();
      } else if (object0 instanceof Map map1) {
         return map1.isEmpty();
      } else {
         return object0.getClass().isArray() ? Array.getLength(object0) == 0 : false;
      }
   }
}
