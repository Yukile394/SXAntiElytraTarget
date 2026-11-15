package studio.glace.glantielytratarget.checks;

public final class GlideAck {
    static final long ACK_MARGIN_MS = 150L;
    static final long PING_CAP_MS = 400L;

    private GlideAck() {
    }

    public static boolean awaiting(long l, long l2, int n) {
        long l3;
        long l4;
        long l5 = 0L;
        if (l == l5) {
            return false;
        }
        long l6 = l2 - l;
        long l7 = Math.max(n, 0);
        long l8 = Math.min(l7, l4 = 400L);
        long l9 = l8;
        long l10 = l9 + (l3 = 150L);
        boolean bl = l6 < l10;
        return bl;
    }
}

