package org.thecelestialworkshop.celestisynth.common.attack.breezebreaker;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BreezebreakerWheelAttack extends BreezebreakerAttack {

    public BreezebreakerWheelAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_BREEZEBREAKER_JUMP_ATTACK.get();
    }

    @Override
    public int getCooldown() {
        return buffStateModified(40);
    }

    @Override
    public int getAttackStopTime() {
        return 10;
    }

    @Override
    public boolean getCondition() {
        return !player.onGround();
    }

    @Override
    public void startUsing() {
        super.startUsing();
        useAndDamageItem(stack, level, player, 4);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            double range = 7.5;
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, 3).move(0, 1, 0));

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        this.attributeDependentAttack(player, target, stack, 1.4F, AttackHurtTypes.REGULAR);
                        target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(level, ParticleTypes.POOF, target.blockPosition().above(), 45, 0);
                    }
                }
            }

            CSEffectEntity.createInstance(player, null, CSVisualTypes.BREEZEBREAKER_WHEEL.get(), 0, -1, 0);
            CSEffectEntity.createInstance(player, null, CSVisualTypes.BREEZEBREAKER_WHEEL_IMPACT.get(), calculateXLook(player) * 3, 1.5 + calculateYLook(player) * 3, calculateZLook(player) * 3);
            player.playSound(CSSoundEvents.FIRE_SHOOT.get(), 1.0F, 1.0F);
            player.playSound(CSSoundEvents.AIR_SWING.get(), 1.0F, 1.0F);
            player.playSound(CSSoundEvents.WIND_STRIKE.get());
            sendExpandingParticles(level, ParticleTypes.END_ROD, player.blockPosition().above(), 75, 0);
        }
    }

    @Override
    public void stopUsing() {

    }
}
