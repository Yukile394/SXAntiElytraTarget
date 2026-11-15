package studio.yukilem.yukilem.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncOptions;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncResult;
import studio.yukilem.yukilem.libs.nixlib.config.template.TemplateSync;
import studio.yukilem.yukilem.libs.nixlib.config.yaml.Nodes;
import studio.yukilem.yukilem.libs.nixlib.version.SemanticVersion;
import studio.yukilem.yukilem.punishments.CheckMatcher;
import studio.yukilem.yukilem.punishments.PunishmentCommand;
import studio.yukilem.yukilem.punishments.PunishmentCommandParser;
import studio.yukilem.yukilem.punishments.PunishmentProfile;

public final class PunishmentsConfigurationService {
   private static final String BUNDLED_RESOURCE = "punishments.yml";
   private static final String VERSION_KEY = "punishments-version";
   private final JavaPlugin plugin;
   private ConfigView view;
   private SemanticVersion loadedVersion;
   private List<PunishmentProfile> profiles;
   private long removeViolationsAfterSeconds;

   public PunishmentsConfigurationService(@NotNull JavaPlugin javaPlugin1) {
      SemanticVersion semanticVersion4 = SemanticVersion.ZERO;
      this.loadedVersion = semanticVersion4;
      List list9 = Collections.emptyList();
      this.profiles = list9;
      long l10 = 600L;
      this.removeViolationsAfterSeconds = l10;
      this.plugin = javaPlugin1;
   }

   public void initialize() throws IOException {
      File file5 = this.plugin.getDataFolder();
      File file9 = file5;
      if (!file9.exists()) {
         File file11 = file5;
         if (!file11.mkdirs()) {
            File file3 = file5;
            String string19 = "Could not create data folder: " + file3.getAbsolutePath();
            IOException ioException13 = new IOException(string19);
            throw ioException13;
         }
      }

      File file20 = file5;
      File file14 = new File(file20, "punishments.yml");
      File file6 = file14;
      File file17 = file6;
      this.syncWith(file17);
      this.rebuildCache();
   }

   public void reload() throws IOException {
      if (this.view == null) {
         throw new IOException("punishments.yml is not initialized");
      }

      File file13 = this.plugin.getDataFolder();
      File file7 = new File(file13, "punishments.yml");
      File file5 = file7;
      File file11 = file5;
      this.syncWith(file11);
      this.rebuildCache();
   }

   private void syncWith(@NotNull File file1) throws IOException {
      InputStream inputStream9 = this.plugin.getResource("punishments.yml");

      try {
         InputStream inputStream15 = inputStream9;
         if (inputStream15 == null) {
            throw new IOException("Bundled resource not found: punishments.yml");
         }

         InputStream inputStream16 = inputStream9;
         String string10 = readAll(inputStream16);
         String string20;
         if (file1.exists()) {
            Path path40 = file1.toPath();
            Charset charset49 = StandardCharsets.UTF_8;
            string20 = Files.readString(path40, charset49);
         } else {
            string20 = null;
         }

         SyncResult syncResult12;
         label41: {
            String string11 = string20;
            String string21 = string10;
            String string42 = string11;
            SyncOptions syncOptions50 = SyncOptions.create().versionKey("punishments-version");
            String[] strings57 = new String[]{"profiles"};
            SyncOptions syncOptions52 = syncOptions50.freeform(strings57).mergeDuplicateKeys();
            Consumer consumer59 = arg1x -> {
               Logger logger5 = this.plugin.getLogger();
               String string6 = "[punishments.yml] " + arg1x;
               logger5.warning(string6);
            };
            SyncOptions syncOptions53 = syncOptions52.onWarning(consumer59);
            syncResult12 = TemplateSync.synchronize(string21, string42, syncOptions53);
            String string23 = string11;
            if (string23 != null) {
               SyncResult syncResult24 = syncResult12;
               if (!syncResult24.changed()) {
                  break label41;
               }
            }

            Path path27 = file1.toPath();
            SyncResult syncResult43 = syncResult12;
            String string44 = syncResult43.mergedYaml();
            Charset charset54 = StandardCharsets.UTF_8;
            OpenOption[] openOptions61 = new OpenOption[0];
            Path path28 = Files.writeString(path27, string44, charset54, openOptions61);
         }

         SyncResult syncResult62 = syncResult12;
         Map map63 = syncResult62.mergedData();
         ConfigView configView45 = new ConfigView(map63);
         this.view = configView45;
         SyncResult syncResult46 = syncResult12;
         SemanticVersion semanticVersion47 = syncResult46.bundledVersion();
         this.loadedVersion = semanticVersion47;
      } catch (Throwable throwable67) {
         InputStream inputStream34 = inputStream9;
         if (inputStream34 != null) {
            try {
               InputStream inputStream35 = inputStream9;
               inputStream35.close();
            } catch (Throwable throwable66) {
               throwable67.addSuppressed(throwable66);
            }
         }

         throw throwable67;
      }

      InputStream inputStream31 = inputStream9;
      if (inputStream31 != null) {
         InputStream inputStream32 = inputStream9;
         inputStream32.close();
      }
   }

   @NotNull
   private SemanticVersion readBundledVersion(@NotNull Map<String, Object> map1) {
      Object object4 = Nodes.get(map1, "punishments-version");
      Object object6 = object4;
      if (object6 == null) {
         return SemanticVersion.ZERO;
      }

      Object object7 = object4;
      String string8 = String.valueOf(object7);
      SemanticVersion semanticVersion11 = SemanticVersion.ZERO;
      return SemanticVersion.parseOr(string8, semanticVersion11);
   }

   private void rebuildCache() {
      ConfigView configView13 = this.view;
      long l5 = 600L;
      long l14 = configView13.getLong("remove-violations-after", l5);
      this.removeViolationsAfterSeconds = l14;
      long l8 = this.removeViolationsAfterSeconds;
      long l20 = 0L;
      if (l8 < l20) {
         long l16 = 0L;
         this.removeViolationsAfterSeconds = l16;
      }

      ConfigView configView23 = this.view;
      List list19 = this.loadProfiles(configView23);
      this.profiles = list19;
   }

   @NotNull
   private List<PunishmentProfile> loadProfiles(@NotNull ConfigView configView1) {
      Object object9 = configView1.getRaw("profiles");
      Object object30 = object9;
      if (!(object30 instanceof Map)) {
         return Collections.emptyList();
      }

      Object object33 = object9;
      Map map10 = (Map)object33;
      Map map5 = map10;
      int i128 = map5.size();
      ArrayList arrayList35 = new ArrayList(i128);
      ArrayList arrayList11 = arrayList35;
      Map map36 = map10;
      Iterator iterator12 = map36.entrySet().iterator();

      while (true) {
         Iterator iterator39 = iterator12;
         if (!iterator39.hasNext()) {
            ArrayList arrayList116 = arrayList11;
            return Collections.unmodifiableList(arrayList116);
         }

         Iterator iterator41 = iterator12;
         Entry entry13 = (Entry)iterator41.next();
         Entry entry44 = entry13;
         String string14 = String.valueOf(entry44.getKey());
         Entry entry47 = entry13;
         Object object16 = entry47.getValue();
         Object object49 = object16;
         if (object49 instanceof Map) {
            Object object54 = object16;
            Map map15 = (Map)object54;
            Map map56 = map15;
            Object object137 = map56.get("checks");
            ArrayList arrayList17 = new ArrayList();
            Object object59 = object137;
            if (object59 instanceof List) {
               Object object61 = object137;
               Iterator iterator20 = ((List)object61).iterator();

               while (true) {
                  Iterator iterator65 = iterator20;
                  if (!iterator65.hasNext()) {
                     break;
                  }

                  Iterator iterator67 = iterator20;
                  Object object21 = iterator67.next();
                  Object object69 = object21;
                  if (object69 != null) {
                     ArrayList arrayList70 = arrayList17;
                     Object object121 = object21;
                     String string122 = String.valueOf(object121);
                     boolean flag71 = arrayList70.add(string122);
                  }
               }
            } else {
               Object object110 = object137;
               if (object110 instanceof String) {
                  Object object112 = object137;
                  String string139 = (String)object112;
                  ArrayList arrayList114 = arrayList17;
                  String string127 = string139;
                  boolean flag115 = arrayList114.add(string127);
               }
            }

            ArrayList arrayList72 = arrayList17;
            CheckMatcher checkMatcher138 = CheckMatcher.parse(arrayList72);
            ArrayList arrayList19 = new ArrayList();
            Map map75 = map15;
            Object object140 = map75.get("commands");
            Object object77 = object140;
            if (object77 instanceof Map) {
               Object object79 = object140;
               Iterator iterator22 = ((Map)object79).entrySet().iterator();

               while (true) {
                  Iterator iterator84 = iterator22;
                  if (!iterator84.hasNext()) {
                     break;
                  }

                  Iterator iterator86 = iterator22;
                  Entry entry23 = (Entry)iterator86.next();
                  Entry entry89 = entry23;
                  Object object24 = entry89.getKey();
                  Entry entry91 = entry23;
                  Object object25 = entry91.getValue();
                  Object object93 = object24;
                  if (object93 != null) {
                     Object object94 = object25;
                     if (object94 != null) {
                        Object object95 = object25;
                        if (object95 instanceof List) {
                           Object object98 = object25;
                           Iterator iterator27 = ((List)object98).iterator();

                           while (true) {
                              Iterator iterator102 = iterator27;
                              if (!iterator102.hasNext()) {
                                 break;
                              }

                              Iterator iterator104 = iterator27;
                              Object object28 = iterator104.next();
                              Object object106 = object28;
                              if (object106 != null) {
                                 ArrayList arrayList125 = arrayList19;
                                 String string130 = string14;
                                 Object object131 = object24;
                                 Object object134 = object28;
                                 String string135 = String.valueOf(object134);
                                 this.addParsedCommand(arrayList125, string130, object131, string135);
                              }
                           }
                        } else {
                           ArrayList arrayList124 = arrayList19;
                           String string129 = string14;
                           Object object6 = object24;
                           Object object7 = object25;
                           String string133 = String.valueOf(object7);
                           this.addParsedCommand(arrayList124, string129, object6, string133);
                        }
                     }
                  }
               }
            }

            ArrayList arrayList108 = arrayList11;
            String string132 = string14;
            CheckMatcher checkMatcher136 = checkMatcher138;
            ArrayList arrayList8 = arrayList19;
            PunishmentProfile punishmentProfile126 = new PunishmentProfile(string132, checkMatcher136, arrayList8);
            boolean flag109 = arrayList108.add(punishmentProfile126);
         } else {
            Logger logger53 = this.plugin.getLogger();
            String string118 = string14;
            String string119 = "[punishments.yml] profile '" + string118 + "' is not a section; skipping.";
            logger53.warning(string119);
         }
      }
   }

   private void addParsedCommand(@NotNull List<PunishmentCommand> list1, @NotNull String string2, @NotNull Object object3, @NotNull String string4) {
      PunishmentCommand punishmentCommand9 = PunishmentCommandParser.parse(object3, string4);
      PunishmentCommand punishmentCommand11 = punishmentCommand9;
      if (punishmentCommand11 == null) {
         Logger logger14 = this.plugin.getLogger();
         String string20 = String.valueOf(object3);
         String string18 = "[punishments.yml] profile '" + string2 + "' has invalid command key '" + string20 + "'; skipping.";
         logger14.warning(string18);
      } else {
         PunishmentCommand punishmentCommand19 = punishmentCommand9;
         boolean flag16 = list1.add(punishmentCommand19);
      }
   }

   @NotNull
   public List<PunishmentProfile> profiles() {
      return this.profiles;
   }

   public long removeViolationsAfterSeconds() {
      return this.removeViolationsAfterSeconds;
   }

   @NotNull
   public String getVersion() {
      return this.loadedVersion.toString();
   }

   private static String readAll(InputStream inputStream0) throws IOException {
      int i30 = inputStream0.available();
      int i28 = Math.max(64, i30);
      ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream(i28);
      ByteArrayOutputStream byteArrayOutputStream5 = byteArrayOutputStream2;

      String string8;
      try {
         byte[] bs6 = new byte[4096];

         while (true) {
            byte[] bs23 = bs6;
            int i12 = inputStream0.read(bs23);
            int i7 = i12;
            if (i12 == -1) {
               ByteArrayOutputStream byteArrayOutputStream19 = byteArrayOutputStream5;
               Charset charset27 = StandardCharsets.UTF_8;
               string8 = byteArrayOutputStream19.toString(charset27);
               break;
            }

            ByteArrayOutputStream byteArrayOutputStream13 = byteArrayOutputStream5;
            byte[] bs25 = bs6;
            int i31 = i7;
            byteArrayOutputStream13.write(bs25, 0, i31);
         }
      } catch (Throwable throwable35) {
         try {
            ByteArrayOutputStream byteArrayOutputStream15 = byteArrayOutputStream5;
            byteArrayOutputStream15.close();
         } catch (Throwable throwable34) {
            throwable35.addSuppressed(throwable34);
         }

         throw throwable35;
      }

      ByteArrayOutputStream byteArrayOutputStream21 = byteArrayOutputStream5;
      byteArrayOutputStream21.close();
      return string8;
   }
}
