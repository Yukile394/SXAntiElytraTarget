package studio.yukilem.yukilem.data;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class ViolationRecord {
    private final long id;
    private final UUID uuid;
    private final String name;
    private final String checkName;
    private final int seq;
    private final long createdAt;
    private final double bps;
    private final String clientVersion;
    private final int ping;
    private final String tps;
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final String gamemode;
    private final boolean gliding;
    private final boolean onGround;
    private final boolean sprinting;

    public ViolationRecord(long l, @NotNull UUID uUID, @NotNull String string, @NotNull String string2, int n, long l2, double d, @NotNull String string3, int n2, @NotNull String string4, @NotNull String string5, double d2, double d3, double d4, float f, float f2, @NotNull String string6, boolean bl, boolean bl2, boolean bl3) {
        this.id = l;
        this.uuid = uUID;
        this.name = string;
        this.checkName = string2;
        this.seq = n;
        this.createdAt = l2;
        this.bps = d;
        this.clientVersion = string3;
        this.ping = n2;
        this.tps = string4;
        this.world = string5;
        this.x = d2;
        this.y = d3;
        this.z = d4;
        this.yaw = f;
        this.pitch = f2;
        this.gamemode = string6;
        this.gliding = bl;
        this.onGround = bl2;
        this.sprinting = bl3;
    }

    public long id() {
        return this.id;
    }

    @NotNull
    public UUID uuid() {
        return this.uuid;
    }

    @NotNull
    public String name() {
        return this.name;
    }

    @NotNull
    public String checkName() {
        return this.checkName;
    }

    public int seq() {
        return this.seq;
    }

    public long createdAt() {
        return this.createdAt;
    }

    public double bps() {
        return this.bps;
    }

    @NotNull
    public String clientVersion() {
        return this.clientVersion;
    }

    public int ping() {
        return this.ping;
    }

    @NotNull
    public String tps() {
        return this.tps;
    }

    @NotNull
    public String world() {
        return this.world;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }

    public float yaw() {
        return this.yaw;
    }

    public float pitch() {
        return this.pitch;
    }

    @NotNull
    public String gamemode() {
        return this.gamemode;
    }

    public boolean gliding() {
        return this.gliding;
    }

    public boolean onGround() {
        return this.onGround;
    }

    public boolean sprinting() {
        return this.sprinting;
    }
}

