package studio.glace.glantielytratarget.libs.nixlib.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Optional;

public final class NumberUtils {
    private static final DecimalFormatSymbols US_SYMBOLS = DecimalFormatSymbols.getInstance(Locale.US);
    private static final DecimalFormat COMPACT_2DP = new DecimalFormat("#,##0.##", US_SYMBOLS);
    private static final String[] SUFFIXES = new String[]{"", "K", "M", "B", "T", "Q"};

    private NumberUtils() {
    }

    public static Optional<Integer> tryInt(String string) {
        if (string == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(string.trim()));
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> tryLong(String string) {
        if (string == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Long.parseLong(string.trim()));
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> tryDouble(String string) {
        if (string == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Double.parseDouble(string.trim()));
        }
        catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static int clamp(int n, int n2, int n3) {
        return Math.max(n2, Math.min(n, n3));
    }

    public static double clamp(double d, double d2, double d3) {
        return Math.max(d2, Math.min(d, d3));
    }

    public static String formatWithCommas(long l) {
        return COMPACT_2DP.format(l);
    }

    public static String formatCompact(long l) {
        double d;
        if (l < 1000L) {
            return Long.toString(l);
        }
        int n = 0;
        for (d = (double)l; d >= 1000.0 && n < SUFFIXES.length - 1; d /= 1000.0, ++n) {
        }
        return COMPACT_2DP.format(d) + SUFFIXES[n];
    }

    public static String formatPercent(double d) {
        return COMPACT_2DP.format(d * 100.0) + "%";
    }
}

