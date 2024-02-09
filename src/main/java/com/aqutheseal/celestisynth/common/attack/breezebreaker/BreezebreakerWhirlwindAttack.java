package com.aqutheseal.celestisynth.common.attack.breezebreaker;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.entity.skill.SkillCastBreezebreakerTornado;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BreezebreakerWhirlwindAttack extends BreezebreakerAttack {

    public BreezebreakerWhirlwindAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        if (player.getMainHandItem() == stack && player.getOffhandItem() != stack) return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_RIGHT;
        else if (player.getOffhandItem() == stack && player.getMainHandItem() != stack) return AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_LEFT;
        else if (player.getOffhandItem() == stack && player.getMainHandItem() == stack) {
            boolean shouldShiftRight = player.getRandom().nextBoolean();

            return shouldShiftRight ? AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_RIGHT : AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_LEFT;
        }
        return AnimationManager.AnimationsList.CLEAR;
    }

    @Override
    public void startUsing() {
        super.startUsing();

        useAndDamageItem(stack, level, player, 3);
    }

    @Override
    public int getCooldown() {
        return buffStateModified(CSConfigManager.COMMON.breezebreakerShiftSkillCD.get());
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
            player.playSound(CSSoundEvents.CS_WIND_STRIKE.get());
            player.playSound(CSSoundEvents.CS_WHIRLWIND.get());

            if (!level.isClientSide()) {
                SkillCastBreezebreakerTornado tornadoSkillCast = CSEntityTypes.BREEZEBREAKER_TORNADO.get().create(level);

                tornadoSkillCast.setOwnerUuid(player.getUUID());
                tornadoSkillCast.setAngleX((float) calculateXLook(player));
                tornadoSkillCast.setAngleY((float) calculateYLook(player));
                tornadoSkillCast.setAngleZ((float) calculateZLook(player));
                tornadoSkillCast.setAddAngleX((float) calculateXLook(player));
                tornadoSkillCast.setAddAngleY((float) calculateYLook(player));
                tornadoSkillCast.setAddAngleZ((float) calculateZLook(player));
                tornadoSkillCast.moveTo(player.getX(), player.getY() + 1, player.getZ());

                level.addFreshEntity(tornadoSkillCast);
            }
        }
    }

    @Override
    public void stopUsing() {

    }
}
