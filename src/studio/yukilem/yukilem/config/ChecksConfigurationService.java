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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncOptions;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncResult;
import studio.yukilem.yukilem.libs.nixlib.config.template.TemplateSync;
import studio.yukilem.yukilem.libs.nixlib.config.yaml.Nodes;
import studio.yukilem.yukilem.libs.nixlib.version.SemanticVersion;

public final class ChecksConfigurationService {
   private static final String BUNDLED_RESOURCE = "checks.yml";
   private static final String VERSION_KEY = "checks-version";
   private final JavaPlugin plugin;
   private ConfigView view;
   private SemanticVersion loadedVersion;

   public ChecksConfigurationService(@NotNull JavaPlugin javaPlugin1) {
      SemanticVersion semanticVersion4 = SemanticVersion.ZERO;
      this.loadedVersion = semanticVersion4;
      this.plugin = javaPlugin1;
   }

   public void initialize() throws IOException {
      File file5 = this.plugin.getDataFolder();
      File file9 = file5;
      if (!file9.exists()) {
         File file11 = file5;
         if (!file11.mkdirs()) {
            File file3 = file5;
            String string18 = "Could not create data folder: " + file3.getAbsolutePath();
            IOException ioException13 = new IOException(string18);
            throw ioException13;
         }
      }

      File file19 = file5;
      File file14 = new File(file19, "checks.yml");
      File file6 = file14;
      File file16 = file6;
      this.syncWith(file16);
   }

   public void reload() throws IOException {
      if (this.view == null) {
         throw new IOException("checks.yml is not initialized");
      }

      File file12 = this.plugin.getDataFolder();
      File file7 = new File(file12, "checks.yml");
      File file5 = file7;
      File file10 = file5;
      this.syncWith(file10);
   }

   private void syncWith(@NotNull File file1) throws IOException {
      InputStream inputStream7 = this.plugin.getResource("checks.yml");

      try {
         InputStream inputStream13 = inputStream7;
         if (inputStream13 == null) {
            throw new IOException("Bundled resource not found: checks.yml");
         }

         InputStream inputStream21 = inputStream7;
         String string58 = readAll(inputStream21);
         String string39;
         if (file1.exists()) {
            Path path38 = file1.toPath();
            Charset charset47 = StandardCharsets.UTF_8;
            string39 = Files.readString(path38, charset47);
         } else {
            string39 = null;
         }

         SyncResult syncResult10;
         label41: {
            String string59 = string39;
            String string25 = string58;
            String string41 = string59;
            SyncOptions syncOptions49 = SyncOptions.create().versionKey("checks-version");
            Consumer consumer53 = arg1x -> {
               Logger logger5 = this.plugin.getLogger();
               String string6 = "[checks.yml] " + arg1x;
               logger5.warning(string6);
            };
            SyncOptions syncOptions50 = syncOptions49.onWarning(consumer53);
            syncResult10 = TemplateSync.synchronize(string25, string41, syncOptions50);
            String string27 = string59;
            if (string27 != null) {
               SyncResult syncResult28 = syncResult10;
               if (!syncResult28.changed()) {
                  break label41;
               }
            }

            Path path31 = file1.toPath();
            SyncResult syncResult42 = syncResult10;
            String string43 = syncResult42.mergedYaml();
            Charset charset51 = StandardCharsets.UTF_8;
            OpenOption[] openOptions55 = new OpenOption[0];
            Path path32 = Files.writeString(path31, string43, charset51, openOptions55);
         }

         SyncResult syncResult56 = syncResult10;
         Map map57 = syncResult56.mergedData();
         ConfigView configView44 = new ConfigView(map57);
         this.view = configView44;
         SyncResult syncResult45 = syncResult10;
         SemanticVersion semanticVersion46 = syncResult45.bundledVersion();
         this.loadedVersion = semanticVersion46;
      } catch (Throwable throwable61) {
         InputStream inputStream16 = inputStream7;
         if (inputStream16 != null) {
            try {
               InputStream inputStream17 = inputStream7;
               inputStream17.close();
            } catch (Throwable throwable60) {
               throwable61.addSuppressed(throwable60);
            }
         }

         throw throwable61;
      }

      InputStream inputStream35 = inputStream7;
      if (inputStream35 != null) {
         InputStream inputStream36 = inputStream7;
         inputStream36.close();
      }
   }

   @NotNull
   public String getVersion() {
      return this.loadedVersion.toString();
   }

   public boolean getBoolean(@NotNull String string1, boolean flag2) {
      return this.view.getBoolean(string1, flag2);
   }

   public int getInt(@NotNull String string1, int i2) {
      return this.view.getInt(string1, i2);
   }

   public long getLong(@NotNull String string1, long l2) {
      return this.view.getLong(string1, l2);
   }

   public double getDouble(@NotNull String string1, double d2) {
      return this.view.getDouble(string1, d2);
   }

   @NotNull
   public String getString(@NotNull String string1, @NotNull String string2) {
      return this.view.getString(string1, string2);
   }

   @NotNull
   public List<String> getStringList(@NotNull String string1) {
      return this.view.getStringList(string1);
   }

   @NotNull
   public ConfigView view() {
      return this.view;
   }

   @NotNull
   private static SemanticVersion readBundledVersion(@NotNull Map<String, Object> map0) {
      Object object3 = Nodes.get(map0, "checks-version");
      Object object5 = object3;
      if (object5 == null) {
         return SemanticVersion.ZERO;
      }

      Object object6 = object3;
      String string7 = String.valueOf(object6);
      SemanticVersion semanticVersion10 = SemanticVersion.ZERO;
      return SemanticVersion.parseOr(string7, semanticVersion10);
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
               ByteArrayOutputStream byteArrayOutputStream13 = byteArrayOutputStream5;
               Charset charset25 = StandardCharsets.UTF_8;
               string8 = byteArrayOutputStream13.toString(charset25);
               break;
            }

            ByteArrayOutputStream byteArrayOutputStream22 = byteArrayOutputStream5;
            byte[] bs27 = bs6;
            int i31 = i7;
            byteArrayOutputStream22.write(bs27, 0, i31);
         }
      } catch (Throwable throwable35) {
         try {
            ByteArrayOutputStream byteArrayOutputStream18 = byteArrayOutputStream5;
            byteArrayOutputStream18.close();
         } catch (Throwable throwable34) {
            throwable35.addSuppressed(throwable34);
         }

         throw throwable35;
      }

      ByteArrayOutputStream byteArrayOutputStream15 = byteArrayOutputStream5;
      byteArrayOutputStream15.close();
      return string8;
   }
}
