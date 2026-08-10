package org.thecelestialworkshop.celestisynth.common.attack.breezebreaker;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.entity.helper.CSVisualType;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BreezebreakerWindRoarAttack extends BreezebreakerAttack {

    public BreezebreakerWindRoarAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_BREEZEBREAKER_SPRINT_ATTACK.get();
    }

    @Override
    public int getCooldown() {
        return buffStateModified(15);
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return player.isSprinting();
    }

    @Override
    public void startUsing() {
        super.startUsing();
        
        useAndDamageItem(stack, level, player, 5);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            sendExpandingParticles(level, ParticleTypes.CAMPFIRE_COSY_SMOKE, player.blockPosition(), 45, 0.01F);

            for (LivingEntity target : this.getEntitiesInLine(player, 10)) {
                if (target != player) {
                    double targetDist = target.distanceTo(player);
                    this.attributeDependentAttack(player, target, stack, 2F + ((float) targetDist * 0.075F), AttackHurtTypes.NO_KB);
                    target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.WEAKNESS, 60, 2));
                    target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                    sendExpandingParticles(level, ParticleTypes.FIREWORK, player.blockPosition().above(), 45, 0.2F);
                }
            }

            double speed = 7;
            Vec3 delta;
            for (float distii = 0; distii < speed; distii += 0.25F) {
                BlockPos newPos = new BlockPos((int) (player.getX() + calculateXLook(player) * distii), (int) player.getY(), (int) (player.getZ() + calculateZLook(player) * distii));
                if (!level.isEmptyBlock(newPos)) {
                    speed = distii;
                    break;
                }
            }
            delta = new Vec3(calculateXLook(player) * speed, 0, calculateZLook(player) * speed);
            player.moveTo(player.getX() + calculateXLook(player) * speed, player.getY(), player.getZ() + calculateZLook(player) * speed);

            double[] multipliers = {2, 1.5, 1, 0.5, 0};
            CSVisualType[] effectTypes = {
                    CSVisualTypes.BREEZEBREAKER_DASH.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_2.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_3.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_3.get(),
                    CSVisualTypes.BREEZEBREAKER_DASH_3.get()
            };

            for (int i = 0; i < multipliers.length; i++) {
                int yOffset = i > 1 ? 1 : 0;

                CSEffectEntity.createInstance(player, null, effectTypes[i], delta.x() * multipliers[i], yOffset, delta.z() * multipliers[i]);
            }

            player.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.0F, 1.5F);
            player.playSound(CSSoundEvents.IMPACT_HIT.get(), 1.0F, 1.0F);
            player.playSound(CSSoundEvents.STEP.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void stopUsing() {

    }
}
