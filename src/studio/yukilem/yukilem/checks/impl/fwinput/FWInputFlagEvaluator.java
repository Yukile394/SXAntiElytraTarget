package studio.glace.glantielytratarget.checks.impl.fwinput;

import com.github.retrooper.packetevents.protocol.player.User;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.checks.impl.LegitSwapLogic;
import studio.glace.glantielytratarget.checks.impl.fwinput.FWInputSession;
import studio.glace.glantielytratarget.checks.impl.fwinput.FWInputSessionLifecycle;

public final class FWInputFlagEvaluator {
    private final BiPredicate<Player, User> tickReliable;
    private final Consumer<Player> flag;

    public FWInputFlagEvaluator(@NotNull BiPredicate<Player, User> biPredicate, @NotNull Consumer<Player> consumer) {
        this.tickReliable = biPredicate;
        this.flag = consumer;
    }

    public void trySwapFlag(@NotNull Player player, @NotNull User user, @NotNull FWInputSession fWInputSession) {
        if (!fWInputSession.valid) {
            return;
        }
        int n = fWInputSession.heldSlot;
        if (!LegitSwapLogic.shouldFlag(fWInputSession.track(n))) {
            return;
        }
        if (!this.tickReliable.test(player, user)) {
            FWInputSessionLifecycle.clearSwap(fWInputSession);
            return;
        }
        FWInputSessionLifecycle.clearSwap(fWInputSession);
        this.flag.accept(player);
    }
}

