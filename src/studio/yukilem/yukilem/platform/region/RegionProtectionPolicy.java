package studio.glace.glantielytratarget.platform.region;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.checks.CheckId;

public interface RegionProtectionPolicy {
    public boolean active();

    public boolean disables(@NotNull Player local1, @NotNull CheckId local2);

    public boolean disablesLocation(@NotNull Player local1, @NotNull Location local2, @NotNull CheckId checkId3);

    public void refreshSettings();

    public void refreshPlayer(@NotNull Player local1, @NotNull Location local2);

    public void clearPlayer(@NotNull UUID local1);

    public void shutdown();
}

