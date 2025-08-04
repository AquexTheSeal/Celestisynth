package org.thecelestialworkshop.celestisynth.common.entity.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

import java.util.function.Predicate;

public class CSLookAroundGoal extends RandomLookAroundGoal {
    public final Mob mob;
    public final Predicate<Mob> condition;

    public CSLookAroundGoal(Mob pMob, Predicate<Mob> pCondition) {
        super(pMob);
        this.mob = pMob;
        this.condition = pCondition;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && condition.test(mob);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && condition.test(mob);
    }

    @Override
    public void tick() {
        super.tick();
        this.lookAt(this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
    }

    public void lookAt(double x, double y, double z) {
        double d0 = x - this.mob.getX();
        double d2 = y - this.mob.getZ();
        double d1 = z - this.mob.getEyeY();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(Mth.atan2(d2, d0) * 57.2957763671875) - 90.0F;
        float f1 = (float)(-(Mth.atan2(d1, d3) * 57.2957763671875));
        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f1, 30));
        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 30));
    }

    private float rotlerp(float pAngle, float pTargetAngle, float pMaxIncrease) {
        float f = Mth.wrapDegrees(pTargetAngle - pAngle);
        if (f > pMaxIncrease) {
            f = pMaxIncrease;
        }

        if (f < -pMaxIncrease) {
            f = -pMaxIncrease;
        }
        return pAngle + f;
    }
}
