package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.SkillCastCrescentiaRanged;
import com.aqutheseal.celestisynth.item.weapons.CrescentiaItem;
import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

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
        return CSConfig.COMMON.crescentiaShiftSkillCD.get();
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
        if (getTimerProgress() <= 20) {
            setDeltaPlayer(getPlayer(), 0, 0, 0);
        }
        if (getTimerProgress() == 20) {
            if (!getPlayer().level.isClientSide()) {
                SkillCastCrescentiaRanged projectile = CSEntityRegistry.CRESCENTIA_RANGED.get().create(getPlayer().level);
                projectile.setOwnerUuid(getPlayer().getUUID());
                projectile.setAngleX((float) calculateXLook(getPlayer()));
                projectile.setAngleY((float) calculateYLook(getPlayer()));
                projectile.setAngleZ((float) calculateZLook(getPlayer()));
                projectile.setAddAngleX((float) calculateXLook(getPlayer()) / 2);
                projectile.setAddAngleY((float) calculateYLook(getPlayer()) / 2);
                projectile.setAddAngleZ((float) calculateZLook(getPlayer()) / 2);
                projectile.moveTo(getPlayer().getX(), getPlayer().getY() + 1, getPlayer().getZ());
                getPlayer().level.addFreshEntity(projectile);
            }

            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                float offX = random.nextFloat() * 12 - 6;
                float offY = random.nextFloat() * 12 - 6;
                float offZ = random.nextFloat() * 12 - 6;
                CrescentiaItem.createCrescentiaFirework(getStack(), getPlayer().level, getPlayer(), getPlayer().getX() + offX, getPlayer().getY() + offY, getPlayer().getZ() + offZ, true);
                getPlayer().playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
            }
        }
    }

    @Override
    public void stopUsing() {
    }
}
