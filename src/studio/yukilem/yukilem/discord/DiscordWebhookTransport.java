package studio.yukilem.yukilem.discord;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

final class DiscordWebhookTransport {
    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final int READ_TIMEOUT_MS = 5000;
    private final AtomicReference<HttpURLConnection> activeConnection = new AtomicReference<>();
    private final AtomicBoolean cancelled = new AtomicBoolean();

    DiscordWebhookTransport() {
    }

    void post(@NotNull Plugin plugin, @NotNull String url, @NotNull String payload) {
        HttpURLConnection connection = null;
        Logger logger = plugin.getLogger();
        try {
            connection = (HttpURLConnection)URI.create(url).toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "glAntiElytraTarget-Webhook/1.0");
            connection.setDoOutput(true);
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            this.activeConnection.set(connection);
            if (this.cancelled.get()) {
                return;
            }
            byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(bytes);
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == 429) {
                logger.warning("Discord webhook rate limited. Consider reducing notification frequency.");
            } else if (responseCode < 200 || responseCode >= 300) {
                logger.warning("Discord webhook failed (HTTP " + responseCode + ")");
            }
        }
        catch (Exception e) {
            logger.warning("Failed to send Discord webhook: " + e.getMessage());
        }
        finally {
            if (connection != null) {
                this.activeConnection.compareAndSet(connection, null);
                connection.disconnect();
            }
        }
    }

    void cancel() {
        this.cancelled.set(true);
        HttpURLConnection connection = this.activeConnection.getAndSet(null);
        if (connection != null) {
            connection.disconnect();
        }
    }
}
