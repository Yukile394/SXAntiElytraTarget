package studio.yukilem.yukilem.punishments;

import org.jetbrains.annotations.NotNull;

public enum PunishmentAction {
   CONSOLE_COMMAND,
   ALERT,
   WEBHOOK;

   PunishmentAction() {
   }

   @NotNull
   public static PunishmentAction fromRaw(@NotNull String string0) {
      String string3 = string0.trim();
      String string5 = string3;
      if (string5.startsWith("[ALERT]")) {
         return ALERT;
      }

      String string7 = string3;
      return string7.startsWith("[WEBHOOK]") ? WEBHOOK : CONSOLE_COMMAND;
   }

   @NotNull
   public static String stripPrefix(@NotNull String string0) {
      String string3 = string0.trim();
      String string5 = string3;
      if (string5.startsWith("[ALERT]")) {
         String string13 = string3;
         int i20 = "[ALERT]".length();
         return string13.substring(i20).trim();
      } else {
         String string7 = string3;
         if (string7.startsWith("[WEBHOOK]")) {
            String string9 = string3;
            int i18 = "[WEBHOOK]".length();
            return string9.substring(i18).trim();
         } else {
            return string3;
         }
      }
   }
}
