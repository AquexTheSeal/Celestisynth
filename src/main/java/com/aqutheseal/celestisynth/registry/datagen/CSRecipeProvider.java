package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class CSRecipeProvider extends RecipeProvider {

    public CSRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CSItemRegistry.SOLARIS.get())
                .pattern("sns")
                .pattern("sns")
                .pattern(" i ")
                .define('s', Ingredient.of(CSBlockRegistry.SOLAR_CRYSTAL.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlockRegistry.SOLAR_CRYSTAL.get()))
                .save(consumer, "solaris");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CSItemRegistry.CRESCENTIA.get())
                .pattern("lnl")
                .pattern(" ln")
                .pattern("i l")
                .define('l', Ingredient.of(CSBlockRegistry.LUNAR_STONE.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlockRegistry.LUNAR_STONE.get()))
                .save(consumer, "crescentia");
    }
}
