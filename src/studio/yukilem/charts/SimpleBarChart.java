package studio.yukilem.charts;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import studio.yukilem.charts.CustomChart;
import studio.yukilem.json.JsonObjectBuilder;

public class SimpleBarChart
extends CustomChart {
    private final Callable<Map<String, Integer>> callable;

    public SimpleBarChart(String string, Callable<Map<String, Integer>> callable) {
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
        Map<String, Integer> map4 = map2;
        Iterator<Map.Entry<String, Integer>> iterator2 = map4.entrySet().iterator();
        while ((iterator = iterator2).hasNext()) {
            int n;
            Iterator<Map.Entry<String, Integer>> iterator3 = iterator2;
            Map.Entry<String, Integer> entry = iterator3.next();
            JsonObjectBuilder jsonObjectBuilder2 = jsonObjectBuilder;
            Map.Entry<String, Integer> entry2 = entry;
            String key = entry2.getKey();
            int[] nArray = new int[1];
            Map.Entry<String, Integer> entry3 = entry;
            nArray[0] = n = entry3.getValue().intValue();
            JsonObjectBuilder jsonObjectBuilder3 = jsonObjectBuilder2.appendField(key, nArray);
        }
        JsonObjectBuilder jsonObjectBuilder4 = new JsonObjectBuilder();
        JsonObjectBuilder jsonObjectBuilder5 = jsonObjectBuilder;
        JsonObjectBuilder.JsonObject jsonObject = jsonObjectBuilder5.build();
        return jsonObjectBuilder4.appendField("values", jsonObject).build();
    }
}

