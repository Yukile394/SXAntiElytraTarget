package studio.glace.glantielytratarget.discord;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.config.settings.DiscordSettings;

final class DiscordWebhookPayload {
   private DiscordWebhookPayload() {
   }

   static String build(@NotNull DiscordSettings discordSettings0, @NotNull WebhookMessage webhookMessage1) {
      DiscordJsonBuilder discordJsonBuilder6 = new DiscordJsonBuilder();
      String string7 = joinLines(discordSettings0.content(), webhookMessage1);
      String string13 = string7;
      if (!string13.isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder15 = discordJsonBuilder6;
         String string4 = string7;
         discordJsonBuilder15.put("content", string4);
      }

      if (!discordSettings0.username().isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder19 = discordJsonBuilder6;
         String string45 = apply(discordSettings0.username(), webhookMessage1);
         discordJsonBuilder19.put("username", string45);
      }

      if (!discordSettings0.avatarUrl().isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder23 = discordJsonBuilder6;
         String string48 = apply(discordSettings0.avatarUrl(), webhookMessage1);
         discordJsonBuilder23.put("avatar_url", string48);
      }

      DiscordSettings.Embed embed8 = discordSettings0.embed();
      DiscordSettings.Embed embed26 = embed8;
      if (embed26.enabled()) {
         DiscordSettings.Embed embed28 = embed8;
         String string9 = buildEmbed(embed28, webhookMessage1);
         String string30 = string9;
         if (string30 != null) {
            DiscordJsonBuilder discordJsonBuilder31 = discordJsonBuilder6;
            String string49 = string9;
            String string50 = "[" + string49 + "]";
            discordJsonBuilder31.putRaw("embeds", string50);
         }
      }

      DiscordJsonBuilder discordJsonBuilder32 = discordJsonBuilder6;
      if (discordJsonBuilder32.isEmpty()) {
         return null;
      }

      DiscordJsonBuilder discordJsonBuilder34 = discordJsonBuilder6;
      return discordJsonBuilder34.build();
   }

   @NotNull
   private static String buildEmbed(@NotNull DiscordSettings.Embed embed0, @NotNull WebhookMessage webhookMessage1) {
      DiscordJsonBuilder discordJsonBuilder6 = new DiscordJsonBuilder();
      if (!embed0.title().isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder18 = discordJsonBuilder6;
         String string147 = apply(embed0.title(), webhookMessage1);
         discordJsonBuilder18.put("title", string147);
      }

      if (!embed0.url().isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder22 = discordJsonBuilder6;
         String string150 = apply(embed0.url(), webhookMessage1);
         discordJsonBuilder22.put("url", string150);
      }

      String string7 = joinLines(embed0.description(), webhookMessage1);
      String string26 = string7;
      if (!string26.isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder28 = discordJsonBuilder6;
         String string151 = string7;
         discordJsonBuilder28.put("description", string151);
      }

      DiscordJsonBuilder discordJsonBuilder29 = discordJsonBuilder6;
      String string155 = String.valueOf(embed0.color() & 16777215);
      discordJsonBuilder29.putRaw("color", string155);
      if (embed0.timestamp()) {
         DiscordJsonBuilder discordJsonBuilder32 = discordJsonBuilder6;
         String string157 = Instant.now().toString();
         discordJsonBuilder32.put("timestamp", string157);
      }

      label85: {
         DiscordSettings.Author author8 = embed0.author();
         DiscordSettings.Author author35 = author8;
         if (author35.name().isEmpty()) {
            DiscordSettings.Author author38 = author8;
            if (author38.iconUrl().isEmpty()) {
               DiscordSettings.Author author41 = author8;
               if (author41.url().isEmpty()) {
                  break label85;
               }
            }
         }

         DiscordJsonBuilder discordJsonBuilder9 = new DiscordJsonBuilder();
         DiscordSettings.Author author45 = author8;
         if (!author45.name().isEmpty()) {
            DiscordJsonBuilder discordJsonBuilder48 = discordJsonBuilder9;
            DiscordSettings.Author author158 = author8;
            String string160 = apply(author158.name(), webhookMessage1);
            discordJsonBuilder48.put("name", string160);
         }

         DiscordSettings.Author author49 = author8;
         if (!author49.url().isEmpty()) {
            DiscordJsonBuilder discordJsonBuilder52 = discordJsonBuilder9;
            DiscordSettings.Author author161 = author8;
            String string163 = apply(author161.url(), webhookMessage1);
            discordJsonBuilder52.put("url", string163);
         }

         DiscordSettings.Author author53 = author8;
         if (!author53.iconUrl().isEmpty()) {
            DiscordJsonBuilder discordJsonBuilder56 = discordJsonBuilder9;
            DiscordSettings.Author author164 = author8;
            String string166 = apply(author164.iconUrl(), webhookMessage1);
            discordJsonBuilder56.put("icon_url", string166);
         }

         DiscordJsonBuilder discordJsonBuilder57 = discordJsonBuilder6;
         DiscordJsonBuilder discordJsonBuilder167 = discordJsonBuilder9;
         String string168 = discordJsonBuilder167.build();
         discordJsonBuilder57.putRaw("author", string168);
      }

      label86: {
         DiscordSettings.Footer footer201 = embed0.footer();
         DiscordSettings.Footer footer60 = footer201;
         if (footer60.text().isEmpty()) {
            DiscordSettings.Footer footer63 = footer201;
            if (footer63.iconUrl().isEmpty()) {
               break label86;
            }
         }

         DiscordJsonBuilder discordJsonBuilder10 = new DiscordJsonBuilder();
         DiscordSettings.Footer footer67 = footer201;
         if (!footer67.text().isEmpty()) {
            DiscordJsonBuilder discordJsonBuilder70 = discordJsonBuilder10;
            DiscordSettings.Footer footer169 = footer201;
            String string171 = apply(footer169.text(), webhookMessage1);
            discordJsonBuilder70.put("text", string171);
         }

         DiscordSettings.Footer footer71 = footer201;
         if (!footer71.iconUrl().isEmpty()) {
            DiscordJsonBuilder discordJsonBuilder74 = discordJsonBuilder10;
            DiscordSettings.Footer footer172 = footer201;
            String string174 = apply(footer172.iconUrl(), webhookMessage1);
            discordJsonBuilder74.put("icon_url", string174);
         }

         DiscordJsonBuilder discordJsonBuilder75 = discordJsonBuilder6;
         DiscordJsonBuilder discordJsonBuilder175 = discordJsonBuilder10;
         String string176 = discordJsonBuilder175.build();
         discordJsonBuilder75.putRaw("footer", string176);
      }

      if (!embed0.imageUrl().isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder79 = discordJsonBuilder6;
         String string181 = "{\"url\":\"" + escapeJson(apply(embed0.imageUrl(), webhookMessage1)) + "\"}";
         discordJsonBuilder79.putRaw("image", string181);
      }

      if (!embed0.thumbnailUrl().isEmpty()) {
         DiscordJsonBuilder discordJsonBuilder83 = discordJsonBuilder6;
         String string186 = "{\"url\":\"" + escapeJson(apply(embed0.thumbnailUrl(), webhookMessage1)) + "\"}";
         discordJsonBuilder83.putRaw("thumbnail", string186);
      }

      List list202 = embed0.fields();
      List list86 = list202;
      if (!list86.isEmpty()) {
         StringBuilder stringBuilder11 = new StringBuilder("[");
         boolean flag12 = true;
         List list90 = list202;
         Iterator iterator13 = list90.iterator();

         while (true) {
            Iterator iterator92 = iterator13;
            if (!iterator92.hasNext()) {
               StringBuilder stringBuilder109 = stringBuilder11;
               StringBuilder stringBuilder110 = stringBuilder109.append(']');
               DiscordJsonBuilder discordJsonBuilder111 = discordJsonBuilder6;
               StringBuilder stringBuilder190 = stringBuilder11;
               String string191 = stringBuilder190.toString();
               discordJsonBuilder111.putRaw("fields", string191);
               break;
            }

            Iterator iterator94 = iterator13;
            DiscordSettings.Field field14 = (DiscordSettings.Field)iterator94.next();
            boolean flag97 = flag12;
            if (!flag97) {
               StringBuilder stringBuilder98 = stringBuilder11;
               StringBuilder stringBuilder99 = stringBuilder98.append(',');
            }

            flag12 = false;
            StringBuilder stringBuilder101 = stringBuilder11;
            StringBuilder stringBuilder102 = stringBuilder101.append("{\"name\":\"");
            DiscordSettings.Field field131 = field14;
            String string134 = escapeJson(apply(field131.name(), webhookMessage1));
            StringBuilder stringBuilder104 = stringBuilder102.append(string134).append("\",\"value\":\"");
            DiscordSettings.Field field136 = field14;
            String string139 = escapeJson(apply(field136.value(), webhookMessage1));
            StringBuilder stringBuilder106 = stringBuilder104.append(string139).append("\",\"inline\":");
            DiscordSettings.Field field141 = field14;
            boolean flag142 = field141.inline();
            StringBuilder stringBuilder108 = stringBuilder106.append(flag142).append('}');
         }
      }

      DiscordJsonBuilder discordJsonBuilder112 = discordJsonBuilder6;
      return discordJsonBuilder112.build();
   }

   @NotNull
   private static String joinLines(@NotNull List<String> list0, @NotNull WebhookMessage webhookMessage1) {
      if (list0.isEmpty()) {
         return "";
      }

      StringBuilder stringBuilder5 = new StringBuilder();
      int i6 = 0;

      while (true) {
         int i11 = i6;
         int i20 = list0.size();
         if (i11 >= i20) {
            StringBuilder stringBuilder17 = stringBuilder5;
            return stringBuilder17.toString();
         }

         int i12 = i6;
         if (i12 > 0) {
            StringBuilder stringBuilder13 = stringBuilder5;
            StringBuilder stringBuilder14 = stringBuilder13.append('\n');
         }

         StringBuilder stringBuilder15 = stringBuilder5;
         int i4 = i6;
         String string25 = apply((String)list0.get(i4), webhookMessage1);
         StringBuilder stringBuilder16 = stringBuilder15.append(string25);
         i6++;
      }
   }

   @NotNull
   private static String apply(@NotNull String string0, @NotNull WebhookMessage webhookMessage1) {
      if (!string0.isEmpty() && string0.indexOf(37) >= 0) {
         String string6 = String.valueOf(webhookMessage1.vl());
         String string7 = String.valueOf(webhookMessage1.ms());
         String string29 = webhookMessage1.playerName();
         String string18 = string0.replace("%player%", string29);
         String string31 = webhookMessage1.checkName();
         String string19 = string18.replace("%check_name%", string31);
         String string32 = string6;
         String string20 = string19.replace("%vl%", string32);
         String string33 = string7;
         String string21 = string20.replace("%ms%", string33);
         String string35 = webhookMessage1.tps();
         return string21.replace("%tps%", string35);
      } else {
         return string0;
      }
   }

   static String escapeJson(String string0) {
      if (string0 == null) {
         return "";
      }

      int i51 = string0.length();
      StringBuilder stringBuilder9 = new StringBuilder(i51);
      StringBuilder stringBuilder6 = stringBuilder9;
      int i7 = 0;

      while (true) {
         int i11 = i7;
         int i38 = string0.length();
         if (i11 >= i38) {
            StringBuilder stringBuilder12 = stringBuilder6;
            return stringBuilder12.toString();
         }

         int i39 = i7;
         char c8 = string0.charAt(i39);
         char c16 = c8;
         switch (c16) {
            case '\b':
               StringBuilder stringBuilder23 = stringBuilder6;
               StringBuilder stringBuilder24 = stringBuilder23.append("\\b");
               break;
            case '\t':
               StringBuilder stringBuilder19 = stringBuilder6;
               StringBuilder stringBuilder20 = stringBuilder19.append("\\t");
               break;
            case '\n':
               StringBuilder stringBuilder21 = stringBuilder6;
               StringBuilder stringBuilder22 = stringBuilder21.append("\\n");
               break;
            case '\f':
               StringBuilder stringBuilder34 = stringBuilder6;
               StringBuilder stringBuilder35 = stringBuilder34.append("\\f");
               break;
            case '\r':
               StringBuilder stringBuilder25 = stringBuilder6;
               StringBuilder stringBuilder26 = stringBuilder25.append("\\r");
               break;
            case '"':
               StringBuilder stringBuilder17 = stringBuilder6;
               StringBuilder stringBuilder18 = stringBuilder17.append("\\\"");
               break;
            case '\\':
               StringBuilder stringBuilder27 = stringBuilder6;
               StringBuilder stringBuilder28 = stringBuilder27.append("\\\\");
               break;
            default:
               char c29 = c8;
               if (c29 < ' ') {
                  StringBuilder stringBuilder32 = stringBuilder6;
                  Object[] objects53 = new Object[1];
                  char c5 = c8;
                  Integer integer54 = Integer.valueOf(c5);
                  objects53[0] = integer54;
                  String string49 = String.format("\\u%04x", objects53);
                  StringBuilder stringBuilder33 = stringBuilder32.append(string49);
               } else {
                  StringBuilder stringBuilder30 = stringBuilder6;
                  char c47 = c8;
                  StringBuilder stringBuilder31 = stringBuilder30.append(c47);
               }
         }

         i7++;
      }
   }
}
