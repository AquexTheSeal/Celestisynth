package com.aqutheseal.celestisynth.item.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

    // I recommend utilizing this method, replacing the usage of the "inventoryTick" method, as it occasionally encounters server-client conflicts.
    default void forceTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
    }

    default void resetExtraValues(ItemStack stack, Player player) {
    }
}
