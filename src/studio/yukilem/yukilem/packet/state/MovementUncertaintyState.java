package studio.glace.glantielytratarget.packet.state;

import lombok.Generated;
import studio.glace.glantielytratarget.util.LastInstance;

public final class MovementUncertaintyState {
    private static final double BPS_ALPHA_RISE = 0.25;
    private static final double BPS_ALPHA_FALL = 0.6;
    private static final double BPS_DEADZONE = 0.75;
    private static final long BPS_STALE_NANOS = 1000000000L;
    private final LastInstance lastPointThree;
    private final LastInstance lastVehicleSwitch;
    private int clientFlyingSeq;
    private boolean packetOnGround;
    private double lastX;
    private double lastY;
    private double lastZ;
    private boolean wasInVehicle;
    private volatile double smoothedBps;
    private volatile long lastSpeedSampleNanos;

    public void incrementClientFlyingSeq() {
        int n;
        this.clientFlyingSeq = n = this.clientFlyingSeq + 1;
    }

    public void invalidatePacketContinuity() {
        long l;
        double d;
        double d2;
        double d3;
        double d4;
        this.lastPointThree.reset();
        this.lastVehicleSwitch.reset();
        this.lastX = d4 = Double.NaN;
        this.lastY = d3 = Double.NaN;
        this.lastZ = d2 = Double.NaN;
        this.wasInVehicle = false;
        this.smoothedBps = d = 0.0;
        this.lastSpeedSampleNanos = l = 0L;
    }

    public void recordHorizontalDelta(double d) {
        double d2;
        long l;
        this.lastSpeedSampleNanos = l = System.nanoTime();
        double d3 = 20.0;
        double d4 = d * d3;
        double d5 = this.smoothedBps;
        double d6 = d4;
        double d7 = 0.75;
        if (d6 < d7) {
            double d8 = d5;
            double d9 = 0.75;
            if (d8 <= d9) {
                double d10;
                this.smoothedBps = d10 = 0.0;
            } else {
                double d11;
                double d12 = d5;
                double d13 = 0.6;
                double d14 = 0.0;
                double d15 = d5;
                double d16 = d14 - d15;
                double d17 = d13 * d16;
                this.smoothedBps = d11 = d12 + d17;
            }
            return;
        }
        double d18 = d4;
        double d19 = d5;
        double d20 = d18 < d19 ? 0.6 : 0.25;
        double d21 = d20;
        double d22 = d5;
        double d23 = d21;
        double d24 = d4;
        double d25 = d5;
        double d26 = d24 - d25;
        double d27 = d23 * d26;
        this.smoothedBps = d2 = d22 + d27;
    }

    public double getSmoothedBps() {
        long l;
        long l2;
        long l3 = this.lastSpeedSampleNanos;
        long l4 = 0L;
        if (l3 == l4) {
            return 0.0;
        }
        long l5 = System.nanoTime();
        long l6 = l5 - (l2 = this.lastSpeedSampleNanos);
        if (l6 > (l = 1000000000L)) {
            return 0.0;
        }
        return this.smoothedBps;
    }

    @Generated
    public MovementUncertaintyState() {
        long l;
        double d;
        double d2;
        double d3;
        double d4;
        LastInstance lastInstance;
        LastInstance lastInstance2;
        this.lastPointThree = lastInstance2 = new LastInstance();
        this.lastVehicleSwitch = lastInstance = new LastInstance();
        this.lastX = d4 = Double.NaN;
        this.lastY = d3 = Double.NaN;
        this.lastZ = d2 = Double.NaN;
        this.smoothedBps = d = 0.0;
        this.lastSpeedSampleNanos = l = 0L;
    }

    @Generated
    public LastInstance getLastPointThree() {
        return this.lastPointThree;
    }

    @Generated
    public LastInstance getLastVehicleSwitch() {
        return this.lastVehicleSwitch;
    }

    @Generated
    public int getClientFlyingSeq() {
        return this.clientFlyingSeq;
    }

    @Generated
    public boolean isPacketOnGround() {
        return this.packetOnGround;
    }

    @Generated
    public double getLastX() {
        return this.lastX;
    }

    @Generated
    public double getLastY() {
        return this.lastY;
    }

    @Generated
    public double getLastZ() {
        return this.lastZ;
    }

    @Generated
    public boolean isWasInVehicle() {
        return this.wasInVehicle;
    }

    @Generated
    public long getLastSpeedSampleNanos() {
        return this.lastSpeedSampleNanos;
    }

    @Generated
    public void setClientFlyingSeq(int n) {
        this.clientFlyingSeq = n;
    }

    @Generated
    public void setPacketOnGround(boolean bl) {
        this.packetOnGround = bl;
    }

    @Generated
    public void setLastX(double d) {
        this.lastX = d;
    }

    @Generated
    public void setLastY(double d) {
        this.lastY = d;
    }

    @Generated
    public void setLastZ(double d) {
        this.lastZ = d;
    }

    @Generated
    public void setWasInVehicle(boolean bl) {
        this.wasInVehicle = bl;
    }

    @Generated
    public void setSmoothedBps(double d) {
        this.smoothedBps = d;
    }

    @Generated
    public void setLastSpeedSampleNanos(long l) {
        this.lastSpeedSampleNanos = l;
    }

    @Generated
    public boolean equals(Object object) {
        LastInstance lastInstance;
        LastInstance lastInstance2;
        LastInstance lastInstance3;
        LastInstance lastInstance4;
        LastInstance lastInstance5;
        LastInstance lastInstance6;
        MovementUncertaintyState movementUncertaintyState;
        long l;
        MovementUncertaintyState movementUncertaintyState2;
        double d;
        MovementUncertaintyState movementUncertaintyState3;
        boolean bl;
        MovementUncertaintyState movementUncertaintyState4;
        double d2;
        MovementUncertaintyState movementUncertaintyState5;
        double d3;
        MovementUncertaintyState movementUncertaintyState6;
        double d4;
        MovementUncertaintyState movementUncertaintyState7;
        boolean bl2;
        MovementUncertaintyState movementUncertaintyState8;
        int n;
        if (object == this) {
            return true;
        }
        if (!(object instanceof MovementUncertaintyState)) {
            return false;
        }
        MovementUncertaintyState movementUncertaintyState9 = (MovementUncertaintyState)object;
        int clientFlyingSeq = this.getClientFlyingSeq();
        if (clientFlyingSeq != (n = (movementUncertaintyState8 = movementUncertaintyState9).getClientFlyingSeq())) {
            return false;
        }
        boolean packetOnGround = this.isPacketOnGround();
        if (packetOnGround != (bl2 = (movementUncertaintyState7 = movementUncertaintyState9).isPacketOnGround())) {
            return false;
        }
        double lastX = this.getLastX();
        if (Double.compare(lastX, d4 = (movementUncertaintyState6 = movementUncertaintyState9).getLastX()) != 0) {
            return false;
        }
        double lastY = this.getLastY();
        if (Double.compare(lastY, d3 = (movementUncertaintyState5 = movementUncertaintyState9).getLastY()) != 0) {
            return false;
        }
        double lastZ = this.getLastZ();
        if (Double.compare(lastZ, d2 = (movementUncertaintyState4 = movementUncertaintyState9).getLastZ()) != 0) {
            return false;
        }
        boolean wasInVehicle = this.isWasInVehicle();
        if (wasInVehicle != (bl = (movementUncertaintyState3 = movementUncertaintyState9).isWasInVehicle())) {
            return false;
        }
        double smoothedBps = this.getSmoothedBps();
        if (Double.compare(smoothedBps, d = (movementUncertaintyState2 = movementUncertaintyState9).getSmoothedBps()) != 0) {
            return false;
        }
        long lastSpeedSampleNanos = this.getLastSpeedSampleNanos();
        if (lastSpeedSampleNanos != (l = (movementUncertaintyState = movementUncertaintyState9).getLastSpeedSampleNanos())) {
            return false;
        }
        LastInstance lastPointThree = this.getLastPointThree();
        MovementUncertaintyState movementUncertaintyState10 = movementUncertaintyState9;
        LastInstance lastPointThree2 = movementUncertaintyState10.getLastPointThree();
        LastInstance lastInstance7 = lastPointThree;
        if (lastInstance7 == null ? (lastInstance6 = lastPointThree2) != null : !(lastInstance5 = lastPointThree).equals(lastInstance4 = lastPointThree2)) {
            return false;
        }
        LastInstance lastVehicleSwitch = this.getLastVehicleSwitch();
        MovementUncertaintyState movementUncertaintyState11 = movementUncertaintyState9;
        LastInstance lastVehicleSwitch2 = movementUncertaintyState11.getLastVehicleSwitch();
        LastInstance lastInstance8 = lastVehicleSwitch;
        return !(lastInstance8 == null ? (lastInstance3 = lastVehicleSwitch2) != null : !(lastInstance2 = lastVehicleSwitch).equals(lastInstance = lastVehicleSwitch2));
    }

    @Generated
    public int hashCode() {
        int n;
        int n2;
        int n3 = 59;
        int clientFlyingSeq = this.getClientFlyingSeq();
        int n4 = (n3 + clientFlyingSeq) * 59;
        int n5 = this.isPacketOnGround() ? 79 : 97;
        int n6 = n4 + n5;
        long l = Double.doubleToLongBits(this.getLastX());
        int n7 = n6;
        int n8 = n7 * 59;
        long l2 = l;
        long l3 = l2 >>> 32;
        long l4 = l;
        int n9 = (int)(l3 ^ l4);
        int n10 = n8 + n9;
        long l5 = Double.doubleToLongBits(this.getLastY());
        int n11 = n10;
        int n12 = n11 * 59;
        long l6 = l5;
        long l7 = l6 >>> 32;
        long l8 = l5;
        int n13 = (int)(l7 ^ l8);
        int n14 = n12 + n13;
        long l9 = Double.doubleToLongBits(this.getLastZ());
        int n15 = n14;
        int n16 = n15 * 59;
        long l10 = l9;
        long l11 = l10 >>> 32;
        long l12 = l9;
        int n17 = (int)(l11 ^ l12);
        int n18 = (n16 + n17) * 59;
        int n19 = this.isWasInVehicle() ? 79 : 97;
        int n20 = n18 + n19;
        long l13 = Double.doubleToLongBits(this.getSmoothedBps());
        int n21 = n20;
        int n22 = n21 * 59;
        long l14 = l13;
        long l15 = l14 >>> 32;
        long l16 = l13;
        int n23 = (int)(l15 ^ l16);
        int n24 = n22 + n23;
        long lastSpeedSampleNanos = this.getLastSpeedSampleNanos();
        int n25 = n24;
        int n26 = n25 * 59;
        long l17 = lastSpeedSampleNanos;
        long l18 = l17 >>> 32;
        long l19 = lastSpeedSampleNanos;
        int n27 = (int)(l18 ^ l19);
        int n28 = n26 + n27;
        LastInstance lastPointThree = this.getLastPointThree();
        int n29 = n28;
        int n30 = n29 * 59;
        LastInstance lastInstance = lastPointThree;
        if (lastInstance == null) {
            n2 = 43;
        } else {
            LastInstance lastInstance2 = lastPointThree;
            n2 = lastInstance2.hashCode();
        }
        int n31 = n30 + n2;
        LastInstance lastVehicleSwitch = this.getLastVehicleSwitch();
        int n32 = n31;
        int n33 = n32 * 59;
        LastInstance lastInstance3 = lastVehicleSwitch;
        if (lastInstance3 == null) {
            n = 43;
        } else {
            LastInstance lastInstance4 = lastVehicleSwitch;
            n = lastInstance4.hashCode();
        }
        return n33 + n;
    }

    @Generated
    public String toString() {
        String string = String.valueOf(this.getLastPointThree());
        String string2 = String.valueOf(this.getLastVehicleSwitch());
        int clientFlyingSeq = this.getClientFlyingSeq();
        boolean packetOnGround = this.isPacketOnGround();
        double lastX = this.getLastX();
        double lastY = this.getLastY();
        double lastZ = this.getLastZ();
        boolean wasInVehicle = this.isWasInVehicle();
        double smoothedBps = this.getSmoothedBps();
        long lastSpeedSampleNanos = this.getLastSpeedSampleNanos();
        return "MovementUncertaintyState(lastPointThree=" + string + ", lastVehicleSwitch=" + string2 + ", clientFlyingSeq=" + clientFlyingSeq + ", packetOnGround=" + packetOnGround + ", lastX=" + lastX + ", lastY=" + lastY + ", lastZ=" + lastZ + ", wasInVehicle=" + wasInVehicle + ", smoothedBps=" + smoothedBps + ", lastSpeedSampleNanos=" + lastSpeedSampleNanos + ")";
    }
}

