package studio.glace.glantielytratarget.bootstrap;

import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

public final class GraceConsoleBanner {
    private static final int CONSOLE_WIDTH = 64;
    private static final String ANSI_RESET = "\u001b[0m";
    private static final String ANSI_RULE = GraceConsoleBanner.ansiFg(102, 0, 0);
    private static final String ANSI_TITLE = "\u001b[1m" + GraceConsoleBanner.ansiFg(255, 0, 0);
    private static final String ANSI_BODY = GraceConsoleBanner.ansiFg(255, 119, 119);
    private static final String RULE_LINE;

    private GraceConsoleBanner() {
    }

    public static void graceRequired(@NotNull Logger logger) {
        String[] stringArray = new String[]{"Set license-key in plugins/glAntiElytraTarget/config.yml", "You have 1 minute to enter your license key.", "After 1 minute the plugin verifies your key automatically.", "Or save config and run /aet reload to verify without waiting."};
        GraceConsoleBanner.print(logger, "LICENSE KEY REQUIRED", stringArray);
    }

    public static void graceExpired(@NotNull Logger logger) {
        String[] stringArray = new String[]{"No license key was entered within 1 minute.", "Set license-key in config.yml and restart the server."};
        GraceConsoleBanner.print(logger, "LICENSE GRACE PERIOD EXPIRED", stringArray);
    }

    private static void print(@NotNull Logger logger, @NotNull String string, @NotNull String[] stringArray) {
        int n;
        int n2;
        String string2 = RULE_LINE;
        logger.info(string2);
        String string3 = ANSI_TITLE;
        String string4 = GraceConsoleBanner.center(string);
        String string5 = string3 + string4 + ANSI_RESET;
        logger.info(string5);
        int length = stringArray.length;
        int n3 = 0;
        while ((n2 = n3) < (n = length)) {
            int n4 = n3++;
            String string6 = stringArray[n4];
            String string7 = ANSI_BODY;
            String string8 = string6;
            String string9 = string7 + "  " + string8 + ANSI_RESET;
            logger.info(string9);
        }
        String string10 = RULE_LINE;
        logger.info(string10);
    }

    private static String center(@NotNull String string) {
        if (string.length() >= 64) {
            return string;
        }
        int length = string.length();
        int n = (64 - length) / 2;
        return " ".repeat(n) + string;
    }

    private static String ansiFg(int n, int n2, int n3) {
        return "\u001b[38;2;" + n + ";" + n2 + ";" + n3 + "m";
    }

    static {
        String string = ANSI_RULE;
        String string2 = "=".repeat(64);
        RULE_LINE = string + string2 + ANSI_RESET;
    }
}

