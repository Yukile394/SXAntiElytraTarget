package studio.glace.charts;

import java.util.function.BiConsumer;
import studio.glace.json.JsonObjectBuilder;

public abstract class CustomChart {
    private final String chartId;

    protected CustomChart(String string) {
        if (string == null) {
            throw new IllegalArgumentException("chartId must not be null");
        }
        this.chartId = string;
    }

    public JsonObjectBuilder.JsonObject getRequestJsonObject(BiConsumer<String, Throwable> biConsumer, boolean bl) {
        JsonObjectBuilder.JsonObject jsonObject;
        JsonObjectBuilder jsonObjectBuilder;
        block4: {
            JsonObjectBuilder jsonObjectBuilder2 = jsonObjectBuilder = new JsonObjectBuilder();
            String string = this.chartId;
            JsonObjectBuilder jsonObjectBuilder3 = jsonObjectBuilder2.appendField("chartId", string);
            try {
                JsonObjectBuilder.JsonObject jsonObject2 = jsonObject = this.getChartData();
                if (jsonObject2 != null) break block4;
                return null;
            }
            catch (Throwable e) {
                Throwable e2 = e;
                if (bl) {
                    StringBuilder sb = new StringBuilder().append("Failed to get data for custom chart with id ");
                    String string2 = this.chartId;
                    String string3 = sb.append(string2).toString();
                    Throwable e3 = e2;
                    biConsumer.accept(string3, e3);
                }
                return null;
            }
        }
        JsonObjectBuilder jsonObjectBuilder4 = jsonObjectBuilder;
        JsonObjectBuilder.JsonObject jsonObject3 = jsonObject;
        JsonObjectBuilder jsonObjectBuilder5 = jsonObjectBuilder4.appendField("data", jsonObject3);
        JsonObjectBuilder jsonObjectBuilder6 = jsonObjectBuilder;
        return jsonObjectBuilder6.build();
    }

    protected abstract JsonObjectBuilder.JsonObject getChartData() throws Exception;
}

