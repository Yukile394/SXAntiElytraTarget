package studio.glace.glantielytratarget.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.libs.nixlib.config.yaml.Nodes;
import studio.glace.glantielytratarget.libs.nixlib.config.yaml.YamlReader;
import studio.glace.glantielytratarget.libs.nixlib.config.yaml.YamlWriter;

final class LanguageYamlMigration {
    private LanguageYamlMigration() {
    }

    @Nullable
    static String migrateUserText(@Nullable String string) {
        Map<String, Object> map;
        if (string == null || string.isBlank()) {
            return string;
        }
        try {
            map = YamlReader.read(string);
        }
        catch (Exception e) {
            Exception e2 = e;
            return string;
        }
        Map<String, Object> map2 = map;
        if (map2 == null) {
            return string;
        }
        Map<String, Object> map3 = map;
        if (!LanguageYamlMigration.normalizeMenuKeys(map3)) {
            return string;
        }
        Map<String, Object> map4 = map;
        Map<String, List<String>> map5 = Collections.emptyMap();
        return LanguageYamlMigration.ensureQuotedMenuYesNo(YamlWriter.write(map4, map5));
    }

    static boolean normalizeMenuKeys(@NotNull Map<String, Object> map) {
        Map map2;
        Object v;
        Boolean bl;
        Map map3;
        Object v2;
        Object v3;
        Map map4;
        Object v4;
        Object v5;
        Map map5;
        Object object = Nodes.get(map, "menu");
        Object object2 = object;
        if (!(object2 instanceof Map)) {
            return false;
        }
        Object object3 = object;
        Map map6 = (Map)object3;
        Map map7 = map5 = map6;
        boolean bl2 = false;
        Map map8 = map7;
        Boolean bl3 = Boolean.TRUE;
        Object v6 = v5 = map8.remove(bl3);
        if (v6 == null) {
            Map map9 = map7;
            v5 = map9.remove("true");
        }
        if ((v4 = v5) != null && !(map4 = map7).containsKey("yes")) {
            Map map10 = map7;
            Object v7 = v5;
            Object v8 = map10.put("yes", v7);
            bl2 = true;
        }
        if ((v3 = (v2 = (map3 = map7).remove(bl = Boolean.FALSE))) == null) {
            Map map11 = map7;
            v2 = map11.remove("false");
        }
        if ((v = v2) != null && !(map2 = map7).containsKey("no")) {
            Map map12 = map7;
            Object v9 = v2;
            Object v10 = map12.put("no", v9);
            bl2 = true;
        }
        boolean bl4 = bl2;
        return bl4;
    }

    @NotNull
    static String ensureQuotedMenuYesNo(@NotNull String string) {
        return string.replaceAll("(?m)^  true: ", "  \"yes\": ").replaceAll("(?m)^  false: ", "  \"no\": ").replaceAll("(?m)^  yes: ", "  \"yes\": ").replaceAll("(?m)^  no: ", "  \"no\": ");
    }
}

