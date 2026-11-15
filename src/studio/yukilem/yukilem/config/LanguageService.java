package studio.glace.glantielytratarget.config;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Generated;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.config.ConfigurationService;
import studio.glace.glantielytratarget.config.LanguageYamlMigration;
import studio.glace.glantielytratarget.libs.nixlib.config.template.SyncOptions;
import studio.glace.glantielytratarget.libs.nixlib.config.template.SyncResult;
import studio.glace.glantielytratarget.libs.nixlib.config.template.TemplateSync;
import studio.glace.glantielytratarget.libs.nixlib.config.yaml.Nodes;
import studio.glace.glantielytratarget.libs.nixlib.utils.ColorUtils;

public final class LanguageService {
    private static final String[] AVAILABLE_LANGS;
    private static final String DEFAULT_PREFIX = "<gradient:#E82E3D:#F8575E><bold>glAET</bold></gradient> <dark_gray>\u00bb";
    private static final String LANGUAGE_VERSION_KEY = "language-version";
    private final JavaPlugin plugin;
    private final ConfigurationService configurationService;
    private final Map<String, Map<String, Object>> languageData;
    private final Map<String, Component> componentCache;
    private String currentLanguage;
    private String prefix;
    private boolean startupSummaryLogged;

    public LanguageService(@NotNull JavaPlugin javaPlugin, @NotNull ConfigurationService configurationService) {
        HashMap<String, Map<String, Object>> hashMap = new HashMap<String, Map<String, Object>>();
        this.languageData = hashMap;
        ConcurrentHashMap<String, Component> concurrentHashMap = new ConcurrentHashMap<String, Component>();
        this.componentCache = concurrentHashMap;
        this.plugin = javaPlugin;
        this.configurationService = configurationService;
    }

    public void initialize() {
        this.initializeInternal();
        this.logStartupSummary();
    }

    public void initializeSilent() {
        this.initializeInternal();
    }

    private void initializeInternal() {
        try {
            int n;
            int n2;
            String[] stringArray;
            File file;
            File file2;
            File file3;
            String string;
            this.currentLanguage = string = this.configurationService.getString("language", "en");
            File dataFolder = this.plugin.getDataFolder();
            File file4 = file3 = (file2 = new File(dataFolder, "lang"));
            if (!file4.exists() && !(file = file3).mkdirs()) {
                this.plugin.getLogger().warning("Could not create lang directory");
            }
            String[] stringArray2 = stringArray = AVAILABLE_LANGS;
            int length = stringArray2.length;
            int n3 = 0;
            while ((n2 = n3) < (n = length)) {
                String[] stringArray3 = stringArray;
                int n4 = n3++;
                String string2 = stringArray3[n4];
                this.loadLanguageFile(string2);
            }
            this.updatePrefix();
        }
        catch (Exception e) {
            Exception e2 = e;
            Logger logger = this.plugin.getLogger();
            Exception e3 = e2;
            String string = "Failed to load language files: " + e3.getMessage();
            logger.severe(string);
        }
    }

    public void logStartupSummary() {
        int n;
        int n2;
        String[] stringArray;
        if (this.startupSummaryLogged) {
            return;
        }
        String[] stringArray2 = stringArray = AVAILABLE_LANGS;
        int length = stringArray2.length;
        int n3 = 0;
        while ((n2 = ++n3) < (n = length)) {
            Map<String, Map<String, Object>> map = this.languageData;
            String[] stringArray3 = stringArray;
            int n4 = n3;
            String string = stringArray3[n4];
            String string2 = string;
            Map<String, Object> map2 = map.get(string2);
            Map<String, Object> map3 = map2;
            if (map3 == null) continue;
            Map<String, Object> map4 = map2;
            Object object = Nodes.get(map4, LANGUAGE_VERSION_KEY);
            Logger logger = this.plugin.getLogger();
            String string3 = string;
            Object object2 = object;
            Object object3 = object2 == null ? "unknown" : object;
            String string4 = String.valueOf(object3);
            String string5 = "  Loaded: " + string3 + " (v" + string4 + ")";
            logger.info(string5);
        }
        Logger logger = this.plugin.getLogger();
        String string = "All language files loaded! Current language: " + this.currentLanguage;
        logger.info(string);
        this.startupSummaryLogged = true;
    }

    public void reload() {
        try {
            int n;
            int n2;
            String[] stringArray;
            String string;
            String string2 = this.currentLanguage;
            this.currentLanguage = string = this.configurationService.getString("language", "en");
            this.plugin.getLogger().info("Reloading language files...");
            Logger logger = this.plugin.getLogger();
            String string3 = string2;
            String string4 = this.currentLanguage;
            String string5 = "Language changed from '" + string3 + "' to '" + string4 + "'";
            logger.info(string5);
            int n3 = 0;
            String[] stringArray2 = stringArray = AVAILABLE_LANGS;
            int length = stringArray2.length;
            int n4 = 0;
            while ((n2 = ++n4) < (n = length)) {
                String[] stringArray3 = stringArray;
                int n5 = n4;
                String string6 = stringArray3[n5];
                try {
                    String string7 = string6;
                    this.loadLanguageFile(string7);
                    ++n3;
                }
                catch (Exception e) {
                    Exception e2 = e;
                    Logger logger2 = this.plugin.getLogger();
                    String string8 = string6;
                    Exception e3 = e2;
                    String message = e3.getMessage();
                    String string9 = "Failed to reload language: " + string8 + " - " + message;
                    logger2.warning(string9);
                }
            }
            this.updatePrefix();
            Logger logger3 = this.plugin.getLogger();
            int n6 = n3;
            int length2 = AVAILABLE_LANGS.length;
            String string10 = "Language files reloaded! (" + n6 + "/" + length2 + ")";
            logger3.info(string10);
        }
        catch (Exception e) {
            Exception e4 = e;
            Logger logger = this.plugin.getLogger();
            Exception e5 = e4;
            String string = "Failed to reload language files: " + e5.getMessage();
            logger.severe(string);
        }
    }

    private void loadLanguageFile(@NotNull String string) throws IOException {
        block11: {
            InputStream inputStream;
            File file;
            String string3 = "lang_" + string + ".yml";
            File dataFolder = this.plugin.getDataFolder();
            String string4 = string3;
            String string5 = "lang/" + string4;
            File file2 = file = new File(dataFolder, string5);
            JavaPlugin javaPlugin = this.plugin;
            String string6 = string3;
            String string7 = "lang/" + string6;
            InputStream resource = javaPlugin.getResource(string7);
            try {
                SyncResult syncResult;
                String string8;
                String string9;
                SyncResult syncResult2;
                Map<String, Object> map;
                SyncResult syncResult3;
                String string10;
                String string11;
                String string12;
                InputStream inputStream2 = resource;
                if (inputStream2 == null) {
                    String string13 = string3;
                    String string14 = "Bundled language resource missing: " + string13;
                    IOException e = new IOException(string14);
                    throw e;
                }
                InputStream inputStream3 = resource;
                String string15 = LanguageService.readAll(inputStream3);
                File file3 = file2;
                if (file3.exists()) {
                    File file4 = file2;
                    Path path = file4.toPath();
                    Charset charset = StandardCharsets.UTF_8;
                    string12 = Files.readString(path, charset);
                } else {
                    string12 = null;
                }
                String string16 = string11 = string12;
                String string17 = string10 = LanguageYamlMigration.migrateUserText(string16);
                if (string17 != null) {
                    String string18;
                    string11 = string18 = string10;
                }
                String string19 = string15;
                String string20 = string11;
                SyncOptions syncOptions = SyncOptions.create().versionKey(LANGUAGE_VERSION_KEY);
                Consumer<String> consumer = warning -> {
                    Logger logger = this.plugin.getLogger();
                    String line = "[Lang " + string + "] " + warning;
                    logger.warning(line);
                };
                SyncOptions syncOptions2 = syncOptions.onWarning(consumer);
                SyncResult syncResult4 = syncResult3 = TemplateSync.synchronize(string19, string20, syncOptions2);
                Map<String, Object> map2 = map = syncResult4.mergedData();
                boolean bl = LanguageYamlMigration.normalizeMenuKeys(map2);
                SyncResult syncResult5 = syncResult3;
                String string21 = LanguageYamlMigration.ensureQuotedMenuYesNo(syncResult5.mergedYaml());
                String string22 = string11;
                boolean bl2 = string22 == null || (syncResult2 = syncResult3).changed() || !(string9 = string21).equals(string8 = (syncResult = syncResult3).mergedYaml());
                boolean bl3 = bl2;
                boolean bl4 = bl3;
                if (bl4) {
                    File file5 = file2;
                    if (file5.getParentFile() != null) {
                        File file6 = file2;
                        boolean bl5 = file6.getParentFile().mkdirs();
                    }
                    File file7 = file2;
                    Path path = file7.toPath();
                    String string23 = string21;
                    Charset charset = StandardCharsets.UTF_8;
                    OpenOption[] openOptionArray = new OpenOption[]{};
                    Path path2 = Files.writeString(path, (CharSequence)string23, charset, openOptionArray);
                }
                Map<String, Map<String, Object>> map3 = this.languageData;
                Map<String, Object> map4 = map;
                Map<String, Object> map5 = map3.put(string, map4);
                InputStream inputStream4 = resource;
                if (inputStream4 == null) break block11;
                inputStream = resource;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                InputStream inputStream5 = resource;
                if (inputStream5 != null) {
                    try {
                        InputStream inputStream6 = resource;
                        inputStream6.close();
                    }
                    catch (Throwable e3) {
                        Throwable e4 = e3;
                        Throwable e5 = e2;
                        Throwable e6 = e4;
                        e5.addSuppressed(e6);
                    }
                }
                throw e;
            }
            inputStream.close();
        }
    }

    private void updatePrefix() {
        String string;
        Object object;
        Object object2;
        Map<String, Object> map;
        Map<String, Map<String, Object>> map2 = this.languageData;
        String string2 = this.currentLanguage;
        Map<String, Object> map3 = map2.get(string2);
        Map<String, Object> map4 = map3;
        if (map4 == null) {
            map3 = this.languageData.get("en");
        }
        if ((map = map3) == null) {
            object2 = null;
        } else {
            Map<String, Object> map5 = map3;
            object2 = Nodes.get(map5, "prefix");
        }
        Object object3 = object = object2;
        if (object3 == null) {
            string = DEFAULT_PREFIX;
        } else {
            Object object4 = object;
            string = String.valueOf(object4);
        }
        this.prefix = string;
        this.componentCache.clear();
    }

    @NotNull
    public String getRawMessage(@NotNull String string) {
        String string2;
        String string3;
        String string4;
        String string5 = this.currentLanguage;
        String string6 = this.lookupString(string5, string);
        String string7 = string6;
        if (string7 == null && !"en".equals(string4 = this.currentLanguage)) {
            string6 = this.lookupString("en", string);
        }
        if ((string3 = string6) == null) {
            Logger logger = this.plugin.getLogger();
            String string8 = this.currentLanguage;
            String string9 = "Message not found: " + string + " (Language: " + string8 + ")";
            logger.warning(string9);
            return "<#FF3333>Message not found: <#BA2525>" + string;
        }
        String string10 = string6;
        if (string10.contains("{prefix}")) {
            String string11 = string6;
            String string12 = this.prefix;
            string2 = string11.replace("{prefix}", string12);
        } else {
            string2 = string6;
        }
        return string2;
    }

    @NotNull
    public Component getMessage(@NotNull String string) {
        Component component = this.componentCache.get(string);
        Component component2 = component;
        if (component2 != null) {
            Component component3 = component;
            return component3;
        }
        Component component4 = ColorUtils.translate(this.getRawMessage(string));
        Map<String, Component> map = this.componentCache;
        Component component5 = component4;
        Component component6 = map.put(string, component5);
        Component component7 = component4;
        return component7;
    }

    @NotNull
    public Component getMessage(@NotNull String string, @NotNull String string2) {
        String string3;
        String string4 = this.currentLanguage;
        String string5 = this.lookupString(string4, string);
        String string6 = string5;
        if (string6 == null) {
            string5 = string2;
        }
        if ((string3 = string5).contains("{prefix}")) {
            String string7 = string5;
            String string8 = this.prefix;
            string5 = string7.replace("{prefix}", string8);
        }
        String string9 = string5;
        return ColorUtils.translate(string9);
    }

    @NotNull
    public Component message(@NotNull String string, @NotNull Map<String, String> map) {
        return ColorUtils.translate(LanguageService.applyPlaceholders(this.getRawMessage(string), map));
    }

    @NotNull
    public List<Component> getMessageList(@NotNull String key) {
        Stream<String> stream = this.collectList(key).stream();
        Function<String, Component> function = string -> {
            String string2;
            if (string.contains("{prefix}")) {
                String string3 = this.prefix;
                string2 = string.replace("{prefix}", string3);
            } else {
                string2 = string;
            }
            return ColorUtils.translate(string2);
        };
        Stream<Component> stream2 = stream.map(function);
        return stream2.collect(Collectors.toList());
    }

    @NotNull
    public List<String> getRawMessageList(@NotNull String key) {
        Stream<String> stream = this.collectList(key).stream();
        Function<String, String> function = string -> {
            String string2;
            if (string.contains("{prefix}")) {
                String string3 = this.prefix;
                string2 = string.replace("{prefix}", string3);
            } else {
                string2 = string;
            }
            return string2;
        };
        Stream<String> stream2 = stream.map(function);
        return stream2.collect(Collectors.toList());
    }

    @NotNull
    public List<Component> messageList(@NotNull String key, @NotNull Map<String, String> map) {
        Stream<String> stream = this.collectList(key).stream();
        Function<String, Component> function = string -> {
            String string2;
            if (string.contains("{prefix}")) {
                String string3 = this.prefix;
                string2 = string.replace("{prefix}", string3);
            } else {
                string2 = string;
            }
            return ColorUtils.translate(LanguageService.applyPlaceholders(string2, map));
        };
        Stream<Component> stream2 = stream.map(function);
        return stream2.collect(Collectors.toList());
    }

    @NotNull
    private static String applyPlaceholders(@NotNull String string, @NotNull Map<String, String> map) {
        Iterator<Map.Entry<String, String>> iterator;
        String string2 = string;
        Iterator<Map.Entry<String, String>> iterator2 = map.entrySet().iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Map.Entry<String, String>> iterator3 = iterator2;
            Map.Entry<String, String> entry = iterator3.next();
            String string3 = string2;
            Map.Entry<String, String> entry2 = entry;
            CharSequence key = entry2.getKey();
            Map.Entry<String, String> entry3 = entry;
            CharSequence value = entry3.getValue();
            string2 = string3.replace(key, value);
        }
        String string4 = string2;
        return string4;
    }

    @NotNull
    private List<String> collectList(@NotNull String string) {
        List<String> list;
        String string2;
        String string3 = this.currentLanguage;
        List<String> list2 = this.lookupList(string3, string);
        List<String> list3 = list2;
        if (list3.isEmpty() && !"en".equals(string2 = this.currentLanguage)) {
            list2 = this.lookupList("en", string);
        }
        if ((list = list2).isEmpty()) {
            Logger logger = this.plugin.getLogger();
            String string4 = this.currentLanguage;
            String string5 = "Message list not found: " + string + " (Language: " + string4 + ")";
            logger.warning(string5);
        }
        List<String> list4 = list2;
        return list4;
    }

    private String lookupString(@NotNull String string, @NotNull String string2) {
        String string3;
        Map<String, Object> map = this.languageData.get(string);
        Map<String, Object> map2 = map;
        if (map2 == null) {
            return null;
        }
        Map<String, Object> map3 = map;
        Object object = Nodes.get(map3, string2);
        Object object2 = object;
        if (object2 == null) {
            string3 = null;
        } else {
            Object object3 = object;
            string3 = String.valueOf(object3);
        }
        return string3;
    }

    @NotNull
    private List<String> lookupList(@NotNull String string, @NotNull String string2) {
        Iterator iterator;
        ArrayList<String> arrayList;
        Map<String, Object> map = this.languageData.get(string);
        Map<String, Object> map2 = map;
        if (map2 == null) {
            return List.of();
        }
        Map<String, Object> map3 = map;
        Object object = Nodes.get(map3, string2);
        Object object2 = object;
        if (!(object2 instanceof List)) {
            return List.of();
        }
        Object object3 = object;
        List list = (List)object3;
        List list2 = list;
        int size = list2.size();
        ArrayList<String> arrayList2 = arrayList = new ArrayList<String>(size);
        List list3 = list;
        Iterator iterator2 = list3.iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator iterator3 = iterator2;
            Object e = iterator3.next();
            Object e2 = e;
            if (e2 == null) continue;
            ArrayList<String> arrayList3 = arrayList2;
            Object e3 = e;
            String string3 = String.valueOf(e3);
            boolean bl = arrayList3.add(string3);
        }
        ArrayList<String> arrayList4 = arrayList2;
        return arrayList4;
    }

    private static String readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream;
        String string;
        ByteArrayOutputStream byteArrayOutputStream2;
        int n = inputStream.available();
        int n2 = Math.max(64, n);
        ByteArrayOutputStream byteArrayOutputStream3 = byteArrayOutputStream2 = new ByteArrayOutputStream(n2);
        try {
            byte[] bArray = new byte[4096];
            while (true) {
                int n3;
                byte[] bArray2 = bArray;
                int n4 = n3 = inputStream.read(bArray2);
                if (n3 == -1) break;
                ByteArrayOutputStream byteArrayOutputStream4 = byteArrayOutputStream3;
                byte[] bArray3 = bArray;
                int n5 = n4;
                byteArrayOutputStream4.write(bArray3, 0, n5);
            }
            ByteArrayOutputStream byteArrayOutputStream5 = byteArrayOutputStream3;
            Charset charset = StandardCharsets.UTF_8;
            string = byteArrayOutputStream5.toString(charset);
            byteArrayOutputStream = byteArrayOutputStream3;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            try {
                ByteArrayOutputStream byteArrayOutputStream6 = byteArrayOutputStream3;
                byteArrayOutputStream6.close();
            }
            catch (Throwable e3) {
                Throwable e4 = e3;
                Throwable e5 = e2;
                Throwable e6 = e4;
                e5.addSuppressed(e6);
            }
            throw e;
        }
        byteArrayOutputStream.close();
        String string2 = string;
        return string2;
    }

    @Generated
    public String getCurrentLanguage() {
        return this.currentLanguage;
    }

    @Generated
    public String getPrefix() {
        return this.prefix;
    }

    static {
        String[] stringArray = new String[]{"en", "fr", "es", "pl", "tr"};
        AVAILABLE_LANGS = stringArray;
    }
}

