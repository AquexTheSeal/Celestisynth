package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
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
        return buffStateModified(CSConfig.COMMON.breezebreakerSprintSkillCD.get());
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return player.isSprinting();
    }

    @Override
    public void startUsing() {
        super.startUsing();
        useAndDamageItem(stack, player.level, player, 5);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            sendExpandingParticles(player.level, ParticleTypes.CAMPFIRE_COSY_SMOKE, player.blockPosition(), 45, 0.2F);

            Entity lookAtTarget = getLookAtEntity(player, 16);
            LivingEntity living = lookAtTarget instanceof LivingEntity entity ? entity : null;
            if (living != null) {
                double attackDamage = CSConfig.COMMON.breezebreakerSprintSkillDmg.get() + getSharpnessValue(stack, 1);
                hurtNoKB(player, living, (float) attackDamage);
                player.level.explode(player, living.getX(), living.getY(), living.getZ(), 1.0F, Explosion.BlockInteraction.NONE);
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                sendExpandingParticles(player.level, ParticleTypes.FIREWORK, player.blockPosition().above(), 45, 0.2F);
            }

            double speed = 7;
            Vec3 delta;
            for (float distii = 0; distii < speed; distii += 0.25F) {
                BlockPos newPos = new BlockPos(player.getX() + calculateXLook(player) * distii, player.getY(), player.getZ() + calculateZLook(player) * distii);
                if (!player.level.isEmptyBlock(newPos)) {
                    speed = distii;
                    break;
                }
            }
            delta = new Vec3(calculateXLook(player) * speed, 0, calculateZLook(player) * speed);
            player.moveTo(player.getX() + calculateXLook(player) * speed, player.getY(), player.getZ() + calculateZLook(player) * speed);

            double[] multipliers = {2, 1.5, 1, 0.5, 0};
            CSEffectTypes[] effectTypes = {CSEffectTypes.BREEZEBREAKER_DASH, CSEffectTypes.BREEZEBREAKER_DASH_2, CSEffectTypes.BREEZEBREAKER_DASH_3, CSEffectTypes.BREEZEBREAKER_DASH_3, CSEffectTypes.BREEZEBREAKER_DASH_3};
            for (int i = 0; i < multipliers.length; i++) {
                int yOffset = i > 1 ? 1 : 0;
                CSEffect.createInstance(player, null, effectTypes[i], delta.x() * multipliers[i], yOffset, delta.z() * multipliers[i]);
            }

            player.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
            player.playSound(CSSoundRegistry.CS_IMPACT_HIT.get(), 1.0F, 1.0F);
            player.playSound(CSSoundRegistry.CS_STEP.get(), 1.0F, 1.0F);

        }
    }

    @Override
    public void stopUsing() {

    }
}
