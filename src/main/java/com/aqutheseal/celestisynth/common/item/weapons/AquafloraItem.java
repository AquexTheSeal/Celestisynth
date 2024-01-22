package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.common.attack.aquaflora.AquafloraBlastOffAttack;
import com.aqutheseal.celestisynth.common.attack.aquaflora.AquafloraFlowersAwayAttack;
import com.aqutheseal.celestisynth.common.attack.aquaflora.AquafloraPetalPiercesAttack;
import com.aqutheseal.celestisynth.common.attack.aquaflora.AquafloraSlashFrenzyAttack;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import software.bernie.example.client.model.item.JackInTheBoxModel;
import software.bernie.example.client.renderer.item.JackInTheBoxRenderer;
import software.bernie.example.item.JackInTheBoxItem;

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
            if (player.getMainHandItem() == pStack) CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_SLICE.get(), 0, 1.3, 0);
            if (player.getOffhandItem() == pStack) CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_SLICE_INVERTED.get(), 0, 1.3, 0);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
