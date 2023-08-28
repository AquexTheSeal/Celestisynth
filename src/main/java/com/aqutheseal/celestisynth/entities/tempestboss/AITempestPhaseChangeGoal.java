package com.aqutheseal.celestisynth.entities.tempestboss;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class AITempestPhaseChangeGoal extends AITempestAbilityGoal {
    public int chargeTime;

    public AITempestPhaseChangeGoal(TempestBoss tempest) {
        super(tempest, TempestBoss.PHASE_TRANSITION_DASH_1);
    }

    @Override
    public void tickAttack(Level level) {
        if (chargeTime == 40) {
            if (!level.isClientSide()) {
                level.addFreshEntity(EntityType.LIGHTNING_BOLT.create(level));
            }
        }
        if (chargeTime >= 80) {
            tempest.cyclePhase();
            this.stop();
        }
    }
}
