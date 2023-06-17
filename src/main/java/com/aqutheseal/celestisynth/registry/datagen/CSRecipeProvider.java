package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class CSRecipeProvider extends RecipeProvider {

    public CSRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(CSItemRegistry.SOLARIS.get())
                .pattern("sns")
                .pattern("sns")
                .pattern(" i ")
                .define('s', Ingredient.of(CSBlockRegistry.SOLAR_CRYSTAL.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlockRegistry.SOLAR_CRYSTAL.get()))
                .save(consumer, "solaris");

        ShapedRecipeBuilder.shaped(CSItemRegistry.CRESCENTIA.get())
                .pattern("lnl")
                .pattern("l n")
                .pattern("i  ")
                .define('l', Ingredient.of(CSBlockRegistry.LUNAR_STONE.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlockRegistry.LUNAR_STONE.get()))
                .save(consumer, "crescentia");

        ShapedRecipeBuilder.shaped(CSItemRegistry.BREEZEBREAKER.get())
                .pattern(" nz")
                .pattern("znn")
                .pattern("iz ")
                .define('z', Ingredient.of(CSBlockRegistry.ZEPHYR_DEPOSIT.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlockRegistry.LUNAR_STONE.get()))
                .save(consumer, "breezebreaker");
    }
}
