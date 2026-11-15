package studio.glace.charts;

import java.util.concurrent.Callable;
import studio.glace.charts.CustomChart;
import studio.glace.json.JsonObjectBuilder;

public class SimplePie
extends CustomChart {
    private final Callable<String> callable;

    public SimplePie(String string, Callable<String> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        String string;
        String string2 = this.callable.call();
        String string3 = string2;
        if (string3 == null || (string = string2).isEmpty()) {
            return null;
        }
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        String string4 = string2;
        return jsonObjectBuilder.appendField("value", string4).build();
    }
}

