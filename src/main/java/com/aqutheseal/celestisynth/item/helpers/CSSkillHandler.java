package com.aqutheseal.celestisynth.item.helpers;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface CSSkillHandler extends CSWeapon {
    ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration);

}
