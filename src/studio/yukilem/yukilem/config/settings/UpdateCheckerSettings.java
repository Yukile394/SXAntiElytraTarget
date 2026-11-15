package studio.yukilem.yukilem.config.settings;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.ConfigView;

public record UpdateCheckerSettings(boolean enabled, boolean checkOnEnable, boolean notifyOnJoin) {
    @NotNull
    public static UpdateCheckerSettings load(@NotNull ConfigView configView) {
        boolean boolean_ = configView.getBoolean("update-checker.enabled", true);
        boolean boolean_2 = configView.getBoolean("update-checker.check-on-enable", true);
        boolean boolean_3 = configView.getBoolean("update-checker.notify-on-join", true);
        UpdateCheckerSettings updateCheckerSettings = new UpdateCheckerSettings(boolean_, boolean_2, boolean_3);
        return updateCheckerSettings;
    }
}

