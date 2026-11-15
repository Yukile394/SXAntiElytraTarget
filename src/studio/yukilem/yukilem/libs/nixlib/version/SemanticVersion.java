package studio.yukilem.yukilem.libs.nixlib.version;

import java.util.Objects;

public final class SemanticVersion
implements Comparable<SemanticVersion> {
    public static final SemanticVersion ZERO = new SemanticVersion(0, 0, 0);
    private final int major;
    private final int minor;
    private final int patch;

    private SemanticVersion(int n, int n2, int n3) {
        this.major = n;
        this.minor = n2;
        this.patch = n3;
    }

    public static SemanticVersion of(int n, int n2, int n3) {
        if (n < 0 || n2 < 0 || n3 < 0) {
            throw new IllegalArgumentException("semver components must be non-negative");
        }
        return new SemanticVersion(n, n2, n3);
    }

    public static SemanticVersion parse(String string) {
        if (string == null) {
            throw new IllegalArgumentException("version string is null");
        }
        String string2 = string.trim();
        if (string2.isEmpty()) {
            throw new IllegalArgumentException("version string is empty");
        }
        if (string2.charAt(0) == 'v' || string2.charAt(0) == 'V') {
            string2 = string2.substring(1);
        }
        int n = string2.indexOf(45);
        int n2 = string2.indexOf(43);
        int n3 = -1;
        if (n >= 0) {
            n3 = n;
        }
        if (n2 >= 0 && (n3 < 0 || n2 < n3)) {
            n3 = n2;
        }
        if (n3 >= 0) {
            string2 = string2.substring(0, n3);
        }
        String[] stringArray = string2.split("\\.");
        int n4 = SemanticVersion.digits(stringArray, 0);
        int n5 = SemanticVersion.digits(stringArray, 1);
        int n6 = SemanticVersion.digits(stringArray, 2);
        return new SemanticVersion(n4, n5, n6);
    }

    public static SemanticVersion parseOr(String string, SemanticVersion semanticVersion) {
        try {
            return SemanticVersion.parse(string);
        }
        catch (RuntimeException e) {
            return semanticVersion;
        }
    }

    private static int digits(String[] stringArray, int n) {
        char c;
        if (n >= stringArray.length) {
            return 0;
        }
        String string = stringArray[n];
        StringBuilder sb = new StringBuilder(string.length());
        for (int n2 = 0; n2 < string.length() && (c = string.charAt(n2)) >= '0' && c <= '9'; ++n2) {
            sb.append(c);
        }
        if (sb.length() == 0) {
            return 0;
        }
        try {
            return Integer.parseInt(sb.toString());
        }
        catch (NumberFormatException e) {
            return 0;
        }
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

    public boolean isOlderThan(SemanticVersion semanticVersion) {
        return this.compareTo(semanticVersion) < 0;
    }

    public boolean isNewerThan(SemanticVersion semanticVersion) {
        return this.compareTo(semanticVersion) > 0;
    }

    @Override
    public int compareTo(SemanticVersion semanticVersion) {
        int n = Integer.compare(this.major, semanticVersion.major);
        if (n != 0) {
            return n;
        }
        n = Integer.compare(this.minor, semanticVersion.minor);
        if (n != 0) {
            return n;
        }
        return Integer.compare(this.patch, semanticVersion.patch);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SemanticVersion)) {
            return false;
        }
        SemanticVersion semanticVersion = (SemanticVersion)object;
        return this.major == semanticVersion.major && this.minor == semanticVersion.minor && this.patch == semanticVersion.patch;
    }

    public int hashCode() {
        return Objects.hash(this.major, this.minor, this.patch);
    }

    public String toString() {
        return this.major + "." + this.minor + "." + this.patch;
    }
}

