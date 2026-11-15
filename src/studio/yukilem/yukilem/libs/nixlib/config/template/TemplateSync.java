package studio.yukilem.yukilem.libs.nixlib.config.template;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import studio.yukilem.yukilem.libs.nixlib.config.ConfigException;
import studio.yukilem.yukilem.libs.nixlib.config.yaml.YamlReader;
import studio.yukilem.yukilem.libs.nixlib.config.yaml.YamlWriter;
import studio.yukilem.yukilem.libs.nixlib.version.SemanticVersion;

public final class TemplateSync {
   private TemplateSync() {
   }

   public static SyncResult synchronize(String string0, String string1, SyncOptions syncOptions2) {
      SyncOptions syncOptions3 = syncOptions2 == null ? SyncOptions.create() : syncOptions2;
      if (string0 == null) {
         throw new ConfigException("template yaml is null");
      }

      boolean flag4 = syncOptions3.shouldMergeDuplicateKeys();
      Map map5 = flag4 ? YamlReader.readMergingDuplicates(string0) : YamlReader.read(string0);
      Map map6 = string1 != null && !string1.isBlank() ? (flag4 ? YamlReader.readMergingDuplicates(string1) : YamlReader.read(string1)) : new LinkedHashMap();
      SemanticVersion semanticVersion7 = readVersion(map5, syncOptions3.versionKey());
      SemanticVersion semanticVersion8 = readVersion(map6, syncOptions3.versionKey());
      Map map9 = mergeMap(map5, map6, "", syncOptions3);
      map9.put(syncOptions3.versionKey(), semanticVersion7.toString());
      if (syncOptions3.shouldVersionKeyLast()) {
         map9 = withVersionKeyLast(map9, syncOptions3.versionKey());
      }

      Map map10 = CommentScanner.extract(string0);
      String string11 = YamlWriter.write(map9, map10);
      boolean flag12 = string1 == null || !string11.equals(string1);
      return new SyncResult(string11, map9, flag12, semanticVersion7, semanticVersion8);
   }

   public static SyncResult synchronizeFile(InputStream inputStream0, Path path1, SyncOptions syncOptions2) {
      if (inputStream0 == null) {
         throw new ConfigException("template stream is null");
      }

      String string3 = readAll(inputStream0);
      String string4 = null;

      try {
         if (path1 != null && Files.exists(path1)) {
            string4 = Files.readString(path1, StandardCharsets.UTF_8);
         }
      } catch (IOException ioException8) {
         throw new ConfigException("failed to read user file " + path1, ioException8);
      }

      SyncResult syncResult5 = synchronize(string3, string4, syncOptions2);
      if (path1 != null && (string4 == null || syncResult5.changed())) {
         try {
            if (path1.getParent() != null) {
               Files.createDirectories(path1.getParent());
            }

            Files.writeString(path1, syncResult5.mergedYaml(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
         } catch (IOException ioException7) {
            throw new ConfigException("failed to write merged config to " + path1, ioException7);
         }
      }

      return syncResult5;
   }

   private static String readAll(InputStream inputStream0) {
      try (InputStreamReader inputStreamReader1 = new InputStreamReader(inputStream0, StandardCharsets.UTF_8)) {
         StringBuilder stringBuilder2 = new StringBuilder();
         char[] cs3 = new char[4096];

         int i4;
         while ((i4 = inputStreamReader1.read(cs3)) != -1) {
            stringBuilder2.append(cs3, 0, i4);
         }

         return stringBuilder2.toString();
      } catch (IOException ioException8) {
         throw new ConfigException("failed to read template stream", ioException8);
      }
   }

   private static Map<String, Object> withVersionKeyLast(Map<String, Object> map0, String string1) {
      if (!map0.containsKey(string1)) {
         return map0;
      }

      Object object2 = map0.remove(string1);
      LinkedHashMap linkedHashMap3 = new LinkedHashMap(map0.size() + 1);
      linkedHashMap3.putAll(map0);
      linkedHashMap3.put(string1, object2);
      return linkedHashMap3;
   }

   private static SemanticVersion readVersion(Map<String, Object> map0, String string1) {
      Object object2 = map0.get(string1);
      return object2 == null ? SemanticVersion.ZERO : SemanticVersion.parseOr(String.valueOf(object2), SemanticVersion.ZERO);
   }

   private static Map<String, Object> mergeMap(Map<String, Object> map0, Map<String, Object> map1, String string2, SyncOptions syncOptions3) {
      boolean flag4 = syncOptions3.isFreeform(string2);
      if (flag4) {
         LinkedHashMap linkedHashMap14 = new LinkedHashMap(map1.size());

         for (Entry entry19 : map1.entrySet()) {
            String string22 = (String)entry19.getKey();
            if (!string2.isEmpty() || !string22.equals(syncOptions3.versionKey())) {
               String string25 = string2.isEmpty() ? string22 : string2 + "." + string22;
               Object object26 = entry19.getValue();
               if (map0.get(string22) instanceof Map arg29 && object26 instanceof Map map31) {
                  linkedHashMap14.put(string22, mergeMap(asStringMap(arg29), asStringMap(map31), string25, syncOptions3));
               } else {
                  linkedHashMap14.put(string22, object26);
               }
            }
         }

         return linkedHashMap14;
      } else {
         LinkedHashMap linkedHashMap5 = new LinkedHashMap(map0.size());

         for (Entry entry7 : map0.entrySet()) {
            String string8 = (String)entry7.getKey();
            if (!string2.isEmpty() || !string8.equals(syncOptions3.versionKey())) {
               String string9 = string2.isEmpty() ? string8 : string2 + "." + string8;
               Object object10 = entry7.getValue();
               if (!map1.containsKey(string8)) {
                  if (!flag4) {
                     linkedHashMap5.put(string8, deepCopy(object10));
                  }
               } else {
                  Object object11 = map1.get(string8);
                  if (object10 instanceof Map map28) {
                     if (object11 instanceof Map map30) {
                        linkedHashMap5.put(string8, mergeMap(asStringMap(map28), asStringMap(map30), string9, syncOptions3));
                     } else if (flag4) {
                        linkedHashMap5.put(string8, object11);
                     } else {
                        syncOptions3.warn("type mismatch at " + string9 + ": expected map, using template default");
                        linkedHashMap5.put(string8, deepCopy(object10));
                     }
                  } else if (object10 instanceof Collection collection12) {
                     if (object11 == null) {
                        linkedHashMap5.put(string8, flag4 ? null : deepCopyList(collection12));
                     } else if (object11 instanceof Collection collection13) {
                        linkedHashMap5.put(string8, new ArrayList(collection13));
                     } else if (flag4) {
                        linkedHashMap5.put(string8, object11);
                     } else {
                        syncOptions3.warn("type mismatch at " + string9 + ": expected list, using template default");
                        linkedHashMap5.put(string8, deepCopyList(collection12));
                     }
                  } else if (!(object11 instanceof Map) && !(object11 instanceof Collection)) {
                     linkedHashMap5.put(string8, object11);
                  } else if (flag4) {
                     linkedHashMap5.put(string8, object11);
                  } else {
                     syncOptions3.warn("type mismatch at " + string9 + ": expected scalar, using template default");
                     linkedHashMap5.put(string8, deepCopy(object10));
                  }
               }
            }
         }

         boolean flag15 = !syncOptions3.shouldDropUnknown() || flag4 || syncOptions3.freeformRoot() && string2.isEmpty();
         if (flag15) {
            for (Entry entry20 : map1.entrySet()) {
               String string23 = (String)entry20.getKey();
               if (!string23.equals(syncOptions3.versionKey()) && !map0.containsKey(string23)) {
                  linkedHashMap5.put(string23, entry20.getValue());
               }
            }
         } else {
            for (String string21 : map1.keySet()) {
               if (!string21.equals(syncOptions3.versionKey()) && !map0.containsKey(string21)) {
                  String string24 = string2.isEmpty() ? string21 : string2 + "." + string21;
                  syncOptions3.warn("dropping unknown key: " + string24);
               }
            }
         }

         return linkedHashMap5;
      }
   }

   private static Object deepCopy(Object object0) {
      if (!(object0 instanceof Map map1)) {
         return object0 instanceof Collection arg5 ? deepCopyList(arg5) : object0;
      } else {
         LinkedHashMap linkedHashMap2 = new LinkedHashMap(map1.size());

         for (Entry<?, ?> entry4 : ((Map<?, ?>)map1).entrySet()) {
            linkedHashMap2.put(String.valueOf(entry4.getKey()), deepCopy(entry4.getValue()));
         }

         return linkedHashMap2;
      }
   }

   private static List<Object> deepCopyList(Collection<?> collection0) {
      ArrayList arrayList1 = new ArrayList(collection0.size());

      for (Object object3 : collection0) {
         arrayList1.add(deepCopy(object3));
      }

      return arrayList1;
   }

   private static Map<String, Object> asStringMap(Map<?, ?> map0) {
      LinkedHashMap linkedHashMap1 = new LinkedHashMap(map0.size());

      for (Entry entry3 : map0.entrySet()) {
         linkedHashMap1.put(String.valueOf(entry3.getKey()), entry3.getValue());
      }

      return linkedHashMap1;
   }
}
