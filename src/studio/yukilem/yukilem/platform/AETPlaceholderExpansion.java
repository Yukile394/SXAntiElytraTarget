package studio.glace.glantielytratarget.platform;

import com.github.retrooper.packetevents.protocol.player.User;
import java.util.List;
import java.util.UUID;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.bootstrap.PluginContext;
import studio.glace.glantielytratarget.checks.PlayerCheckData;
import studio.glace.glantielytratarget.data.FlagRepository;
import studio.glace.glantielytratarget.platform.ClientVersionResolver;

public final class AETPlaceholderExpansion
extends PlaceholderExpansion {
    private final PluginContext ctx;
    private final String author;
    private final String version;

    public AETPlaceholderExpansion(@NotNull PluginContext pluginContext) {
        String string;
        String string2;
        this.ctx = pluginContext;
        List<String> authors = pluginContext.plugin().getDescription().getAuthors();
        this.author = string2 = String.join((CharSequence)", ", authors);
        this.version = string = pluginContext.plugin().getDescription().getVersion();
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "aet";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return this.author;
    }

    @Override
    @NotNull
    public String getVersion() {
        return this.version;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String string) {
        User user;
        if (player == null) {
            return "";
        }
        if (string.equalsIgnoreCase("bps")) {
            double d;
            double d2;
            PlayerCheckData playerCheckData = PlayerCheckData.getIfPresent(player.getUniqueId());
            PlayerCheckData playerCheckData2 = playerCheckData;
            if (playerCheckData2 == null) {
                d2 = 0.0;
            } else {
                PlayerCheckData playerCheckData3 = playerCheckData;
                d2 = playerCheckData3.movementUncertaintyState().getSmoothedBps();
            }
            double d3 = d = d2;
            return AETPlaceholderExpansion.formatOneDecimal(d3);
        }
        if (string.equalsIgnoreCase("client_version")) {
            user = this.ctx.sessionCache().userOf(player);
            User user2 = user;
            if (user2 == null) {
                return "Unknown";
            }
        } else {
            if (string.equalsIgnoreCase("total_violations") || string.equalsIgnoreCase("total_flags")) {
                if (this.ctx.flagRepository() != null) {
                    FlagRepository flagRepository = this.ctx.flagRepository();
                    UUID uniqueId = player.getUniqueId();
                    return String.valueOf(flagRepository.getTotalViolations(uniqueId));
                }
                return "0";
            }
            return null;
        }
        User user3 = user;
        UUID uniqueId = player.getUniqueId();
        return ClientVersionResolver.resolve(user3, uniqueId).getReleaseName();
    }

    @NotNull
    private static String formatOneDecimal(double d) {
        long l;
        double d2 = 10.0;
        long l2 = l = Math.round(d * d2);
        long l3 = 10L;
        long l4 = l2 / l3;
        long l5 = l;
        long l6 = 10L;
        long l7 = Math.abs(l5 % l6);
        return l4 + "." + l7;
    }
}

