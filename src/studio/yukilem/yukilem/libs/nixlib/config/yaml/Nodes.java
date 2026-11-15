package studio.yukilem.yukilem.libs.nixlib.config.yaml;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Nodes {
    private Nodes() {
    }

    public static Object get(Map<String, Object> map, String string) {
        Object object = map;
        for (String string2 : string.split("\\.")) {
            if (!(object instanceof Map)) {
                return null;
            }
            Map<String, Object> map2 = (Map<String, Object>)object;
            object = map2.get(string2);
            if (object != null) continue;
            return null;
        }
        return object;
    }

    public static void set(Map<String, Object> map, String string, Object object) {
        String[] stringArray = string.split("\\.");
        Map map2 = map;
        for (int n = 0; n < stringArray.length - 1; ++n) {
            Object child = map2.get(stringArray[n]);
            if (!(child instanceof Map)) {
                child = new LinkedHashMap();
                map2.put(stringArray[n], child);
            }
            map2 = (Map)child;
        }
        map2.put((String)stringArray[stringArray.length - 1], (Object)object);
    }

    public static boolean contains(Map<String, Object> map, String string) {
        Object object = map;
        String[] stringArray = string.split("\\.");
        for (int n = 0; n < stringArray.length; ++n) {
            if (!(object instanceof Map)) {
                return false;
            }
            Map<String, Object> map2 = (Map<String, Object>)object;
            if (!map2.containsKey(stringArray[n])) {
                return false;
            }
            object = map2.get(stringArray[n]);
        }
        return true;
    }

    public static boolean remove(Map<String, Object> map, String string) {
        String[] stringArray = string.split("\\.");
        Map map2 = map;
        for (int n = 0; n < stringArray.length - 1; ++n) {
            Object object = map2.get(stringArray[n]);
            if (!(object instanceof Map)) {
                return false;
            }
            map2 = (Map)object;
        }
        return map2.remove(stringArray[stringArray.length - 1]) != null;
    }
}

