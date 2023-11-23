package com.aqutheseal.celestisynth.common.registry;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;

public class CSRecipeBookCategories {

    public static final RecipeBookCategories CELESTIAL_CRAFTING_CATEGORY = RecipeBookCategories.create("celestial_crafting", new ItemStack(CSItems.CELESTIAL_CORE.get()));
    public static final RecipeBookCategories CELESTIAL_WEAPONS_CATEGORY = RecipeBookCategories.create("celestial_weapons", new ItemStack(CSItems.SUPERNAL_NETHERITE_INGOT.get()));
}
