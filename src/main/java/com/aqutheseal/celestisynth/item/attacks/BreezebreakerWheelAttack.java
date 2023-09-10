package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BreezebreakerWheelAttack extends BreezebreakerAttack {

    public BreezebreakerWheelAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_JUMP_ATTACK;
    }

    @Override
    public int getCooldown() {
        return buffStateModified(CSConfig.COMMON.breezebreakerMidairSkillCD.get());
    }

    @Override
    public int getAttackStopTime() {
        return 25;
    }

    @Override
    public boolean getCondition() {
        if (player.isSprinting() || player.isCrouching() || player.isOnGround()) {
            return false;
        }
        return !player.isOnGround();
    }

    @Override
    public void startUsing() {
        super.startUsing();
        useAndDamageItem(stack, player.level, player, 4);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            double range = 7.5;
            List<Entity> entities = player.level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, 3).move(0, 1, 0));
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        hurtNoKB(player, target, (float) (double) CSConfig.COMMON.breezebreakerSprintSkillDmg.get() + getSharpnessValue(stack, 1.5F));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(player.level, ParticleTypes.POOF, target.blockPosition().above(), 45, 0);
                    }
                }
            }

            CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_WHEEL, 0, -1, 0);
            CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_WHEEL_IMPACT, calculateXLook(player) * 3, 1.5 + calculateYLook(player) * 3, calculateZLook(player) * 3);
            player.playSound(CSSoundRegistry.CS_FIRE_SHOOT.get(), 1.0F, 1.0F);
            player.playSound(CSSoundRegistry.CS_AIR_SWING.get(), 1.0F, 1.0F);
            player.playSound(CSSoundRegistry.CS_WIND_STRIKE.get());
            sendExpandingParticles(player.level, ParticleTypes.END_ROD, player.blockPosition().above(), 75, 0);
        }
    }

    @Override
    public void stopUsing() {

    }
}
