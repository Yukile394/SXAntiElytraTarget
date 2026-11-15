package studio.yukilem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import studio.yukilem.charts.CustomChart;
import studio.yukilem.json.JsonObjectBuilder;

public class MetricsBase {
    public static final String METRICS_VERSION = "3.2.1";
    private static final String REPORT_URL = "https://bStats.org/api/v2/data/%s";
    private final ScheduledExecutorService scheduler;
    private final String platform;
    private final String serverUuid;
    private final int serviceId;
    private final Consumer<JsonObjectBuilder> appendPlatformDataConsumer;
    private final Consumer<JsonObjectBuilder> appendServiceDataConsumer;
    private final Consumer<Runnable> submitTaskConsumer;
    private final Supplier<Boolean> checkServiceEnabledSupplier;
    private final BiConsumer<String, Throwable> errorLogger;
    private final Consumer<String> infoLogger;
    private final boolean logErrors;
    private final boolean logSentData;
    private final boolean logResponseStatusText;
    private final Set<CustomChart> customCharts;
    private final boolean enabled;

    public MetricsBase(String string, String string2, int n, boolean bl, Consumer<JsonObjectBuilder> consumer, Consumer<JsonObjectBuilder> consumer2, Consumer<Runnable> consumer3, Supplier<Boolean> supplier, BiConsumer<String, Throwable> biConsumer, Consumer<String> consumer4, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor2;
        HashSet<CustomChart> hashSet = new HashSet<CustomChart>();
        this.customCharts = hashSet;
        ThreadFactory threadFactory = runnable -> {
            Thread thread;
            Thread thread2 = thread = new Thread(runnable, "bStats-Metrics");
            thread2.setDaemon(true);
            Thread thread3 = thread;
            return thread3;
        };
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor3 = scheduledThreadPoolExecutor2 = (scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, threadFactory));
        scheduledThreadPoolExecutor3.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor4 = scheduledThreadPoolExecutor2;
        this.scheduler = scheduledThreadPoolExecutor4;
        this.platform = string;
        this.serverUuid = string2;
        this.serviceId = n;
        this.enabled = bl;
        this.appendPlatformDataConsumer = consumer;
        this.appendServiceDataConsumer = consumer2;
        this.submitTaskConsumer = consumer3;
        this.checkServiceEnabledSupplier = supplier;
        this.errorLogger = biConsumer;
        this.infoLogger = consumer4;
        this.logErrors = bl2;
        this.logSentData = bl3;
        this.logResponseStatusText = bl4;
        if (!bl5) {
            this.checkRelocation();
        }
        if (bl) {
            this.startSubmitting();
        }
    }

    public void addCustomChart(CustomChart customChart) {
        boolean bl = this.customCharts.add(customChart);
    }

    public void shutdown() {
        this.scheduler.shutdown();
    }

    private void startSubmitting() {
        Runnable runnable = () -> {
            if (!this.enabled || !this.checkServiceEnabledSupplier.get().booleanValue()) {
                this.scheduler.shutdown();
                return;
            }
            if (this.submitTaskConsumer != null) {
                Consumer<Runnable> consumer = this.submitTaskConsumer;
                Runnable submitTask = this::submitData;
                consumer.accept(submitTask);
            } else {
                this.submitData();
            }
        };
        double d = 60000.0;
        double d2 = 3.0;
        double d3 = Math.random();
        double d4 = 3.0;
        double d5 = d3 * d4;
        double d6 = d2 + d5;
        long l = (long)(d * d6);
        double d7 = 60000.0;
        double d8 = Math.random();
        double d9 = 30.0;
        double d10 = d8 * d9;
        long l2 = (long)(d7 * d10);
        ScheduledExecutorService scheduledExecutorService = this.scheduler;
        Runnable runnable2 = runnable;
        long l3 = l;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(runnable2, l3, timeUnit);
        ScheduledExecutorService scheduledExecutorService2 = this.scheduler;
        Runnable runnable3 = runnable;
        long l4 = l;
        long l5 = l2;
        long l6 = l4 + l5;
        long l7 = 1800000L;
        TimeUnit timeUnit2 = TimeUnit.MILLISECONDS;
        ScheduledFuture<?> scheduledFuture2 = scheduledExecutorService2.scheduleAtFixedRate(runnable3, l6, l7, timeUnit2);
    }

    private void submitData() {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        Consumer<JsonObjectBuilder> consumer = this.appendPlatformDataConsumer;
        JsonObjectBuilder jsonObjectBuilder2 = jsonObjectBuilder;
        consumer.accept(jsonObjectBuilder2);
        JsonObjectBuilder jsonObjectBuilder3 = new JsonObjectBuilder();
        Consumer<JsonObjectBuilder> consumer2 = this.appendServiceDataConsumer;
        JsonObjectBuilder jsonObjectBuilder4 = jsonObjectBuilder3;
        consumer2.accept(jsonObjectBuilder4);
        Stream stream = this.customCharts.stream();
        Function<CustomChart, JsonObjectBuilder.JsonObject> function = customChart -> {
            BiConsumer<String, Throwable> biConsumer = this.errorLogger;
            boolean bl = this.logErrors;
            return customChart.getRequestJsonObject(biConsumer, bl);
        };
        Stream<JsonObjectBuilder.JsonObject> stream2 = stream.map(function);
        Predicate<JsonObjectBuilder.JsonObject> predicate = Objects::nonNull;
        Stream<JsonObjectBuilder.JsonObject> stream3 = stream2.filter(predicate);
        IntFunction<JsonObjectBuilder.JsonObject[]> intFunction = JsonObjectBuilder.JsonObject[]::new;
        JsonObjectBuilder.JsonObject[] jsonObjectArray = (JsonObjectBuilder.JsonObject[])stream3.toArray(intFunction);
        JsonObjectBuilder jsonObjectBuilder5 = jsonObjectBuilder3;
        int n = this.serviceId;
        JsonObjectBuilder jsonObjectBuilder6 = jsonObjectBuilder5.appendField("id", n);
        JsonObjectBuilder jsonObjectBuilder7 = jsonObjectBuilder3;
        JsonObjectBuilder.JsonObject[] jsonObjectArray2 = jsonObjectArray;
        JsonObjectBuilder jsonObjectBuilder8 = jsonObjectBuilder7.appendField("customCharts", jsonObjectArray2);
        JsonObjectBuilder jsonObjectBuilder9 = jsonObjectBuilder;
        JsonObjectBuilder jsonObjectBuilder10 = jsonObjectBuilder3;
        JsonObjectBuilder.JsonObject jsonObject = jsonObjectBuilder10.build();
        JsonObjectBuilder jsonObjectBuilder11 = jsonObjectBuilder9.appendField("service", jsonObject);
        JsonObjectBuilder jsonObjectBuilder12 = jsonObjectBuilder;
        String string = this.serverUuid;
        JsonObjectBuilder jsonObjectBuilder13 = jsonObjectBuilder12.appendField("serverUUID", string);
        JsonObjectBuilder jsonObjectBuilder14 = jsonObjectBuilder;
        JsonObjectBuilder jsonObjectBuilder15 = jsonObjectBuilder14.appendField("metricsVersion", METRICS_VERSION);
        JsonObjectBuilder jsonObjectBuilder16 = jsonObjectBuilder;
        JsonObjectBuilder.JsonObject jsonObject2 = jsonObjectBuilder16.build();
        ScheduledExecutorService scheduledExecutorService = this.scheduler;
        JsonObjectBuilder.JsonObject jsonObject3 = jsonObject2;
        Runnable runnable = () -> {
            block2: {
                try {
                    this.sendData(jsonObject3);
                }
                catch (Exception e) {
                    Exception e2 = e;
                    if (!this.logErrors) break block2;
                    BiConsumer<String, Throwable> biConsumer = this.errorLogger;
                    Exception e3 = e2;
                    biConsumer.accept("Could not submit bStats metrics data", e3);
                }
            }
        };
        scheduledExecutorService.execute(runnable);
    }

    private void sendData(JsonObjectBuilder.JsonObject jsonObject) throws Exception {
        BufferedReader bufferedReader;
        DataOutputStream dataOutputStream;
        DataOutputStream dataOutputStream2;
        String string;
        if (this.logSentData) {
            Consumer<String> consumer = this.infoLogger;
            StringBuilder sb = new StringBuilder().append("Sent bStats metrics data: ");
            String string2 = jsonObject.toString();
            String string3 = sb.append(string2).toString();
            consumer.accept(string3);
        }
        Object[] objectArray = new Object[1];
        String string4 = this.platform;
        objectArray[0] = string4;
        String string5 = string = String.format(REPORT_URL, objectArray);
        URL url = new URL(string5);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)url.openConnection();
        byte[] bArray = MetricsBase.compress(jsonObject.toString());
        HttpsURLConnection httpsURLConnection2 = httpsURLConnection;
        httpsURLConnection2.setRequestMethod("POST");
        HttpsURLConnection httpsURLConnection3 = httpsURLConnection;
        httpsURLConnection3.addRequestProperty("Accept", "application/json");
        HttpsURLConnection httpsURLConnection4 = httpsURLConnection;
        httpsURLConnection4.addRequestProperty("Connection", "close");
        HttpsURLConnection httpsURLConnection5 = httpsURLConnection;
        httpsURLConnection5.addRequestProperty("Content-Encoding", "gzip");
        HttpsURLConnection httpsURLConnection6 = httpsURLConnection;
        byte[] bArray2 = bArray;
        String string6 = String.valueOf(bArray2.length);
        httpsURLConnection6.addRequestProperty("Content-Length", string6);
        HttpsURLConnection httpsURLConnection7 = httpsURLConnection;
        httpsURLConnection7.setRequestProperty("Content-Type", "application/json");
        HttpsURLConnection httpsURLConnection8 = httpsURLConnection;
        httpsURLConnection8.setRequestProperty("User-Agent", "Metrics-Service/1");
        HttpsURLConnection httpsURLConnection9 = httpsURLConnection;
        httpsURLConnection9.setDoOutput(true);
        HttpsURLConnection httpsURLConnection10 = httpsURLConnection;
        OutputStream outputStream = httpsURLConnection10.getOutputStream();
        DataOutputStream dataOutputStream3 = dataOutputStream2 = new DataOutputStream(outputStream);
        try {
            DataOutputStream dataOutputStream4 = dataOutputStream3;
            byte[] bArray3 = bArray;
            dataOutputStream4.write(bArray3);
            dataOutputStream = dataOutputStream3;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            try {
                DataOutputStream dataOutputStream5 = dataOutputStream3;
                dataOutputStream5.close();
            }
            catch (Throwable e3) {
                Throwable e4 = e3;
                Throwable e5 = e2;
                Throwable e6 = e4;
                e5.addSuppressed(e6);
            }
            throw e;
        }
        dataOutputStream.close();
        StringBuilder sb = new StringBuilder();
        HttpsURLConnection httpsURLConnection11 = httpsURLConnection;
        InputStream inputStream = httpsURLConnection11.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader2 = bufferedReader = new BufferedReader(inputStreamReader);
        try {
            while (true) {
                String string7;
                BufferedReader bufferedReader3 = bufferedReader2;
                String string8 = string7 = bufferedReader3.readLine();
                if (string7 != null) {
                    StringBuilder sb2 = sb;
                    String string9 = string8;
                    StringBuilder sb3 = sb2.append(string9);
                    continue;
                }
                break;
            }
        }
        catch (Throwable e) {
            Throwable e8 = e;
            try {
                BufferedReader bufferedReader4 = bufferedReader2;
                bufferedReader4.close();
            }
            catch (Throwable e9) {
                Throwable e10 = e9;
                Throwable e11 = e8;
                Throwable e12 = e10;
                e11.addSuppressed(e12);
            }
            throw e;
        }
        BufferedReader bufferedReader5 = bufferedReader2;
        bufferedReader5.close();
        if (this.logResponseStatusText) {
            Consumer<String> consumer = this.infoLogger;
            StringBuilder sb4 = new StringBuilder().append("Sent data to bStats and received response: ");
            StringBuilder sb5 = sb;
            String string10 = sb4.append((Object)sb5).toString();
            consumer.accept(string10);
        }
    }

    private void checkRelocation() {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            String string;
            String string2;
            String string3;
            String string4;
            String string5;
            byte[] bArray = new byte[]{111, 114, 103, 46, 98, 115, 116, 97, 116, 115};
            String string6 = string5 = new String(bArray);
            byte[] bArray2 = new byte[]{121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101};
            String string7 = string4 = new String(bArray2);
            String name = MetricsBase.class.getPackage().getName();
            if (name.startsWith(string3 = string6) || (string2 = MetricsBase.class.getPackage().getName()).startsWith(string = string7)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }

    private static byte[] compress(String string) throws IOException {
        GZIPOutputStream gZIPOutputStream;
        GZIPOutputStream gZIPOutputStream2;
        ByteArrayOutputStream byteArrayOutputStream;
        if (string == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream3 = gZIPOutputStream2 = new GZIPOutputStream(byteArrayOutputStream2);
        try {
            GZIPOutputStream gZIPOutputStream4 = gZIPOutputStream3;
            Charset charset = StandardCharsets.UTF_8;
            byte[] bytes = string.getBytes(charset);
            gZIPOutputStream4.write(bytes);
            gZIPOutputStream = gZIPOutputStream3;
        }
        catch (Throwable e) {
            Throwable e2 = e;
            try {
                GZIPOutputStream gZIPOutputStream5 = gZIPOutputStream3;
                gZIPOutputStream5.close();
            }
            catch (Throwable e3) {
                Throwable e4 = e3;
                Throwable e5 = e2;
                Throwable e6 = e4;
                e5.addSuppressed(e6);
            }
            throw e;
        }
        gZIPOutputStream.close();
        ByteArrayOutputStream byteArrayOutputStream3 = byteArrayOutputStream;
        return byteArrayOutputStream3.toByteArray();
    }
}

