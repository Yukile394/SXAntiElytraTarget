package studio.glace.glantielytratarget.platform.runtime;

import org.jetbrains.annotations.Nullable;
import studio.glace.glantielytratarget.platform.runtime.RuntimeAuthority;

public final class RuntimeProfile {
    private RuntimeProfile() {
    }

    public static int foldInt(@Nullable RuntimeAuthority runtimeAuthority, int n, int n2, int n3) {
        int n4;
        byte[] bArray = RuntimeProfile.sample(runtimeAuthority, n, 4);
        byte[] bArray2 = bArray;
        if (bArray2 == null) {
            return n3;
        }
        byte[] bArray3 = bArray;
        int n5 = bArray3[0] & 0xFF;
        byte[] bArray4 = bArray;
        int n6 = bArray4[1] & 0xFF;
        int n7 = n5 ^ n6;
        byte[] bArray5 = bArray;
        int n8 = bArray5[2] & 0xFF;
        int n9 = n7 ^ n8;
        byte[] bArray6 = bArray;
        int n10 = bArray6[3] & 0xFF;
        int n11 = n9 ^ n10;
        int n12 = n11;
        int n13 = n * -1640531535 & 0xFF;
        if ((n12 ^ n13) == 0) {
            return n3;
        }
        int n14 = n11;
        int n15 = n4 = n14 & 1;
        int n16 = n2 + n15;
        int n17 = n4;
        return n16 - n17;
    }

    public static long foldLong(@Nullable RuntimeAuthority runtimeAuthority, int n, long l, long l2) {
        byte[] bArray = RuntimeProfile.sample(runtimeAuthority, n, 4);
        byte[] bArray2 = bArray;
        if (bArray2 == null) {
            return l2;
        }
        byte[] bArray3 = bArray;
        long l3 = bArray3[0];
        long l4 = 255L;
        long l5 = l3 & l4;
        byte[] bArray4 = bArray;
        long l6 = bArray4[1];
        long l7 = 255L;
        long l8 = l6 & l7;
        long l9 = l5 ^ l8;
        byte[] bArray5 = bArray;
        long l10 = bArray5[2];
        long l11 = 255L;
        long l12 = l10 & l11;
        long l13 = l9 ^ l12;
        byte[] bArray6 = bArray;
        long l14 = bArray6[3];
        long l15 = 255L;
        long l16 = l14 & l15;
        long l17 = l13 ^ l16;
        long l18 = n;
        long l19 = 2654435761L;
        long l20 = l18 * l19;
        long l21 = 255L;
        long l22 = l20 & l21;
        long l23 = l17 ^ l22;
        long l24 = 0L;
        if (l23 == l24) {
            return l2;
        }
        return l;
    }

    public static double foldFactor(@Nullable RuntimeAuthority runtimeAuthority, int n, double d, double d2, double d3) {
        byte[] bArray = RuntimeProfile.sample(runtimeAuthority, n, 2);
        byte[] bArray2 = bArray;
        if (bArray2 == null) {
            return d3;
        }
        byte[] bArray3 = bArray;
        int n2 = bArray3[0] & 0xFF;
        byte[] bArray4 = bArray;
        int n3 = bArray4[1] & 0xFF;
        int n4 = n2 ^ n3;
        int n5 = n4;
        if (n5 == 0) {
            return d3;
        }
        int n6 = n4;
        double d4 = n6;
        double d5 = 255.0;
        double d6 = d4 / d5;
        double d7 = d2 - d;
        double d8 = d6 * d7;
        return d + d8;
    }

    private static byte[] sample(@Nullable RuntimeAuthority runtimeAuthority, int n, int n2) {
        if (runtimeAuthority == null) {
            return null;
        }
        return runtimeAuthority._sx(n, n2);
    }
}

