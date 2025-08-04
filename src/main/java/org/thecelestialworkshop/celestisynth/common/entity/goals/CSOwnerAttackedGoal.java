package org.thecelestialworkshop.celestisynth.common.entity.goals;

import org.thecelestialworkshop.celestisynth.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

public class CSOwnerAttackedGoal<T extends Mob & OwnableEntity> extends TargetGoal {
    private final T ownable;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public CSOwnerAttackedGoal(T ownable) {
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
                this.ownerLastHurtBy = livingentity.getLastHurtByMob();
                int i = livingentity.getLastHurtByMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, EntityUtil.isValidTargetForOwnableCondition(ownable));
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        LivingEntity livingentity = this.ownable.getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}
