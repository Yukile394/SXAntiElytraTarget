package studio.yukilem.yukilem.discord;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.bootstrap.PluginContext;
import studio.yukilem.yukilem.config.settings.DiscordSettings;
import studio.yukilem.yukilem.discord.DiscordWebhookPayload;
import studio.yukilem.yukilem.discord.DiscordWebhookTransport;
import studio.yukilem.yukilem.discord.WebhookMessage;
import studio.yukilem.yukilem.glAntiElytraTarget;
import studio.yukilem.yukilem.punishments.PlaceholderContext;

public final class DiscordWebhookService {
    private static final long RATE_LIMIT_DELAY_MS = 1000L;
    private final PluginContext ctx;
    private final BlockingQueue<WebhookMessage> messageQueue;
    private final AtomicLong lastMessageTime;
    private final AtomicBoolean running;
    private final DiscordWebhookTransport transport;
    private Thread workerThread;

    public DiscordWebhookService(PluginContext pluginContext) {
        DiscordWebhookTransport discordWebhookTransport;
        AtomicBoolean atomicBoolean;
        AtomicLong atomicLong;
        LinkedBlockingQueue<WebhookMessage> linkedBlockingQueue = new LinkedBlockingQueue<WebhookMessage>();
        this.messageQueue = linkedBlockingQueue;
        long l = 0L;
        this.lastMessageTime = atomicLong = new AtomicLong(l);
        this.running = atomicBoolean = new AtomicBoolean(true);
        this.transport = discordWebhookTransport = new DiscordWebhookTransport();
        this.ctx = pluginContext;
        this.startWorker();
    }

    public boolean isEnabled() {
        return this.ctx.webhookConfig().discord().enabled();
    }

    public void sendViolation(@NotNull PlaceholderContext placeholderContext) {
        if (!this.running.get() || !this.isEnabled()) {
            return;
        }
        BlockingQueue<WebhookMessage> blockingQueue = this.messageQueue;
        String string = placeholderContext.player();
        String string2 = placeholderContext.checkName();
        int n = placeholderContext.vl();
        int n2 = placeholderContext.ms();
        String string3 = placeholderContext.tps();
        WebhookMessage webhookMessage = new WebhookMessage(string, string2, n, n2, string3);
        boolean bl = blockingQueue.offer(webhookMessage);
    }

    private void startWorker() {
        Thread thread;
        Runnable runnable = () -> {
            while (this.running.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    BlockingQueue<WebhookMessage> blockingQueue = this.messageQueue;
                    long l = 500L;
                    TimeUnit timeUnit = TimeUnit.MILLISECONDS;
                    WebhookMessage webhookMessage = blockingQueue.poll(l, timeUnit);
                    WebhookMessage webhookMessage2 = webhookMessage;
                    if (webhookMessage2 == null) continue;
                    WebhookMessage webhookMessage3 = webhookMessage;
                    this.processMessage(webhookMessage3);
                }
                catch (InterruptedException e) {
                    InterruptedException e2 = e;
                    Thread.currentThread().interrupt();
                    break;
                }
                catch (Exception e) {
                    Exception e3 = e;
                    if (!this.running.get()) continue;
                    Logger logger = this.ctx.plugin().getLogger();
                    Exception e4 = e3;
                    String string = "Error in webhook worker: " + e4.getMessage();
                    logger.warning(string);
                }
            }
        };
        this.workerThread = thread = new Thread(runnable, "AET-DiscordWorker");
        this.workerThread.setDaemon(true);
        this.workerThread.start();
    }

    private void processMessage(WebhookMessage webhookMessage) {
        block5: {
            try {
                DiscordSettings discordSettings;
                DiscordSettings discordSettings2;
                String string;
                String string2;
                long l;
                long l2 = System.currentTimeMillis();
                long l3 = this.lastMessageTime.get();
                long l4 = l = l2 - l3;
                long l5 = 1000L;
                if (l4 < l5) {
                    long l6 = 1000L;
                    long l7 = l;
                    Thread.sleep(l6 - l7);
                }
                if ((string2 = (string = DiscordWebhookPayload.build(discordSettings2 = (discordSettings = this.ctx.webhookConfig().discord()), webhookMessage))) != null) {
                    DiscordWebhookTransport discordWebhookTransport = this.transport;
                    glAntiElytraTarget glAntiElytraTarget2 = this.ctx.plugin();
                    DiscordSettings discordSettings3 = discordSettings;
                    String string3 = discordSettings3.webhookUrl();
                    String string4 = string;
                    discordWebhookTransport.post(glAntiElytraTarget2, string3, string4);
                }
                AtomicLong atomicLong = this.lastMessageTime;
                long l8 = System.currentTimeMillis();
                atomicLong.set(l8);
            }
            catch (InterruptedException e) {
                InterruptedException e2 = e;
                Thread.currentThread().interrupt();
            }
            catch (Exception e) {
                Exception e3 = e;
                if (!this.running.get()) break block5;
                Logger logger = this.ctx.plugin().getLogger();
                Exception e4 = e3;
                String string = "Failed to process webhook message: " + e4.getMessage();
                logger.warning(string);
            }
        }
    }

    public void shutdown() {
        if (!this.running.compareAndSet(true, false)) {
            return;
        }
        this.messageQueue.clear();
        this.transport.cancel();
        Thread thread = this.workerThread;
        this.workerThread = null;
        Thread thread2 = thread;
        if (thread2 != null) {
            Thread thread3 = thread;
            thread3.interrupt();
        }
    }
}

