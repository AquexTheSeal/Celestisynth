package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.LivingMixinSupport;
import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public class PoltergeistBarrierCallAttack extends WeaponAttackInstance {
    public PoltergeistBarrierCallAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_POLTERGEIST_RETREAT;
    }

    @Override
    public int getCooldown() {
        return CSConfig.COMMON.poltergeistShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 5;
    }

    @Override
    public boolean getCondition() {
        return getPlayer().isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        double range = 4;
        for (Entity entityBatch : iterateEntities(player.level, createAABB(player.blockPosition().above(), range))) {
            if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                hurtNoKB(player, target, (float) (double) CSConfig.COMMON.poltergeistShiftSkillDmg.get() + getSharpnessValue(getStack(), 1.2F));
                target.playSound(CSSoundRegistry.CS_SWORD_CLASH.get(), 0.25F, 0.5F);
                if (target instanceof LivingMixinSupport lms) {
                    lms.setPhantomTagger(player);
                }
            } else if (entityBatch instanceof Projectile) {
                entityBatch.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        CSEffect.createInstance(player, null, CSEffectTypes.POLTERGEIST_RETREAT, calculateXLook(player) * 2, 1, calculateZLook(player) * 2);
        sendExpandingParticles(player.level, ParticleTypes.SOUL, player.blockPosition(), 45, 0.5F);

        double multiplier = player.isOnGround() ? -2 : -1.5;
        player.setDeltaMovement(calculateXLook(player) * multiplier, 0, calculateZLook(player) * multiplier);
        player.hurtMarked = true;
        player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1.0F, 1.5F);
        player.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.5F);
        useAndDamageItem(getStack(), getPlayer().level, getPlayer(), 2);
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
    }
}
