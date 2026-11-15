package studio.yukilem.yukilem.libs.nixlib.config.migration;

import java.util.HashMap;
import java.util.Map;
import studio.yukilem.yukilem.libs.nixlib.config.ConfigException;
import studio.yukilem.yukilem.libs.nixlib.config.migration.MigrationContext;
import studio.yukilem.yukilem.libs.nixlib.config.migration.MigrationStep;

public final class MigrationRegistry {
    private final Map<Integer, Entry> steps = new HashMap<Integer, Entry>();

    public MigrationRegistry register(int n, int n2, MigrationStep migrationStep) {
        if (n2 != n + 1) {
            throw new IllegalArgumentException("migration must be sequential, got " + n + " -> " + n2);
        }
        if (this.steps.containsKey(n)) {
            throw new IllegalStateException("duplicate migration from version " + n);
        }
        this.steps.put(n, new Entry(n2, migrationStep));
        return this;
    }

    public void apply(int n, int n2, Map<String, Object> map) {
        int n3 = n;
        int n4 = 0;
        while (n3 < n2) {
            Entry entry = this.steps.get(n3);
            if (entry == null) {
                throw new ConfigException("no migration registered from v" + n3 + "; cannot reach v" + n2);
            }
            entry.step.migrate(new MigrationContext(map, n3, entry.to));
            n3 = entry.to;
            if (++n4 <= 256) continue;
            throw new ConfigException("migration loop detected near v" + n3);
        }
    }

    private record Entry(int to, MigrationStep step) {
    }
}

