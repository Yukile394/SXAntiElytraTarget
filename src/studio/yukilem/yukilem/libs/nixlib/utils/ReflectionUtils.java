package studio.glace.glantielytratarget.libs.nixlib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public final class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static boolean classExists(String string) {
        try {
            Class.forName(string, false, ReflectionUtils.class.getClassLoader());
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Optional<Class<?>> findClass(String string) {
        try {
            return Optional.of(Class.forName(string));
        }
        catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static Optional<Method> findMethod(Class<?> clazz, String string, Class<?> ... classArray) {
        try {
            Method declaredMethod = clazz.getDeclaredMethod(string, classArray);
            declaredMethod.setAccessible(true);
            return Optional.of(declaredMethod);
        }
        catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    public static Optional<Field> findField(Class<?> clazz, String string) {
        for (Class<?> clazz2 = clazz; clazz2 != null; clazz2 = clazz2.getSuperclass()) {
            try {
                Field declaredField = clazz2.getDeclaredField(string);
                declaredField.setAccessible(true);
                return Optional.of(declaredField);
            }
            catch (NoSuchFieldException e) {
                continue;
            }
        }
        return Optional.empty();
    }

    public static <T> T invokeUnchecked(Method method, Object object, Object ... objectArray) {
        try {
            Object object2 = method.invoke(object, objectArray);
            return (T)object2;
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException("reflection invoke failed: " + method.getName(), e);
        }
    }
}

