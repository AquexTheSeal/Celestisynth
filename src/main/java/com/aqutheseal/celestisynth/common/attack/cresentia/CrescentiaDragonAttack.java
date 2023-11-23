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
        return getPlayer().isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        useAndDamageItem(getStack(), getPlayer().level, getPlayer(), 5);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() <= 20) setDeltaPlayer(getPlayer(), 0, 0, 0);

        if (getTimerProgress() == 20) {
            if (!player.level.isClientSide()) {
                SkillCastCrescentiaRanged cresentiaSkillCast = CSEntityTypes.CRESCENTIA_RANGED.get().create(getPlayer().level);

                cresentiaSkillCast.setOwnerUuid(getPlayer().getUUID());
                cresentiaSkillCast.setAngleX((float) calculateXLook(getPlayer()));
                cresentiaSkillCast.setAngleY((float) calculateYLook(getPlayer()));
                cresentiaSkillCast.setAngleZ((float) calculateZLook(getPlayer()));
                cresentiaSkillCast.setAddAngleX((float) calculateXLook(getPlayer()) / 2);
                cresentiaSkillCast.setAddAngleY((float) calculateYLook(getPlayer()) / 2);
                cresentiaSkillCast.setAddAngleZ((float) calculateZLook(getPlayer()) / 2);
                cresentiaSkillCast.moveTo(getPlayer().getX(), getPlayer().getY() + 1, getPlayer().getZ());

                getPlayer().level.addFreshEntity(cresentiaSkillCast);
            }

            for (int i = 0; i < 10; i++) {
                float offX = getPlayer().level.random.nextFloat() * 12 - 6;
                float offY = getPlayer().level.random.nextFloat() * 12 - 6;
                float offZ = getPlayer().level.random.nextFloat() * 12 - 6;

                CrescentiaItem.createCrescentiaFirework(getStack(), getPlayer().level, getPlayer(), getPlayer().getX() + offX, getPlayer().getY() + offY, getPlayer().getZ() + offZ, true);

                getPlayer().playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
            }
        }
    }

    @Override
    public void stopUsing() {
    }
}
