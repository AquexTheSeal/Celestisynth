package org.thecelestialworkshop.celestisynth.common.entity.goals;

import org.thecelestialworkshop.celestisynth.common.entity.base.FixedMovesetEntity;
import net.minecraft.world.entity.PathfinderMob;

public class ActionStoppableMoveToTargetGoal<T extends PathfinderMob & FixedMovesetEntity> extends MoveToTargetGoal {
    public final T mob;

    public ActionStoppableMoveToTargetGoal(T pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, double reach) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen, reach);
        this.mob = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mob.hasNoAction();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && mob.hasNoAction();
    }
}
