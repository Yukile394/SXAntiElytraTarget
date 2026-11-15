package studio.glace.glantielytratarget.checks.impl.pivot;

import org.jetbrains.annotations.NotNull;

public final class PivotSettleCoordinator {
   public static final int RESULT_PIVOT = 1;
   private final PivotSettleCoordinator.Thresholds t;

   public PivotSettleCoordinator(@NotNull PivotSettleCoordinator.Thresholds thresholds1) {
      this.t = thresholds1;
   }

   @NotNull
   public PivotSettleCoordinator.Thresholds thresholds() {
      return this.t;
   }

   public void onAttack(@NotNull PivotSettleSession pivotSettleSession1) {
      pivotSettleSession1.attackPending = true;
   }

   public int onRotation(@NotNull PivotSettleSession pivotSettleSession1, float f2, float f3, long l4) {
      int i12 = 0;
      if (pivotSettleSession1.attackPending && pivotSettleSession1.hasBaseline) {
         label16: {
            float f37 = pivotSettleSession1.lastYaw;
            float f13 = Math.abs(RotationMath.normalizeYawDelta(f2 - f37));
            float f39 = pivotSettleSession1.lastPitch;
            float f14 = Math.abs(f3 - f39);
            float f26 = f13;
            float f42 = this.t.minYawDelta();
            if (!(f26 > f42)) {
               float f28 = f14;
               float f45 = this.t.minPitchDelta();
               if (!(f28 > f45)) {
                  break label16;
               }
            }

            if (this.countPivot(pivotSettleSession1, l4)) {
               int i32 = i12;
               i12 = i32 | 1;
            }
         }

         pivotSettleSession1.attackPending = false;
      }

      this.rebaseline(pivotSettleSession1, f2, f3);
      return i12;
   }

   public void rebaseline(@NotNull PivotSettleSession pivotSettleSession1, float f2, float f3) {
      pivotSettleSession1.lastYaw = f2;
      pivotSettleSession1.lastPitch = f3;
      pivotSettleSession1.hasBaseline = true;
   }

   public void resetTransient(@NotNull PivotSettleSession pivotSettleSession1) {
      pivotSettleSession1.attackPending = false;
      pivotSettleSession1.hasBaseline = false;
   }

   private boolean countPivot(@NotNull PivotSettleSession pivotSettleSession1, long l2) {
      label15: {
         long l10 = pivotSettleSession1.pivotWindowStartMs;
         long l8 = 0L;
         if (l10 != l8) {
            long l37 = pivotSettleSession1.pivotWindowStartMs;
            long l15 = l2 - l37;
            long l41 = this.t.pivotWinMs();
            if (l15 <= l41) {
               break label15;
            }
         }

         pivotSettleSession1.pivotWindowStartMs = l2;
         pivotSettleSession1.pivotCount = 0;
      }

      int i29 = pivotSettleSession1.pivotCount + 1;
      pivotSettleSession1.pivotCount = i29;
      int i22 = pivotSettleSession1.pivotCount;
      int i32 = this.t.pivotReq();
      if (i22 >= i32) {
         long l33 = 0L;
         pivotSettleSession1.pivotWindowStartMs = l33;
         pivotSettleSession1.pivotCount = 0;
         return true;
      } else {
         return false;
      }
   }

   public record Thresholds(float minYawDelta, float minPitchDelta, int pivotReq, long pivotWinMs) {
      public Thresholds(float minYawDelta, float minPitchDelta, int pivotReq, long pivotWinMs) {
         float f32 = Math.max(1.0F, minYawDelta);
         float f33 = Math.max(1.0F, minPitchDelta);
         int i34 = Math.max(1, pivotReq);
         long l35 = Math.max(1L, pivotWinMs);
         float f27 = f32;
         this.minYawDelta = f27;
         float f28 = f33;
         this.minPitchDelta = f28;
         int i29 = i34;
         this.pivotReq = i29;
         long l30 = l35;
         this.pivotWinMs = l30;
      }
   }
}
