package org.thecelestialworkshop.celestisynth.common.entity.goals;

import org.thecelestialworkshop.celestisynth.common.entity.base.FixedMovesetEntity;
import net.minecraft.world.entity.Mob;

import java.util.function.Predicate;

public class AnimatedTriggerGoal<T extends Mob & FixedMovesetEntity> extends AnimatedMeleeGoal<T> {
    public final Runnable trigger;

    public AnimatedTriggerGoal(T mob, int animationAction, double animationDuration, double attackTimestamp, double reachInBlocks, Predicate<T> useCondition, Predicate<T> resetActionCondition, Runnable trigger) {
        super(mob, animationAction, animationDuration, attackTimestamp, reachInBlocks, useCondition, resetActionCondition);
        this.trigger = trigger;
    }

    public AnimatedTriggerGoal(T mob, int animationAction, double animationDuration, double attackTimestamp, double reachInBlocks, Predicate<T> useCondition, Runnable trigger) {
        super(mob, animationAction, animationDuration, attackTimestamp, reachInBlocks, useCondition);
        this.trigger = trigger;
    }

    public AnimatedTriggerGoal(T mob, int animationAction, double animationDuration, double attackTimestamp, double reachInBlocks, Runnable trigger) {
        super(mob, animationAction, animationDuration, attackTimestamp, reachInBlocks);
        this.trigger = trigger;
    }

    @Override
    public void tick() {
        mob.incrementAnimationTick();
        if (mob.getTarget() != null) {
            mob.lookAt(mob.getTarget(), 180, 180);
            if (mob.getAnimationTick() == attackTimestamp) {
                mob.setDeltaMovement(0, mob.getDeltaMovement().y(), 0);
                trigger.run();
            }
        }
    }
}
