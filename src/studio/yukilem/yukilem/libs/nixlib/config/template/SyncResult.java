package studio.glace.glantielytratarget.libs.nixlib.config.template;

import java.util.Map;
import studio.glace.glantielytratarget.libs.nixlib.version.SemanticVersion;

public final class SyncResult {
    private final String mergedYaml;
    private final Map<String, Object> mergedData;
    private final boolean changed;
    private final SemanticVersion bundledVersion;
    private final SemanticVersion userVersion;

    SyncResult(String string, Map<String, Object> map, boolean bl, SemanticVersion semanticVersion, SemanticVersion semanticVersion2) {
        this.mergedYaml = string;
        this.mergedData = map;
        this.changed = bl;
        this.bundledVersion = semanticVersion;
        this.userVersion = semanticVersion2;
    }

    public String mergedYaml() {
        return this.mergedYaml;
    }

    public Map<String, Object> mergedData() {
        return this.mergedData;
    }

    public boolean changed() {
        return this.changed;
    }

    public SemanticVersion bundledVersion() {
        return this.bundledVersion;
    }

    public SemanticVersion userVersion() {
        return this.userVersion;
    }

    public boolean isUserBehind() {
        return this.userVersion.isOlderThan(this.bundledVersion);
    }
}

