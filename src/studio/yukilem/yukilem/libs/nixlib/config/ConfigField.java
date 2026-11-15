package studio.yukilem.yukilem.libs.nixlib.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import studio.yukilem.yukilem.libs.nixlib.config.Config;
import studio.yukilem.yukilem.libs.nixlib.config.ConfigException;
import studio.yukilem.yukilem.libs.nixlib.config.annotations.Comment;
import studio.yukilem.yukilem.libs.nixlib.config.annotations.Path;

final class ConfigField {
    final Field field;
    final String path;
    final List<String> comments;

    private ConfigField(Field field, String string, List<String> list) {
        this.field = field;
        this.path = string;
        this.comments = list;
    }

    static List<ConfigField> scan(Class<?> clazz) {
        ArrayList<ConfigField> arrayList = new ArrayList<ConfigField>();
        for (Class<?> clazz2 = clazz; clazz2 != null && clazz2 != Object.class && clazz2 != Config.class; clazz2 = clazz2.getSuperclass()) {
            for (Field field : clazz2.getDeclaredFields()) {
                Path path;
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) || (path = field.getAnnotation(Path.class)) == null) continue;
                field.setAccessible(true);
                Comment annotation = field.getAnnotation(Comment.class);
                List<String> list = annotation == null ? Collections.emptyList() : List.of(annotation.value());
                arrayList.add(new ConfigField(field, path.value(), list));
            }
        }
        return arrayList;
    }

    Object read(Object object) {
        try {
            return this.field.get(object);
        }
        catch (IllegalAccessException e) {
            throw new ConfigException("cannot read field " + this.field.getName(), e);
        }
    }

    void write(Object object, Object object2) {
        try {
            this.field.set(object, object2);
        }
        catch (IllegalAccessException e) {
            throw new ConfigException("cannot write field " + this.field.getName(), e);
        }
    }
}

