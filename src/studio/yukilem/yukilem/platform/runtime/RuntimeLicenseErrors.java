package studio.yukilem.yukilem.platform.runtime;

import org.jetbrains.annotations.NotNull;

final class RuntimeLicenseErrors {
    private RuntimeLicenseErrors() {
    }

    @NotNull
    static String message(@NotNull String string, @NotNull String string2) {
        int n = -1;
        switch (string.hashCode()) {
            case -2053935687: {
                if (!string.equals("INVALID_LICENSE")) break;
                n = 0;
                break;
            }
            case -1648937886: {
                if (!string.equals("IP_NOT_WHITELISTED")) break;
                n = 2;
                break;
            }
            case 179862766: {
                if (!string.equals("INVALID_PARAMS")) break;
                n = 7;
                break;
            }
            case -1462697271: {
                if (!string.equals("LICENSE_INACTIVE")) break;
                n = 1;
                break;
            }
            case 260533430: {
                if (!string.equals("RATE_LIMIT_EXCEEDED")) break;
                n = 4;
                break;
            }
            case -535128833: {
                if (!string.equals("NO_RESPONSE")) break;
                n = 5;
                break;
            }
            case 1166752090: {
                if (!string.equals("PLUGIN_MISMATCH")) break;
                n = 3;
                break;
            }
            case -1816281924: {
                if (!string.equals("TIMESTAMP_EXPIRED")) break;
                n = 6;
            }
        }
        int n2 = n;
        return switch (n2) {
            case 2 -> "IP is not whitelisted (detected: " + string2 + "). Add this IP with /license ip-add on Discord: https://discord.gg/6rtdX89nK4";
            case 5 -> "License server unreachable.";
            case 6 -> "License rejected: request timestamp expired.";
            case 0 -> "License Key Invalid.";
            case 8 -> "License verification failed.";
            case 7 -> "License rejected: invalid verification request.";
            case 1 -> "License Key Inactive.";
            case 4 -> "License rejected: rate limit exceeded.";
            case 3 -> "License Key Invalid: not valid for this plugin.";
            default -> "License rejected: " + string;
        };
    }
}

