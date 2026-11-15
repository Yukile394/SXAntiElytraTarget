package studio.yukilem.yukilem.libs.nixlib.config.migration;

import java.util.Map;
import studio.yukilem.yukilem.libs.nixlib.config.yaml.Nodes;

public final class MigrationContext {
    private final Map<String, Object> root;
    private final int from;
    private final int to;

    MigrationContext(Map<String, Object> map, int n, int n2) {
        this.root = map;
        this.from = n;
        this.to = n2;
    }

    public int from() {
        return this.from;
    }

    public int to() {
        return this.to;
    }

    public Map<String, Object> root() {
        return this.root;
    }

    public Object get(String string) {
        return Nodes.get(this.root, string);
    }

    public boolean has(String string) {
        return Nodes.contains(this.root, string);
    }

    public void set(String string, Object object) {
        Nodes.set(this.root, string, object);
    }

    public void rename(String string, String string2) {
        Object object = Nodes.get(this.root, string);
        if (object == null) {
            return;
        }
        Nodes.remove(this.root, string);
        Nodes.set(this.root, string2, object);
    }

    public boolean remove(String string) {
        return Nodes.remove(this.root, string);
    }
}

