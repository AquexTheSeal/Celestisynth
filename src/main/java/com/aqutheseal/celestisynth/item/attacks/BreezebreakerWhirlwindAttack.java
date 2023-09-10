package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.SkillCastBreezebreakerTornado;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BreezebreakerWhirlwindAttack extends BreezebreakerAttack {

    public BreezebreakerWhirlwindAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        if (player.getMainHandItem() == stack && player.getOffhandItem() != stack) {
            return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_RIGHT;
        } else if (player.getOffhandItem() == stack && player.getMainHandItem() != stack) {
            return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_LEFT;
        } else if (player.getOffhandItem() == stack && player.getMainHandItem() == stack) {
            boolean shouldShiftRight = player.getRandom().nextBoolean();
            if (shouldShiftRight) {
                return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_RIGHT;
            } else {
                return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_LEFT;
            }
        }
        return AnimationManager.AnimationsList.CLEAR;
    }

    @Override
    public void startUsing() {
        super.startUsing();
        useAndDamageItem(stack, player.level, player, 3);
    }

    @Override
    public int getCooldown() {
        return buffStateModified(CSConfig.COMMON.breezebreakerShiftSkillCD.get());
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return player.isCrouching();
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            player.playSound(CSSoundRegistry.CS_WIND_STRIKE.get());
            player.playSound(CSSoundRegistry.CS_WHIRLWIND.get());
            if (!player.level.isClientSide()) {
                SkillCastBreezebreakerTornado projectile = CSEntityRegistry.BREEZEBREAKER_TORNADO.get().create(player.level);
                projectile.setOwnerUuid(player.getUUID());
                projectile.setAngleX((float) calculateXLook(player));
                projectile.setAngleY((float) calculateYLook(player));
                projectile.setAngleZ((float) calculateZLook(player));
                projectile.setAddAngleX((float) calculateXLook(player));
                projectile.setAddAngleY((float) calculateYLook(player));
                projectile.setAddAngleZ((float) calculateZLook(player));
                projectile.moveTo(player.getX(), player.getY() + 1, player.getZ());
                player.level.addFreshEntity(projectile);
            }
        }
    }

    @Override
    public void stopUsing() {

    }
}
