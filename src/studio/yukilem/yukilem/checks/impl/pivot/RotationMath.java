package studio.glace.glantielytratarget.checks.impl.pivot;

public final class RotationMath {
    private RotationMath() {
    }

    public static float normalizeYawDelta(float f) {
        float f2 = 360.0f;
        float f3 = f % f2;
        float f4 = 540.0f;
        float f5 = f3 + f4;
        float f6 = 360.0f;
        float f7 = f5 % f6;
        float f8 = 180.0f;
        return f7 - f8;
    }
}

