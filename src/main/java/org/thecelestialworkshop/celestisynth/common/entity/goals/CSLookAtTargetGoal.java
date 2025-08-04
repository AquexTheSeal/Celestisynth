package org.thecelestialworkshop.celestisynth.common.entity.goals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class CSLookAtTargetGoal extends Goal {
    public final Mob mob;

    public CSLookAtTargetGoal(Mob pMob) {
        this.mob = pMob;
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getTarget() != null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (mob.getTarget() != null) {
            this.mob.lookAt(mob.getTarget(), 180.0F, 180.0F);
            this.mob.getLookControl().setLookAt(mob.getTarget().getX(), mob.getTarget().getEyeY(), mob.getTarget().getZ(), 180.0F, 180.0F);
        }
    }
}
