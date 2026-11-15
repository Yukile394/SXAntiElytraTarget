package studio.yukilem.yukilem.packet.state;

import lombok.Generated;

public final class TickPacketState {
    private boolean lastPacketWasTeleport;
    private boolean lastPacketWasOnePointSeventeenDuplicate;
    private boolean didSendMovementBeforeTickEnd;
    private boolean pendingTeleportConfirm;

    public void endPacket() {
        this.lastPacketWasOnePointSeventeenDuplicate = false;
        this.lastPacketWasTeleport = false;
    }

    public void invalidatePacketContinuity() {
        this.lastPacketWasTeleport = false;
        this.lastPacketWasOnePointSeventeenDuplicate = false;
        this.didSendMovementBeforeTickEnd = false;
        this.pendingTeleportConfirm = false;
    }

    @Generated
    public TickPacketState() {
    }

    @Generated
    public boolean isLastPacketWasTeleport() {
        return this.lastPacketWasTeleport;
    }

    @Generated
    public boolean isLastPacketWasOnePointSeventeenDuplicate() {
        return this.lastPacketWasOnePointSeventeenDuplicate;
    }

    @Generated
    public boolean isDidSendMovementBeforeTickEnd() {
        return this.didSendMovementBeforeTickEnd;
    }

    @Generated
    public boolean isPendingTeleportConfirm() {
        return this.pendingTeleportConfirm;
    }

    @Generated
    public void setLastPacketWasTeleport(boolean bl) {
        this.lastPacketWasTeleport = bl;
    }

    @Generated
    public void setLastPacketWasOnePointSeventeenDuplicate(boolean bl) {
        this.lastPacketWasOnePointSeventeenDuplicate = bl;
    }

    @Generated
    public void setDidSendMovementBeforeTickEnd(boolean bl) {
        this.didSendMovementBeforeTickEnd = bl;
    }

    @Generated
    public void setPendingTeleportConfirm(boolean bl) {
        this.pendingTeleportConfirm = bl;
    }

    @Generated
    public boolean equals(Object object) {
        TickPacketState tickPacketState;
        boolean bl;
        TickPacketState tickPacketState2;
        boolean bl2;
        TickPacketState tickPacketState3;
        boolean bl3;
        TickPacketState tickPacketState4;
        boolean bl4;
        if (object == this) {
            return true;
        }
        if (!(object instanceof TickPacketState)) {
            return false;
        }
        TickPacketState tickPacketState5 = (TickPacketState)object;
        boolean lastPacketWasTeleport = this.isLastPacketWasTeleport();
        if (lastPacketWasTeleport != (bl4 = (tickPacketState4 = tickPacketState5).isLastPacketWasTeleport())) {
            return false;
        }
        boolean lastPacketWasOnePointSeventeenDuplicate = this.isLastPacketWasOnePointSeventeenDuplicate();
        if (lastPacketWasOnePointSeventeenDuplicate != (bl3 = (tickPacketState3 = tickPacketState5).isLastPacketWasOnePointSeventeenDuplicate())) {
            return false;
        }
        boolean didSendMovementBeforeTickEnd = this.isDidSendMovementBeforeTickEnd();
        if (didSendMovementBeforeTickEnd != (bl2 = (tickPacketState2 = tickPacketState5).isDidSendMovementBeforeTickEnd())) {
            return false;
        }
        boolean pendingTeleportConfirm = this.isPendingTeleportConfirm();
        return pendingTeleportConfirm == (bl = (tickPacketState = tickPacketState5).isPendingTeleportConfirm());
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = this.isLastPacketWasTeleport() ? 79 : 97;
        int n3 = (n + n2) * 59;
        int n4 = this.isLastPacketWasOnePointSeventeenDuplicate() ? 79 : 97;
        int n5 = (n3 + n4) * 59;
        int n6 = this.isDidSendMovementBeforeTickEnd() ? 79 : 97;
        int n7 = (n5 + n6) * 59;
        int n8 = this.isPendingTeleportConfirm() ? 79 : 97;
        return n7 + n8;
    }

    @Generated
    public String toString() {
        boolean lastPacketWasTeleport = this.isLastPacketWasTeleport();
        boolean lastPacketWasOnePointSeventeenDuplicate = this.isLastPacketWasOnePointSeventeenDuplicate();
        boolean didSendMovementBeforeTickEnd = this.isDidSendMovementBeforeTickEnd();
        boolean pendingTeleportConfirm = this.isPendingTeleportConfirm();
        return "TickPacketState(lastPacketWasTeleport=" + lastPacketWasTeleport + ", lastPacketWasOnePointSeventeenDuplicate=" + lastPacketWasOnePointSeventeenDuplicate + ", didSendMovementBeforeTickEnd=" + didSendMovementBeforeTickEnd + ", pendingTeleportConfirm=" + pendingTeleportConfirm + ")";
    }
}

