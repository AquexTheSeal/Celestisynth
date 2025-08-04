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

public class BreezebreakerGalestormAttack extends BreezebreakerAttack {

    public BreezebreakerGalestormAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_BREEZEBREAKER_NORMAL_SINGLE.get();
    }

    @Override
    public int getCooldown() {
        return buffStateModified(15);
    }

    @Override
    public int getAttackStopTime() {
        return 15;
    }

    @Override
    public boolean getCondition() {
        return !player.isSprinting() && !player.isCrouching() && player.onGround() && heldDuration < 6;
    }

    @Override
    public void startUsing() {
        super.startUsing();

        useAndDamageItem(stack, level, player, 1);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 6) {
            double range = 6.0;
            List<Entity> entities = iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 3), 1, (int) (calculateZLook(player) * 3)), range));

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        this.attributeDependentAttack(player, target, stack, 1.3F, AttackHurtTypes.REGULAR);
                        target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(level, ParticleTypes.POOF, target.blockPosition().above(), 15, 0);
                    }
                }
            }

            player.playSound(CSSoundEvents.WIND_STRIKE.get());
            CSEffectEntity.createInstance(player, null, CSVisualTypes.BREEZEBREAKER_SLASH.get(), calculateXLook(player), 0, calculateZLook(player));
            player.playSound(CSSoundEvents.AIR_SWING.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void stopUsing() {

    }
}
