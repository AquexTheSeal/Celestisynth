package org.thecelestialworkshop.celestisynth.common.entity.goals;

import org.thecelestialworkshop.celestisynth.common.entity.base.FixedMovesetEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.function.Predicate;

public class AnimatedMeleeGoal<T extends Mob & FixedMovesetEntity> extends Goal {
    public final T mob;
    public final int animationAction;
    public final double animationDuration;
    public final double attackTimestamp;
    public final double reachInBlocks;
    public final Predicate<T> useCondition;
    public final Predicate<T> resetActionCondition;

    public AnimatedMeleeGoal(T mob, int animationAction, double animationDuration, double attackTimestamp, double reachInBlocks, Predicate<T> useCondition, Predicate<T> resetActionCondition) {
        this.mob = mob;
        this.animationAction = animationAction;
        this.animationDuration = animationDuration;
        this.attackTimestamp = attackTimestamp;
        this.reachInBlocks = reachInBlocks;
        this.useCondition = useCondition;
        this.resetActionCondition = resetActionCondition;
    }

    public AnimatedMeleeGoal(T mob, int animationAction, double animationDuration, double attackTimestamp, double reachInBlocks, Predicate<T> useCondition) {
        this(mob, animationAction, animationDuration, attackTimestamp, reachInBlocks, useCondition, m -> true);
    }

    public AnimatedMeleeGoal(T mob, int animationAction, double animationDuration, double attackTimestamp, double reachInBlocks) {
        this(mob, animationAction, animationDuration, attackTimestamp, reachInBlocks, m -> true);
    }

    @Override
    public boolean canUse() {
        return useCondition.test(mob) && mob.hasNoAction() && mob.getTarget() != null && mob.distanceTo(mob.getTarget()) <= reachInBlocks + mob.getTarget().getBbWidth();
    }

    @Override
    public boolean canContinueToUse() {
        return (mob.hasNoAction() || mob.getAction() == animationAction) && mob.getAnimationTick() < animationDuration;
    }

    @Override
    public void start() {
        mob.resetAnimationTick();
        mob.setAction(animationAction);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        mob.incrementAnimationTick();
        if (mob.getTarget() != null) {
            mob.lookAt(mob.getTarget(), 180, 180);
            if (mob.getAnimationTick() == attackTimestamp) {
                mob.setDeltaMovement(0, mob.getDeltaMovement().y(), 0);
                if (mob.distanceTo(mob.getTarget()) <= reachInBlocks + mob.getTarget().getBbWidth()) {
                    mob.doHurtTarget(mob.getTarget());
                }
            }
        }
    }

    @Override
    public void stop() {
        mob.resetAnimationTick();
        if (resetActionCondition.test(mob)) {
            mob.resetAction();
        }
    }
}
