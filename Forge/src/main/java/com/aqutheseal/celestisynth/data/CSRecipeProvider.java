package com.aqutheseal.celestisynth.data;

import com.aqutheseal.celestisynth.common.block.CSBlocks;
import com.aqutheseal.celestisynth.common.item.CSItems;
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
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CSItems.SOLARIS.get())
                .pattern("sns")
                .pattern("sns")
                .pattern(" i ")
                .define('s', Ingredient.of(CSBlocks.SOLAR_CRYSTAL.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlocks.SOLAR_CRYSTAL.get()))
                .save(consumer, "solaris");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CSItems.CRESCENTIA.get())
                .pattern("lnl")
                .pattern("l n")
                .pattern("i  ")
                .define('l', Ingredient.of(CSBlocks.LUNAR_STONE.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, "crescentia");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CSItems.BREEZEBREAKER.get())
                .pattern(" nz")
                .pattern("znn")
                .pattern("iz ")
                .define('z', Ingredient.of(CSBlocks.ZEPHYR_DEPOSIT.get()))
                .define('n', Ingredient.of(Items.NETHERITE_INGOT))
                .define('i', Ingredient.of(Items.IRON_SWORD))
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, "breezebreaker");
    }
}
