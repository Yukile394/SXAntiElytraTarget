package studio.glace.charts;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import studio.glace.charts.CustomChart;
import studio.glace.json.JsonObjectBuilder;

public class AdvancedBarChart
extends CustomChart {
    private final Callable<Map<String, int[]>> callable;

    public AdvancedBarChart(String string, Callable<Map<String, int[]>> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        Iterator<Map.Entry<String, int[]>> iterator;
        Map<String, int[]> map;
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        Map<String, int[]> map2 = this.callable.call();
        Map<String, int[]> map3 = map2;
        if (map3 == null || (map = map2).isEmpty()) {
            return null;
        }
        boolean bl = true;
        Map<String, int[]> map4 = map2;
        Iterator<Map.Entry<String, int[]>> iterator2 = map4.entrySet().iterator();
        while ((iterator = iterator2).hasNext()) {
            Iterator<Map.Entry<String, int[]>> iterator3 = iterator2;
            Map.Entry<String, int[]> entry = iterator3.next();
            Map.Entry<String, int[]> entry2 = entry;
            if (entry2.getValue().length == 0) continue;
            bl = false;
            JsonObjectBuilder jsonObjectBuilder2 = jsonObjectBuilder;
            Map.Entry<String, int[]> entry3 = entry;
            String key = entry3.getKey();
            Map.Entry<String, int[]> entry4 = entry;
            int[] value = entry4.getValue();
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

