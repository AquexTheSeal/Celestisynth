package com.aqutheseal.celestisynth.common.item.misc;

import com.aqutheseal.celestisynth.common.registry.CSItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CelestialCoreItem extends Item {

    public CelestialCoreItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(CSItems.CELESTIAL_CORE.get());
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

        if (this == CSItems.CELESTIAL_CORE_HEATED.get()) {
            if (pEntity instanceof Player player && (player.isCreative() || player.isSpectator())) return;

            pEntity.setSecondsOnFire(3);
        }
    }
}
