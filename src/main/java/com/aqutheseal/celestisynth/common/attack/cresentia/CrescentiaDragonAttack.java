package com.aqutheseal.celestisynth.common.attack.cresentia;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.skill.SkillCastCrescentiaRanged;
import com.aqutheseal.celestisynth.common.item.weapons.CrescentiaItem;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CrescentiaDragonAttack extends WeaponAttackInstance {

    public CrescentiaDragonAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_CRESCENTIA_THROW;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.crescentiaShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 30;
    }

    @Override
    public boolean getCondition() {
        return player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        useAndDamageItem(getStack(), level, player, 5);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() <= 20) setDeltaPlayer(player, 0, 0, 0);

        if (getTimerProgress() == 20) {
            if (!level.isClientSide()) {
                SkillCastCrescentiaRanged cresentiaSkillCast = CSEntityTypes.CRESCENTIA_RANGED.get().create(level);

                cresentiaSkillCast.setOwnerUuid(player.getUUID());
                cresentiaSkillCast.setAngleX((float) calculateXLook(player));
                cresentiaSkillCast.setAngleY((float) calculateYLook(player));
                cresentiaSkillCast.setAngleZ((float) calculateZLook(player));
                cresentiaSkillCast.setAddAngleX((float) calculateXLook(player) / 2);
                cresentiaSkillCast.setAddAngleY((float) calculateYLook(player) / 2);
                cresentiaSkillCast.setAddAngleZ((float) calculateZLook(player) / 2);
                cresentiaSkillCast.moveTo(player.getX(), player.getY() + 1, player.getZ());

                level.addFreshEntity(cresentiaSkillCast);
            }

            for (int i = 0; i < 10; i++) {
                float offX = level.random.nextFloat() * 12 - 6;
                float offY = level.random.nextFloat() * 12 - 6;
                float offZ = level.random.nextFloat() * 12 - 6;

                CrescentiaItem.createCrescentiaFirework(getStack(), level, player, player.getX() + offX, player.getY() + offY, player.getZ() + offZ, true);

                player.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
            }
        }
    }

    @Override
    public void stopUsing() {
    }
}
