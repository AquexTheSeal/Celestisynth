package org.thecelestialworkshop.celestisynth.common.attack.poltergeist;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public class PoltergeistBarrierCallAttack extends WeaponAttackInstance {
    public PoltergeistBarrierCallAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_POLTERGEIST_RETREAT.get();
    }

    @Override
    public int getCooldown() {
        return 80;
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        double range = 4;

        for (Entity entityBatch : iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 2), 0, (int) (calculateZLook(player) * 2)), range))) {
            if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                this.attributeDependentAttack(player, target, stack, 0.8F, AttackHurtTypes.REGULAR);
                target.playSound(CSSoundEvents.SWORD_CLASH.get(), 0.25F, 0.5F);
                CSEntityCapabilityProvider.get(target).ifPresent(data -> {
                    data.setPhantomTag(player, 200);
                });

            } else if (entityBatch instanceof Projectile) entityBatch.remove(Entity.RemovalReason.DISCARDED);
        }

        CSEffectEntity.createInstance(player, null, CSVisualTypes.POLTERGEIST_RETREAT.get(), calculateXLook(player) * 2, 1, calculateZLook(player) * 2);
        sendExpandingParticles(level, ParticleTypes.SOUL, player.blockPosition(), 45, 0.5F);

        double deltaY = player.onGround() ? 3 : 0.9;
        player.setDeltaMovement(calculateXLook(player) * -0.7, deltaY, calculateZLook(player) * -0.7);
        player.hurtMarked = true;
        player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1.0F, 1.5F);
        player.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.5F);
        useAndDamageItem(getStack(), level, player, 2);
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
    }
}
