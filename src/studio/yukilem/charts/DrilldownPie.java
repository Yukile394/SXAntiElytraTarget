package studio.yukilem.charts;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import studio.yukilem.charts.CustomChart;
import studio.yukilem.json.JsonObjectBuilder;

public class DrilldownPie
extends CustomChart {
    private final Callable<Map<String, Map<String, Integer>>> callable;

    public DrilldownPie(String string, Callable<Map<String, Map<String, Integer>>> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    public JsonObjectBuilder.JsonObject getChartData() throws Exception {
        Iterator<Map.Entry<String, Map<String, Integer>>> iterator;
        Map<String, Map<String, Integer>> map;
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        Map<String, Map<String, Integer>> map2 = this.callable.call();
        Map<String, Map<String, Integer>> map3 = map2;
        if (map3 == null || (map = map2).isEmpty()) {
            return null;
        }
        boolean bl = true;
        Map<String, Map<String, Integer>> map4 = map2;
        Iterator<Map.Entry<String, Map<String, Integer>>> iterator2 = map4.entrySet().iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Map.Entry<String, Integer>> iterator3;
            Iterator<Map.Entry<String, Map<String, Integer>>> iterator4 = iterator2;
            Map.Entry<String, Map<String, Integer>> entry = iterator4.next();
            JsonObjectBuilder jsonObjectBuilder2 = new JsonObjectBuilder();
            boolean bl2 = true;
            Map<String, Map<String, Integer>> map5 = map2;
            Map.Entry<String, Map<String, Integer>> entry2 = entry;
            String key = entry2.getKey();
            Iterator<Map.Entry<String, Integer>> iterator5 = map5.get(key).entrySet().iterator();
            while ((iterator3 = iterator5).hasNext()) {
                Iterator<Map.Entry<String, Integer>> iterator6 = iterator5;
                Map.Entry<String, Integer> entry3 = iterator6.next();
                JsonObjectBuilder jsonObjectBuilder3 = jsonObjectBuilder2;
                Map.Entry<String, Integer> entry4 = entry3;
                String key2 = entry4.getKey();
                Map.Entry<String, Integer> entry5 = entry3;
                int value = entry5.getValue();
                JsonObjectBuilder jsonObjectBuilder4 = jsonObjectBuilder3.appendField(key2, value);
                bl2 = false;
            }
            boolean bl3 = bl2;
            if (bl3) continue;
            bl = false;
            JsonObjectBuilder jsonObjectBuilder5 = jsonObjectBuilder;
            Map.Entry<String, Map<String, Integer>> entry6 = entry;
            String key3 = entry6.getKey();
            JsonObjectBuilder jsonObjectBuilder6 = jsonObjectBuilder2;
            JsonObjectBuilder.JsonObject jsonObject = jsonObjectBuilder6.build();
            JsonObjectBuilder jsonObjectBuilder7 = jsonObjectBuilder5.appendField(key3, jsonObject);
        }
        boolean bl4 = bl;
        if (bl4) {
            return null;
        }
        JsonObjectBuilder jsonObjectBuilder8 = new JsonObjectBuilder();
        JsonObjectBuilder jsonObjectBuilder9 = jsonObjectBuilder;
        JsonObjectBuilder.JsonObject jsonObject = jsonObjectBuilder9.build();
        return jsonObjectBuilder8.appendField("values", jsonObject).build();
    }
}

