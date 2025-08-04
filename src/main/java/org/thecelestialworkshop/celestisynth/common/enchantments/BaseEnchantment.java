package org.thecelestialworkshop.celestisynth.common.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.Set;

public class BaseEnchantment extends Enchantment {
    public BaseEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    /**
     * Alternative method for {@link Enchantment#doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel)}
     * with {@link net.minecraft.world.item.ItemStack} parameter.
     */
    public void afterAttack(LivingEntity pAttacker, Entity pTarget, ItemStack pStack, int pLevel) {
    }

    @Override
    public boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories) {
        return false;
    }
}
