package studio.glace.glantielytratarget.libs.nixlib.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isBlank(String string) {
        return string == null || string.isBlank();
    }

    public static String orDefault(String string, String string2) {
        return StringUtils.isBlank(string) ? string2 : string;
    }

    public static String capitalize(String string) {
        if (StringUtils.isNullOrEmpty(string)) {
            return string;
        }
        return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase(Locale.ROOT);
    }

    public static String prettyEnum(Enum<?> enum_) {
        if (enum_ == null) {
            return "";
        }
        String[] stringArray = enum_.name().toLowerCase(Locale.ROOT).split("_");
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < stringArray.length; ++n) {
            if (n > 0) {
                sb.append(' ');
            }
            sb.append(StringUtils.capitalize(stringArray[n]));
        }
        return sb.toString();
    }

    public static List<String> wrap(String string, int n) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (StringUtils.isBlank(string)) {
            arrayList.add("");
            return arrayList;
        }
        StringBuilder sb = new StringBuilder();
        for (String string2 : string.split("\\s+")) {
            if (sb.length() == 0) {
                sb.append(string2);
                continue;
            }
            if (sb.length() + 1 + string2.length() <= n) {
                sb.append(' ').append(string2);
                continue;
            }
            arrayList.add(sb.toString());
            sb.setLength(0);
            sb.append(string2);
        }
        if (sb.length() > 0) {
            arrayList.add(sb.toString());
        }
        return arrayList;
    }

    public static String truncate(String string, int n, String string2) {
        if (string == null) {
            return null;
        }
        if (string.length() <= n) {
            return string;
        }
        if (string2 == null) {
            string2 = "";
        }
        int n2 = Math.max(0, n - string2.length());
        return string.substring(0, n2) + string2;
    }

    public static String join(Iterable<?> iterable, String string) {
        StringBuilder sb = new StringBuilder();
        boolean bl = true;
        for (Object obj : iterable) {
            if (!bl) {
                sb.append(string);
            }
            sb.append(obj);
            bl = false;
        }
        return sb.toString();
    }
}

