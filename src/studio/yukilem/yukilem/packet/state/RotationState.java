package studio.yukilem.yukilem.packet.state;

import lombok.Generated;

public final class RotationState {
    private float yaw;
    private float pitch;
    private float lastYaw;
    private float lastPitch;

    @Generated
    public RotationState() {
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public float getLastYaw() {
        return this.lastYaw;
    }

    @Generated
    public float getLastPitch() {
        return this.lastPitch;
    }

    @Generated
    public void setYaw(float f) {
        this.yaw = f;
    }

    @Generated
    public void setPitch(float f) {
        this.pitch = f;
    }

    @Generated
    public void setLastYaw(float f) {
        this.lastYaw = f;
    }

    @Generated
    public void setLastPitch(float f) {
        this.lastPitch = f;
    }

    @Generated
    public boolean equals(Object object) {
        RotationState rotationState;
        float f;
        RotationState rotationState2;
        float f2;
        RotationState rotationState3;
        float f3;
        RotationState rotationState4;
        float f4;
        if (object == this) {
            return true;
        }
        if (!(object instanceof RotationState)) {
            return false;
        }
        RotationState rotationState5 = (RotationState)object;
        float yaw = this.getYaw();
        if (Float.compare(yaw, f4 = (rotationState4 = rotationState5).getYaw()) != 0) {
            return false;
        }
        float pitch = this.getPitch();
        if (Float.compare(pitch, f3 = (rotationState3 = rotationState5).getPitch()) != 0) {
            return false;
        }
        float lastYaw = this.getLastYaw();
        if (Float.compare(lastYaw, f2 = (rotationState2 = rotationState5).getLastYaw()) != 0) {
            return false;
        }
        float lastPitch = this.getLastPitch();
        return Float.compare(lastPitch, f = (rotationState = rotationState5).getLastPitch()) == 0;
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = Float.floatToIntBits(this.getYaw());
        int n3 = (n + n2) * 59;
        int n4 = Float.floatToIntBits(this.getPitch());
        int n5 = (n3 + n4) * 59;
        int n6 = Float.floatToIntBits(this.getLastYaw());
        int n7 = (n5 + n6) * 59;
        int n8 = Float.floatToIntBits(this.getLastPitch());
        return n7 + n8;
    }

    @Generated
    public String toString() {
        float yaw = this.getYaw();
        float pitch = this.getPitch();
        float lastYaw = this.getLastYaw();
        float lastPitch = this.getLastPitch();
        return "RotationState(yaw=" + yaw + ", pitch=" + pitch + ", lastYaw=" + lastYaw + ", lastPitch=" + lastPitch + ")";
    }
}

