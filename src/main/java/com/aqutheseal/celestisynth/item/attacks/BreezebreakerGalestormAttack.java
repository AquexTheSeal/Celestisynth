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

public class BreezebreakerGalestormAttack extends BreezebreakerAttack {

    public BreezebreakerGalestormAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_NORMAL_SINGLE;
    }

    @Override
    public int getCooldown() {
        return buffStateModified(CSConfig.COMMON.breezebreakerSkillCD.get());
    }

    @Override
    public int getAttackStopTime() {
        return 15;
    }

    @Override
    public boolean getCondition() {
        if (player.isSprinting() || player.isCrouching() || !player.isOnGround()) {
            return false;
        }
        return heldDuration < 6;
    }

    @Override
    public void startUsing() {
        super.startUsing();
        useAndDamageItem(stack, player.level, player, 1);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 6) {
            double range = 6.0;
            List<Entity> entities = iterateEntities(player.level, createAABB(player.blockPosition().offset(calculateXLook(player) * 3, 1, calculateZLook(player) * 3), range));
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        hurtNoKB(player, target, (float) (CSConfig.COMMON.breezebreakerSkillDmg.get() + getSharpnessValue(stack, 1)));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(player.level, ParticleTypes.POOF, target.blockPosition().above(), 15, 0);
                    }
                }
            }

            player.playSound(CSSoundRegistry.CS_WIND_STRIKE.get());
            CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_SLASH, calculateXLook(player), 0, calculateZLook(player));
            player.playSound(CSSoundRegistry.CS_AIR_SWING.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void stopUsing() {

    }
}
