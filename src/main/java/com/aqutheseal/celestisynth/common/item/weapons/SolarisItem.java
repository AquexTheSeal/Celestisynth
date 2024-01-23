package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.solaris.SolarisFullRoundAttack;
import com.aqutheseal.celestisynth.common.attack.solaris.SolarisSoulDashAttack;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class SolarisItem extends SkilledSwordItem {

    public SolarisItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new SolarisFullRoundAttack(player, stack),
                new SolarisSoulDashAttack(player, stack)
        );
    }

    @Override
    public int getSkillsAmount() {
        return 2;
    }

    @Override
    public boolean hasPassive() {
        return true;
    }

    @Override
    public int getPassiveAmount() {
        return 1;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.setSecondsOnFire(5);
        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof SolarisItem)) player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 0));
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
    }
}
