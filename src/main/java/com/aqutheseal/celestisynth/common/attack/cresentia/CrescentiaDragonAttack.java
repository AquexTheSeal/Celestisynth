package com.aqutheseal.celestisynth.common.attack.cresentia;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.projectile.CrescentiaDragon;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
        if (getTimerProgress() <= 20) {
            setDeltaPlayer(player, 0, 0, 0);
        }

        if (getTimerProgress() == 20) {
            FloatArrayList angles = new FloatArrayList();
            angles.add(0.0F);
            angles.add(-30.0F);
            angles.add(30.0F);
            if (!level.isClientSide()) {
                for (float i : angles) {
                    CrescentiaDragon dragon = new CrescentiaDragon(CSEntityTypes.CRESCENTIA_DRAGON.get(), player, level);
                    dragon.moveTo(player.getX(), dragon.getY(), player.getZ());
                    Vec3 vec31 = player.getUpVector(1.0F);
                    Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(i * ((float) Math.PI / 180F), vec31.x, vec31.y, vec31.z);
                    Vec3 vec3 = player.getViewVector(1.0F);
                    Vector3f shootAngle = vec3.toVector3f().rotate(quaternionf);
                    dragon.shoot(shootAngle.x, shootAngle.y, shootAngle.z, 1.5F, 0);
                    level.addFreshEntity(dragon);
                }
            }
            player.playSound(SoundEvents.ELDER_GUARDIAN_CURSE, 1F, 2F + (float) (level.random.nextGaussian() * 0.25F));
            player.playSound(SoundEvents.ENDER_DRAGON_HURT, 1F, 0.5F + (float) (level.random.nextGaussian() * 0.25F));
            player.playSound(CSSoundEvents.WHIRLWIND.get(), 0.2F, 0.5F + (float) (level.random.nextGaussian() * 0.25F));
        }
    }

    @Override
    public void stopUsing() {
    }
}
