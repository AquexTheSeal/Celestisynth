package org.thecelestialworkshop.celestisynth.common.attack.solaris;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.util.Color;

import java.util.List;

public class SolarisSoulDashAttack extends WeaponAttackInstance {
    public static final String STARTED = "cs.hasStartedSoulDash";
    public static final String HEAD_ROT_LOCK_KEY = "cs.headRotLock";

    public SolarisSoulDashAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_SOLARIS_SPIN.get();
    }

    @Override
    public int getCooldown() {
        return 130;
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
        this.chantMessage(player, "solaris2", 30, Color.WHITE.getColor());
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(STARTED, false);
        getTagController().putFloat(HEAD_ROT_LOCK_KEY, 0);
    }

    @Override
    public void tickAttack() {
        RandomSource rand = level.random;
        if (getTimerProgress() == 13) {
            player.playSound(CSSoundEvents.STEP.get());
            for (int i = 0; i < 15; i++) {
                ParticleUtil.sendParticles(level, ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), 0, (-1 + rand.nextFloat() * 2) * 0.5, 0.1, (-1 + rand.nextFloat() * 2) * 0.5);
            }
        }

        if (getTimerProgress() == 23) {
            this.chantMessage(player, "solaris3", 20, Color.CYAN.getColor());
        }

        if (getTimerProgress() > 0 && getTimerProgress() < 24) {
            ParticleUtil.sendParticles(level, ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY(), player.getZ(), 0, -1 + rand.nextFloat() * 2, 0.1, -1 + rand.nextFloat() * 2);

            player.setDeltaMovement(0, 0, 0);
            player.hurtMarked = true;

        } else if (getTimerProgress() > 23 && getTimerProgress() < 60) {
            BlockPos blockPosForAttack = player.blockPosition();
            int range = 7;
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, AABB.encapsulatingFullBlocks(blockPosForAttack.offset(-(range), -(range), -(range)), blockPosForAttack.offset(range, range, range)));

            for (LivingEntity target : entities) {
                if (target != player && !player.isAlliedTo(target) && target.isAlive()) {
                    this.attributeDependentAttack(player, target, stack, 0.18F, AttackHurtTypes.RAPID_NO_KB);
                    target.igniteForSeconds(5);
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
