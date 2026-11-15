package studio.glace.charts;

import java.util.concurrent.Callable;
import studio.glace.charts.CustomChart;
import studio.glace.json.JsonObjectBuilder;

public class SingleLineChart
extends CustomChart {
    private final Callable<Integer> callable;

    public SingleLineChart(String string, Callable<Integer> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        int n = this.callable.call();
        int n2 = n;
        if (n2 == 0) {
            return null;
        }
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        int n3 = n;
        return jsonObjectBuilder.appendField("value", n3).build();
    }
}

