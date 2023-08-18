package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.recipe.CelestialShapedRecipeBuilder;
import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
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
        return new ResourceLocation(Celestisynth.MODID, path);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(CSItemRegistry.CELESTIAL_CORE.get())
                .pattern(" x ").pattern("xyx").pattern(" x ")
                .define('x', Ingredient.of(Items.AMETHYST_SHARD)).define('y', Ingredient.of(csItemTag("celestial_core_bases")))
                .unlockedBy("has_item", has(Items.AMETHYST_SHARD))
                .save(consumer, this.modLoc("celestial_core"));

        ShapedRecipeBuilder.shaped(CSItemRegistry.CELESTIAL_CORE.get())
                .pattern(" x ").pattern("xyx").pattern(" x ")
                .define('x', Ingredient.of(Items.AMETHYST_SHARD)).define('y', Ingredient.of(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .save(consumer, this.modLoc("celestial_core_dupe"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSItemRegistry.CELESTIAL_CORE.get()), CSItemRegistry.CELESTIAL_CORE_HEATED.get(), 0.25F, 600)
                .unlockedBy("has_item", has(CSItemRegistry.CELESTIAL_CORE.get()))
                .save(consumer, this.modLoc("celestial_core_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSItemRegistry.CELESTIAL_CORE.get()), CSItemRegistry.CELESTIAL_CORE_HEATED.get(), 0.45F, 300)
                .unlockedBy("has_item", has(CSItemRegistry.CELESTIAL_CORE.get()))
                .save(consumer, this.modLoc("celestial_core_blasting"));

        UpgradeRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.GHAST_TEAR), CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get())
                .unlocks("has_item", has(Items.NETHERITE_INGOT))
                .save(consumer, this.modLoc("supernal_netherite_ingot_smithing"));

        UpgradeRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(CSItemRegistry.CELESTIAL_CORE.get()), CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get())
                .unlocks("has_item", has(Items.NETHERITE_INGOT))
                .save(consumer, this.modLoc("supernal_netherite_ingot_smithing_from_core"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()), CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get(), 0.6F, 1000)
                .unlockedBy("has_item", has(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()))
                .save(consumer, this.modLoc("celestial_netherite_ingot_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()), CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get(), 0.75F, 500)
                .unlockedBy("has_item", has(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()))
                .save(consumer, this.modLoc("celestial_netherite_ingot_blasting"));

        ShapedRecipeBuilder.shaped(CSBlockRegistry.CELESTIAL_CRAFTING_TABLE.get())
                .pattern("bnb").pattern("ncn").pattern("ooo")
                .define('b', Ingredient.of(Items.NETHERITE_INGOT)).define('n', Ingredient.of(CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get()))
                .define('c', Ingredient.of(Items.CRAFTING_TABLE)).define('o', Ingredient.of(Items.OBSIDIAN))
                .unlockedBy("has_item", has(CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get()))
                .save(consumer, this.modLoc("celestial_crafting_table"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSBlockRegistry.LUNAR_STONE.get()), CSItemRegistry.LUNAR_SCRAP.get(), 0.15F, 200)
                .unlockedBy("has_item", has(CSBlockRegistry.LUNAR_STONE.get()))
                .save(consumer, this.modLoc("lunar_scrap_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSBlockRegistry.LUNAR_STONE.get()), CSItemRegistry.LUNAR_SCRAP.get(), 0.2F, 100)
                .unlockedBy("has_item", has(CSBlockRegistry.LUNAR_STONE.get()))
                .save(consumer, this.modLoc("lunar_scrap_blasting"));

        ShapelessRecipeBuilder.shapeless(CSItemRegistry.EYEBOMINATION.get()).requires(Items.ENDER_EYE, 4).requires(Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(Items.ENDER_EYE))
                .save(consumer, this.modLoc("eyebomination"));

        CelestialShapedRecipeBuilder.shaped(CSItemRegistry.SOLARIS.get())
                .pattern("sns")
                .pattern("sis")
                .pattern(" n ")
                .define('s', Ingredient.of(CSBlockRegistry.SOLAR_CRYSTAL.get()))
                .define('n', Ingredient.of(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlockRegistry.SOLAR_CRYSTAL.get()))
                .save(consumer, this.modLoc("solaris"));

        CelestialShapedRecipeBuilder.shaped(CSItemRegistry.CRESCENTIA.get())
                .pattern("lll")
                .pattern("l l")
                .pattern("nni")
                .define('l', Ingredient.of(CSItemRegistry.LUNAR_SCRAP.get()))
                .define('n', Ingredient.of(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlockRegistry.LUNAR_STONE.get()))
                .save(consumer, this.modLoc("crescentia"));

        CelestialShapedRecipeBuilder.shaped(CSItemRegistry.BREEZEBREAKER.get())
                .pattern(" nz")
                .pattern("znn")
                .pattern("iz ")
                .define('z', Ingredient.of(CSBlockRegistry.ZEPHYR_DEPOSIT.get()))
                .define('n', Ingredient.of(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlockRegistry.ZEPHYR_DEPOSIT.get()))
                .save(consumer, this.modLoc("breezebreaker"));

        CelestialShapedRecipeBuilder.shaped(CSItemRegistry.POLTERGEIST.get())
                .pattern("eee")
                .pattern("ean")
                .pattern(" ni")
                .define('e', Ingredient.of(CSItemRegistry.EYEBOMINATION.get()))
                .define('a', Ingredient.of(Items.NETHERITE_AXE))
                .define('n', Ingredient.of(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItemRegistry.EYEBOMINATION.get()))
                .save(consumer, this.modLoc("poltergeist"));

        CelestialShapedRecipeBuilder.shaped(CSItemRegistry.AQUAFLORA.get())
                .pattern("efi")
                .pattern("fif")
                .pattern("nfe")
                .define('f', Ingredient.of(ItemTags.FLOWERS))
                .define('e', Ingredient.of(Items.LILY_PAD))
                .define('n', Ingredient.of(CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(Items.LILY_PAD))
                .save(consumer, this.modLoc("aquaflora"));

        CelestialShapedRecipeBuilder.shaped(CSItemRegistry.RAINFALL_SERENITY.get())
                .pattern("nfs")
                .pattern("ibs")
                .pattern("nfs")
                .define('b', Ingredient.of(Items.BOW))
                .define('n', Ingredient.of(CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get()))
                .define('f', Ingredient.of(CSItemRegistry.STARSTRUCK_FEATHER.get()))
                .define('s', Ingredient.of(CSItemRegistry.STARSTRUCK_SCRAP.get()))
                .define('i', Ingredient.of(CSItemRegistry.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItemRegistry.STARSTRUCK_FEATHER.get()))
                .save(consumer, this.modLoc("rainfall_serenity"));
    }

    public TagKey<Item> csItemTag(String name) {
        return ItemTags.create(new ResourceLocation(Celestisynth.MODID, name));
    }
}
