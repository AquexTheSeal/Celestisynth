package com.aqutheseal.celestisynth.common.entity.tempestboss;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public abstract class AITempestAbilityGoal extends Goal {
    public final TempestBoss tempest;
    public final int attackId;

    public int chargeTime;

    public AITempestAbilityGoal(TempestBoss tempest, int attackId) {
        this.tempest = tempest;
        this.attackId = attackId;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return tempest.getAttackState() == attackId;
    }

    public void start() {
        this.chargeTime = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = tempest.getTarget();
        if (target != null) {
            Level level = tempest.level;
            ++this.chargeTime;
            if (target.distanceToSqr(tempest) < 4096.0D) {
                tickAttack(level);
            } else if (this.chargeTime > 0) {
                this.chargeTime = 0;
            }
        }
    }

    public abstract void tickAttack(Level level);

    @Override
    public void stop() {
        tempest.setAttackState(TempestBoss.NONE);
    }
}
