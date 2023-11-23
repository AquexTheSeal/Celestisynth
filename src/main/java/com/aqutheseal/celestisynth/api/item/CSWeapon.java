package com.aqutheseal.celestisynth.api.item;

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

    /**
     * Forcefully ticks the specified item in the entity's inventory, with side-proofing.
     *
     * @param itemStack The target {@link ItemStack} to tick.
     * @param level The level in which the target {@link ItemStack} is being ticked.
     * @param entity The entity's inventory in which the target {@link ItemStack} is being ticked.
     * @param itemSlot The target slot in which the target {@link ItemStack} is being ticked.
     * @param isSelected Whether the target {@link ItemStack} is being ticked in the entity's hand (I.E. if it's currently being held in either hand).
     */
    default void forceTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
    }

    default void resetExtraValues(ItemStack stack, Player player) {
    }
}
