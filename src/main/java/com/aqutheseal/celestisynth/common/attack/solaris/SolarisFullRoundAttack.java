package com.aqutheseal.celestisynth.common.attack.solaris;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
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
        return !getPlayer().isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        getTagController().putInt(DIRECTION_INDEX_KEY, getPlayer().getRandom().nextInt(2));
        useAndDamageItem(getStack(), getPlayer().level, getPlayer(), 2);
    }

    @Override
    public void stopUsing() {
        getTagController().putInt(DIRECTION_INDEX_KEY, getPlayer().getRandom().nextInt(2));
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 13) {
            getPlayer().playSound(CSSoundEvents.CS_STEP.get());
            for (int i = 0; i < 15; i++) {
                Random rand = new Random();
                if (!getPlayer().level.isClientSide()) {
                    ParticleUtil.sendParticles((ServerLevel) getPlayer().level, ParticleTypes.LARGE_SMOKE, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, (-1 + rand.nextFloat(2)) * 0.5, 0.1, (-1 + rand.nextFloat(2)) * 0.5);
                }
            }
        }
        if (getTimerProgress() > 0 && getTimerProgress() < 24) {
            if (!getPlayer().level.isClientSide()) ParticleUtil.sendParticles((ServerLevel) getPlayer().level, ParticleTypes.FLAME, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, 0, 0.1, 0);

            getPlayer().setDeltaMovement(0, 0, 0);
            getPlayer().hurtMarked = true;

        } else if (getTimerProgress() > 23 && getTimerProgress() < 60) {
            BlockPos blockPosForAttack = getPlayer().blockPosition();
            int range = 4;
            List<LivingEntity> entities = getPlayer().level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPosForAttack.offset(-(range), -(range), -(range)), blockPosForAttack.offset(range, range, range)));

            for (LivingEntity target : entities) {
                if (target != getPlayer() && !getPlayer().isAlliedTo(target) && target.isAlive()) {
                    hurtNoKB(getPlayer(), target, (float) ((CSConfigManager.COMMON.solarisSkillDmg.get()) + getSharpnessValue(getStack(), 0.5F)));
                    target.setSecondsOnFire(5);
                }
            }
            getPlayer().playSound(SoundEvents.SWEET_BERRY_BUSH_BREAK);
            if (getTagController().getInt(DIRECTION_INDEX_KEY) == 0) {
                movePlayerInCircularMotion(getPlayer(), getTimerProgress(), false);
                CSEffectEntity.createInstance(getPlayer(), null, CSVisualTypes.SOLARIS_BLITZ.get());
                CSEffectEntity.createInstance(getPlayer(), null, CSVisualTypes.SOLARIS_AIR.get());
                playRandomBladeSound(getPlayer(), 4);
            } else if (getTagController().getInt(DIRECTION_INDEX_KEY) == 1) {
                movePlayerInCircularMotion(getPlayer(), getTimerProgress(), true);
                CSEffectEntity.createInstance(getPlayer(), null, CSVisualTypes.SOLARIS_BLITZ.get());
                CSEffectEntity.createInstance(getPlayer(), null, CSVisualTypes.SOLARIS_AIR.get());
                playRandomBladeSound(getPlayer(), 4);
            }

            BlockPos playerPos = getPlayer().blockPosition();
            double radius = 3;
            double particleCount = 50;
            double angleIncrement = (2 * Math.PI) / particleCount;

            for (int i = 0; i < particleCount; i++) {
                double angle = i * angleIncrement;
                double rotationX = getPlayer().level.random.nextDouble() * 360.0;
                double rotationZ = getPlayer().level.random.nextDouble() * 360.0;
                double x = playerPos.getX() + radius * Math.cos(angle);
                double y = playerPos.getY() + 1.5;
                double z = playerPos.getZ() + radius * Math.sin(angle);

                double motionX = Math.sin(Math.toRadians(rotationX)) * Math.cos(Math.toRadians(rotationZ));
                double motionY = Math.sin(Math.toRadians(rotationZ));
                double motionZ = Math.cos(Math.toRadians(rotationX)) * Math.cos(Math.toRadians(rotationZ));

                if (!getPlayer().level.isClientSide()) ParticleUtil.sendParticles((ServerLevel) getPlayer().level, ParticleTypes.FLAME, x + 0.5, y, z + 0.5, 0, motionX, motionY, motionZ);
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
        double finalX = isRight ? getPlayer().getX() + forwardX * offsetX - perpendicularX * offsetZ : getPlayer().getX() + forwardX * offsetX + perpendicularX * offsetZ;
        double finalZ = isRight ? getPlayer().getZ() + forwardZ * offsetX - perpendicularZ * offsetZ : getPlayer().getZ() + forwardZ * offsetX + perpendicularZ * offsetZ;

        getPlayer().setDeltaMovement(finalX - getPlayer().getX(), getPlayer().getDeltaMovement().y, finalZ - getPlayer().getZ());
    }
}
