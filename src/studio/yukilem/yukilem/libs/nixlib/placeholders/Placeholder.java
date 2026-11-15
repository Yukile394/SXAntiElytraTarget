package studio.yukilem.yukilem.libs.nixlib.placeholders;

import org.bukkit.OfflinePlayer;

@FunctionalInterface
public interface Placeholder {
    public String resolve(OfflinePlayer offlinePlayer1, String string2);
}

