package studio.glace.glantielytratarget.libs.nixlib.config;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import studio.glace.glantielytratarget.libs.nixlib.config.ConfigException;
import studio.glace.glantielytratarget.libs.nixlib.config.ConfigField;
import studio.glace.glantielytratarget.libs.nixlib.config.TypeCoercion;
import studio.glace.glantielytratarget.libs.nixlib.config.annotations.ConfigVersion;
import studio.glace.glantielytratarget.libs.nixlib.config.migration.MigrationRegistry;
import studio.glace.glantielytratarget.libs.nixlib.config.validation.ValidationException;
import studio.glace.glantielytratarget.libs.nixlib.config.validation.Validators;
import studio.glace.glantielytratarget.libs.nixlib.config.yaml.Nodes;
import studio.glace.glantielytratarget.libs.nixlib.config.yaml.YamlReader;
import studio.glace.glantielytratarget.libs.nixlib.config.yaml.YamlWriter;

public abstract class Config {
    private final List<ConfigField> fields;
    private final int declaredVersion;
    private final String versionKey;
    private final MigrationRegistry migrations = new MigrationRegistry();
    private Path file;
    private final List<String> warnings = new ArrayList<String>();

    protected Config() {
        this.fields = ConfigField.scan(this.getClass());
        ConfigVersion annotation = this.getClass().getAnnotation(ConfigVersion.class);
        this.declaredVersion = annotation == null ? 1 : annotation.value();
        this.versionKey = annotation == null ? "config-version" : annotation.key();
        this.registerMigrations(this.migrations);
    }

    protected void registerMigrations(MigrationRegistry migrationRegistry) {
    }

    public final int version() {
        return this.declaredVersion;
    }

    public final List<String> warnings() {
        return List.copyOf(this.warnings);
    }

    public final void load(Path path) {
        this.file = path;
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent(), new FileAttribute[0]);
                }
                this.save();
                this.warnings.clear();
                this.onLoad();
                return;
            }
            String string = Files.readString(path, StandardCharsets.UTF_8);
            this.loadFromString(string);
            this.save();
        }
        catch (IOException e) {
            throw new ConfigException("failed to load " + String.valueOf(path), e);
        }
    }

    public final void loadFromString(String string) {
        Map<String, Object> map = YamlReader.read(new StringReader(string));
        this.applyData(map);
    }

    public final void loadFromReader(Reader reader) {
        this.applyData(YamlReader.read(reader));
    }

    private void applyData(Map<String, Object> map) {
        this.warnings.clear();
        int n = this.readVersion(map);
        if (n < this.declaredVersion) {
            this.migrations.apply(n, this.declaredVersion, map);
            map.put(this.versionKey, this.declaredVersion);
        } else if (n > this.declaredVersion) {
            this.warnings.add("config file is newer (v" + n + ") than expected (v" + this.declaredVersion + "); reading anyway");
        }
        for (ConfigField configField : this.fields) {
            Object object = Nodes.get(map, configField.path);
            if (object == null) {
                this.warnings.add("missing key: " + configField.path + " (using default)");
                continue;
            }
            this.applyField(configField, object);
        }
        this.onLoad();
    }

    private void applyField(ConfigField configField, Object object) {
        Class<?> type = configField.field.getType();
        Optional<Object> optional = TypeCoercion.coerce(object, type);
        if (optional.isEmpty()) {
            this.warnings.add("type mismatch at " + configField.path + ": expected " + type.getSimpleName() + " but got " + object.getClass().getSimpleName() + " (using default)");
            return;
        }
        Object object2 = optional.get();
        try {
            Validators.validate(configField.field, object2);
        }
        catch (ValidationException e) {
            this.warnings.add("validation failed at " + configField.path + ": " + e.getMessage() + " (using default)");
            return;
        }
        configField.write(this, object2);
    }

    private int readVersion(Map<String, Object> map) {
        Object object = map.get(this.versionKey);
        if (object instanceof Number) {
            Number number = (Number)object;
            return number.intValue();
        }
        if (object instanceof String) {
            String string = (String)object;
            try {
                return Integer.parseInt(string.trim());
            }
            catch (NumberFormatException e) {
                // empty catch block
            }
        }
        return this.declaredVersion;
    }

    public final void save() {
        if (this.file == null) {
            throw new ConfigException("no file bound; call load(path) first");
        }
        try {
            if (this.file.getParent() != null) {
                Files.createDirectories(this.file.getParent(), new FileAttribute[0]);
            }
            Files.writeString(this.file, (CharSequence)this.dump(), StandardCharsets.UTF_8, new OpenOption[0]);
        }
        catch (IOException e) {
            throw new ConfigException("failed to save " + String.valueOf(this.file), e);
        }
    }

    public final String dump() {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, List<String>> linkedHashMap2 = new LinkedHashMap<String, List<String>>();
        linkedHashMap.put(this.versionKey, this.declaredVersion);
        linkedHashMap2.put(this.versionKey, List.of("Schema version for this file. Do not edit by hand."));
        for (ConfigField configField : this.fields) {
            Nodes.set(linkedHashMap, configField.path, configField.read(this));
            if (configField.comments.isEmpty()) continue;
            linkedHashMap2.put(configField.path, configField.comments);
        }
        return YamlWriter.write(linkedHashMap, linkedHashMap2);
    }

    public final void reload() {
        if (this.file == null) {
            throw new ConfigException("no file bound; call load(path) first");
        }
        this.load(this.file);
    }

    protected void onLoad() {
    }
}

