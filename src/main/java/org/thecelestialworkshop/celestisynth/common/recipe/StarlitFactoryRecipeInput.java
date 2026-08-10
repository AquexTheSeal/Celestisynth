package org.thecelestialworkshop.celestisynth.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

/**
 * Snapshot of the six Starlit Factory material slots, in recipe order
 * (core x3 then supporting x3).
 */
public record StarlitFactoryRecipeInput(List<ItemStack> items) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }
}
