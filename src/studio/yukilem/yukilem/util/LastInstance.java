package studio.glace.glantielytratarget.util;

public final class LastInstance {
    private int ticksAgo = 100;

    public boolean hasOccurredSince(int n) {
        boolean bl = this.ticksAgo <= n;
        return bl;
    }

    public void reset() {
        this.ticksAgo = 0;
    }

    public void tick() {
        int n;
        if (this.ticksAgo == Integer.MAX_VALUE) {
            this.ticksAgo = 100;
        }
        this.ticksAgo = n = this.ticksAgo + 1;
    }
}

