package studio.yukilem.yukilem.punishments;

import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.punishments.PunishmentAction;

public final class PunishmentCommand {
    private final int threshold;
    private final int interval;
    private final PunishmentAction action;
    private final String payload;
    private final String raw;

    public PunishmentCommand(int n, int n2, @NotNull PunishmentAction punishmentAction, @NotNull String string, @NotNull String string2) {
        int n3;
        this.threshold = n;
        this.interval = n3 = Math.max(0, n2);
        this.action = punishmentAction;
        this.payload = string;
        this.raw = string2;
    }

    public int threshold() {
        return this.threshold;
    }

    public int interval() {
        return this.interval;
    }

    public boolean isInterval() {
        boolean bl = this.interval > 0;
        return bl;
    }

    @NotNull
    public PunishmentAction action() {
        return this.action;
    }

    @NotNull
    public String payload() {
        return this.payload;
    }

    @NotNull
    public String raw() {
        return this.raw;
    }

    public boolean matchesViolation(int n) {
        int n2 = this.threshold;
        if (n < n2) {
            return false;
        }
        if (!this.isInterval()) {
            int n3 = this.threshold;
            if (n != n3) return false;
            return true;
        }
        int n4 = this.threshold;
        int n5 = n - n4;
        int n6 = this.interval;
        if (n5 % n6 != 0) return false;
        return true;
    }
}

