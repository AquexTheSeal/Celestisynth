package com.aqutheseal.celestisynth.common.attack.aquaflora;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
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
        return CSConfigManager.COMMON.aquafloraShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 0;
    }

    @Override
    public boolean getCondition() {
        return !getTagController().getBoolean(CHECK_PASSIVE) && getPlayer().isCrouching();
    }

    @Override
    public void startUsing() {
        List<Entity> surroundingEntities = iterateEntities(getPlayer().level(), createAABB(player.blockPosition().offset(calculateXLook(player) * 4, 2 + (calculateYLook(player) * 3), calculateZLook(player) * 4), 3));

        getPlayer().playSound(SoundEvents.WITHER_BREAK_BLOCK, 0.7F, 1.5F);
        CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_BASH.get(), calculateXLook(player) * 2, 1.5, calculateZLook(player) * 2);

        for (Entity entityBatch : surroundingEntities) {
            if (entityBatch instanceof LivingEntity target) {
                if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                    target.setDeltaMovement((target.getX() - getPlayer().getX()) * 0.4,   1, (target.getZ() - getPlayer().getZ()) * 0.4);
                    hurtNoKB(player, target, (float) (double) CSConfigManager.COMMON.aquafloraShiftSkillDmg.get() + getSharpnessValue(getStack(), 1F));
                    createHitEffect(getStack(), getPlayer().level(), player, target);
                    CSWeaponUtil.disableRunningWeapon(target);
                }
            }
        }

        double check = getPlayer().isOnGround() ? 0.3 : 0.14;

        if (getPlayer().level().isClientSide()) shakeScreens(player, 3, 2, 0.015F);

        getPlayer().setDeltaMovement(player.getDeltaMovement().add(calculateXLook(player) * check, 0, calculateZLook(player) * check));
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(CHECK_PASSIVE, true);
    }
}
