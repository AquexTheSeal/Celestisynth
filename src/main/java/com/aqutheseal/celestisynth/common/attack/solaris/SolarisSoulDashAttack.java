package com.aqutheseal.celestisynth.common.attack.solaris;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;

public class SolarisSoulDashAttack extends WeaponAttackInstance {
    public static final String STARTED = "cs.hasStartedSoulDash";
    public static final String HEAD_ROT_LOCK_KEY = "cs.headRotLock";

    public SolarisSoulDashAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_SOLARIS_SPIN;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.solarisShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 80;
    }

    @Override
    public boolean getCondition() {
        return player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        getTagController().putBoolean(STARTED, true);
        getTagController().putFloat(HEAD_ROT_LOCK_KEY, player.getYRot());
        useAndDamageItem(getStack(), level, player, 3);
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(STARTED, false);
        getTagController().putFloat(HEAD_ROT_LOCK_KEY, 0);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 13) {
            player.playSound(CSSoundEvents.STEP.get());
            for (int i = 0; i < 15; i++) {
                Random rand = new Random();

                if (!level.isClientSide()) ParticleUtil.sendParticles((ServerLevel) level, ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), 0, (-1 + rand.nextFloat(2)) * 0.5, 0.1, (-1 + rand.nextFloat(2)) * 0.5);
            }
        }
        if (getTimerProgress() > 0 && getTimerProgress() < 24) {
            if (!level.isClientSide()) {
                for (int i = 0; i < 10; i++) ParticleUtil.sendParticles((ServerLevel) level, ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY(), player.getZ(), 0, -1 + new Random().nextFloat(2), 0.1, -1 + new Random().nextFloat(2));
            }

            player.setDeltaMovement(0, 0, 0);
            player.hurtMarked = true;

        } else if (getTimerProgress() > 23 && getTimerProgress() < 60) {
            BlockPos blockPosForAttack = player.blockPosition();
            int range = 7;
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(blockPosForAttack.offset(-(range), -(range), -(range)), blockPosForAttack.offset(range, range, range)));

            for (LivingEntity target : entities) {
                if (target != player && !player.isAlliedTo(target) && target.isAlive()) {
                    initiateAbilityAttack(player, target, (float) ((CSConfigManager.COMMON.solarisShiftSkillDmg.get()) + getSharpnessValue(getStack(), 0.5F)), AttackHurtTypes.RAPID_NO_KB);
                    target.setSecondsOnFire(5);
                }
            }

            movePlayerInStraightMotion(player, getTagController().getInt(HEAD_ROT_LOCK_KEY));
            CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_BLITZ_SOUL.get(), 0, 2.5, 0);
            CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_AIR_LARGE.get());
            dashSound(0.5 + (player.getRandom().nextGaussian() / 2));
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
                if (!level.isClientSide()) {
                    ParticleUtil.sendParticles((ServerLevel) level, ParticleTypes.SOUL_FIRE_FLAME, x + 0.5, y, z + 0.5, 0, motionX, motionY, motionZ);
                }
            }
        }
    }

    private void dashSound(double pitch) {
        if (player.getRandom().nextBoolean()) {
            if (player.getRandom().nextBoolean()) {
                player.playSound(CSSoundEvents.SWORD_SWING.get(), (float) 0.2, (float) pitch);
            } else {
                player.playSound(CSSoundEvents.AIR_SWING.get(),  (float) 0.2, (float) pitch);
            }
        } else {
            if (player.getRandom().nextBoolean()) {
                player.playSound(CSSoundEvents.SWORD_SWING_FIRE.get(), (float) 0.2, (float) pitch);
            } else {
                player.playSound(CSSoundEvents.IMPACT_HIT.get(), (float) 0.2, (float) pitch);
            }
        }
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
