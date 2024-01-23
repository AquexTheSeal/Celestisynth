package com.aqutheseal.celestisynth.api.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface CSWeapon extends CSWeaponUtil {
    String ATTACK_INDEX_KEY = "cs.AttackIndex";

    int getSkillsAmount();

    default int getPassiveAmount() {
        return 1;
    }

    default boolean hasPassive() {
        return false;
    }

    default void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
    }

    default void resetExtraValues(ItemStack stack, Player player) {
    }
}
