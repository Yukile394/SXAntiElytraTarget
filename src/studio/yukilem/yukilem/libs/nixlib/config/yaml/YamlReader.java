package studio.yukilem.yukilem.libs.nixlib.config.yaml;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;

public final class YamlReader {
    private YamlReader() {
    }

    private static Yaml strictYaml() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(64);
        loaderOptions.setCodePointLimit(Integer.MAX_VALUE);
        loaderOptions.setAllowDuplicateKeys(false);
        return new Yaml(new SafeConstructor(loaderOptions));
    }

    private static Yaml lenientYaml() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(64);
        loaderOptions.setCodePointLimit(Integer.MAX_VALUE);
        loaderOptions.setAllowDuplicateKeys(true);
        return new Yaml(new MergingSafeConstructor(loaderOptions));
    }

    public static Map<String, Object> read(String string) {
        if (string == null || string.isBlank()) {
            return new LinkedHashMap<String, Object>();
        }
        return YamlReader.read(new StringReader(string));
    }

    public static Map<String, Object> read(Reader reader) {
        return YamlReader.readWith(reader, YamlReader.strictYaml());
    }

    public static Map<String, Object> readMergingDuplicates(String string) {
        if (string == null || string.isBlank()) {
            return new LinkedHashMap<String, Object>();
        }
        return YamlReader.readMergingDuplicates(new StringReader(string));
    }

    public static Map<String, Object> readMergingDuplicates(Reader reader) {
        return YamlReader.readWith(reader, YamlReader.lenientYaml());
    }

    private static Map<String, Object> readWith(Reader reader, Yaml yaml) {
        Object t = yaml.load(reader);
        if (t == null) {
            return new LinkedHashMap<String, Object>();
        }
        if (!(t instanceof Map)) {
            throw new IllegalStateException("root yaml node is not a mapping");
        }
        Map map = (Map)t;
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>(map.size());
        for (Map.Entry<?, ?> entry : ((Map<?, ?>)map).entrySet()) {
            linkedHashMap.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return linkedHashMap;
    }

    private static final class MergingSafeConstructor
    extends SafeConstructor {
        MergingSafeConstructor(LoaderOptions loaderOptions) {
            super(loaderOptions);
        }

        @Override
        protected void flattenMapping(MappingNode mappingNode) {
            super.flattenMapping(mappingNode);
        }

        @Override
        protected void constructMapping2ndStep(MappingNode mappingNode, Map<Object, Object> map) {
            for (NodeTuple nodeTuple : mappingNode.getValue()) {
                Node keyNode = nodeTuple.getKeyNode();
                Node valueNode = nodeTuple.getValueNode();
                Object object = this.constructObject(keyNode);
                Object object2 = this.constructObject(valueNode);
                if (map.containsKey(object)) {
                    Object object3 = map.get(object);
                    map.put(object, MergingSafeConstructor.mergeValues(object3, object2));
                    continue;
                }
                map.put(object, object2);
            }
        }

        @Override
        protected Map<Object, Object> constructMapping(MappingNode mappingNode) {
            Map<Object, Object> map = this.createDefaultMap(mappingNode.getValue().size());
            this.constructMapping2ndStep(mappingNode, map);
            return map;
        }

        private static Object mergeValues(Object object, Object object2) {
            List list;
            ArrayList<Object> arrayList = new ArrayList<Object>();
            if (object instanceof List) {
                list = (List)object;
                arrayList.addAll(list);
            } else {
                arrayList.add(object);
            }
            if (object2 instanceof List) {
                list = (List)object2;
                arrayList.addAll(list);
            } else {
                arrayList.add(object2);
            }
            return arrayList;
        }
    }
}

