package studio.glace.glantielytratarget.libs.nixlib.version;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerVersion
implements Comparable<ServerVersion> {
    private static final Pattern VERSION_PATTERN = Pattern.compile("(?:^|\\D)(1)\\.(\\d{1,2})(?:\\.(\\d{1,2}))?");
    public static final ServerVersion V1_16_5 = ServerVersion.of(1, 16, 5);
    public static final ServerVersion V1_17 = ServerVersion.of(1, 17, 0);
    public static final ServerVersion V1_18 = ServerVersion.of(1, 18, 0);
    public static final ServerVersion V1_19 = ServerVersion.of(1, 19, 0);
    public static final ServerVersion V1_19_4 = ServerVersion.of(1, 19, 4);
    public static final ServerVersion V1_20 = ServerVersion.of(1, 20, 0);
    public static final ServerVersion V1_20_5 = ServerVersion.of(1, 20, 5);
    public static final ServerVersion V1_20_6 = ServerVersion.of(1, 20, 6);
    public static final ServerVersion V1_21 = ServerVersion.of(1, 21, 0);
    public static final ServerVersion V1_21_4 = ServerVersion.of(1, 21, 4);
    private final int major;
    private final int minor;
    private final int patch;

    private ServerVersion(int n, int n2, int n3) {
        this.major = n;
        this.minor = n2;
        this.patch = n3;
    }

    public static ServerVersion of(int n, int n2, int n3) {
        if (n < 0 || n2 < 0 || n3 < 0) {
            throw new IllegalArgumentException("version components must be non-negative");
        }
        return new ServerVersion(n, n2, n3);
    }

    public static ServerVersion parse(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("empty version string");
        }
        Matcher matcher = VERSION_PATTERN.matcher(string);
        if (!matcher.find()) {
            throw new IllegalArgumentException("unrecognised version: " + string);
        }
        int n = Integer.parseInt(matcher.group(1));
        int n2 = Integer.parseInt(matcher.group(2));
        int n3 = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
        return ServerVersion.of(n, n2, n3);
    }

    public int major() {
        return this.major;
    }

    public int minor() {
        return this.minor;
    }

    public int patch() {
        return this.patch;
    }

    public boolean isAtLeast(ServerVersion serverVersion) {
        return this.compareTo(serverVersion) >= 0;
    }

    public boolean isOlderThan(ServerVersion serverVersion) {
        return this.compareTo(serverVersion) < 0;
    }

    @Override
    public int compareTo(ServerVersion serverVersion) {
        int n = Integer.compare(this.major, serverVersion.major);
        if (n != 0) {
            return n;
        }
        n = Integer.compare(this.minor, serverVersion.minor);
        if (n != 0) {
            return n;
        }
        return Integer.compare(this.patch, serverVersion.patch);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ServerVersion)) {
            return false;
        }
        ServerVersion serverVersion = (ServerVersion)object;
        return this.major == serverVersion.major && this.minor == serverVersion.minor && this.patch == serverVersion.patch;
    }

    public int hashCode() {
        return Objects.hash(this.major, this.minor, this.patch);
    }

    public String toString() {
        return this.patch == 0 ? this.major + "." + this.minor : this.major + "." + this.minor + "." + this.patch;
    }
}

