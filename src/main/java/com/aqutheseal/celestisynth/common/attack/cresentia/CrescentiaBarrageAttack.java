package com.aqutheseal.celestisynth.common.attack.cresentia;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.item.weapons.CrescentiaItem;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CrescentiaBarrageAttack extends WeaponAttackInstance {

    public CrescentiaBarrageAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_CRESCENTIA_STRIKE;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.crescentiaSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 70;
    }

    @Override
    public boolean getCondition() {
        return !getPlayer().isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        useAndDamageItem(getStack(), getPlayer().level(), getPlayer(), 4);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 15) getPlayer().playSound(CSSoundEvents.CS_WHIRLWIND.get(), 0.35F, 0.5F + player.level().random.nextFloat());

        if (getTimerProgress() >= 15 && getTimerProgress() <= 60) {
            double range = 7.0;
            double rangeSq = Mth.square(range);
            List<Entity> entities = getPlayer().level().getEntitiesOfClass(Entity.class, getPlayer().getBoundingBox().inflate(range, range, range).move(calculateXLook(getPlayer()), 0, calculateZLook(getPlayer())));

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != getPlayer() && target.isAlive() && !getPlayer().isAlliedTo(target) && target.distanceToSqr(getPlayer()) < rangeSq) {
                        hurtNoKB(getPlayer(), target, (float) (CSConfigManager.COMMON.crescentiaSkillDmg.get() + getSharpnessValue(getStack(), 0.25F)));
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                    }
                }
                if (entityBatch instanceof Projectile projectile) {
                    CrescentiaItem.createCrescentiaFirework(getStack(), getPlayer().level(), getPlayer(), projectile.getX(), projectile.getY(), projectile.getZ(), true);
                    getPlayer().playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F);
                    projectile.remove(Entity.RemovalReason.DISCARDED);
                }
            }

            if (getTimerProgress() % 30 == 0) getPlayer().playSound(CSSoundEvents.CS_WHIRLWIND.get(), 0.15F, 1.5F);

            if (getTimerProgress() % 3 == 0) {
                if (getPlayer().level().random.nextBoolean()) CSEffectEntity.createInstance(getPlayer(), null, CSVisualTypes.CRESCENTIA_STRIKE.get(), calculateXLook(getPlayer()), -0.3, calculateZLook(getPlayer()));
                else CSEffectEntity.createInstance(getPlayer(), null, CSVisualTypes.CRESCENTIA_STRIKE_INVERTED.get(), calculateXLook(getPlayer()), -0.3, calculateZLook(getPlayer()));

                playRandomBladeSound(getPlayer(), BASE_WEAPON_EFFECTS.length);
            }
            CSEffectEntity.createInstance(getPlayer(), null, CSVisualTypes.SOLARIS_AIR_LARGE.get(), 0, -1, 0);

            float offX = (getPlayer().level().random.nextFloat() * 16) - 8;
            float offY = (getPlayer().level().random.nextFloat() * 16) - 8;
            float offZ = (getPlayer().level().random.nextFloat() * 16) - 8;

            CrescentiaItem.createCrescentiaFirework(getStack(), getPlayer().level(), getPlayer(), getPlayer().getX() + offX, getPlayer().getY() + offY, getPlayer().getZ() + offZ, false);

            if (getPlayer().level().random.nextBoolean()) CrescentiaItem.createCrescentiaFirework(getStack(), getPlayer().level(), getPlayer(), getPlayer().getX() + offZ, getPlayer().getY() + offX, getPlayer().getZ() + offY, false);
        }
    }

    @Override
    public void stopUsing() {
    }
}
