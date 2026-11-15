package studio.yukilem.yukilem.libs.nixlib.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class TypeCoercion {
    private TypeCoercion() {
    }

    public static Optional<Object> coerce(Object object, Class<?> clazz) {
        if (object == null) {
            return Optional.empty();
        }
        if (clazz.isInstance(object)) {
            return Optional.of(object);
        }
        if (clazz == String.class) {
            return Optional.of(String.valueOf(object));
        }
        if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return TypeCoercion.coerceBoolean(object).map(bl -> bl);
        }
        if (Number.class.isAssignableFrom(TypeCoercion.box(clazz))) {
            return TypeCoercion.coerceNumber(object, TypeCoercion.box(clazz));
        }
        if (clazz == Character.class || clazz == Character.TYPE) {
            String string = object.toString();
            return string.isEmpty() ? Optional.empty() : Optional.of(Character.valueOf(string.charAt(0)));
        }
        if (clazz.isEnum()) {
            return TypeCoercion.coerceEnum(object, clazz);
        }
        if (List.class.isAssignableFrom(clazz)) {
            return Optional.of(TypeCoercion.toList(object));
        }
        if (Set.class.isAssignableFrom(clazz)) {
            return Optional.of(new LinkedHashSet<Object>(TypeCoercion.toList(object)));
        }
        if (Map.class.isAssignableFrom(clazz) && object instanceof Map) {
            Map map = (Map)object;
            LinkedHashMap linkedHashMap = new LinkedHashMap(map.size());
            for (Map.Entry<?, ?> entry : ((Map<?, ?>)map).entrySet()) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
            return Optional.of(linkedHashMap);
        }
        return Optional.empty();
    }

    private static Class<?> box(Class<?> clazz) {
        if (clazz == Integer.TYPE) {
            return Integer.class;
        }
        if (clazz == Long.TYPE) {
            return Long.class;
        }
        if (clazz == Double.TYPE) {
            return Double.class;
        }
        if (clazz == Float.TYPE) {
            return Float.class;
        }
        if (clazz == Short.TYPE) {
            return Short.class;
        }
        if (clazz == Byte.TYPE) {
            return Byte.class;
        }
        return clazz;
    }

    private static Optional<Boolean> coerceBoolean(Object object) {
        String string;
        if (object instanceof Boolean) {
            Boolean bl = (Boolean)object;
            return Optional.of(bl);
        }
        if (object instanceof Number) {
            Number number = (Number)object;
            return Optional.of(number.intValue() != 0);
        }
        return switch (string = object.toString().trim().toLowerCase()) {
            case "true", "yes", "on", "1" -> Optional.of(true);
            case "false", "no", "off", "0" -> Optional.of(false);
            default -> Optional.empty();
        };
    }

    private static Optional<Object> coerceNumber(Object object, Class<?> clazz) {
        Number number;
        if (object instanceof Number) {
            number = (Number)object;
        } else {
            try {
                number = Double.parseDouble(object.toString().trim());
            }
            catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        if (clazz == Integer.class) {
            return Optional.of(number.intValue());
        }
        if (clazz == Long.class) {
            return Optional.of(number.longValue());
        }
        if (clazz == Double.class) {
            return Optional.of(number.doubleValue());
        }
        if (clazz == Float.class) {
            return Optional.of(Float.valueOf(number.floatValue()));
        }
        if (clazz == Short.class) {
            return Optional.of(number.shortValue());
        }
        if (clazz == Byte.class) {
            return Optional.of(number.byteValue());
        }
        return Optional.of(number);
    }

    private static Optional<Object> coerceEnum(Object object, Class<?> clazz) {
        String string = object.toString().trim();
        for (Object obj : clazz.getEnumConstants()) {
            if (!((Enum)obj).name().equalsIgnoreCase(string)) continue;
            return Optional.of(obj);
        }
        try {
            return Optional.of((Object)Enum.valueOf((Class<Enum>)clazz, string.toUpperCase()));
        }
        catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private static List<Object> toList(Object object) {
        if (object instanceof Collection) {
            Collection collection = (Collection)object;
            return new ArrayList<Object>(collection);
        }
        if (object.getClass().isArray()) {
            Object[] objectArray = (Object[])object;
            ArrayList<Object> arrayList = new ArrayList<Object>(objectArray.length);
            for (Object object2 : objectArray) {
                arrayList.add(object2);
            }
            return arrayList;
        }
        ArrayList<Object> arrayList = new ArrayList<Object>(1);
        arrayList.add(object);
        return arrayList;
    }
}

