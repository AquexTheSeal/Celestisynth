package org.thecelestialworkshop.celestisynth.common.entity.goals;

import org.thecelestialworkshop.celestisynth.common.entity.base.FixedMovesetEntity;
import net.minecraft.world.entity.Mob;

import java.util.function.Predicate;

public class AnimatedMultiTriggerGoal<T extends Mob & FixedMovesetEntity> extends AnimatedMeleeGoal<T> {
    public final Triggerable[] triggers;

    public AnimatedMultiTriggerGoal(T mob, int animationAction, double animationDuration, double reachInBlocks, Predicate<T> useCondition, Predicate<T> resetActionCondition, Triggerable... trigger) {
        super(mob, animationAction, animationDuration, 0, reachInBlocks, useCondition, resetActionCondition);
        this.triggers = trigger;
    }

    public AnimatedMultiTriggerGoal(T mob, int animationAction, double animationDuration, double reachInBlocks, Predicate<T> useCondition, Triggerable... trigger) {
        super(mob, animationAction, animationDuration, 0, reachInBlocks, useCondition);
        this.triggers = trigger;
    }

    public AnimatedMultiTriggerGoal(T mob, int animationAction, double animationDuration, double reachInBlocks, Triggerable... trigger) {
        super(mob, animationAction, animationDuration, 0, reachInBlocks);
        this.triggers = trigger;
    }

    @Override
    public void tick() {
        mob.incrementAnimationTick();
        if (mob.getTarget() != null) {
            mob.lookAt(mob.getTarget(), 180, 180);
            for (Triggerable triggerable : triggers) {
                if (mob.getAnimationTick() == triggerable.timeStamp()) {
                    triggerable.run();
                }
            }
        }
    }

    public record Triggerable(int timeStamp, Runnable trigger) {
        public void run() {
            trigger.run();
        }
    }
}
