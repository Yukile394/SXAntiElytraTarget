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
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.config.settings.DiscordSettings;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncOptions;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncResult;
import studio.yukilem.yukilem.libs.nixlib.config.template.TemplateSync;
import studio.yukilem.yukilem.libs.nixlib.version.SemanticVersion;

public final class WebhookConfigurationService {
   private static final String BUNDLED_RESOURCE = "webhook.yml";
   private static final String VERSION_KEY = "webhook-version";
   private final JavaPlugin plugin;
   private ConfigView view;
   private SemanticVersion loadedVersion;
   private DiscordSettings discord;

   public WebhookConfigurationService(@NotNull JavaPlugin javaPlugin1) {
      SemanticVersion semanticVersion4 = SemanticVersion.ZERO;
      this.loadedVersion = semanticVersion4;
      DiscordSettings discordSettings8 = DiscordSettings.disabled();
      this.discord = discordSettings8;
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
      File file14 = new File(file20, "webhook.yml");
      File file6 = file14;
      File file17 = file6;
      this.syncWith(file17);
      this.rebuildCache();
   }

   public void reload() throws IOException {
      if (this.view == null) {
         throw new IOException("webhook.yml is not initialized");
      }

      File file14 = this.plugin.getDataFolder();
      File file8 = new File(file14, "webhook.yml");
      File file5 = file8;
      File file11 = file5;
      this.syncWith(file11);
      this.rebuildCache();
   }

   private void syncWith(@NotNull File file1) throws IOException {
      InputStream inputStream7 = this.plugin.getResource("webhook.yml");

      try {
         InputStream inputStream13 = inputStream7;
         if (inputStream13 == null) {
            throw new IOException("Bundled resource not found: webhook.yml");
         }

         InputStream inputStream21 = inputStream7;
         String string58 = readAll(inputStream21);
         String string39;
         if (file1.exists()) {
            Path path26 = file1.toPath();
            Charset charset41 = StandardCharsets.UTF_8;
            string39 = Files.readString(path26, charset41);
         } else {
            string39 = null;
         }

         SyncResult syncResult10;
         label41: {
            String string59 = string39;
            String string27 = string58;
            String string42 = string59;
            SyncOptions syncOptions49 = SyncOptions.create().versionKey("webhook-version");
            Consumer consumer53 = arg1x -> {
               Logger logger5 = this.plugin.getLogger();
               String string6 = "[webhook.yml] " + arg1x;
               logger5.warning(string6);
            };
            SyncOptions syncOptions50 = syncOptions49.onWarning(consumer53);
            syncResult10 = TemplateSync.synchronize(string27, string42, syncOptions50);
            String string29 = string59;
            if (string29 != null) {
               SyncResult syncResult30 = syncResult10;
               if (!syncResult30.changed()) {
                  break label41;
               }
            }

            Path path33 = file1.toPath();
            SyncResult syncResult43 = syncResult10;
            String string44 = syncResult43.mergedYaml();
            Charset charset51 = StandardCharsets.UTF_8;
            OpenOption[] openOptions55 = new OpenOption[0];
            Path path34 = Files.writeString(path33, string44, charset51, openOptions55);
         }

         SyncResult syncResult56 = syncResult10;
         Map map57 = syncResult56.mergedData();
         ConfigView configView45 = new ConfigView(map57);
         this.view = configView45;
         SyncResult syncResult46 = syncResult10;
         SemanticVersion semanticVersion47 = syncResult46.bundledVersion();
         this.loadedVersion = semanticVersion47;
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

      InputStream inputStream37 = inputStream7;
      if (inputStream37 != null) {
         InputStream inputStream38 = inputStream7;
         inputStream38.close();
      }
   }

   private void rebuildCache() {
      DiscordSettings discordSettings5 = DiscordSettings.load(this.view);
      this.discord = discordSettings5;
   }

   @NotNull
   public DiscordSettings discord() {
      return this.discord;
   }

   @NotNull
   public ConfigView view() {
      return this.view;
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

   @Nullable
   private static Object placeholder() {
      return null;
   }
}
