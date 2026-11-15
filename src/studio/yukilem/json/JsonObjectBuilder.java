package studio.glace.json;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JsonObjectBuilder {
    private StringBuilder builder;
    private boolean hasAtLeastOneField;

    public JsonObjectBuilder() {
        StringBuilder sb;
        this.builder = sb = new StringBuilder();
        this.hasAtLeastOneField = false;
        StringBuilder sb2 = this.builder.append("{");
    }

    public JsonObjectBuilder appendNull(String string) {
        this.appendFieldUnescaped(string, "null");
        return this;
    }

    public JsonObjectBuilder appendField(String string, String string2) {
        if (string2 == null) {
            throw new IllegalArgumentException("JSON value must not be null");
        }
        StringBuilder sb = new StringBuilder().append("\"");
        String string3 = JsonObjectBuilder.escape(string2);
        String string4 = sb.append(string3).append("\"").toString();
        this.appendFieldUnescaped(string, string4);
        return this;
    }

    public JsonObjectBuilder appendField(String string, int n) {
        String string2 = String.valueOf(n);
        this.appendFieldUnescaped(string, string2);
        return this;
    }

    public JsonObjectBuilder appendField(String string, JsonObject jsonObject) {
        if (jsonObject == null) {
            throw new IllegalArgumentException("JSON object must not be null");
        }
        String string2 = jsonObject.toString();
        this.appendFieldUnescaped(string, string2);
        return this;
    }

    public JsonObjectBuilder appendField(String string2, String[] stringArray) {
        if (stringArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        Stream<String> stream = Arrays.stream(stringArray);
        Function<String, String> function = string -> {
            StringBuilder sb = new StringBuilder().append("\"");
            String escaped = JsonObjectBuilder.escape(string);
            return sb.append(escaped).append("\"").toString();
        };
        Stream<String> stream2 = stream.map(function);
        Collector<CharSequence, ?, String> collector = Collectors.joining(",");
        String string3 = stream2.collect(collector);
        StringBuilder sb = new StringBuilder().append("[");
        String string4 = string3;
        String string5 = sb.append(string4).append("]").toString();
        this.appendFieldUnescaped(string2, string5);
        return this;
    }

    public JsonObjectBuilder appendField(String string, int[] nArray) {
        if (nArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        IntStream intStream = Arrays.stream(nArray);
        IntFunction<String> intFunction = String::valueOf;
        Stream<String> stream = intStream.mapToObj(intFunction);
        Collector<CharSequence, ?, String> collector = Collectors.joining(",");
        String string2 = stream.collect(collector);
        StringBuilder sb = new StringBuilder().append("[");
        String string3 = string2;
        String string4 = sb.append(string3).append("]").toString();
        this.appendFieldUnescaped(string, string4);
        return this;
    }

    public JsonObjectBuilder appendField(String string, JsonObject[] jsonObjectArray) {
        if (jsonObjectArray == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        Stream<JsonObject> stream = Arrays.stream(jsonObjectArray);
        Function<JsonObject, String> function = JsonObject::toString;
        Stream<String> stream2 = stream.map(function);
        Collector<CharSequence, ?, String> collector = Collectors.joining(",");
        String string2 = stream2.collect(collector);
        StringBuilder sb = new StringBuilder().append("[");
        String string3 = string2;
        String string4 = sb.append(string3).append("]").toString();
        this.appendFieldUnescaped(string, string4);
        return this;
    }

    private void appendFieldUnescaped(String string, String string2) {
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        if (string == null) {
            throw new IllegalArgumentException("JSON key must not be null");
        }
        if (this.hasAtLeastOneField) {
            StringBuilder sb = this.builder.append(",");
        }
        StringBuilder sb = this.builder.append("\"");
        String string3 = JsonObjectBuilder.escape(string);
        StringBuilder sb2 = sb.append(string3).append("\":").append(string2);
        this.hasAtLeastOneField = true;
    }

    public JsonObject build() {
        JsonObject jsonObject;
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        String string = this.builder.append("}").toString();
        JsonObject jsonObject2 = jsonObject = new JsonObject(string);
        this.builder = null;
        JsonObject jsonObject3 = jsonObject2;
        return jsonObject3;
    }

    private static String escape(String string) {
        StringBuilder sb = new StringBuilder();
        int n = 0;
        while (true) {
            char c;
            int n2;
            int n3;
            if ((n3 = ++n) < (n2 = string.length())) {
                int n4 = n;
                c = string.charAt(n4);
                char c2 = c;
                if (c2 == '\"') {
                    StringBuilder sb2 = sb;
                    StringBuilder sb3 = sb2.append("\\\"");
                    continue;
                }
            } else {
                StringBuilder sb4 = sb;
                return sb4.toString();
            }
            char c3 = c;
            if (c3 == '\\') {
                StringBuilder sb5 = sb;
                StringBuilder sb6 = sb5.append("\\\\");
                continue;
            }
            char c4 = c;
            if (c4 <= '\u000f') {
                StringBuilder sb7 = sb;
                StringBuilder sb8 = sb7.append("\\u000");
                char c5 = c;
                String string2 = Integer.toHexString(c5);
                StringBuilder sb9 = sb8.append(string2);
                continue;
            }
            char c6 = c;
            if (c6 <= '\u001f') {
                StringBuilder sb10 = sb;
                StringBuilder sb11 = sb10.append("\\u00");
                char c7 = c;
                String string3 = Integer.toHexString(c7);
                StringBuilder sb12 = sb11.append(string3);
                continue;
            }
            StringBuilder sb13 = sb;
            char c8 = c;
            StringBuilder sb14 = sb13.append(c8);
        }
    }

    public static class JsonObject {
        private final String value;

        private JsonObject(String string) {
            this.value = string;
        }

        public String toString() {
            return this.value;
        }
    }
}

