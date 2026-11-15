package studio.yukilem.bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import studio.yukilem.MetricsBase;
import studio.yukilem.charts.CustomChart;
import studio.yukilem.json.JsonObjectBuilder;

public class Metrics {
    private final Plugin plugin;
    private final MetricsBase metricsBase;

    public Metrics(Plugin plugin, int n) {
        MetricsBase metricsBase;
        YamlConfiguration yamlConfiguration;
        File file;
        File file2;
        File file3;
        File file4;
        this.plugin = plugin;
        File parentFile = plugin.getDataFolder().getParentFile();
        File file5 = file4 = (file3 = new File(parentFile, "bStats"));
        File file6 = file2 = (file = new File(file5, "config.yml"));
        YamlConfiguration yamlConfiguration2 = yamlConfiguration = YamlConfiguration.loadConfiguration(file6);
        if (!yamlConfiguration2.isSet("serverUuid")) {
            YamlConfiguration yamlConfiguration3 = yamlConfiguration;
            Boolean bl = true;
            yamlConfiguration3.addDefault("enabled", bl);
            YamlConfiguration yamlConfiguration4 = yamlConfiguration;
            String string2 = UUID.randomUUID().toString();
            yamlConfiguration4.addDefault("serverUuid", string2);
            YamlConfiguration yamlConfiguration5 = yamlConfiguration;
            Boolean bl2 = false;
            yamlConfiguration5.addDefault("logFailedRequests", bl2);
            YamlConfiguration yamlConfiguration6 = yamlConfiguration;
            Boolean bl3 = false;
            yamlConfiguration6.addDefault("logSentData", bl3);
            YamlConfiguration yamlConfiguration7 = yamlConfiguration;
            Boolean bl4 = false;
            yamlConfiguration7.addDefault("logResponseStatusText", bl4);
            YamlConfiguration yamlConfiguration8 = yamlConfiguration;
            YamlConfigurationOptions yamlConfigurationOptions = yamlConfiguration8.options().header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.\nLearn more here: https://bstats.org/docs/server-owners").copyDefaults(true);
            try {
                YamlConfiguration yamlConfiguration9 = yamlConfiguration;
                File file7 = file2;
                yamlConfiguration9.save(file7);
            }
            catch (IOException e2) {
                IOException e3 = e2;
            }
        }
        YamlConfiguration yamlConfiguration10 = yamlConfiguration;
        boolean boolean_ = yamlConfiguration10.getBoolean("enabled", true);
        YamlConfiguration yamlConfiguration11 = yamlConfiguration;
        String string3 = yamlConfiguration11.getString("serverUuid");
        YamlConfiguration yamlConfiguration12 = yamlConfiguration;
        boolean boolean_2 = yamlConfiguration12.getBoolean("logFailedRequests", false);
        YamlConfiguration yamlConfiguration13 = yamlConfiguration;
        boolean boolean_3 = yamlConfiguration13.getBoolean("logSentData", false);
        YamlConfiguration yamlConfiguration14 = yamlConfiguration;
        boolean boolean_4 = yamlConfiguration14.getBoolean("logResponseStatusText", false);
        boolean bl = false;
        try {
            boolean bl5 = Class.forName("io.papermc.paper.threadedregions.RegionizedServer") != null;
            bl = bl5;
        }
        catch (Exception e4) {
            Exception e5 = e4;
        }
        String string4 = string3;
        boolean bl6 = boolean_;
        Consumer<JsonObjectBuilder> consumer = this::appendPlatformData;
        Consumer<JsonObjectBuilder> consumer2 = this::appendServiceData;
        boolean bl7 = bl;
        Consumer<Runnable> consumer3 = bl7 ? null : runnable -> {
            BukkitTask bukkitTask = Bukkit.getScheduler().runTask(plugin, (Runnable)runnable);
        };
        Plugin plugin2 = Objects.requireNonNull(plugin);
        Supplier<Boolean> supplier = plugin::isEnabled;
        BiConsumer<String, Throwable> biConsumer = (string, e) -> {
            Logger logger = this.plugin.getLogger();
            Level level = Level.WARNING;
            logger.log(level, (String)string, (Throwable)e);
        };
        Consumer<String> consumer4 = string -> {
            Logger logger = this.plugin.getLogger();
            Level level = Level.INFO;
            logger.log(level, (String)string);
        };
        boolean bl8 = boolean_2;
        boolean bl9 = boolean_3;
        boolean bl10 = boolean_4;
        this.metricsBase = metricsBase = new MetricsBase("bukkit", string4, n, bl6, consumer, consumer2, consumer3, supplier, biConsumer, consumer4, bl8, bl9, bl10, false);
    }

    public void shutdown() {
        this.metricsBase.shutdown();
    }

    public void addCustomChart(CustomChart customChart) {
        this.metricsBase.addCustomChart(customChart);
    }

    private void appendPlatformData(JsonObjectBuilder jsonObjectBuilder) {
        int playerAmount = this.getPlayerAmount();
        JsonObjectBuilder jsonObjectBuilder2 = jsonObjectBuilder.appendField("playerAmount", playerAmount);
        int n = Bukkit.getOnlineMode() ? 1 : 0;
        JsonObjectBuilder jsonObjectBuilder3 = jsonObjectBuilder.appendField("onlineMode", n);
        String string = Bukkit.getVersion();
        JsonObjectBuilder jsonObjectBuilder4 = jsonObjectBuilder.appendField("bukkitVersion", string);
        String string2 = Bukkit.getName();
        JsonObjectBuilder jsonObjectBuilder5 = jsonObjectBuilder.appendField("bukkitName", string2);
        String string3 = System.getProperty("java.version");
        JsonObjectBuilder jsonObjectBuilder6 = jsonObjectBuilder.appendField("javaVersion", string3);
        String string4 = System.getProperty("os.name");
        JsonObjectBuilder jsonObjectBuilder7 = jsonObjectBuilder.appendField("osName", string4);
        String string5 = System.getProperty("os.arch");
        JsonObjectBuilder jsonObjectBuilder8 = jsonObjectBuilder.appendField("osArch", string5);
        String string6 = System.getProperty("os.version");
        JsonObjectBuilder jsonObjectBuilder9 = jsonObjectBuilder.appendField("osVersion", string6);
        int n2 = Runtime.getRuntime().availableProcessors();
        JsonObjectBuilder jsonObjectBuilder10 = jsonObjectBuilder.appendField("coreCount", n2);
    }

    private void appendServiceData(JsonObjectBuilder jsonObjectBuilder) {
        String version = this.plugin.getDescription().getVersion();
        JsonObjectBuilder jsonObjectBuilder2 = jsonObjectBuilder.appendField("pluginVersion", version);
    }

    private int getPlayerAmount() {
        try {
            int n;
            Method method;
            Class<?> clazz = Class.forName("org.bukkit.Server");
            Class[] clazzArray = new Class[]{};
            Method method2 = method = clazz.getMethod("getOnlinePlayers", clazzArray);
            Class<?> returnType = method2.getReturnType();
            Class<Collection> clazz2 = Collection.class;
            if (returnType.equals(clazz2)) {
                Method method3 = method;
                Server server = Bukkit.getServer();
                Object[] objectArray = new Object[]{};
                n = ((Collection)method3.invoke((Object)server, objectArray)).size();
            } else {
                Method method4 = method;
                Server server = Bukkit.getServer();
                Object[] objectArray = new Object[]{};
                n = ((Player[])method4.invoke((Object)server, objectArray)).length;
            }
            return n;
        }
        catch (Exception e) {
            Exception e2 = e;
            return Bukkit.getOnlinePlayers().size();
        }
    }
}

