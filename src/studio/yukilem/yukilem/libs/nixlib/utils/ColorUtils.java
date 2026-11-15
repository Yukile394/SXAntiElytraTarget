package studio.yukilem.yukilem.libs.nixlib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class ColorUtils {
    private static final Pattern BUKKIT_HEX = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern AMP_CODE = Pattern.compile("&([0-9a-fk-orA-FK-OR])");
    private static final Pattern X_HEX = Pattern.compile("(?i)&x(&[0-9a-f]){6}");
    private static final MiniMessage MINI = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer AMP_LEGACY = LegacyComponentSerializer.builder().character('&').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final LegacyComponentSerializer SECTION_LEGACY = LegacyComponentSerializer.builder().character('\u00a7').hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();

    private ColorUtils() {
    }

    public static Component parse(String string) {
        if (string == null || string.isEmpty()) {
            return Component.empty();
        }
        return AMP_LEGACY.deserialize(ColorUtils.translateBukkitHex(string));
    }

    public static Component miniMessage(String string) {
        if (string == null || string.isEmpty()) {
            return Component.empty();
        }
        return MINI.deserialize(string);
    }

    public static Component translate(String string) {
        if (string == null || string.isEmpty()) {
            return Component.empty();
        }
        return MINI.deserialize(ColorUtils.legacyToMiniMessage(string));
    }

    public static List<Component> parseAll(List<String> list) {
        if (list == null) {
            return List.of();
        }
        ArrayList<Component> arrayList = new ArrayList<Component>(list.size());
        for (String string : list) {
            arrayList.add(ColorUtils.parse(string));
        }
        return arrayList;
    }

    public static List<Component> translateAll(List<String> list) {
        if (list == null) {
            return List.of();
        }
        ArrayList<Component> arrayList = new ArrayList<Component>(list.size());
        for (String string : list) {
            arrayList.add(ColorUtils.translate(string));
        }
        return arrayList;
    }

    public static String toLegacySection(Component component) {
        if (component == null) {
            return "";
        }
        return SECTION_LEGACY.serialize(component);
    }

    public static String toMiniMessage(Component component) {
        if (component == null) {
            return "";
        }
        return (String)MINI.serialize(component);
    }

    public static String stripColor(String string) {
        if (string == null) {
            return null;
        }
        return PLAIN.serialize(ColorUtils.translate(string));
    }

    public static String stripColor(Component component) {
        if (component == null) {
            return "";
        }
        return PLAIN.serialize(component);
    }

    private static String translateBukkitHex(String string) {
        if (string.indexOf(35) < 0) {
            return string;
        }
        Matcher matcher = BUKKIT_HEX.matcher(string);
        if (!matcher.find()) {
            return string;
        }
        matcher.reset();
        StringBuilder sb = new StringBuilder(string.length());
        int n = 0;
        while (matcher.find()) {
            sb.append(string, n, matcher.start()).append("&x");
            String string2 = matcher.group(1);
            for (int n2 = 0; n2 < string2.length(); ++n2) {
                sb.append('&').append(Character.toLowerCase(string2.charAt(n2)));
            }
            n = matcher.end();
        }
        sb.append(string, n, string.length());
        return sb.toString();
    }

    private static String translateUnusualHex(String string) {
        if (string.indexOf(120) < 0 && string.indexOf(88) < 0) {
            return string;
        }
        Matcher matcher = X_HEX.matcher(string);
        if (!matcher.find()) {
            return string;
        }
        matcher.reset();
        StringBuilder sb = new StringBuilder(string.length());
        while (matcher.find()) {
            StringBuilder sb2 = new StringBuilder(6);
            for (int n = 0; n < matcher.group().length(); ++n) {
                char c = matcher.group().charAt(n);
                if (!(c >= '0' && c <= '9' || c >= 'a' && c <= 'f') && (c < 'A' || c > 'F')) continue;
                sb2.append(c);
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement("<#" + String.valueOf(sb2) + ">"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String legacyToMiniMessage(String string) {
        String string2 = ColorUtils.translateUnusualHex(string);
        String string3 = BUKKIT_HEX.matcher(string2).replaceAll("<#$1>");
        Matcher matcher = AMP_CODE.matcher(string3);
        if (!matcher.find()) {
            return string3;
        }
        matcher.reset();
        StringBuilder sb = new StringBuilder(string3.length());
        while (matcher.find()) {
            String string4 = switch (matcher.group(1).toLowerCase()) {
                case "0" -> "<black>";
                case "1" -> "<dark_blue>";
                case "2" -> "<dark_green>";
                case "3" -> "<dark_aqua>";
                case "4" -> "<dark_red>";
                case "5" -> "<dark_purple>";
                case "6" -> "<gold>";
                case "7" -> "<gray>";
                case "8" -> "<dark_gray>";
                case "9" -> "<blue>";
                case "a" -> "<green>";
                case "b" -> "<aqua>";
                case "c" -> "<red>";
                case "d" -> "<light_purple>";
                case "e" -> "<yellow>";
                case "f" -> "<white>";
                case "k" -> "<obfuscated>";
                case "l" -> "<bold>";
                case "m" -> "<strikethrough>";
                case "n" -> "<underlined>";
                case "o" -> "<italic>";
                case "r" -> "<reset>";
                default -> matcher.group(0);
            };
            matcher.appendReplacement(sb, Matcher.quoteReplacement(string4));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}

