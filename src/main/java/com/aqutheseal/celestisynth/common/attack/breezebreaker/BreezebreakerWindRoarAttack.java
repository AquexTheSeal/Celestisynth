package com.aqutheseal.celestisynth.common.attack.breezebreaker;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public class BreezebreakerWindRoarAttack extends BreezebreakerAttack {

    public BreezebreakerWindRoarAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SPRINT_ATTACK;
    }

    @Override
    public int getCooldown() {
        return buffStateModified(CSConfigManager.COMMON.breezebreakerSprintSkillCD.get());
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return getPlayer().isSprinting();
    }

    @Override
    public void startUsing() {
        super.startUsing();
        
        useAndDamageItem(stack, getPlayer().level(), player, 5);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            sendExpandingParticles(player.level(), ParticleTypes.CAMPFIRE_COSY_SMOKE, getPlayer().blockPosition(), 45, 0.2F);

            Entity lookAtTarget = getLookedAtEntity(player, 16);
            LivingEntity observedLivingTarget = lookAtTarget instanceof LivingEntity entity ? entity : null;
            
            if (observedLivingTarget != null) {
                double attackDamage = CSConfigManager.COMMON.breezebreakerSprintSkillDmg.get() + getSharpnessValue(stack, 1);

                hurtNoKB(player, observedLivingTarget, (float) attackDamage);
                getPlayer().level().explode(player, observedLivingTarget.getX(), observedLivingTarget.getY(), observedLivingTarget.getZ(), 1.0F, Explosion.BlockInteraction.NONE);
                observedLivingTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2));
                observedLivingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                sendExpandingParticles(player.level(), ParticleTypes.FIREWORK, getPlayer().blockPosition().above(), 45, 0.2F);
            }

            double speed = 7;
            Vec3 delta;

            for (float distii = 0; distii < speed; distii += 0.25F) {
                BlockPos newPos = new BlockPos(player.getX() + calculateXLook(player) * distii, getPlayer().getY(), getPlayer().getZ() + calculateZLook(player) * distii);

                if (!player.level().isEmptyBlock(newPos)) {
                    speed = distii;
                    break;
                }
            }

            delta = new Vec3(calculateXLook(player) * speed, 0, calculateZLook(player) * speed);
            getPlayer().moveTo(player.getX() + calculateXLook(player) * speed, getPlayer().getY(), getPlayer().getZ() + calculateZLook(player) * speed);

            double[] multipliers = {2, 1.5, 1, 0.5, 0};
            CSVisualType[] effectTypes = {
                    CSVisualTypes.BREEZEBREAKER_DASH.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_2.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_3.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_3.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_3.get()
            };

            for (int i = 0; i < multipliers.length; i++) {
                int yOffset = i > 1 ? 1 : 0;

                CSEffectEntity.createInstance(player, null, effectTypes[i], delta.x() * multipliers[i], yOffset, delta.z() * multipliers[i]);
            }

            getPlayer().playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
            getPlayer().playSound(CSSoundEvents.CS_IMPACT_HIT.get(), 1.0F, 1.0F);
            getPlayer().playSound(CSSoundEvents.CS_STEP.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void stopUsing() {

    }
}
