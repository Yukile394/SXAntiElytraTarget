package studio.yukilem.yukilem.libs.nixlib.placeholders;

import java.util.Objects;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import studio.yukilem.yukilem.libs.nixlib.placeholders.PlaceholderRegistry;

public final class PlaceholderApiBridge {
    private PlaceholderApiBridge() {
    }

    public static boolean isPresent() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static boolean register(Plugin plugin, String string, PlaceholderRegistry placeholderRegistry) {
        if (!PlaceholderApiBridge.isPresent()) {
            return false;
        }
        new Expansion(plugin, string, placeholderRegistry).register();
        return true;
    }

    private static final class Expansion
    extends PlaceholderExpansion {
        private final Plugin plugin;
        private final String identifier;
        private final PlaceholderRegistry registry;

        Expansion(Plugin plugin, String string, PlaceholderRegistry placeholderRegistry) {
            this.plugin = Objects.requireNonNull(plugin);
            this.identifier = string.toLowerCase();
            this.registry = placeholderRegistry;
        }

        @Override
        public String getIdentifier() {
            return this.identifier;
        }

        @Override
        public String getAuthor() {
            return String.join((CharSequence)",", this.plugin.getDescription().getAuthors());
        }

        @Override
        public String getVersion() {
            return this.plugin.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public String onRequest(OfflinePlayer offlinePlayer, String string) {
            return this.registry.apply(offlinePlayer, "%" + string + "%");
        }
    }
}

