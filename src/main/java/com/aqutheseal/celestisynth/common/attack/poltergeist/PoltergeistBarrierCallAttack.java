package com.aqutheseal.celestisynth.common.attack.poltergeist;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.mixin.LivingMixinSupport;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
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
        return CSConfigManager.COMMON.poltergeistShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 1;
    }

    @Override
    public boolean getCondition() {
        return getPlayer().isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        double range = 4;

        for (Entity entityBatch : iterateEntities(player.level, createAABB(player.blockPosition().above().offset(calculateXLook(player) * 2, 0, calculateZLook(player) * 2), range))) {
            if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                hurtNoKB(player, target, (float) (double) CSConfigManager.COMMON.poltergeistShiftSkillDmg.get() + getSharpnessValue(getStack(), 1.2F));
                target.playSound(CSSoundEvents.CS_SWORD_CLASH.get(), 0.25F, 0.5F);

                if (target instanceof LivingMixinSupport lms) lms.setPhantomTagger(player);
            } else if (entityBatch instanceof Projectile) entityBatch.remove(Entity.RemovalReason.DISCARDED);
        }

        CSEffectEntity.createInstance(player, null, CSVisualTypes.POLTERGEIST_RETREAT.get(), calculateXLook(player) * -2, 1, calculateZLook(player) * -2);
        sendExpandingParticles(player.level, ParticleTypes.SOUL, getPlayer().blockPosition(), 45, 0.5F);

        double deltaY = player.isOnGround() ? 3 : 0.9;
        getPlayer().setDeltaMovement(calculateXLook(player) * -0.5, deltaY, calculateZLook(player) * -0.5);
        getPlayer().hurtMarked = true;
        getPlayer().playSound(SoundEvents.ENDER_CHEST_OPEN, 1.0F, 1.5F);
        getPlayer().playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.5F);
        useAndDamageItem(getStack(), getPlayer().level, getPlayer(), 2);
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
    }
}
