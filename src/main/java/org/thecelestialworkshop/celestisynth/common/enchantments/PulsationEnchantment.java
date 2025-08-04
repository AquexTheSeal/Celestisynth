package org.thecelestialworkshop.celestisynth.common.enchantments;

import org.thecelestialworkshop.celestisynth.common.registry.CSDamageSources;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;

public class PulsationEnchantment extends BaseEnchantment {
    public PulsationEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }


    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        super.doPostAttack(pAttacker, pTarget, pLevel);

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

//    @Override
//    public void afterAttack(LivingEntity pAttacker, Entity pTarget, ItemStack pStack, int pLevel) {
//
//        // Prevents double-execution when both weapons in both hands have this enchantment.
//        if (pAttacker.getMainHandItem().getAllEnchantments().containsKey(this) && pAttacker.getOffhandItem().getAllEnchantments().containsKey(this)) {
//            if (pAttacker.getOffhandItem() == pStack) {
//                return;
//            }
//        }
//
//    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
