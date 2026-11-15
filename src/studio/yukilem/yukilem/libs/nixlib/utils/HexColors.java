package studio.glace.glantielytratarget.libs.nixlib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HexColors {
    private static final Pattern PLAIN_HEX = Pattern.compile("^#?([0-9a-fA-F]{6})$");
    private static final Pattern SHORT_HEX = Pattern.compile("^#?([0-9a-fA-F]{3})$");
    private static final Pattern BUKKIT_HEX = Pattern.compile("^&#([0-9a-fA-F]{6})$", 2);
    private static final Pattern UNUSUAL_HEX = Pattern.compile("(?i)^&x(&[0-9a-f]){6}$");
    private static final Pattern EMBEDDED_HEX = Pattern.compile("#?([0-9a-fA-F]{6})");

    private HexColors() {
    }

    @Nullable
    public static String normalize(@Nullable String string) {
        Object object;
        if (string == null) {
            return null;
        }
        String string2 = HexColors.plainInput(string);
        if (string2.isEmpty()) {
            String string3 = HexColors.extractLegacySectionHex(string);
            if (string3 != null) {
                return "#" + string3.toUpperCase();
            }
            return null;
        }
        Matcher matcher = BUKKIT_HEX.matcher(string2);
        if (matcher.matches()) {
            return "#" + matcher.group(1).toUpperCase();
        }
        Matcher matcher2 = UNUSUAL_HEX.matcher(string2);
        if (matcher2.matches()) {
            object = new StringBuilder(6);
            for (int n = 0; n < matcher2.group().length(); ++n) {
                char c = matcher2.group().charAt(n);
                if (!HexColors.isHexDigit(c)) continue;
                ((StringBuilder)object).append(c);
            }
            if (((StringBuilder)object).length() == 6) {
                return "#" + ((StringBuilder)object).toString().toUpperCase();
            }
        }
        if (((Matcher)(object = PLAIN_HEX.matcher(string2))).matches()) {
            return "#" + ((Matcher)object).group(1).toUpperCase();
        }
        Matcher matcher3 = SHORT_HEX.matcher(string2);
        if (matcher3.matches()) {
            String string4 = matcher3.group(1);
            return "#" + ("" + string4.charAt(0) + string4.charAt(0) + string4.charAt(1) + string4.charAt(1) + string4.charAt(2) + string4.charAt(2)).toUpperCase();
        }
        Matcher matcher4 = EMBEDDED_HEX.matcher(string2);
        if (!string2.matches("^[#0-9a-fA-F]+$") && matcher4.find()) {
            return "#" + matcher4.group(1).toUpperCase();
        }
        return null;
    }

    public static boolean isValid(@Nullable String string) {
        return HexColors.normalize(string) != null;
    }

    @NotNull
    public static TextColor color(@NotNull String string) {
        TextColor textColor = TextColor.fromCSSHexString((String)(string.startsWith("#") ? string : "#" + string));
        return textColor != null ? textColor : NamedTextColor.WHITE;
    }

    @NotNull
    private static String plainInput(@NotNull String string) {
        String string2 = string.trim();
        if (string2.isEmpty()) {
            return "";
        }
        if (string2.indexOf(167) >= 0) {
            try {
                string2 = PlainTextComponentSerializer.plainText().serialize(LegacyComponentSerializer.legacySection().deserialize(string2));
            }
            catch (Throwable e) {
                string2 = string2.replaceAll("\u00a7.", "");
            }
        }
        string2 = string2.replace('\u00a0', ' ').trim();
        return string2;
    }

    @Nullable
    private static String extractLegacySectionHex(@NotNull String string) {
        if (string.indexOf(167) < 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(6);
        for (int n = 0; n < string.length() - 1; ++n) {
            char c;
            if (string.charAt(n) != '\u00a7' || !HexColors.isHexDigit(c = string.charAt(n + 1))) continue;
            sb.append(c);
        }
        if (sb.length() < 6) {
            return null;
        }
        return sb.substring(sb.length() - 6);
    }

    private static boolean isHexDigit(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
    }
}

