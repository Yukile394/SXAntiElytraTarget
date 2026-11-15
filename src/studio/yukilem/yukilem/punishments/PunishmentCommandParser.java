package studio.yukilem.yukilem.punishments;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PunishmentCommandParser {
   private PunishmentCommandParser() {
   }

   @Nullable
   public static PunishmentCommand parse(@NotNull Object object0, @NotNull String string1) {
      String string10 = String.valueOf(object0).trim();
      String string16 = string10;
      if (string16.isEmpty()) {
         return null;
      }

      String string19 = string10;
      int i11 = string19.indexOf(58);

      int i50;
      int i51;
      try {
         int i21 = i11;
         if (i21 < 0) {
            String string41 = string10;
            i50 = Integer.parseInt(string41);
            i51 = 0;
         } else {
            String string22 = string10;
            int i5 = i11;
            i50 = Integer.parseInt(string22.substring(0, i5).trim());
            String string26 = string10;
            int i45 = i11;
            int i46 = i45 + 1;
            i51 = Integer.parseInt(string26.substring(i46).trim());
         }
      } catch (NumberFormatException numberFormatException53) {
         return null;
      }

      int i30 = i50;
      if (i30 < 1) {
         return null;
      }

      int i32 = i51;
      if (i32 < 0) {
         i51 = 0;
      }

      PunishmentAction punishmentAction12 = PunishmentAction.fromRaw(string1);
      String string13 = PunishmentAction.stripPrefix(string1);
      int i49 = i50;
      int i6 = i51;
      PunishmentAction punishmentAction7 = punishmentAction12;
      String string8 = string13;
      return new PunishmentCommand(i49, i6, punishmentAction7, string8, string1);
   }
}
