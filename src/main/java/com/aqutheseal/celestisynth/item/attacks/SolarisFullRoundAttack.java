package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
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
    public static final String HEAD_ROT_LOCK_KEY = "cs.headRotLock";
    
    public SolarisFullRoundAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_SOLARIS_SPIN;
    }

    @Override
    public int getCooldown() {
        return getPlayer().isShiftKeyDown() ? CSConfig.COMMON.solarisShiftSkillCD.get() : CSConfig.COMMON.solarisSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 80;
    }

    @Override
    public boolean getCondition() {
        return true;
    }

    @Override
    public void startUsing() {
        if (getPlayer().isShiftKeyDown()) {
            getTagController().putInt(DIRECTION_INDEX_KEY, 2);
            getTagController().putFloat(HEAD_ROT_LOCK_KEY, getPlayer().getYRot());
            useAndDamageItem(getStack(), getPlayer().level, getPlayer(), 2);
        } else {
            getTagController().putInt(DIRECTION_INDEX_KEY, getPlayer().getRandom().nextInt(2));
            useAndDamageItem(getStack(), getPlayer().level, getPlayer(), 3);
        }
    }

    @Override
    public void stopUsing() {
        getTagController().putInt(DIRECTION_INDEX_KEY, getPlayer().getRandom().nextInt(2));
        getTagController().putFloat(HEAD_ROT_LOCK_KEY, 0);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 13) {
            getPlayer().playSound(CSSoundRegistry.CS_STEP.get());
            for (int i = 0; i < 15; i++) {
                Random rand = new Random();
                if (!getPlayer().level.isClientSide()) {
                    CSUtilityFunctions.sendParticles((ServerLevel) getPlayer().level, ParticleTypes.LARGE_SMOKE, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, (-1 + rand.nextFloat(2)) * 0.5, 0.1, (-1 + rand.nextFloat(2)) * 0.5);
                }
            }
        }
        if (getTimerProgress() > 0 && getTimerProgress() < 24) {
            if (!getPlayer().level.isClientSide()) {
                if (getTagController().getInt(DIRECTION_INDEX_KEY) == 2) {
                    for (int i = 0; i < 10; i++) {
                        CSUtilityFunctions.sendParticles((ServerLevel) getPlayer().level, ParticleTypes.SOUL_FIRE_FLAME, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, -1 + new Random().nextFloat(2), 0.1, -1 + new Random().nextFloat(2));
                    }
                } else {
                    CSUtilityFunctions.sendParticles((ServerLevel) getPlayer().level, ParticleTypes.FLAME, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 0, 0, 0.1, 0);
                }
            }
            getPlayer().setDeltaMovement(0, 0, 0);
            getPlayer().hurtMarked = true;

        } else if (getTimerProgress() > 23 && getTimerProgress() < 60) {
            BlockPos blockPosForAttack = getPlayer().blockPosition();
            boolean isStraight = getTagController().getInt(DIRECTION_INDEX_KEY) == 2;
            int range = isStraight ? 7 : 4;
            List<LivingEntity> entities = getPlayer().level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPosForAttack.offset(-(range), -(range), -(range)), blockPosForAttack.offset(range, range, range)));
            for (LivingEntity target : entities) {
                if (target != getPlayer() && !getPlayer().isAlliedTo(target) && target.isAlive()) {
                    hurtNoKB(getPlayer(), target, (float) ((isStraight ? CSConfig.COMMON.solarisShiftSkillDmg.get() : CSConfig.COMMON.solarisSkillDmg.get()) + getSharpnessValue(getStack(), 0.5F)));
                    target.setSecondsOnFire(5);
                }
            }
            getPlayer().playSound(SoundEvents.SWEET_BERRY_BUSH_BREAK);
            if (getTagController().getInt(DIRECTION_INDEX_KEY) == 2) {
                movePlayerInStraightMotion(getPlayer(), getTagController().getInt(HEAD_ROT_LOCK_KEY));
                CSEffect.createInstance(getPlayer(), null, CSEffectTypes.SOLARIS_BLITZ_SOUL);
                CSEffect.createInstance(getPlayer(), null, CSEffectTypes.SOLARIS_AIR_LARGE);
                playRandomBladeSound(getPlayer(), BASE_WEAPON_EFFECTS.length);
            } else if (getTagController().getInt(DIRECTION_INDEX_KEY) == 0) {
                movePlayerInCircularMotion(getPlayer(), getTimerProgress(), false);
                CSEffect.createInstance(getPlayer(), null, CSEffectTypes.SOLARIS_BLITZ);
                CSEffect.createInstance(getPlayer(), null, CSEffectTypes.SOLARIS_AIR);
                playRandomBladeSound(getPlayer(), 4);
            } else if (getTagController().getInt(DIRECTION_INDEX_KEY) == 1) {
                movePlayerInCircularMotion(getPlayer(), getTimerProgress(), true);
                CSEffect.createInstance(getPlayer(), null, CSEffectTypes.SOLARIS_BLITZ);
                CSEffect.createInstance(getPlayer(), null, CSEffectTypes.SOLARIS_AIR);
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

                if (!getPlayer().level.isClientSide()) {
                    CSUtilityFunctions.sendParticles((ServerLevel) getPlayer().level, getTagController().getInt(DIRECTION_INDEX_KEY) == 2 ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, x + 0.5, y, z + 0.5, 0, motionX, motionY, motionZ);
                    if (getTagController().getInt(DIRECTION_INDEX_KEY) == 2) {
                        CSUtilityFunctions.sendParticles((ServerLevel) getPlayer().level, ParticleTypes.SOUL, x + 0.5, y, z + 0.5, 0, motionX, motionY, motionZ);
                    }
                }
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

    private void movePlayerInStraightMotion(Player player, float yRot) {
        double speed = 1.5;
        double lookX = -Math.sin(Math.toRadians(yRot));
        double lookZ = Math.cos(Math.toRadians(yRot));
        double motionX = lookX * speed;
        double motionZ = lookZ * speed;
        player.setDeltaMovement(motionX, player.getDeltaMovement().y, motionZ);
    }
}
