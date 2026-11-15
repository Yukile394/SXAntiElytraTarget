package studio.glace.glantielytratarget.libs.nixlib.config.template;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CommentScanner {
    private static final Pattern KEY_LINE = Pattern.compile("^(?<indent>[ \\t]*)(?<key>(?:\"[^\"]*\"|'[^']*'|[^#\\s:][^:]*?))\\s*:(?=\\s|$).*");

    private CommentScanner() {
    }

    static Map<String, List<String>> extract(String string) {
        if (string == null || string.isEmpty()) {
            return Map.of();
        }
        LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap<String, List<String>>();
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayList<Frame> arrayList2 = new ArrayList<Frame>();
        String[] stringArray = string.split("\\r?\\n", -1);
        int length = stringArray.length;
        for (int n = 0; n < length; ++n) {
            String string2 = CommentScanner.stripBom(stringArray[n]);
            String string3 = string2.trim();
            if (string3.isEmpty()) continue;
            if (string3.startsWith("#")) {
                arrayList.add(CommentScanner.stripCommentMarker(string3));
                continue;
            }
            if (string3.startsWith("- ") || string3.equals("-")) {
                arrayList.clear();
                continue;
            }
            Matcher matcher = KEY_LINE.matcher(string2);
            if (!matcher.matches()) {
                arrayList.clear();
                continue;
            }
            int n2 = CommentScanner.expandIndent(matcher.group("indent"));
            String string4 = CommentScanner.unquote(matcher.group("key").trim());
            while (!arrayList2.isEmpty() && ((Frame)arrayList2.get((int)(arrayList2.size() - 1))).indent >= n2) {
                arrayList2.remove(arrayList2.size() - 1);
            }
            arrayList2.add(new Frame(n2, string4));
            String string5 = CommentScanner.joinPath(arrayList2);
            if (arrayList.isEmpty()) continue;
            linkedHashMap.put(string5, List.copyOf(arrayList));
            arrayList.clear();
        }
        return linkedHashMap;
    }

    private static String stripBom(String string) {
        if (!string.isEmpty() && string.charAt(0) == '\ufeff') {
            return string.substring(1);
        }
        return string;
    }

    private static String stripCommentMarker(String string) {
        String string2 = string.substring(1);
        if (!string2.isEmpty() && string2.charAt(0) == ' ') {
            string2 = string2.substring(1);
        }
        return string2;
    }

    private static int expandIndent(String string) {
        int n = 0;
        for (int n2 = 0; n2 < string.length(); ++n2) {
            n += string.charAt(n2) == '\t' ? 4 : 1;
        }
        return n;
    }

    private static String unquote(String string) {
        if (string.length() >= 2) {
            char c = string.charAt(0);
            char c2 = string.charAt(string.length() - 1);
            if (c == '\"' && c2 == '\"' || c == '\'' && c2 == '\'') {
                return string.substring(1, string.length() - 1);
            }
        }
        return string;
    }

    private static String joinPath(List<Frame> list) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < list.size(); ++n) {
            if (n > 0) {
                sb.append('.');
            }
            sb.append(list.get((int)n).key);
        }
        return sb.toString();
    }

    private record Frame(int indent, String key) {
    }
}

