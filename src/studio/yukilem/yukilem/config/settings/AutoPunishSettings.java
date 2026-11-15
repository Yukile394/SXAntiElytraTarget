package studio.glace.glantielytratarget.config.settings;

import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.config.ConfigView;

public record AutoPunishSettings(boolean enabled, boolean ban, int violations, @NotNull String kickCommand, @NotNull String banCommand, boolean broadcastEnabled, @NotNull List<String> broadcastLines) {
    @NotNull
    public static AutoPunishSettings load(@NotNull ConfigView configView) {
        boolean enabled = configView.getBoolean("auto-punish.enabled", true);
        String type = configView.getString("auto-punish.type", "KICK").trim().toUpperCase(Locale.ROOT);
        int violations = Math.max(1, configView.getInt("auto-punish.violations", 5));
        String kick = configView.getString("auto-punish.kick-command", "kick %player% glAntiElytraTarget %check_name% (vl %vl%, %ms%ms)");
        String ban = configView.getString("auto-punish.ban-command", "ban %player% glAntiElytraTarget %check_name% (vl %vl%, %ms%ms)");
        boolean broadcast = configView.getBoolean("kick-broadcast.enabled", true);
        List<String> lines = configView.getStringList("kick-broadcast.lines");
        return new AutoPunishSettings(enabled, type.equals("BAN"), violations, kick, ban, broadcast, lines);
    }

    @NotNull
    public String command() {
        return this.ban ? this.banCommand : this.kickCommand;
    }
}
