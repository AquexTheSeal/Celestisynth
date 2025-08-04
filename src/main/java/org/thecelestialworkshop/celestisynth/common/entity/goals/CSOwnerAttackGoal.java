package org.thecelestialworkshop.celestisynth.common.entity.goals;

import org.thecelestialworkshop.celestisynth.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

public class CSOwnerAttackGoal<T extends Mob & OwnableEntity> extends TargetGoal {
    private final T ownable;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public CSOwnerAttackGoal(T ownable) {
        super(ownable, false);
        this.ownable = ownable;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        if (this.ownable.getOwner() != null) {
            LivingEntity livingentity = this.ownable.getOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.ownerLastHurt = livingentity.getLastHurtMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurt, EntityUtil.isValidTargetForOwnableCondition(ownable));
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity livingentity = this.ownable.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
