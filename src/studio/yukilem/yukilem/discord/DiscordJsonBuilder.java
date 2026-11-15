package studio.yukilem.yukilem.discord;

import studio.yukilem.yukilem.discord.DiscordWebhookPayload;

final class DiscordJsonBuilder {
    private final StringBuilder sb;
    private boolean first;

    DiscordJsonBuilder() {
        StringBuilder sb;
        this.sb = sb = new StringBuilder("{");
        this.first = true;
    }

    void put(String string, String string2) {
        this.comma();
        StringBuilder sb = this.sb.append('\"');
        String string3 = DiscordWebhookPayload.escapeJson(string);
        StringBuilder sb2 = sb.append(string3).append("\":\"");
        String string4 = DiscordWebhookPayload.escapeJson(string2);
        StringBuilder sb3 = sb2.append(string4).append('\"');
    }

    void putRaw(String string, String string2) {
        this.comma();
        StringBuilder sb = this.sb.append('\"');
        String string3 = DiscordWebhookPayload.escapeJson(string);
        StringBuilder sb2 = sb.append(string3).append("\":").append(string2);
    }

    boolean isEmpty() {
        return this.first;
    }

    String build() {
        return this.sb.append('}').toString();
    }

    private void comma() {
        if (!this.first) {
            StringBuilder sb = this.sb.append(',');
        }
        this.first = false;
    }
}

