package studio.glace.glantielytratarget.config.settings;

import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.config.ConfigView;

public record DamageReductionSettings(boolean enabled, int violations, double multiplier, int durationTicks) {
    @NotNull
    public static DamageReductionSettings load(@NotNull ConfigView configView) {
        double d;
        boolean boolean_ = configView.getBoolean("detection.damage-reduction.enabled", true);
        int intVal = configView.getInt("detection.damage-reduction.violations", 2);
        int n = Math.max(1, intVal);
        double d2 = 0.7;
        double double_ = configView.getDouble("detection.damage-reduction.multiplier", d2);
        double d3 = double_;
        if (d3 < (d = 0.0)) {
            double_ = 0.0;
        } else {
            double d4 = double_;
            double d5 = 1.0;
            if (d4 > d5) {
                double_ = 1.0;
            }
        }
        int intVal2 = configView.getInt("detection.damage-reduction.duration", 200);
        int n2 = Math.max(1, intVal2);
        boolean bl = boolean_;
        int n3 = n;
        double d6 = double_;
        int n4 = n2;
        DamageReductionSettings damageReductionSettings = new DamageReductionSettings(bl, n3, d6, n4);
        return damageReductionSettings;
    }

    public double damageFactor() {
        double d = 1.0;
        double d2 = this.multiplier;
        return d - d2;
    }

    public long durationMillis() {
        long l = this.durationTicks;
        long l2 = 50L;
        return l * l2;
    }
}

