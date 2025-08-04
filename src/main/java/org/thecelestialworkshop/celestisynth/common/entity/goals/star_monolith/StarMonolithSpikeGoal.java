package org.thecelestialworkshop.celestisynth.common.entity.goals.star_monolith;

import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.common.entity.base.MonolithSummonedEntity;
import org.thecelestialworkshop.celestisynth.common.entity.helper.MonolithRunes;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class StarMonolithSpikeGoal extends Goal {

    public StarMonolith mob;

    public StarMonolithSpikeGoal(StarMonolith mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return mob.getRune() == MonolithRunes.BLOOD_RUNE && !this.getTargets().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getRune() == MonolithRunes.BLOOD_RUNE && mob.getAnimationTick() < 20;
    }

    @Override
    public void tick() {
        super.tick();
        mob.incrementAnimationTick();
        if (mob.getAnimationTick() == 5) {
            this.getTargets().forEach(target -> {
                mob.initiateAbilityAttack(mob, target, (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue(), AttackHurtTypes.RAPID);
                target.setDeltaMovement(target.position().subtract(mob.position()).normalize().multiply(4, 0, 4));
            });
        }
    }

    public List<LivingEntity> getTargets() {
        return mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(1, 0, 1), this::targetFilter);
    }

    public boolean targetFilter(LivingEntity target) {
        if (target != mob) {
            if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
                if (!(target instanceof StarMonolith)) {
                    return !(target instanceof MonolithSummonedEntity monolithSummon && monolithSummon.getMonolith() == mob);
                }
            }
        }
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        mob.resetAnimationTick();
        mob.setAction(StarMonolith.ACTION_SPIKE);
    }

    @Override
    public void stop() {
        mob.resetAnimationTick();
        mob.resetAction();
    }
}
