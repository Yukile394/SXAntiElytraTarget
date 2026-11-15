package studio.glace.glantielytratarget.libs.nixlib.placeholders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.OfflinePlayer;
import studio.glace.glantielytratarget.libs.nixlib.placeholders.Placeholder;

public final class PlaceholderRegistry {
    private static final Pattern TOKEN = Pattern.compile("%([a-zA-Z0-9_\\-]+)(?::([^%]+))?%");
    private final Map<String, Placeholder> handlers = new LinkedHashMap<String, Placeholder>();

    public PlaceholderRegistry register(String string, Placeholder placeholder) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("placeholder key cannot be blank");
        }
        this.handlers.put(string.toLowerCase(), placeholder);
        return this;
    }

    public PlaceholderRegistry unregister(String string) {
        this.handlers.remove(string.toLowerCase());
        return this;
    }

    public boolean has(String string) {
        return this.handlers.containsKey(string.toLowerCase());
    }

    public String apply(OfflinePlayer offlinePlayer, String string) {
        if (string == null || string.indexOf(37) < 0) {
            return string;
        }
        Matcher matcher = TOKEN.matcher(string);
        StringBuilder sb = new StringBuilder(string.length());
        int n = 0;
        while (matcher.find()) {
            sb.append(string, n, matcher.start());
            Placeholder placeholder = this.handlers.get(matcher.group(1).toLowerCase());
            if (placeholder == null) {
                sb.append(matcher.group());
            } else {
                String string2 = placeholder.resolve(offlinePlayer, matcher.group(2));
                sb.append(string2 == null ? "" : string2);
            }
            n = matcher.end();
        }
        sb.append(string, n, string.length());
        return sb.toString();
    }

    public int size() {
        return this.handlers.size();
    }
}

