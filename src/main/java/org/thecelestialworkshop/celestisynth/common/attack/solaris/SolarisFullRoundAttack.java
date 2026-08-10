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
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.util.Color;

import java.util.List;

public class SolarisFullRoundAttack extends WeaponAttackInstance {
    public static final String DIRECTION_INDEX_KEY = "cs.directionIndex";
    
    public SolarisFullRoundAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_SOLARIS_SPIN.get();
    }

    @Override
    public int getCooldown() {
        return 70;
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
        this.chantMessage(player, "solaris", 30, Color.WHITE.getColor());
    }

    @Override
    public void stopUsing() {
        getTagController().putInt(DIRECTION_INDEX_KEY, player.getRandom().nextInt(2));
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 13) {
            player.playSound(CSSoundEvents.STEP.get());
            for (int i = 0; i < 15; i++) {
                RandomSource rand = level.random;
                ParticleUtil.sendParticles(level, ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), 0, (-1 + rand.nextFloat() * 2) * 0.5, 0.1, (-1 + rand.nextFloat() * 2) * 0.5);
            }
        }

        if (getTimerProgress() == 23) {
            this.chantMessage(player, "solaris1", 20, Color.ORANGE.getColor());
        }

        if (getTimerProgress() > 0 && getTimerProgress() < 24) {
            ParticleUtil.sendParticles(level, ParticleTypes.FLAME, player.getX(), player.getY(), player.getZ(), 0, 0, 0.1, 0);
            player.setDeltaMovement(0, 0, 0);
            player.hurtMarked = true;

        } else if (getTimerProgress() > 23 && getTimerProgress() < 60) {
            BlockPos blockPosForAttack = player.blockPosition();
            int range = 4;
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, AABB.encapsulatingFullBlocks(blockPosForAttack.offset(-(range), -(range), -(range)), blockPosForAttack.offset(range, range, range)));
            for (LivingEntity target : entities) {
                if (target != player && !player.isAlliedTo(target) && target.isAlive()) {
                    this.attributeDependentAttack(player, target, stack, 0.23F, AttackHurtTypes.RAPID_NO_KB);
                    target.igniteForSeconds(5);
                }
            }
            if (getTagController().getInt(DIRECTION_INDEX_KEY) == 0) {
                movePlayerInCircularMotion(player, getTimerProgress(), false);
            } else if (getTagController().getInt(DIRECTION_INDEX_KEY) == 1) {
                movePlayerInCircularMotion(player, getTimerProgress(), true);
            }

            CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_BLITZ.get(), 0, 2.5, 0);
            CSEffectEntity.createInstance(player, null, CSVisualTypes.SOLARIS_AIR.get());
            dashSound(1 + (player.getRandom().nextGaussian() / 2));

            BlockPos playerPos = player.blockPosition();
            double radius = 3;
            double particleCount = 30;
            double angleIncrement = (2 * Math.PI) / particleCount;

            for (int i = 0; i < particleCount; i++) {
                double angle = i * angleIncrement;
                double rotationX = level.random.nextDouble() * 360;
                double rotationZ = level.random.nextDouble() * 360;
                double x = playerPos.getX() + radius * Math.cos(angle);
                double y = playerPos.getY() + 1.5;
                double z = playerPos.getZ() + radius * Math.sin(angle);
                double motionX = Math.sin(Math.toRadians(rotationX)) * Math.cos(Math.toRadians(rotationZ));
                double motionY = Math.sin(Math.toRadians(rotationZ));
                double motionZ = Math.cos(Math.toRadians(rotationX)) * Math.cos(Math.toRadians(rotationZ));
                if (!level.isClientSide()) ParticleUtil.sendParticles(level, ParticleTypes.FLAME, x + 0.5, y, z + 0.5, 0, motionX, motionY, motionZ);
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
