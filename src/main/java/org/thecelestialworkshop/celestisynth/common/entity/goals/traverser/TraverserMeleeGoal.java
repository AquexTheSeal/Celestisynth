package org.thecelestialworkshop.celestisynth.common.entity.goals.traverser;

import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class TraverserMeleeGoal extends Goal {
    public final Traverser mob;
    public final double reach;

    public boolean hasHit;
    public int tickCount;

    public TraverserMeleeGoal(Traverser mob, double reach) {
        this.mob = mob;
        this.reach = reach;
    }

    @Override
    public boolean canUse() {
        return mob.hasNoAction() && mob.getTarget() != null && mob.distanceTo(mob.getTarget()) <= mob.getAttackReachInBlocks();
    }

    @Override
    public boolean canContinueToUse() {
        return (mob.hasNoAction() || mob.getAction() == Traverser.ACTION_MELEE) && this.tickCount <= 10;
    }

    @Override
    public void start() {
        this.tickCount = 0;
        this.hasHit = false;
        mob.setAction(Traverser.ACTION_MELEE);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
        if (mob.getTarget() != null) {
            if (this.tickCount == 2) {
                if (mob.distanceTo(mob.getTarget()) <= mob.getAttackReachInBlocks()) {
                    boolean flag = mob.getTarget().hurt(mob.damageSources().mobAttack(mob), (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                    if (flag) {
                        this.hasHit = true;
                    }
                }
            }

            if (this.tickCount == 5) {
                Vec3 retreatVector = mob.position().subtract(mob.getTarget().position()).normalize().scale(2);
                mob.setDeltaMovement(retreatVector.x(), mob.getDeltaMovement().y(), retreatVector.z());
                if (mob.level() instanceof ServerLevel server) {
                    server.playSeededSound(null, mob, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WITHER_SHOOT), SoundSource.HOSTILE, 0.6F, 0.5F, server.random.nextLong());
                }
            }

            if (this.tickCount >= 5 && tickCount <= 8) {
                for (int i = 0; i < 10; i++) {
                    ParticleUtil.sendParticle(mob.level(), CSParticleTypes.KERES_ASH.get(), mob.position().add(mob.getRandom().nextGaussian() * 0.1F, mob.getRandom().nextGaussian() * 0.1F, mob.getRandom().nextGaussian() * 0.1F), new Vec3(0, 0.2, 0));
                }
            }

            if (this.tickCount >= 7 && this.hasHit) {
                mob.throwSlash();
            }
        }
    }

    @Override
    public void stop() {
        this.tickCount = 0;
        this.hasHit = false;
        mob.resetAction();
    }
}
