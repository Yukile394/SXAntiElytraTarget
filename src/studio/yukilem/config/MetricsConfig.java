package studio.yukilem.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetricsConfig {
   private final File file;
   private final boolean defaultEnabled;
   private String serverUUID;
   private boolean enabled;
   private boolean logErrors;
   private boolean logSentData;
   private boolean logResponseStatusText;
   private boolean didExistBefore = true;

   public MetricsConfig(File file1, boolean flag2) throws IOException {
      this.file = file1;
      this.defaultEnabled = flag2;
      this.setupConfig();
   }

   public String getServerUUID() {
      return this.serverUUID;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isLogErrorsEnabled() {
      return this.logErrors;
   }

   public boolean isLogSentDataEnabled() {
      return this.logSentData;
   }

   public boolean isLogResponseStatusTextEnabled() {
      return this.logResponseStatusText;
   }

   public boolean didExistBefore() {
      return this.didExistBefore;
   }

   private void setupConfig() throws IOException {
      if (!this.file.exists()) {
         this.didExistBefore = false;
         this.writeConfig();
      }

      this.readConfig();
      if (this.serverUUID == null) {
         this.writeConfig();
         this.readConfig();
      }
   }

   private void writeConfig() throws IOException {
      ArrayList arrayList3 = new ArrayList();
      ArrayList arrayList4 = arrayList3;
      boolean flag5 = arrayList4.add("# bStats (https://bStats.org) collects some basic information for plugin authors, like");
      ArrayList arrayList6 = arrayList3;
      boolean flag7 = arrayList6.add("# how many people use their plugin and their total player count. It's recommended to keep");
      ArrayList arrayList8 = arrayList3;
      boolean flag9 = arrayList8.add("# bStats enabled, but if you're not comfortable with this, you can turn this setting off.");
      ArrayList arrayList10 = arrayList3;
      boolean flag11 = arrayList10.add("# There is no performance penalty associated with having metrics enabled, and data sent to");
      ArrayList arrayList12 = arrayList3;
      boolean flag13 = arrayList12.add("# bStats is fully anonymous.");
      ArrayList arrayList14 = arrayList3;
      boolean flag15 = arrayList14.add("# Learn more here: https://bstats.org/docs/server-owners");
      ArrayList arrayList16 = arrayList3;
      StringBuilder stringBuilder34 = new StringBuilder().append("enabled=");
      boolean flag48 = this.defaultEnabled;
      String string36 = stringBuilder34.append(flag48).toString();
      boolean flag17 = arrayList16.add(string36);
      ArrayList arrayList18 = arrayList3;
      StringBuilder stringBuilder38 = new StringBuilder().append("server-uuid=");
      String string51 = UUID.randomUUID().toString();
      String string40 = stringBuilder38.append(string51).toString();
      boolean flag19 = arrayList18.add(string40);
      ArrayList arrayList20 = arrayList3;
      boolean flag21 = arrayList20.add("log-errors=false");
      ArrayList arrayList22 = arrayList3;
      boolean flag23 = arrayList22.add("log-sent-data=false");
      ArrayList arrayList24 = arrayList3;
      boolean flag25 = arrayList24.add("log-response-status-text=false");
      File file45 = this.file;
      ArrayList arrayList52 = arrayList3;
      this.writeFile(file45, arrayList52);
   }

   private void readConfig() throws IOException {
      List<String> lines = this.readFile(this.file);
      if (lines == null) {
         throw new AssertionError("Content of newly created file is null");
      }

      this.enabled = this.getConfigValue("enabled", lines).map("true"::equals).orElse(true);
      this.serverUUID = this.getConfigValue("server-uuid", lines).orElse(null);
      this.logErrors = this.getConfigValue("log-errors", lines).map("true"::equals).orElse(false);
      this.logSentData = this.getConfigValue("log-sent-data", lines).map("true"::equals).orElse(false);
      this.logResponseStatusText = this.getConfigValue("log-response-status-text", lines).map("true"::equals).orElse(false);
   }

   private Optional<String> getConfigValue(String key, List<String> lines) {
      return lines.stream()
         .filter(line -> line.startsWith(key + "="))
         .map(line -> line.replaceFirst(Pattern.quote(key + "="), ""))
         .findFirst();
   }

   private List<String> readFile(File file) throws IOException {
      if (!file.exists()) {
         return null;
      }

      try (FileReader fileReader = new FileReader(file);
           BufferedReader bufferedReader = new BufferedReader(fileReader)) {
         return bufferedReader.lines().collect(Collectors.toList());
      }
   }

   private void writeFile(File file1, List<String> list2) throws IOException {
      if (!file1.exists()) {
         File file6 = file1.getParentFile();
         File file13 = file6;
         if (file13 != null) {
            File file14 = file6;
            boolean flag15 = file14.mkdirs();
         }

         boolean flag17 = file1.createNewFile();
      }

      FileWriter fileWriter45 = new FileWriter(file1);

      try {
         FileWriter fileWriter44 = fileWriter45;
         BufferedWriter bufferedWriter19 = new BufferedWriter(fileWriter44);
         BufferedWriter bufferedWriter7 = bufferedWriter19;

         try {
            Iterator iterator8 = list2.iterator();

            while (true) {
               Iterator iterator22 = iterator8;
               if (!iterator22.hasNext()) {
                  break;
               }

               Iterator iterator31 = iterator8;
               String string9 = (String)iterator31.next();
               BufferedWriter bufferedWriter34 = bufferedWriter7;
               String string42 = string9;
               bufferedWriter34.write(string42);
               BufferedWriter bufferedWriter35 = bufferedWriter7;
               bufferedWriter35.newLine();
            }
         } catch (Throwable throwable52) {
            try {
               BufferedWriter bufferedWriter37 = bufferedWriter7;
               bufferedWriter37.close();
            } catch (Throwable throwable51) {
               throwable52.addSuppressed(throwable51);
            }

            throw throwable52;
         }

         BufferedWriter bufferedWriter24 = bufferedWriter7;
         bufferedWriter24.close();
      } catch (Throwable throwable53) {
         try {
            FileWriter fileWriter27 = fileWriter45;
            fileWriter27.close();
         } catch (Throwable throwable50) {
            throwable53.addSuppressed(throwable50);
         }

         throw throwable53;
      }

      FileWriter fileWriter25 = fileWriter45;
      fileWriter25.close();
   }
}
