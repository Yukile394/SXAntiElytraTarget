package studio.yukilem.yukilem.util.combat;

public final class CombatSession {
    private int hitCount;
    private int fireworkCount;
    private long windowStart;

    public void begin(long l) {
        this.hitCount = 0;
        this.fireworkCount = 0;
        this.windowStart = l;
    }

    public void addHit() {
        int n;
        this.hitCount = n = this.hitCount + 1;
    }

    public void addFirework() {
        int n;
        this.fireworkCount = n = this.fireworkCount + 1;
    }

    public int getHitCount() {
        return this.hitCount;
    }

    public int getFireworkCount() {
        return this.fireworkCount;
    }

    public long getWindowStart() {
        return this.windowStart;
    }
}

