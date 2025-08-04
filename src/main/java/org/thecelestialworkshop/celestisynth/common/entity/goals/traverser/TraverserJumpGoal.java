package org.thecelestialworkshop.celestisynth.common.entity.goals.traverser;

import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class TraverserJumpGoal extends Goal {
    public Traverser mob;
    public int tickCount;

    public TraverserJumpGoal(Traverser mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return mob.getAction() == Traverser.ACTION_JUMP;
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getAction() == Traverser.ACTION_JUMP && tickCount < 15;
    }

    @Override
    public void start() {
        this.tickCount = 0;
        if (mob.getTarget() != null) {
            mob.setOnGround(false);
            Vec3 vector = mob.position().subtract(mob.getTarget().position()).normalize().scale(1.5);
            mob.setDeltaMovement(new Vec3(vector.x(), 0, vector.z()).add(0, 0.8, 0).yRot((float) (mob.getRandom().nextGaussian() * 360)));
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
        if (mob.getTarget() != null && !mob.onGround()) {
            if (this.tickCount >= 7 && this.tickCount <= 13) {
                mob.throwSlash();
            }
        }
        if (mob.onGround()) {
            mob.resetAction();
        } else {
            for (int i = 0; i < 10; i++) {
                ParticleUtil.sendParticle(mob.level(), CSParticleTypes.KERES_ASH.get(), mob.position().add(mob.getRandom().nextGaussian() * 0.1F, mob.getRandom().nextGaussian() * 0.1F, mob.getRandom().nextGaussian() * 0.1F), new Vec3(0, 0.2, 0));
            }
        }
    }

    @Override
    public void stop() {
        this.tickCount = 0;
        mob.resetAction();
    }
}
