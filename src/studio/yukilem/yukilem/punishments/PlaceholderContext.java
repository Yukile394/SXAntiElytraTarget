package studio.glace.glantielytratarget.punishments;

import org.jetbrains.annotations.NotNull;

public final class PlaceholderContext {
    private final String player;
    private final String checkName;
    private final int vl;
    private final int ms;
    private final String tps;

    public PlaceholderContext(@NotNull String string, @NotNull String string2, int n, int n2, @NotNull String string3) {
        this.player = string;
        this.checkName = string2;
        this.vl = n;
        this.ms = n2;
        this.tps = string3;
    }

    @NotNull
    public String render(@NotNull String string) {
        String string2 = this.player;
        String string3 = string.replace("%player%", string2);
        String string4 = this.checkName;
        String string5 = string3.replace("%check_name%", string4);
        String string6 = Integer.toString(this.vl);
        String string7 = string5.replace("%vl%", string6);
        String string8 = Integer.toString(this.ms);
        String string9 = string7.replace("%ms%", string8);
        String string10 = this.tps;
        return string9.replace("%tps%", string10);
    }

    @NotNull
    public String player() {
        return this.player;
    }

    @NotNull
    public String checkName() {
        return this.checkName;
    }

    public int vl() {
        return this.vl;
    }

    public int ms() {
        return this.ms;
    }

    @NotNull
    public String tps() {
        return this.tps;
    }
}

