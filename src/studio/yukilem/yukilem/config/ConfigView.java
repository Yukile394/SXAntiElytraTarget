package studio.yukilem.yukilem.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.yukilem.yukilem.libs.nixlib.config.TypeCoercion;
import studio.yukilem.yukilem.libs.nixlib.config.yaml.Nodes;

public final class ConfigView {
    private final Map<String, Object> data;

    public ConfigView(@NotNull Map<String, Object> map) {
        this.data = map;
    }

    @Nullable
    public Object getRaw(@NotNull String string) {
        return Nodes.get(this.data, string);
    }

    public boolean getBoolean(@NotNull String string, boolean bl) {
        Object object2 = Nodes.get(this.data, string);
        Class<Boolean> clazz = Boolean.class;
        Optional<Object> optional = TypeCoercion.coerce(object2, clazz);
        Function<Object, Boolean> function = object -> (boolean)((Boolean)object);
        Optional<Boolean> optional2 = optional.map(function);
        Boolean bl2 = bl;
        return optional2.orElse(bl2);
    }

    public int getInt(@NotNull String string, int n) {
        Object object2 = Nodes.get(this.data, string);
        Class<Integer> clazz = Integer.class;
        Optional<Object> optional = TypeCoercion.coerce(object2, clazz);
        Function<Object, Integer> function = object -> (int)((Integer)object);
        Optional<Integer> optional2 = optional.map(function);
        Integer n2 = n;
        return optional2.orElse(n2);
    }

    public long getLong(@NotNull String string, long l) {
        Object object2 = Nodes.get(this.data, string);
        Class<Long> clazz = Long.class;
        Optional<Object> optional = TypeCoercion.coerce(object2, clazz);
        Function<Object, Long> function = object -> (long)((Long)object);
        Optional<Long> optional2 = optional.map(function);
        Long l2 = l;
        return optional2.orElse(l2);
    }

    public double getDouble(@NotNull String string, double d) {
        Object object2 = Nodes.get(this.data, string);
        Class<Double> clazz = Double.class;
        Optional<Object> optional = TypeCoercion.coerce(object2, clazz);
        Function<Object, Double> function = object -> (double)((Double)object);
        Optional<Double> optional2 = optional.map(function);
        Double d2 = d;
        return optional2.orElse(d2);
    }

    @NotNull
    public String getString(@NotNull String string, @NotNull String string2) {
        String string3;
        Object object = Nodes.get(this.data, string);
        Object object2 = object;
        if (object2 == null) {
            string3 = string2;
        } else {
            Object object3 = object;
            string3 = String.valueOf(object3);
        }
        return string3;
    }

    @Nullable
    public String getStringOrNull(@NotNull String string) {
        String string2;
        Object object = Nodes.get(this.data, string);
        Object object2 = object;
        if (object2 == null) {
            string2 = null;
        } else {
            Object object3 = object;
            string2 = String.valueOf(object3);
        }
        return string2;
    }

    @NotNull
    public List<String> getStringList(@NotNull String string) {
        Iterator iterator;
        ArrayList<String> arrayList;
        Object object = Nodes.get(this.data, string);
        Object object2 = object;
        if (!(object2 instanceof List)) {
            return new ArrayList<String>();
        }
        Object object3 = object;
        List list = (List)object3;
        List list2 = list;
        int size = list2.size();
        ArrayList<String> arrayList2 = arrayList = new ArrayList<String>(size);
        List list3 = list;
        Iterator iterator2 = list3.iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator iterator3 = iterator2;
            Object e = iterator3.next();
            Object e2 = e;
            if (e2 == null) continue;
            ArrayList<String> arrayList3 = arrayList2;
            Object e3 = e;
            String string2 = String.valueOf(e3);
            boolean bl = arrayList3.add(string2);
        }
        ArrayList<String> arrayList4 = arrayList2;
        return arrayList4;
    }

    @NotNull
    public List<?> getList(@NotNull String string, @NotNull List<?> list) {
        List list2;
        Object object = Nodes.get(this.data, string);
        Object object2 = object;
        if (object2 instanceof List) {
            Object object3 = object;
            list2 = (List)object3;
        } else {
            list2 = list;
        }
        return list2;
    }

    @NotNull
    public Map<String, Object> data() {
        return Collections.unmodifiableMap(this.data);
    }
}

