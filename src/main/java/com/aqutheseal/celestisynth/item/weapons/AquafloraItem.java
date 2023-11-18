package com.aqutheseal.celestisynth.item.weapons;

import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.SkilledSwordItem;
import com.aqutheseal.celestisynth.item.attacks.AquafloraBlastOffAttack;
import com.aqutheseal.celestisynth.item.attacks.AquafloraFlowersAwayAttack;
import com.aqutheseal.celestisynth.item.attacks.AquafloraPetalPiercesAttack;
import com.aqutheseal.celestisynth.item.attacks.AquafloraSlashFrenzyAttack;
import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class AquafloraItem extends SkilledSwordItem {

    public AquafloraItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration) {
        return ImmutableList.of(
                new AquafloraPetalPiercesAttack(player, stack, useDuration),
                new AquafloraBlastOffAttack(player, stack, useDuration),
                new AquafloraSlashFrenzyAttack(player, stack, useDuration),
                new AquafloraFlowersAwayAttack(player, stack, useDuration)
        );
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
    public int getSkillsAmount() {
        return 4;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player) {
            if (player.getMainHandItem() == pStack) {
                CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_SLICE, 0, 1.3, 0);
            }
            if (player.getOffhandItem() == pStack) {
                CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_SLICE_INVERTED, 0, 1.3, 0);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
