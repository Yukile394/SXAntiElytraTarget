package studio.yukilem.yukilem.config.settings;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.ConfigView;

public record OffhandSettings(boolean enabled) {
    @NotNull
    public static OffhandSettings load(@NotNull ConfigView configView) {
        boolean boolean_ = configView.getBoolean("detection.block-offhand-rocket-use.enabled", true);
        OffhandSettings offhandSettings = new OffhandSettings(boolean_);
        return offhandSettings;
    }
}

