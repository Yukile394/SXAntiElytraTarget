package studio.yukilem.yukilem.libs.nixlib.config.yaml;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class YamlWriter {
    private static final String INDENT = "  ";
    private final StringBuilder out = new StringBuilder();
    private final Map<String, List<String>> comments;

    private YamlWriter(Map<String, List<String>> map) {
        this.comments = map == null ? Map.of() : map;
    }

    public static String write(Map<String, Object> map, Map<String, List<String>> map2) {
        YamlWriter yamlWriter = new YamlWriter(map2);
        yamlWriter.writeMap(map, "", 0);
        return yamlWriter.out.toString();
    }

    private void writeMap(Map<String, Object> map, String string, int n) {
        String string2 = INDENT.repeat(n);
        boolean bl = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Object object = string.isEmpty() ? key : string + "." + key;
            List<String> list = this.comments.get(object);
            if (list != null && !list.isEmpty()) {
                if (!bl && n == 0) {
                    this.out.append('\n');
                }
                for (String string3 : list) {
                    this.out.append(string2).append("# ").append(string3).append('\n');
                }
            }
            this.out.append(string2).append(YamlWriter.escapeKey(key)).append(':');
            if (value instanceof Map) {
                Map map2 = (Map)value;
                if (map2.isEmpty()) {
                    this.out.append(" {}\n");
                } else {
                    this.out.append('\n');
                    this.writeMap(YamlWriter.asStringMap(map2), (String)object, n + 1);
                }
            } else if (value instanceof Collection) {
                Collection collection = (Collection)value;
                this.writeList(collection, n);
            } else {
                this.out.append(' ').append(YamlWriter.renderScalar(value)).append('\n');
            }
            bl = false;
        }
    }

    private void writeList(Collection<?> collection, int n) {
        if (collection.isEmpty()) {
            this.out.append(" []\n");
            return;
        }
        this.out.append('\n');
        String string = INDENT.repeat(n);
        for (Object obj : collection) {
            this.out.append(string).append("- ");
            if (obj instanceof Map) {
                Map map = (Map)obj;
                if (map.isEmpty()) {
                    this.out.append("{}\n");
                    continue;
                }
                this.out.append('\n');
                this.writeMap(YamlWriter.asStringMap(map), "", n + 2);
                continue;
            }
            if (obj instanceof Collection) {
                this.out.append(this.renderInlineList((Collection)obj)).append('\n');
                continue;
            }
            this.out.append(YamlWriter.renderScalar(obj)).append('\n');
        }
    }

    private String renderInlineList(Collection<?> collection) {
        StringBuilder sb = new StringBuilder("[");
        boolean bl = true;
        for (Object obj : collection) {
            if (!bl) {
                sb.append(", ");
            }
            sb.append(YamlWriter.renderScalar(obj));
            bl = false;
        }
        sb.append(']');
        return sb.toString();
    }

    private static LinkedHashMap<String, Object> asStringMap(Map<?, ?> map) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>(map.size());
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            linkedHashMap.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return linkedHashMap;
    }

    private static String renderScalar(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof Boolean || object instanceof Number) {
            return object.toString();
        }
        String string = object.toString();
        return YamlWriter.needsQuoting(string) ? YamlWriter.quote(string) : string;
    }

    private static boolean needsQuoting(String string) {
        if (string.isEmpty()) {
            return true;
        }
        if (string.matches("(?i)^(true|false|null|yes|no|on|off|~)$")) {
            return true;
        }
        if (string.matches("^-?\\d+(\\.\\d+)?$")) {
            return true;
        }
        char c = string.charAt(0);
        if (c == '!' || c == '&' || c == '*' || c == '[' || c == ']' || c == '{' || c == '}' || c == ',' || c == '#' || c == '|' || c == '>' || c == '\'' || c == '\"' || c == '%' || c == '@' || c == '`' || c == ' ') {
            return true;
        }
        for (int n = 0; n < string.length(); ++n) {
            char c2 = string.charAt(n);
            if (c2 == ':' && (n == string.length() - 1 || string.charAt(n + 1) == ' ')) {
                return true;
            }
            if (c2 == '\n' || c2 == '\r' || c2 == '\t' || c2 == '\u0000') {
                return true;
            }
            if (c2 != '#' || n <= 0 || string.charAt(n - 1) != ' ') continue;
            return true;
        }
        return string.charAt(string.length() - 1) == ' ';
    }

    private static String quote(String string) {
        StringBuilder sb = new StringBuilder(string.length() + 2);
        sb.append('\"');
        block8: for (int n = 0; n < string.length(); ++n) {
            char c = string.charAt(n);
            switch (c) {
                case '\\': {
                    sb.append("\\\\");
                    continue block8;
                }
                case '\"': {
                    sb.append("\\\"");
                    continue block8;
                }
                case '\n': {
                    sb.append("\\n");
                    continue block8;
                }
                case '\r': {
                    sb.append("\\r");
                    continue block8;
                }
                case '\t': {
                    sb.append("\\t");
                    continue block8;
                }
                case '\u0000': {
                    sb.append("\\0");
                    continue block8;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    private static String escapeKey(String string) {
        if (string.matches("[A-Za-z0-9_\\-]+")) {
            return string;
        }
        return YamlWriter.quote(string);
    }
}

