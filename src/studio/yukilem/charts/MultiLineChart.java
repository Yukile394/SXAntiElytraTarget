package studio.glace.charts;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import studio.glace.charts.CustomChart;
import studio.glace.json.JsonObjectBuilder;

public class MultiLineChart
extends CustomChart {
    private final Callable<Map<String, Integer>> callable;

    public MultiLineChart(String string, Callable<Map<String, Integer>> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        Iterator<Map.Entry<String, Integer>> iterator;
        Map<String, Integer> map;
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        Map<String, Integer> map2 = this.callable.call();
        Map<String, Integer> map3 = map2;
        if (map3 == null || (map = map2).isEmpty()) {
            return null;
        }
        boolean bl = true;
        Map<String, Integer> map4 = map2;
        Iterator<Map.Entry<String, Integer>> iterator2 = map4.entrySet().iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Map.Entry<String, Integer>> iterator3 = iterator2;
            Map.Entry<String, Integer> entry = iterator3.next();
            Map.Entry<String, Integer> entry2 = entry;
            if (entry2.getValue() == 0) continue;
            bl = false;
            JsonObjectBuilder jsonObjectBuilder2 = jsonObjectBuilder;
            Map.Entry<String, Integer> entry3 = entry;
            String key = entry3.getKey();
            Map.Entry<String, Integer> entry4 = entry;
            int value = entry4.getValue();
            JsonObjectBuilder jsonObjectBuilder3 = jsonObjectBuilder2.appendField(key, value);
        }
        boolean bl2 = bl;
        if (bl2) {
            return null;
        }
        JsonObjectBuilder jsonObjectBuilder4 = new JsonObjectBuilder();
        JsonObjectBuilder jsonObjectBuilder5 = jsonObjectBuilder;
        JsonObjectBuilder.JsonObject jsonObject = jsonObjectBuilder5.build();
        return jsonObjectBuilder4.appendField("values", jsonObject).build();
    }
}

