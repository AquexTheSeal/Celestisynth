package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSWeaponUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AquafloraBlastOffAttack extends AquafloraAttack {
    public AquafloraBlastOffAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_AQUAFLORA_BASH;
    }

    @Override
    public int getCooldown() {
        return CSConfig.COMMON.aquafloraShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 0;
    }

    @Override
    public boolean getCondition() {
        return !getTagController().getBoolean(CHECK_PASSIVE) && player.isCrouching();
    }

    @Override
    public void startUsing() {
        List<Entity> entities = iterateEntities(getPlayer().level, createAABB(player.blockPosition().offset(calculateXLook(player) * 4, 2 + (calculateYLook(player) * 3), calculateZLook(player) * 4), 3));
        player.playSound(SoundEvents.WITHER_BREAK_BLOCK, 0.7F, 1.5F);
        CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_BASH, calculateXLook(player) * 2, 1.5, calculateZLook(player) * 2);
        for (Entity entityBatch : entities) {
            if (entityBatch instanceof LivingEntity target) {
                if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                    target.setDeltaMovement((target.getX() - player.getX()) * 0.4,   1, (target.getZ() - player.getZ()) * 0.4);
                    hurtNoKB(player, target, (float) (double) CSConfig.COMMON.aquafloraShiftSkillDmg.get() + getSharpnessValue(getStack(), 1F));
                    createHitEffect(getStack(), getPlayer().level, player, target);
                    CSWeaponUtil.disableRunningWeapon(target);
                }
            }
        }
        double check = player.isOnGround() ? 0.3 : 0.14;
        if (getPlayer().level.isClientSide()) {
            shakeScreens(player, 3, 2, 0.015F);
        }
        player.setDeltaMovement(player.getDeltaMovement().add(calculateXLook(player) * check, 0, calculateZLook(player) * check));
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(CHECK_PASSIVE, true);
    }
}
