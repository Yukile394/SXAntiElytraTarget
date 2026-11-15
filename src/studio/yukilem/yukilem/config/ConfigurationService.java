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
import lombok.Generated;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.settings.AlertSettings;
import studio.yukilem.yukilem.config.settings.AutoPunishSettings;
import studio.yukilem.yukilem.config.settings.DamageReductionSettings;
import studio.yukilem.yukilem.config.settings.DatabaseSettings;
import studio.yukilem.yukilem.config.settings.LagProtectionSettings;
import studio.yukilem.yukilem.config.settings.MenuTimeSettings;
import studio.yukilem.yukilem.config.settings.OffhandSettings;
import studio.yukilem.yukilem.config.settings.UpdateCheckerSettings;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncOptions;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncResult;
import studio.yukilem.yukilem.libs.nixlib.config.template.TemplateSync;
import studio.yukilem.yukilem.libs.nixlib.version.SemanticVersion;

public final class ConfigurationService {
   private static final String BUNDLED_RESOURCE = "config.yml";
   private static final String VERSION_KEY = "config-version";
   private final JavaPlugin plugin;
   private ConfigView view;
   private SemanticVersion loadedVersion;
   private OffhandSettings offhand;
   private DamageReductionSettings damageReduction;
   private AutoPunishSettings autoPunish;
   private AlertSettings alerts;
   private UpdateCheckerSettings updateChecker;
   private LagProtectionSettings lagProtection;
   private DatabaseSettings database;
   private MenuTimeSettings menuTime;
   private boolean debugEnabled;
   private boolean supportCommandEnabled;

   public ConfigurationService(@NotNull JavaPlugin javaPlugin1) {
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
            File file18 = file5;
            String string20 = "Could not create data folder: " + file18.getAbsolutePath();
            IOException ioException16 = new IOException(string20);
            throw ioException16;
         }
      }

      File file3 = file5;
      File file13 = new File(file3, "config.yml");
      File file6 = file13;
      File file17 = file6;
      this.syncWith(file17);
      this.rebuildCache();
   }

   public void reload() throws IOException {
      if (this.view == null) {
         throw new IOException("Config is not initialized");
      }

      File file14 = this.plugin.getDataFolder();
      File file8 = new File(file14, "config.yml");
      File file5 = file8;
      File file11 = file5;
      this.syncWith(file11);
      this.rebuildCache();
   }

   private void syncWith(@NotNull File file1) throws IOException {
      InputStream inputStream7 = this.plugin.getResource("config.yml");

      try {
         InputStream inputStream13 = inputStream7;
         if (inputStream13 == null) {
            throw new IOException("Bundled resource not found: config.yml");
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
            SyncOptions syncOptions49 = SyncOptions.create().versionKey("config-version");
            Consumer consumer53 = arg1x -> {
               Logger logger5 = this.plugin.getLogger();
               String string6 = "[Config] " + arg1x;
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
      boolean flag15 = this.view.getBoolean("debug.enabled", false);
      this.debugEnabled = flag15;
      boolean flag18 = this.view.getBoolean("support-command", true);
      this.supportCommandEnabled = flag18;
      OffhandSettings offhandSettings21 = OffhandSettings.load(this.view);
      this.offhand = offhandSettings21;
      DamageReductionSettings damageReductionSettings24 = DamageReductionSettings.load(this.view);
      this.damageReduction = damageReductionSettings24;
      this.autoPunish = AutoPunishSettings.load(this.view);
      AlertSettings alertSettings27 = AlertSettings.load(this.view);
      this.alerts = alertSettings27;
      UpdateCheckerSettings updateCheckerSettings30 = UpdateCheckerSettings.load(this.view);
      this.updateChecker = updateCheckerSettings30;
      LagProtectionSettings lagProtectionSettings33 = LagProtectionSettings.load(this.view);
      this.lagProtection = lagProtectionSettings33;
      DatabaseSettings databaseSettings36 = DatabaseSettings.load(this.view);
      this.database = databaseSettings36;
      MenuTimeSettings menuTimeSettings39 = MenuTimeSettings.load(this.view);
      this.menuTime = menuTimeSettings39;
   }

   @NotNull
   public String getConfigVersion() {
      return this.loadedVersion.toString();
   }

   @NotNull
   public String getLicenseKey() {
      return this.view.getString("license-key", "FILL_HERE");
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

   @Generated
   public OffhandSettings getOffhand() {
      return this.offhand;
   }

   public AutoPunishSettings getAutoPunish() {
      return this.autoPunish;
   }

   @Generated
   public DamageReductionSettings getDamageReduction() {
      return this.damageReduction;
   }

   @Generated
   public AlertSettings getAlerts() {
      return this.alerts;
   }

   @Generated
   public UpdateCheckerSettings getUpdateChecker() {
      return this.updateChecker;
   }

   @Generated
   public LagProtectionSettings getLagProtection() {
      return this.lagProtection;
   }

   @Generated
   public DatabaseSettings getDatabase() {
      return this.database;
   }

   @Generated
   public MenuTimeSettings getMenuTime() {
      return this.menuTime;
   }

   @Generated
   public boolean isDebugEnabled() {
      return this.debugEnabled;
   }

   @Generated
   public boolean isSupportCommandEnabled() {
      return this.supportCommandEnabled;
   }
}
