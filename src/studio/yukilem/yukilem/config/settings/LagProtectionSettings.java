package studio.yukilem.yukilem.config.settings;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.ConfigView;

public record LagProtectionSettings(boolean tpsEnabled, double minTps, boolean pingEnabled, int maxPing, int minAffectedPlayers) {
    @NotNull
    public static LagProtectionSettings load(@NotNull ConfigView configView) {
        boolean boolean_ = configView.getBoolean("lag-protection.tps.enabled", true);
        double d = 17.0;
        double double_ = configView.getDouble("lag-protection.tps.min-tps", d);
        boolean boolean_2 = configView.getBoolean("lag-protection.ping.enabled", true);
        int intVal = configView.getInt("lag-protection.ping.max-ping", 200);
        int intVal2 = configView.getInt("lag-protection.ping.min-affected-players", 3);
        LagProtectionSettings lagProtectionSettings = new LagProtectionSettings(boolean_, double_, boolean_2, intVal, intVal2);
        return lagProtectionSettings;
    }
}

