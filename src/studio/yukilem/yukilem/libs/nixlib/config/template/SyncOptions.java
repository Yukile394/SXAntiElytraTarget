package studio.glace.glantielytratarget.libs.nixlib.config.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class SyncOptions {
    private String versionKey = "config-version";
    private Consumer<String> warningSink = null;
    private boolean dropUnknownKeys = true;
    private boolean versionKeyLast = true;
    private boolean freeformRoot = false;
    private boolean mergeDuplicateKeys = false;
    private final List<String> freeformPaths = new ArrayList<String>(0);

    public static SyncOptions create() {
        return new SyncOptions();
    }

    public SyncOptions versionKey(String string) {
        this.versionKey = string == null || string.isBlank() ? "config-version" : string;
        return this;
    }

    public SyncOptions onWarning(Consumer<String> consumer) {
        this.warningSink = consumer;
        return this;
    }

    public SyncOptions dropUnknownKeys(boolean bl) {
        this.dropUnknownKeys = bl;
        return this;
    }

    public SyncOptions versionKeyLast(boolean bl) {
        this.versionKeyLast = bl;
        return this;
    }

    public SyncOptions freeformRoot(boolean bl) {
        this.freeformRoot = bl;
        return this;
    }

    public SyncOptions freeform(String ... stringArray) {
        if (stringArray == null) {
            return this;
        }
        for (String string : stringArray) {
            if (string == null || string.isBlank()) continue;
            this.freeformPaths.add(string);
        }
        return this;
    }

    public SyncOptions freeform(Collection<String> collection) {
        if (collection == null) {
            return this;
        }
        for (String string : collection) {
            if (string == null || string.isBlank()) continue;
            this.freeformPaths.add(string);
        }
        return this;
    }

    public SyncOptions mergeDuplicateKeys() {
        this.mergeDuplicateKeys = true;
        return this;
    }

    public SyncOptions mergeDuplicateKeys(boolean bl) {
        this.mergeDuplicateKeys = bl;
        return this;
    }

    String versionKey() {
        return this.versionKey;
    }

    boolean shouldDropUnknown() {
        return this.dropUnknownKeys;
    }

    boolean shouldVersionKeyLast() {
        return this.versionKeyLast;
    }

    boolean freeformRoot() {
        return this.freeformRoot;
    }

    boolean shouldMergeDuplicateKeys() {
        return this.mergeDuplicateKeys;
    }

    List<String> freeformPaths() {
        return Collections.unmodifiableList(this.freeformPaths);
    }

    boolean isFreeform(String string) {
        if (this.freeformPaths.isEmpty() || string == null) {
            return false;
        }
        for (String string2 : this.freeformPaths) {
            if (string.equals(string2)) {
                return true;
            }
            if (!string.startsWith(string2 + ".")) continue;
            return true;
        }
        return false;
    }

    void warn(String string) {
        if (this.warningSink != null) {
            this.warningSink.accept(string);
        }
    }
}

