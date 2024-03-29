package com.aqutheseal.celestisynth.common.recipe.celestialcrafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

public interface CelestialCraftingRecipe extends Recipe<CraftingContainer> {

    @NotNull
    default ItemStack assemble(@NotNull CraftingContainer container, RegistryAccess pRegistryAccess) {
            return getResultItem(pRegistryAccess).copy();
    }

    @NotNull
    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
}
