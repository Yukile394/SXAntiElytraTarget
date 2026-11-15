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
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncOptions;
import studio.yukilem.yukilem.libs.nixlib.config.template.SyncResult;
import studio.yukilem.yukilem.libs.nixlib.config.template.TemplateSync;

public final class MenuConfigurationService {
   private static final String VERSION_KEY = "menu-version";
   private final JavaPlugin plugin;
   private Map<String, Object> profileMenu;
   private Map<String, Object> violationsMenu;
   private Map<String, Object> confirmMenu;

   public MenuConfigurationService(@NotNull JavaPlugin javaPlugin1) {
      this.plugin = javaPlugin1;
   }

   public void initialize() throws IOException {
      String[] strings17 = new String[]{"items", "filler", "player-info"};
      Map map10 = this.load("menus/profile_menu.yml", strings17);
      this.profileMenu = map10;
      String[] strings19 = new String[]{"items", "filler", "violation-item"};
      Map map12 = this.load("menus/violations_menu.yml", strings19);
      this.violationsMenu = map12;
      String[] strings21 = new String[]{"confirm", "cancel", "filler"};
      Map map14 = this.load("menus/confirm_menu.yml", strings21);
      this.confirmMenu = map14;
   }

   public void reload() throws IOException {
      this.initialize();
   }

   @NotNull
   public Map<String, Object> profileMenu() {
      return this.profileMenu;
   }

   @NotNull
   public Map<String, Object> violationsMenu() {
      return this.violationsMenu;
   }

   @NotNull
   public Map<String, Object> confirmMenu() {
      return this.confirmMenu;
   }

   private Map<String, Object> load(@NotNull String string1, @NotNull String[] strings2) throws IOException {
      File file56 = this.plugin.getDataFolder();
      File file4 = new File(file56, string1);
      File file7 = file4;
      InputStream inputStream8 = this.plugin.getResource(string1);

      Map map12;
      try {
         InputStream inputStream16 = inputStream8;
         if (inputStream16 == null) {
            String string58 = "Bundled resource not found: " + string1;
            IOException ioException17 = new IOException(string58);
            throw ioException17;
         }

         InputStream inputStream24 = inputStream8;
         String string70 = readAll(inputStream24);
         File file26 = file7;
         String string30;
         if (file26.exists()) {
            File file28 = file7;
            Path path29 = file28.toPath();
            Charset charset51 = StandardCharsets.UTF_8;
            string30 = Files.readString(path29, charset51);
         } else {
            string30 = null;
         }

         SyncResult syncResult11;
         label53: {
            String string71 = string30;
            String string31 = string70;
            String string52 = string71;
            SyncOptions syncOptions61 = SyncOptions.create().versionKey("menu-version").freeform(strings2);
            Consumer consumer67 = arg1x -> {
               Logger logger5 = this.plugin.getLogger();
               String string6 = "[Menu] " + arg1x;
               logger5.warning(string6);
            };
            SyncOptions syncOptions62 = syncOptions61.onWarning(consumer67);
            syncResult11 = TemplateSync.synchronize(string31, string52, syncOptions62);
            String string33 = string71;
            if (string33 != null) {
               SyncResult syncResult34 = syncResult11;
               if (!syncResult34.changed()) {
                  break label53;
               }
            }

            File file36 = file7;
            if (file36.getParentFile() != null) {
               File file38 = file7;
               boolean flag40 = file38.getParentFile().mkdirs();
            }

            File file41 = file7;
            Path path42 = file41.toPath();
            SyncResult syncResult53 = syncResult11;
            String string54 = syncResult53.mergedYaml();
            Charset charset63 = StandardCharsets.UTF_8;
            OpenOption[] openOptions69 = new OpenOption[0];
            Path path43 = Files.writeString(path42, string54, charset63, openOptions69);
         }

         SyncResult syncResult44 = syncResult11;
         map12 = syncResult44.mergedData();
      } catch (Throwable throwable73) {
         InputStream inputStream19 = inputStream8;
         if (inputStream19 != null) {
            try {
               InputStream inputStream20 = inputStream8;
               inputStream20.close();
            } catch (Throwable throwable72) {
               throwable73.addSuppressed(throwable72);
            }
         }

         throw throwable73;
      }

      InputStream inputStream46 = inputStream8;
      if (inputStream46 != null) {
         InputStream inputStream47 = inputStream8;
         inputStream47.close();
      }

      return map12;
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
