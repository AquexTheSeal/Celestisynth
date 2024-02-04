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
        return buffStateModified(CSConfigManager.COMMON.breezebreakerSkillCD.get());
    }

    @Override
    public int getAttackStopTime() {
        return 15;
    }

    @Override
    public boolean getCondition() {
        return !player.isSprinting() && !player.isCrouching() && getPlayer().onGround() && heldDuration < 6;
    }

    @Override
    public void startUsing() {
        super.startUsing();

        useAndDamageItem(stack, getPlayer().level(), player, 1);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 6) {
            double range = 6.0;
            List<Entity> entities = iterateEntities(player.level(), createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 3), 1, (int) (calculateZLook(player) * 3)), range));

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        initiateAbilityAttack(player, target, (float) (CSConfigManager.COMMON.breezebreakerSkillDmg.get() + getSharpnessValue(stack, 1)), AttackHurtTypes.REGULAR);
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(player.level(), ParticleTypes.POOF, target.blockPosition().above(), 15, 0);
                    }
                }
            }

            getPlayer().playSound(CSSoundEvents.CS_WIND_STRIKE.get());
            CSEffectEntity.createInstance(player, null, CSVisualTypes.BREEZEBREAKER_SLASH.get(), calculateXLook(player), 0, calculateZLook(player));
            getPlayer().playSound(CSSoundEvents.CS_AIR_SWING.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void stopUsing() {

    }
}
