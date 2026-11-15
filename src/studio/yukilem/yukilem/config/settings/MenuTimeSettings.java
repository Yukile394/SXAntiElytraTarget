package studio.yukilem.yukilem.config.settings;

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import studio.yukilem.yukilem.config.ConfigView;

public record MenuTimeSettings(@NotNull String datePattern, @NotNull String secondSuffix, @NotNull String minuteSuffix, @NotNull String hourSuffix, @NotNull String daySuffix, int timerFormat) {
    private static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";

    @NotNull
    public static MenuTimeSettings load(@NotNull ConfigView configView) {
        String string = configView.getString("menu-time.date-format", DEFAULT_DATE_PATTERN);
        String string2 = configView.getString("menu-time.time.second", "s");
        String string3 = configView.getString("menu-time.time.minute", "m");
        String string4 = configView.getString("menu-time.time.hour", "h");
        String string5 = configView.getString("menu-time.time.day", "d");
        int n = MenuTimeSettings.clampTimerFormat(configView.getInt("menu-time.timer-format", 1));
        MenuTimeSettings menuTimeSettings = new MenuTimeSettings(string, string2, string3, string4, string5, n);
        return menuTimeSettings;
    }

    @NotNull
    public SimpleDateFormat newDateFormat() {
        try {
            String string = this.datePattern;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(string);
            return simpleDateFormat;
        }
        catch (IllegalArgumentException e) {
            IllegalArgumentException e2 = e;
            return new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        }
    }

    @NotNull
    public String formatAgo(long l) {
        long l2;
        long l3 = l;
        long l4 = 0L;
        if (l3 < l4) {
            l = 0L;
        }
        long l5 = l;
        long l6 = 1000L;
        long l7 = l2 = l5 / l6;
        long l8 = 86400L;
        long l9 = l7 / l8;
        long l10 = l2;
        long l11 = 86400L;
        long l12 = l10 % l11;
        long l13 = 3600L;
        long l14 = l12 / l13;
        long l15 = l2;
        long l16 = 3600L;
        long l17 = l15 % l16;
        long l18 = 60L;
        long l19 = l17 / l18;
        long l20 = l2;
        long l21 = 60L;
        long l22 = l20 % l21;
        return switch (this.timerFormat) {
            case 1 -> {
                long l61_23 = l14;
                long l79_24 = l19;
                long l83_25 = l22;
                yield this.formatClock(l61_23, l79_24, l83_25);
            }
            case 3 -> {
                long l59_27 = l9;
                long l77_28 = l14;
                long l81_29 = l19;
                long l85_30 = l22;
                yield this.formatText(l59_27, l77_28, l81_29, l85_30);
            }
            default -> {
                long l6_31 = l9;
                long l10_32 = l14;
                long l12_33 = l19;
                long l14_34 = l22;
                yield this.formatShort(l6_31, l10_32, l12_33, l14_34);
            }
        };
    }

    @NotNull
    private String formatClock(long l, long l2, long l3) {
        Locale locale = Locale.US;
        Object[] objectArray = new Object[3];
        Long l4 = l;
        objectArray[0] = l4;
        Long l5 = l2;
        objectArray[1] = l5;
        Long l6 = l3;
        objectArray[2] = l6;
        return String.format(locale, "%02d:%02d:%02d", objectArray);
    }

    @NotNull
    private String formatShort(long l, long l2, long l3, long l4) {
        long l5 = 0L;
        if (l > l5) {
            String string = this.daySuffix;
            return l + string;
        }
        long l6 = 0L;
        if (l2 > l6) {
            String string = this.hourSuffix;
            return l2 + string;
        }
        long l7 = 0L;
        if (l3 > l7) {
            String string = this.minuteSuffix;
            return l3 + string;
        }
        String string = this.secondSuffix;
        return l4 + string;
    }

    @NotNull
    private String formatText(long l, long l2, long l3, long l4) {
        long l5 = 0L;
        if (l > l5) {
            String string = this.daySuffix;
            String string2 = this.hourSuffix;
            return l + string + " " + l2 + string2;
        }
        long l6 = 0L;
        if (l2 > l6) {
            Locale locale = Locale.US;
            Object[] objectArray = new Object[6];
            Long l7 = l2;
            objectArray[0] = l7;
            String string = this.hourSuffix;
            objectArray[1] = string;
            Long l8 = l3;
            objectArray[2] = l8;
            String string3 = this.minuteSuffix;
            objectArray[3] = string3;
            Long l9 = l4;
            objectArray[4] = l9;
            String string4 = this.secondSuffix;
            objectArray[5] = string4;
            return String.format(locale, "%02d%s %02d%s %02d%s", objectArray);
        }
        long l10 = 0L;
        if (l3 > l10) {
            Locale locale = Locale.US;
            Object[] objectArray = new Object[4];
            Long l11 = l3;
            objectArray[0] = l11;
            String string = this.minuteSuffix;
            objectArray[1] = string;
            Long l12 = l4;
            objectArray[2] = l12;
            String string5 = this.secondSuffix;
            objectArray[3] = string5;
            return String.format(locale, "%02d%s %02d%s", objectArray);
        }
        String string = this.secondSuffix;
        return l4 + string;
    }

    private static int clampTimerFormat(int n) {
        if (n < 1 || n > 3) {
            return 1;
        }
        return n;
    }
}

