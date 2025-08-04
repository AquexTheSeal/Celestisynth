package org.thecelestialworkshop.celestisynth.common.entity.goals.traverser;

import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import net.minecraft.world.entity.ai.goal.Goal;

public class TraverserStunnedGoal extends Goal {
    public Traverser mob;
    public int tickCount;

    public TraverserStunnedGoal(Traverser mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return mob.getAction() == Traverser.ACTION_STUNNED;
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getAction() == Traverser.ACTION_STUNNED && tickCount < 20;
    }

    @Override
    public void start() {
        this.tickCount = 0;
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
        mob.setDeltaMovement(0, mob.getDeltaMovement().y(), 0);
    }

    @Override
    public void stop() {
        this.tickCount = 0;
        mob.resetAction();
    }
}
