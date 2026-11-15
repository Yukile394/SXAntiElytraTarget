package studio.glace.glantielytratarget.libs.nixlib.utils;

import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class SoundUtils {
    private SoundUtils() {
    }

    public static Sound parseSound(String string) {
        Sound sound;
        if (string == null || string.isBlank()) {
            return null;
        }
        String lowerCase = string.toLowerCase(Locale.ROOT);
        if (lowerCase.startsWith("minecraft:")) {
            lowerCase = lowerCase.substring("minecraft:".length());
        }
        if ((sound = SoundUtils.lookup(lowerCase)) != null) {
            return sound;
        }
        if (!lowerCase.contains(".") && lowerCase.contains("_")) {
            sound = SoundUtils.lookup(SoundUtils.legacyEnumKey(lowerCase));
            if (sound != null) {
                return sound;
            }
            sound = SoundUtils.lookup(lowerCase.replace('_', '.'));
        }
        return sound;
    }

    private static Sound lookup(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        return Registry.SOUNDS.get(NamespacedKey.minecraft(string));
    }

    static String legacyEnumKey(String string) {
        String[] stringArray = string.toUpperCase(Locale.ROOT).split("_");
        if (stringArray.length < 2) {
            return string;
        }
        String string2 = switch (stringArray[0]) {
            case "BLOCK" -> "block";
            case "ENTITY" -> "entity";
            case "UI" -> "ui";
            case "ITEM" -> "item";
            case "MUSIC" -> "music";
            default -> stringArray[0].toLowerCase(Locale.ROOT);
        };
        if (stringArray.length == 2) {
            return string2 + "." + stringArray[1].toLowerCase(Locale.ROOT);
        }
        String lowerCase = stringArray[stringArray.length - 1].toLowerCase(Locale.ROOT);
        StringBuilder sb = new StringBuilder();
        for (int n = 1; n < stringArray.length - 1; ++n) {
            if (n > 1) {
                sb.append('_');
            }
            sb.append(stringArray[n].toLowerCase(Locale.ROOT));
        }
        return string2 + "." + String.valueOf(sb) + "." + lowerCase;
    }

    public static void playSound(Player player, String string, float f, float f2) {
        if (player == null) {
            return;
        }
        Sound sound = SoundUtils.parseSound(string);
        if (sound != null) {
            player.playSound(player.getLocation(), sound, f, f2);
        }
    }
}

