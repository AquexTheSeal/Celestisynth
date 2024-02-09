package com.aqutheseal.celestisynth.common.attack.breezebreaker;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
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
        return buffStateModified(CSConfigManager.COMMON.breezebreakerMidairSkillCD.get());
    }

    @Override
    public int getAttackStopTime() {
        return 10;
    }

    @Override
    public boolean getCondition() {
        return !player.onGround();
    }

    @Override
    public void startUsing() {
        super.startUsing();
        useAndDamageItem(stack, level, player, 4);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            double range = 7.5;
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, 3).move(0, 1, 0));

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        initiateAbilityAttack(player, target, (float) (double) CSConfigManager.COMMON.breezebreakerSprintSkillDmg.get() + getSharpnessValue(stack, 1.5F), AttackHurtTypes.NO_KB_PIERCE);
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(level, ParticleTypes.POOF, target.blockPosition().above(), 45, 0);
                    }
                }
            }

            CSEffectEntity.createInstance(player, null, CSVisualTypes.BREEZEBREAKER_WHEEL.get(), 0, -1, 0);
            CSEffectEntity.createInstance(player, null, CSVisualTypes.BREEZEBREAKER_WHEEL_IMPACT.get(), calculateXLook(player) * 3, 1.5 + calculateYLook(player) * 3, calculateZLook(player) * 3);
            player.playSound(CSSoundEvents.FIRE_SHOOT.get(), 1.0F, 1.0F);
            player.playSound(CSSoundEvents.AIR_SWING.get(), 1.0F, 1.0F);
            player.playSound(CSSoundEvents.WIND_STRIKE.get());
            sendExpandingParticles(level, ParticleTypes.END_ROD, player.blockPosition().above(), 75, 0);
        }
    }

    @Override
    public void stopUsing() {

    }
}
