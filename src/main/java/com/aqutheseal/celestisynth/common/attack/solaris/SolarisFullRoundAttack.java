package com.aqutheseal.celestisynth.common.attack.solaris;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;

public class SolarisFullRoundAttack extends WeaponAttackInstance {
    public static final String DIRECTION_INDEX_KEY = "cs.directionIndex";
    
    public SolarisFullRoundAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_SOLARIS_SPIN;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.solarisSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 80;
    }

    @Override
    public boolean getCondition() {
        return !player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        getTagController().putInt(DIRECTION_INDEX_KEY, player.getRandom().nextInt(2));
        useAndDamageItem(getStack(), level, player, 2);
    }

    @Override
    public void stopUsing() {
        getTagController().putInt(DIRECTION_INDEX_KEY, player.getRandom().nextInt(2));
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 13) {
            player.playSound(CSSoundEvents.CS_STEP.get());
            for (int i = 0; i < 15; i++) {
                Random rand = new Random();
                ParticleUtil.sendParticles(level, ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), 0, (-1 + rand.nextFloat(2)) * 0.5, 0.1, (-1 + rand.nextFloat(2)) * 0.5);
            }
        }
        if (getTimerProgress() > 0 && getTimerProgress() < 24) {
            ParticleUtil.sendParticles(level, ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), 0, 0, 0.1, 0);
            player.setDeltaMovement(0, 0, 0);
            player.hurtMarked = true;

        } else if (getTimerProgress() > 23 && getTimerProgress() < 60) {
            BlockPos blockPosForAttack = player.blockPosition();
            int range = 4;
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPosForAttack.offset(-(range), -(range), -(range)), blockPosForAttack.offset(range, range, range)));

            for (LivingEntity target : entities) {
                if (target != player && !player.isAlliedTo(target) && target.isAlive()) {
                    initiateAbilityAttack(player, target, (float) ((CSConfigManager.COMMON.solarisSkillDmg.get()) + getSharpnessValue(getStack(), 0.5F)), AttackHurtTypes.RAPID);
                    target.setSecondsOnFire(5);
                }
            }
            player.playSound(SoundEvents.SWEET_BERRY_BUSH_BREAK);
            if (getTagController().getInt(DIRECTION_INDEX_KEY) == 0) {
                movePlayerInCircularMotion(player, getTimerProgress(), false);
                CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_BLITZ.get());
                CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_AIR.get());
                playRandomBladeSound(player, 4);
            } else if (getTagController().getInt(DIRECTION_INDEX_KEY) == 1) {
                movePlayerInCircularMotion(player, getTimerProgress(), true);
                CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_BLITZ.get());
                CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_AIR.get());
                playRandomBladeSound(player, 4);
            }

            BlockPos playerPos = player.blockPosition();
            double radius = 3;
            double particleCount = 50;
            double angleIncrement = (2 * Math.PI) / particleCount;

            for (int i = 0; i < particleCount; i++) {
                double angle = i * angleIncrement;
                double rotationX = level.random.nextDouble() * 360.0;
                double rotationZ = level.random.nextDouble() * 360.0;
                double x = playerPos.getX() + radius * Math.cos(angle);
                double y = playerPos.getY() + 1.5;
                double z = playerPos.getZ() + radius * Math.sin(angle);
                double motionX = Math.sin(Math.toRadians(rotationX)) * Math.cos(Math.toRadians(rotationZ));
                double motionY = Math.sin(Math.toRadians(rotationZ));
                double motionZ = Math.cos(Math.toRadians(rotationX)) * Math.cos(Math.toRadians(rotationZ));
                if (!level.isClientSide()) ParticleUtil.sendParticles((ServerLevel) level, ParticleTypes.FLAME, x + 0.5, y, z + 0.5, 0, motionX, motionY, motionZ);
            }
        }
    }

    private void movePlayerInCircularMotion(Player player, int tick, boolean isRight) {
        double radius = 1.5;
        double forwardX = Math.sin(Math.toRadians(player.getYRot()));
        double forwardZ = -Math.cos(Math.toRadians(player.getYRot()));
        double perpendicularX = -forwardZ;
        double perpendicularZ = forwardX;
        double angle = (tick - 45) / 25.0 * Math.PI * 2.0;
        double offsetX = radius * Math.cos(angle);
        double offsetZ = radius * Math.sin(angle);
        double finalX = isRight ? player.getX() + forwardX * offsetX - perpendicularX * offsetZ : player.getX() + forwardX * offsetX + perpendicularX * offsetZ;
        double finalZ = isRight ? player.getZ() + forwardZ * offsetX - perpendicularZ * offsetZ : player.getZ() + forwardZ * offsetX + perpendicularZ * offsetZ;

        player.setDeltaMovement(finalX - player.getX(), player.getDeltaMovement().y, finalZ - player.getZ());
    }
}
