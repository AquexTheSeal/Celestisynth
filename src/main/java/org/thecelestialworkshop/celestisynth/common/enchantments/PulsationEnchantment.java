package org.thecelestialworkshop.celestisynth.common.enchantments;

import org.thecelestialworkshop.celestisynth.common.registry.CSDamageSources;
import org.thecelestialworkshop.celestisynth.common.registry.CSDamageTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSEnchantments;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

/**
 * Pulsation is data-driven as of 1.21 (see {@code data/celestisynth/enchantment/pulsation.json});
 * this class carries the custom attack effect, fired from the damage event pipeline.
 */
public final class PulsationEnchantment {

    private PulsationEnchantment() {
    }

    /** Mirrors the doPostAttack trigger of the 1.20.1 code-based enchantment. */
    public static void onAttack(LivingEntity pAttacker, Entity pTarget, DamageSource source) {
        if (pAttacker.level().isClientSide) return;
        if (!(source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK)) || source.is(CSDamageTypes.PULSATION)) return;

        var holder = CSEnchantments.getHolder(pAttacker.level().registryAccess(), CSEnchantments.PULSATION);
        if (holder.isEmpty()) return;

        int pLevel = EnchantmentHelper.getItemEnchantmentLevel(holder.get(), pAttacker.getMainHandItem());
        if (pLevel <= 0) return;

        doPostAttack(pAttacker, pTarget, pLevel);
    }

    public static void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        int rngData = pAttacker.getRandom().nextInt(3) + 1;
        if (rngData <= pLevel) {
            Vec3 targetPos = new Vec3(pTarget.getX(), pAttacker.getEyeY(), pTarget.getZ());
            Vec3 distPos = targetPos.subtract(pAttacker.getEyePosition());
            double xRandom = pAttacker.getRandom().nextGaussian() * 0.1;
            double yRandom = pAttacker.getRandom().nextGaussian() * 0.1;
            double zRandom = pAttacker.getRandom().nextGaussian() * 0.1;
            Vec3 finalPos = targetPos.subtract(distPos.scale(0.25)).add(xRandom, yRandom, zRandom);
            ParticleUtil.sendParticle(pAttacker.level(), CSParticleTypes.PULSATION.get(), finalPos.x(), finalPos.y(), finalPos.z());
            pTarget.hurt(CSDamageSources.instance(pTarget.level()).pulsation(pAttacker), 4 + ((pLevel - 1) * 1.2F));
            pTarget.playSound(CSSoundEvents.SWORD_CLASH.get(), 0.5F, 1 + (float) (pAttacker.getRandom().nextGaussian() * 0.25));
            if (pTarget instanceof LivingEntity living) {
                living.knockback(1 + ((pLevel - 1) * 0.50), Mth.sin(pAttacker.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(pAttacker.getYRot() * Mth.DEG_TO_RAD));
            }
        }
    }
}
