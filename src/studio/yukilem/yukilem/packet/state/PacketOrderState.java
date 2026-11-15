package studio.yukilem.yukilem.packet.state;

import lombok.Generated;

public final class PacketOrderState {
    private boolean openingInventory;
    private boolean swapping;
    private boolean dropping;
    private boolean interacting;
    private boolean attacking;
    private boolean releasing;
    private boolean digging;
    private boolean sprinting;
    private boolean sneaking;
    private boolean placing;
    private boolean using;
    private boolean picking;
    private boolean clickingInInventory;
    private boolean closingInventory;
    private boolean quickMoveClicking;
    private boolean pickUpClicking;
    private boolean leavingBed;
    private boolean startingToGlide;
    private boolean jumpingWithMount;
    private boolean stabbing;

    public void clearAll() {
        this.openingInventory = false;
        this.swapping = false;
        this.dropping = false;
        this.attacking = false;
        this.interacting = false;
        this.releasing = false;
        this.digging = false;
        this.placing = false;
        this.using = false;
        this.picking = false;
        this.sprinting = false;
        this.sneaking = false;
        this.clickingInInventory = false;
        this.closingInventory = false;
        this.quickMoveClicking = false;
        this.pickUpClicking = false;
        this.leavingBed = false;
        this.startingToGlide = false;
        this.jumpingWithMount = false;
        this.stabbing = false;
    }

    public boolean isRightClicking() {
        boolean bl = this.placing || this.using || this.interacting;
        return bl;
    }

    public boolean isAttackingOrStabbing() {
        boolean bl = this.attacking || this.stabbing;
        return bl;
    }

    @Generated
    public PacketOrderState() {
    }

    @Generated
    public boolean isOpeningInventory() {
        return this.openingInventory;
    }

    @Generated
    public boolean isSwapping() {
        return this.swapping;
    }

    @Generated
    public boolean isDropping() {
        return this.dropping;
    }

    @Generated
    public boolean isInteracting() {
        return this.interacting;
    }

    @Generated
    public boolean isAttacking() {
        return this.attacking;
    }

    @Generated
    public boolean isReleasing() {
        return this.releasing;
    }

    @Generated
    public boolean isDigging() {
        return this.digging;
    }

    @Generated
    public boolean isSprinting() {
        return this.sprinting;
    }

    @Generated
    public boolean isSneaking() {
        return this.sneaking;
    }

    @Generated
    public boolean isPlacing() {
        return this.placing;
    }

    @Generated
    public boolean isUsing() {
        return this.using;
    }

    @Generated
    public boolean isPicking() {
        return this.picking;
    }

    @Generated
    public boolean isClickingInInventory() {
        return this.clickingInInventory;
    }

    @Generated
    public boolean isClosingInventory() {
        return this.closingInventory;
    }

    @Generated
    public boolean isQuickMoveClicking() {
        return this.quickMoveClicking;
    }

    @Generated
    public boolean isPickUpClicking() {
        return this.pickUpClicking;
    }

    @Generated
    public boolean isLeavingBed() {
        return this.leavingBed;
    }

    @Generated
    public boolean isStartingToGlide() {
        return this.startingToGlide;
    }

    @Generated
    public boolean isJumpingWithMount() {
        return this.jumpingWithMount;
    }

    @Generated
    public boolean isStabbing() {
        return this.stabbing;
    }

    @Generated
    public void setOpeningInventory(boolean bl) {
        this.openingInventory = bl;
    }

    @Generated
    public void setSwapping(boolean bl) {
        this.swapping = bl;
    }

    @Generated
    public void setDropping(boolean bl) {
        this.dropping = bl;
    }

    @Generated
    public void setInteracting(boolean bl) {
        this.interacting = bl;
    }

    @Generated
    public void setAttacking(boolean bl) {
        this.attacking = bl;
    }

    @Generated
    public void setReleasing(boolean bl) {
        this.releasing = bl;
    }

    @Generated
    public void setDigging(boolean bl) {
        this.digging = bl;
    }

    @Generated
    public void setSprinting(boolean bl) {
        this.sprinting = bl;
    }

    @Generated
    public void setSneaking(boolean bl) {
        this.sneaking = bl;
    }

    @Generated
    public void setPlacing(boolean bl) {
        this.placing = bl;
    }

    @Generated
    public void setUsing(boolean bl) {
        this.using = bl;
    }

    @Generated
    public void setPicking(boolean bl) {
        this.picking = bl;
    }

    @Generated
    public void setClickingInInventory(boolean bl) {
        this.clickingInInventory = bl;
    }

    @Generated
    public void setClosingInventory(boolean bl) {
        this.closingInventory = bl;
    }

    @Generated
    public void setQuickMoveClicking(boolean bl) {
        this.quickMoveClicking = bl;
    }

    @Generated
    public void setPickUpClicking(boolean bl) {
        this.pickUpClicking = bl;
    }

    @Generated
    public void setLeavingBed(boolean bl) {
        this.leavingBed = bl;
    }

    @Generated
    public void setStartingToGlide(boolean bl) {
        this.startingToGlide = bl;
    }

    @Generated
    public void setJumpingWithMount(boolean bl) {
        this.jumpingWithMount = bl;
    }

    @Generated
    public void setStabbing(boolean bl) {
        this.stabbing = bl;
    }

    @Generated
    public boolean equals(Object object) {
        PacketOrderState packetOrderState;
        boolean bl;
        PacketOrderState packetOrderState2;
        boolean bl2;
        PacketOrderState packetOrderState3;
        boolean bl3;
        PacketOrderState packetOrderState4;
        boolean bl4;
        PacketOrderState packetOrderState5;
        boolean bl5;
        PacketOrderState packetOrderState6;
        boolean bl6;
        PacketOrderState packetOrderState7;
        boolean bl7;
        PacketOrderState packetOrderState8;
        boolean bl8;
        PacketOrderState packetOrderState9;
        boolean bl9;
        PacketOrderState packetOrderState10;
        boolean bl10;
        PacketOrderState packetOrderState11;
        boolean bl11;
        PacketOrderState packetOrderState12;
        boolean bl12;
        PacketOrderState packetOrderState13;
        boolean bl13;
        PacketOrderState packetOrderState14;
        boolean bl14;
        PacketOrderState packetOrderState15;
        boolean bl15;
        PacketOrderState packetOrderState16;
        boolean bl16;
        PacketOrderState packetOrderState17;
        boolean bl17;
        PacketOrderState packetOrderState18;
        boolean bl18;
        PacketOrderState packetOrderState19;
        boolean bl19;
        PacketOrderState packetOrderState20;
        boolean bl20;
        if (object == this) {
            return true;
        }
        if (!(object instanceof PacketOrderState)) {
            return false;
        }
        PacketOrderState packetOrderState21 = (PacketOrderState)object;
        boolean openingInventory = this.isOpeningInventory();
        if (openingInventory != (bl20 = (packetOrderState20 = packetOrderState21).isOpeningInventory())) {
            return false;
        }
        boolean swapping = this.isSwapping();
        if (swapping != (bl19 = (packetOrderState19 = packetOrderState21).isSwapping())) {
            return false;
        }
        boolean dropping = this.isDropping();
        if (dropping != (bl18 = (packetOrderState18 = packetOrderState21).isDropping())) {
            return false;
        }
        boolean interacting = this.isInteracting();
        if (interacting != (bl17 = (packetOrderState17 = packetOrderState21).isInteracting())) {
            return false;
        }
        boolean attacking = this.isAttacking();
        if (attacking != (bl16 = (packetOrderState16 = packetOrderState21).isAttacking())) {
            return false;
        }
        boolean releasing = this.isReleasing();
        if (releasing != (bl15 = (packetOrderState15 = packetOrderState21).isReleasing())) {
            return false;
        }
        boolean digging = this.isDigging();
        if (digging != (bl14 = (packetOrderState14 = packetOrderState21).isDigging())) {
            return false;
        }
        boolean sprinting = this.isSprinting();
        if (sprinting != (bl13 = (packetOrderState13 = packetOrderState21).isSprinting())) {
            return false;
        }
        boolean sneaking = this.isSneaking();
        if (sneaking != (bl12 = (packetOrderState12 = packetOrderState21).isSneaking())) {
            return false;
        }
        boolean placing = this.isPlacing();
        if (placing != (bl11 = (packetOrderState11 = packetOrderState21).isPlacing())) {
            return false;
        }
        boolean using = this.isUsing();
        if (using != (bl10 = (packetOrderState10 = packetOrderState21).isUsing())) {
            return false;
        }
        boolean picking = this.isPicking();
        if (picking != (bl9 = (packetOrderState9 = packetOrderState21).isPicking())) {
            return false;
        }
        boolean clickingInInventory = this.isClickingInInventory();
        if (clickingInInventory != (bl8 = (packetOrderState8 = packetOrderState21).isClickingInInventory())) {
            return false;
        }
        boolean closingInventory = this.isClosingInventory();
        if (closingInventory != (bl7 = (packetOrderState7 = packetOrderState21).isClosingInventory())) {
            return false;
        }
        boolean quickMoveClicking = this.isQuickMoveClicking();
        if (quickMoveClicking != (bl6 = (packetOrderState6 = packetOrderState21).isQuickMoveClicking())) {
            return false;
        }
        boolean pickUpClicking = this.isPickUpClicking();
        if (pickUpClicking != (bl5 = (packetOrderState5 = packetOrderState21).isPickUpClicking())) {
            return false;
        }
        boolean leavingBed = this.isLeavingBed();
        if (leavingBed != (bl4 = (packetOrderState4 = packetOrderState21).isLeavingBed())) {
            return false;
        }
        boolean startingToGlide = this.isStartingToGlide();
        if (startingToGlide != (bl3 = (packetOrderState3 = packetOrderState21).isStartingToGlide())) {
            return false;
        }
        boolean jumpingWithMount = this.isJumpingWithMount();
        if (jumpingWithMount != (bl2 = (packetOrderState2 = packetOrderState21).isJumpingWithMount())) {
            return false;
        }
        boolean stabbing = this.isStabbing();
        return stabbing == (bl = (packetOrderState = packetOrderState21).isStabbing());
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = this.isOpeningInventory() ? 79 : 97;
        int n3 = (n + n2) * 59;
        int n4 = this.isSwapping() ? 79 : 97;
        int n5 = (n3 + n4) * 59;
        int n6 = this.isDropping() ? 79 : 97;
        int n7 = (n5 + n6) * 59;
        int n8 = this.isInteracting() ? 79 : 97;
        int n9 = (n7 + n8) * 59;
        int n10 = this.isAttacking() ? 79 : 97;
        int n11 = (n9 + n10) * 59;
        int n12 = this.isReleasing() ? 79 : 97;
        int n13 = (n11 + n12) * 59;
        int n14 = this.isDigging() ? 79 : 97;
        int n15 = (n13 + n14) * 59;
        int n16 = this.isSprinting() ? 79 : 97;
        int n17 = (n15 + n16) * 59;
        int n18 = this.isSneaking() ? 79 : 97;
        int n19 = (n17 + n18) * 59;
        int n20 = this.isPlacing() ? 79 : 97;
        int n21 = (n19 + n20) * 59;
        int n22 = this.isUsing() ? 79 : 97;
        int n23 = (n21 + n22) * 59;
        int n24 = this.isPicking() ? 79 : 97;
        int n25 = (n23 + n24) * 59;
        int n26 = this.isClickingInInventory() ? 79 : 97;
        int n27 = (n25 + n26) * 59;
        int n28 = this.isClosingInventory() ? 79 : 97;
        int n29 = (n27 + n28) * 59;
        int n30 = this.isQuickMoveClicking() ? 79 : 97;
        int n31 = (n29 + n30) * 59;
        int n32 = this.isPickUpClicking() ? 79 : 97;
        int n33 = (n31 + n32) * 59;
        int n34 = this.isLeavingBed() ? 79 : 97;
        int n35 = (n33 + n34) * 59;
        int n36 = this.isStartingToGlide() ? 79 : 97;
        int n37 = (n35 + n36) * 59;
        int n38 = this.isJumpingWithMount() ? 79 : 97;
        int n39 = (n37 + n38) * 59;
        int n40 = this.isStabbing() ? 79 : 97;
        return n39 + n40;
    }

    @Generated
    public String toString() {
        boolean openingInventory = this.isOpeningInventory();
        boolean swapping = this.isSwapping();
        boolean dropping = this.isDropping();
        boolean interacting = this.isInteracting();
        boolean attacking = this.isAttacking();
        boolean releasing = this.isReleasing();
        boolean digging = this.isDigging();
        boolean sprinting = this.isSprinting();
        boolean sneaking = this.isSneaking();
        boolean placing = this.isPlacing();
        boolean using = this.isUsing();
        boolean picking = this.isPicking();
        boolean clickingInInventory = this.isClickingInInventory();
        boolean closingInventory = this.isClosingInventory();
        boolean quickMoveClicking = this.isQuickMoveClicking();
        boolean pickUpClicking = this.isPickUpClicking();
        boolean leavingBed = this.isLeavingBed();
        boolean startingToGlide = this.isStartingToGlide();
        boolean jumpingWithMount = this.isJumpingWithMount();
        boolean stabbing = this.isStabbing();
        return "PacketOrderState(openingInventory=" + openingInventory + ", swapping=" + swapping + ", dropping=" + dropping + ", interacting=" + interacting + ", attacking=" + attacking + ", releasing=" + releasing + ", digging=" + digging + ", sprinting=" + sprinting + ", sneaking=" + sneaking + ", placing=" + placing + ", using=" + using + ", picking=" + picking + ", clickingInInventory=" + clickingInInventory + ", closingInventory=" + closingInventory + ", quickMoveClicking=" + quickMoveClicking + ", pickUpClicking=" + pickUpClicking + ", leavingBed=" + leavingBed + ", startingToGlide=" + startingToGlide + ", jumpingWithMount=" + jumpingWithMount + ", stabbing=" + stabbing + ")";
    }
}

