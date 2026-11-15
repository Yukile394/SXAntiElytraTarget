package studio.glace.glantielytratarget.config.settings;

import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.config.ConfigView;

public record AlertSettings(boolean enableOnJoin, boolean notifyOnEnable) {
    @NotNull
    public static AlertSettings load(@NotNull ConfigView configView) {
        boolean boolean_ = configView.getBoolean("alerts.enable-on-join", true);
        boolean boolean_2 = configView.getBoolean("alerts.notify-on-enable", true);
        AlertSettings alertSettings = new AlertSettings(boolean_, boolean_2);
        return alertSettings;
    }
}

