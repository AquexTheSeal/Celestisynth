package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.recipe.celestialcrafting.CelestialShapedRecipeBuilder;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class CSRecipeProvider extends RecipeProvider {

    public CSRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    private ResourceLocation modLoc(String path) {
        return Celestisynth.prefix(path);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(CSItems.CELESTIAL_CORE.get())
                .pattern(" x ").pattern("xyx").pattern(" x ")
                .define('x', Ingredient.of(Items.AMETHYST_SHARD)).define('y', Ingredient.of(csItemTag("celestial_core_bases")))
                .unlockedBy("has_item", has(Items.AMETHYST_SHARD))
                .save(consumer, modLoc("celestial_core"));

        ShapedRecipeBuilder.shaped(CSItems.CELESTIAL_CORE.get())
                .pattern(" x ").pattern("xyx").pattern(" x ")
                .define('x', Ingredient.of(Items.AMETHYST_SHARD)).define('y', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItems.CELESTIAL_CORE_HEATED.get()))
                .save(consumer, modLoc("celestial_core_dupe"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSItems.CELESTIAL_CORE.get()), CSItems.CELESTIAL_CORE_HEATED.get(), 0.25F, 600)
                .unlockedBy("has_item", has(CSItems.CELESTIAL_CORE.get()))
                .save(consumer, modLoc("celestial_core_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSItems.CELESTIAL_CORE.get()), CSItems.CELESTIAL_CORE_HEATED.get(), 0.45F, 300)
                .unlockedBy("has_item", has(CSItems.CELESTIAL_CORE.get()))
                .save(consumer, modLoc("celestial_core_blasting"));

        UpgradeRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.GHAST_TEAR), CSItems.SUPERNAL_NETHERITE_INGOT.get())
                .unlocks("has_item", has(Items.NETHERITE_INGOT))
                .save(consumer, modLoc("supernal_netherite_ingot_smithing"));

        UpgradeRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(CSItems.CELESTIAL_CORE.get()), CSItems.SUPERNAL_NETHERITE_INGOT.get())
                .unlocks("has_item", has(Items.NETHERITE_INGOT))
                .save(consumer, modLoc("supernal_netherite_ingot_smithing_from_core"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()), CSItems.CELESTIAL_NETHERITE_INGOT.get(), 0.6F, 1000)
                .unlockedBy("has_item", has(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .save(consumer, modLoc("celestial_netherite_ingot_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()), CSItems.CELESTIAL_NETHERITE_INGOT.get(), 0.75F, 500)
                .unlockedBy("has_item", has(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .save(consumer, modLoc("celestial_netherite_ingot_blasting"));

        ShapedRecipeBuilder.shaped(CSBlocks.CELESTIAL_CRAFTING_TABLE.get())
                .pattern("bnb").pattern("ncn").pattern("ooo")
                .define('b', Ingredient.of(Items.NETHERITE_INGOT)).define('n', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('c', Ingredient.of(Items.CRAFTING_TABLE)).define('o', Ingredient.of(Items.OBSIDIAN))
                .unlockedBy("has_item", has(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .save(consumer, modLoc("celestial_crafting_table"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSBlocks.LUNAR_STONE.get()), CSItems.LUNAR_SCRAP.get(), 0.15F, 200)
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, modLoc("lunar_scrap_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSBlocks.LUNAR_STONE.get()), CSItems.LUNAR_SCRAP.get(), 0.2F, 100)
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, modLoc("lunar_scrap_blasting"));

        ShapelessRecipeBuilder.shapeless(CSItems.EYEBOMINATION.get()).requires(Items.ENDER_EYE, 4).requires(Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(Items.ENDER_EYE))
                .save(consumer, modLoc("eyebomination"));

        CelestialShapedRecipeBuilder.shaped(CSItems.SOLARIS.get())
                .pattern("sns")
                .pattern("sis")
                .pattern(" n ")
                .define('s', Ingredient.of(CSBlocks.SOLAR_CRYSTAL.get()))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlocks.SOLAR_CRYSTAL.get()))
                .save(consumer, modLoc("solaris"));

        CelestialShapedRecipeBuilder.shaped(CSItems.CRESCENTIA.get())
                .pattern("lll")
                .pattern("l l")
                .pattern("nni")
                .define('l', Ingredient.of(CSItems.LUNAR_SCRAP.get()))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, modLoc("crescentia"));

        CelestialShapedRecipeBuilder.shaped(CSItems.BREEZEBREAKER.get())
                .pattern(" nz")
                .pattern("znn")
                .pattern("iz ")
                .define('z', Ingredient.of(CSBlocks.ZEPHYR_DEPOSIT.get()))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlocks.ZEPHYR_DEPOSIT.get()))
                .save(consumer, modLoc("breezebreaker"));

        CelestialShapedRecipeBuilder.shaped(CSItems.POLTERGEIST.get())
                .pattern("eee")
                .pattern("ean")
                .pattern(" ni")
                .define('e', Ingredient.of(CSItems.EYEBOMINATION.get()))
                .define('a', Ingredient.of(Items.NETHERITE_AXE))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItems.EYEBOMINATION.get()))
                .save(consumer, modLoc("poltergeist"));

        CelestialShapedRecipeBuilder.shaped(CSItems.AQUAFLORA.get())
                .pattern("efi")
                .pattern("fif")
                .pattern("nfe")
                .define('f', Ingredient.of(ItemTags.FLOWERS))
                .define('e', Ingredient.of(Items.LILY_PAD))
                .define('n', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(Items.LILY_PAD))
                .save(consumer, modLoc("aquaflora"));

        CelestialShapedRecipeBuilder.shaped(CSItems.RAINFALL_SERENITY.get())
                .pattern("nfs")
                .pattern("ibs")
                .pattern("nfs")
                .define('b', Ingredient.of(Items.BOW))
                .define('n', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('f', Ingredient.of(CSItems.STARSTRUCK_FEATHER.get()))
                .define('s', Ingredient.of(CSItems.STARSTRUCK_SCRAP.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItems.STARSTRUCK_FEATHER.get()))
                .save(consumer, modLoc("rainfall_serenity"));
    }

    public TagKey<Item> csItemTag(String name) {
        return ItemTags.create(new ResourceLocation(Celestisynth.MODID,name));
    }
}