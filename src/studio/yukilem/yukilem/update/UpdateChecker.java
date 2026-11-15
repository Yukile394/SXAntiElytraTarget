package studio.yukilem.yukilem.update;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import lombok.Generated;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import studio.yukilem.yukilem.libs.nixlib.utils.ColorUtils;
import studio.yukilem.yukilem.platform.PlatformScheduler;

public final class UpdateChecker {
    private static final String GIST_URL = "https://gist.githubusercontent.com/Nixoly/17ee34dddbf91b9d728939224c08bb80/raw/gistfile1.txt";
    private static final int TIMEOUT_MS = 5000;
    private final PluginContext ctx;
    private final String pluginName;
    private final String currentVersion;
    private final Cache<String, Map<String, String>> versionCache;
    private volatile String latestVersion;
    private volatile boolean updateAvailable;

    public UpdateChecker(@NotNull PluginContext pluginContext, @NotNull String string, @NotNull String string2) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        long l = 5L;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        Caffeine<Object, Object> caffeine2 = caffeine.expireAfterWrite(l, timeUnit);
        long l2 = 1L;
        Cache cache = caffeine2.maximumSize(l2).build();
        this.versionCache = cache;
        this.ctx = pluginContext;
        this.pluginName = string;
        this.currentVersion = string2;
    }

    public void checkForUpdates(@Nullable BiConsumer<Boolean, String> biConsumer) {
        PlatformScheduler platformScheduler = this.ctx.scheduler();
        Runnable runnable = () -> {
            block6: {
                try {
                    String string;
                    String string2;
                    Map<String, String> map = this.fetchVersions();
                    String string3 = this.pluginName;
                    String string4 = string2 = map.get(string3);
                    if (string4 != null && !(string = string2).isEmpty()) {
                        boolean bl;
                        String string5;
                        this.latestVersion = string5 = string2;
                        String string6 = string2;
                        String string7 = this.currentVersion;
                        this.updateAvailable = bl = this.isNewerVersion(string6, string7);
                        if (biConsumer != null) {
                            Boolean bl2 = this.updateAvailable;
                            String string8 = this.latestVersion;
                            biConsumer.accept(bl2, string8);
                        }
                    } else if (biConsumer != null) {
                        Boolean bl = false;
                        biConsumer.accept(bl, null);
                    }
                }
                catch (Exception e) {
                    Exception e2 = e;
                    Logger logger = this.ctx.plugin().getLogger();
                    Exception e3 = e2;
                    String string = "Failed to check for updates: " + e3.getMessage();
                    logger.warning(string);
                    if (biConsumer == null) break block6;
                    Boolean bl = false;
                    biConsumer.accept(bl, null);
                }
            }
        };
        platformScheduler.runAsync(runnable);
    }

    private Map<String, String> fetchVersions() throws Exception {
        Map<String, String> ifPresent = this.versionCache.getIfPresent("ALL");
        Map<String, String> map = ifPresent;
        if (map != null) {
            Map<String, String> map2 = ifPresent;
            return map2;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        HttpURLConnection connection = null;
        try {
            BufferedReader bufferedReader;
            BufferedReader bufferedReader2;
            int n;
            HttpURLConnection connection2 = connection = (HttpURLConnection)URI.create("https://gist.githubusercontent.com/Nixoly/17ee34dddbf91b9d728939224c08bb80/raw/gistfile1.txt?t=" + System.currentTimeMillis()).toURL().openConnection();
            connection2.setRequestMethod("GET");
            HttpURLConnection connection3 = connection;
            connection3.setRequestProperty("User-Agent", "UpdateChecker/1.0");
            HttpURLConnection connection4 = connection;
            connection4.setRequestProperty("Cache-Control", "no-cache");
            HttpURLConnection connection5 = connection;
            connection5.setConnectTimeout(5000);
            HttpURLConnection connection6 = connection;
            connection6.setReadTimeout(5000);
            HttpURLConnection connection7 = connection;
            connection7.setUseCaches(false);
            HttpURLConnection connection8 = connection;
            int n2 = n = connection8.getResponseCode();
            if (n2 != 200) {
                int n3 = n;
                String string = "HTTP response code: " + n3;
                Exception e = new Exception(string);
                throw e;
            }
            HttpURLConnection connection9 = connection;
            InputStream inputStream = connection9.getInputStream();
            Charset charset = StandardCharsets.UTF_8;
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
            BufferedReader bufferedReader3 = bufferedReader2 = new BufferedReader(inputStreamReader);
            try {
                while (true) {
                    String string;
                    String string2;
                    int n4;
                    int n5;
                    String string3;
                    int n6;
                    int n7;
                    String string4;
                    String string5;
                    BufferedReader bufferedReader4 = bufferedReader3;
                    String string6 = string5 = bufferedReader4.readLine();
                    if (string5 == null) break;
                    String string7 = string6;
                    String string8 = string7.trim();
                    String string9 = string8;
                    if (string9.isEmpty() || !(string4 = string8).contains(":") || (n7 = (n6 = (string3 = string8).indexOf(58))) <= 0 || (n5 = n6) >= (n4 = (string2 = string8).length() - 1)) continue;
                    String string10 = string8;
                    int n8 = n6;
                    String string11 = string10.substring(0, n8).trim();
                    String string12 = string8;
                    int n9 = n6;
                    int n10 = n9 + 1;
                    String string13 = string12.substring(n10).trim();
                    String string14 = string11;
                    if (string14.isEmpty() || (string = string13).isEmpty()) continue;
                    HashMap<String, String> hashMap2 = hashMap;
                    String string15 = string11;
                    String string16 = string13;
                    String string17 = hashMap2.put(string15, string16);
                }
                bufferedReader = bufferedReader3;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                try {
                    BufferedReader bufferedReader5 = bufferedReader3;
                    bufferedReader5.close();
                }
                catch (Throwable e3) {
                    Throwable e4 = e3;
                    Throwable e5 = e2;
                    Throwable e6 = e4;
                    e5.addSuppressed(e6);
                }
                throw e;
            }
            bufferedReader.close();
            Cache<String, Map<String, String>> cache = this.versionCache;
            HashMap<String, String> hashMap3 = hashMap;
            cache.put("ALL", hashMap3);
            HttpURLConnection connection10 = connection;
            if (connection10 != null) {
                HttpURLConnection connection11 = connection;
                connection11.disconnect();
            }
        }
        catch (Throwable e) {
            Throwable e8 = e;
            HttpURLConnection connection12 = connection;
            if (connection12 != null) {
                HttpURLConnection connection13 = connection;
                connection13.disconnect();
            }
            throw e;
        }
        HashMap<String, String> hashMap4 = hashMap;
        return hashMap4;
    }

    private boolean isNewerVersion(@NotNull String string, @NotNull String string2) {
        int n;
        int n2;
        String[] stringArray = string.split("\\.");
        String[] stringArray2 = string2.split("\\.");
        String[] stringArray3 = stringArray;
        int length = stringArray3.length;
        String[] stringArray4 = stringArray2;
        int length2 = stringArray4.length;
        int n3 = Math.max(length, length2);
        int n4 = 0;
        while ((n2 = ++n4) < (n = n3)) {
            int n5;
            int n6;
            int n7 = n4;
            String[] stringArray5 = stringArray;
            int length3 = stringArray5.length;
            if (n7 < length3) {
                String[] stringArray6 = stringArray;
                int n8 = n4;
                String string3 = stringArray6[n8];
                n6 = this.parsePart(string3);
            } else {
                n6 = 0;
            }
            int n9 = n6;
            int n10 = n4;
            String[] stringArray7 = stringArray2;
            int length4 = stringArray7.length;
            if (n10 < length4) {
                String[] stringArray8 = stringArray2;
                int n11 = n4;
                String string4 = stringArray8[n11];
                n5 = this.parsePart(string4);
            } else {
                n5 = 0;
            }
            int n12 = n5;
            int n13 = n9;
            int n14 = n12;
            if (n13 > n14) {
                return true;
            }
            int n15 = n9;
            int n16 = n12;
            if (n15 >= n16) continue;
            return false;
        }
        return false;
    }

    private int parsePart(@NotNull String string) {
        try {
            int n = Integer.parseInt(string.replaceAll("[^0-9]", ""));
            return n;
        }
        catch (NumberFormatException e) {
            NumberFormatException e2 = e;
            return 0;
        }
    }

    public void notifyPlayer(@Nullable Player player, @Nullable String string) {
        if (player != null && player.isOnline() && this.updateAvailable && string != null) {
            Component component = ColorUtils.translate(string);
            player.sendMessage(component);
        }
    }

    public void clearCache() {
        this.versionCache.invalidateAll();
    }

    @Generated
    public String getCurrentVersion() {
        return this.currentVersion;
    }

    @Generated
    public String getLatestVersion() {
        return this.latestVersion;
    }

    @Generated
    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }
}

