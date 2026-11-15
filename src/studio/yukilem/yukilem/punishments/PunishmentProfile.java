package studio.glace.glantielytratarget.punishments;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import studio.glace.glantielytratarget.punishments.CheckMatcher;
import studio.glace.glantielytratarget.punishments.PunishmentCommand;

public final class PunishmentProfile {
    private final String name;
    private final CheckMatcher matcher;
    private final List<PunishmentCommand> commands;
    private final int maxThreshold;

    public PunishmentProfile(@NotNull String string, @NotNull CheckMatcher checkMatcher, @NotNull List<PunishmentCommand> list) {
        int n;
        Iterator<PunishmentCommand> iterator;
        this.name = string;
        this.matcher = checkMatcher;
        List<PunishmentCommand> list2 = Collections.unmodifiableList(list);
        this.commands = list2;
        int n2 = 0;
        Iterator<PunishmentCommand> iterator2 = list.iterator();
        while ((iterator = iterator2).hasNext()) {
            int n3;
            Iterator<PunishmentCommand> iterator3 = iterator2;
            PunishmentCommand punishmentCommand = iterator3.next();
            PunishmentCommand punishmentCommand2 = punishmentCommand;
            int n4 = punishmentCommand2.threshold();
            if (n4 <= (n3 = n2)) continue;
            PunishmentCommand punishmentCommand3 = punishmentCommand;
            n2 = punishmentCommand3.threshold();
        }
        this.maxThreshold = n = n2;
    }

    @NotNull
    public String name() {
        return this.name;
    }

    @NotNull
    public CheckMatcher matcher() {
        return this.matcher;
    }

    @NotNull
    public List<PunishmentCommand> commands() {
        return this.commands;
    }

    public int maxThreshold() {
        return this.maxThreshold;
    }
}

