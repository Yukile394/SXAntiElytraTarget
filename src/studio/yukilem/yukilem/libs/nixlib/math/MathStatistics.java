package studio.yukilem.yukilem.libs.nixlib.math;

import java.util.Arrays;

public final class MathStatistics {
    private MathStatistics() {
    }

    public static double average(double[] dArray, int n) {
        if (n <= 0) {
            return 0.0;
        }
        double d = 0.0;
        for (int n2 = 0; n2 < n; ++n2) {
            d += dArray[n2];
        }
        return d / (double)n;
    }

    public static double populationStdev(double[] dArray, int n, double d) {
        if (n <= 0) {
            return 0.0;
        }
        double d2 = 0.0;
        for (int n2 = 0; n2 < n; ++n2) {
            double d3 = dArray[n2] - d;
            d2 += d3 * d3;
        }
        return Math.sqrt(d2 / (double)n);
    }

    public static double median(double[] dArray, int n) {
        if (n <= 0) {
            return 0.0;
        }
        double[] dArray2 = Arrays.copyOf(dArray, n);
        Arrays.sort(dArray2);
        return MathStatistics.medianOfSortedRange(dArray2, 0, n);
    }

    public static double medianAbsoluteDeviation(double[] dArray, int n) {
        if (n <= 0) {
            return 0.0;
        }
        double[] dArray2 = Arrays.copyOf(dArray, n);
        Arrays.sort(dArray2);
        double d = MathStatistics.medianOfSortedRange(dArray2, 0, n);
        double[] dArray3 = new double[n];
        for (int n2 = 0; n2 < n; ++n2) {
            dArray3[n2] = Math.abs(dArray2[n2] - d);
        }
        Arrays.sort(dArray3);
        return MathStatistics.medianOfSortedRange(dArray3, 0, n);
    }

    public static double tightestWindowMedian(double[] dArray, int n, int n2) {
        if (n <= 0 || n2 <= 0) {
            return 0.0;
        }
        int n3 = Math.min(n2, n);
        double[] dArray2 = Arrays.copyOf(dArray, n);
        Arrays.sort(dArray2);
        int n4 = MathStatistics.findTightestWindowStart(dArray2, n, n3);
        return MathStatistics.medianOfSortedRange(dArray2, n4, n4 + n3);
    }

    public static double tightestWindowMad(double[] dArray, int n, int n2) {
        if (n <= 0 || n2 <= 0) {
            return 0.0;
        }
        int n3 = Math.min(n2, n);
        double[] dArray2 = Arrays.copyOf(dArray, n);
        Arrays.sort(dArray2);
        int n4 = MathStatistics.findTightestWindowStart(dArray2, n, n3);
        double d = MathStatistics.medianOfSortedRange(dArray2, n4, n4 + n3);
        double[] dArray3 = new double[n3];
        for (int n5 = 0; n5 < n3; ++n5) {
            dArray3[n5] = Math.abs(dArray2[n4 + n5] - d);
        }
        Arrays.sort(dArray3);
        return MathStatistics.medianOfSortedRange(dArray3, 0, n3);
    }

    public static int outlierCount(double[] dArray, int n, double d) {
        if (n < 2) {
            return 0;
        }
        double[] dArray2 = Arrays.copyOf(dArray, n);
        Arrays.sort(dArray2);
        int n2 = n / 2;
        double d2 = MathStatistics.medianOfSortedRange(dArray2, 0, n2);
        double d3 = MathStatistics.medianOfSortedRange(dArray2, n2, n);
        double d4 = Math.abs(d2 - d3);
        double d5 = d2 - d * d4;
        double d6 = d3 + d * d4;
        int n3 = 0;
        for (int n4 = 0; n4 < n; ++n4) {
            double d7 = dArray2[n4];
            if (!(d7 < d5) && !(d7 > d6)) continue;
            ++n3;
        }
        return n3;
    }

    private static int findTightestWindowStart(double[] dArray, int n, int n2) {
        int n3 = 0;
        double d = Double.POSITIVE_INFINITY;
        int n4 = n - n2;
        for (int n5 = 0; n5 <= n4; ++n5) {
            double d2 = dArray[n5 + n2 - 1] - dArray[n5];
            if (!(d2 < d)) continue;
            d = d2;
            n3 = n5;
        }
        return n3;
    }

    private static double medianOfSortedRange(double[] dArray, int n, int n2) {
        int n3 = n2 - n;
        if (n3 <= 0) {
            return 0.0;
        }
        if ((n3 & 1) == 0) {
            return (dArray[n + n3 / 2] + dArray[n + n3 / 2 - 1]) * 0.5;
        }
        return dArray[n + n3 / 2];
    }
}

