package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.google.common.collect.ImmutableList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class FrostboundItem extends SkilledSwordItem {

    public FrostboundItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
        );
    }

    @Override
    public int getSkillsAmount() {
        return 0;
    }

    @Override
    public boolean hasPassive() {
        return false;
    }

    @Override
    public int getPassiveAmount() {
        return 0;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.getCapability(CSEntityCapabilityProvider.CAPABILITY).ifPresent(data -> {
            data.setFrostbound(100);
        });
        entity.playSound(SoundEvents.PLAYER_HURT_FREEZE);
        return super.hurtEnemy(itemStack, entity, source);
    }
}
