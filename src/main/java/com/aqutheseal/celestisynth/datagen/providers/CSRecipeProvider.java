package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.compat.spellbooks.ISSItemUtil;
import com.aqutheseal.celestisynth.common.recipe.celestialcrafting.CelestialShapedRecipeBuilder;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class CSRecipeProvider extends RecipeProvider {

    public CSRecipeProvider(PackOutput output) {
        super(output);
    }

    public ResourceLocation modLoc(String path) {
        return Celestisynth.prefix(path);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CSItems.CELESTIAL_CORE.get())
                .pattern(" x ").pattern("xyx").pattern(" x ")
                .define('x', Ingredient.of(Items.AMETHYST_SHARD)).define('y', Ingredient.of(csItemTag("celestial_core_bases")))
                .unlockedBy("has_item", has(Items.AMETHYST_SHARD))
                .save(consumer, modLoc("celestial_core"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CSItems.CELESTIAL_CORE.get())
                .pattern(" x ").pattern("xyx").pattern(" x ")
                .define('x', Ingredient.of(Items.AMETHYST_SHARD)).define('y', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItems.CELESTIAL_CORE_HEATED.get()))
                .save(consumer, modLoc("celestial_core_dupe"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSItems.CELESTIAL_CORE.get()), RecipeCategory.MISC, CSItems.CELESTIAL_CORE_HEATED.get(), 0.25F, 600)
                .unlockedBy("has_item", has(CSItems.CELESTIAL_CORE.get()))
                .save(consumer, modLoc("celestial_core_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSItems.CELESTIAL_CORE.get()), RecipeCategory.MISC, CSItems.CELESTIAL_CORE_HEATED.get(), 0.45F, 300)
                .unlockedBy("has_item", has(CSItems.CELESTIAL_CORE.get()))
                .save(consumer, modLoc("celestial_core_blasting"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.GHAST_TEAR), RecipeCategory.MISC, CSItems.SUPERNAL_NETHERITE_INGOT.get())
                .unlocks("has_item", has(Items.NETHERITE_INGOT))
                .save(consumer, modLoc("supernal_netherite_ingot_smithing"));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(CSItems.CELESTIAL_CORE.get()), RecipeCategory.MISC, CSItems.SUPERNAL_NETHERITE_INGOT.get())
                .unlocks("has_item", has(Items.NETHERITE_INGOT))
                .save(consumer, modLoc("supernal_netherite_ingot_smithing_from_core"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()), RecipeCategory.MISC, CSItems.CELESTIAL_NETHERITE_INGOT.get(), 0.6F, 1000)
                .unlockedBy("has_item", has(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .save(consumer, modLoc("celestial_netherite_ingot_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()), RecipeCategory.MISC, CSItems.CELESTIAL_NETHERITE_INGOT.get(), 0.75F, 500)
                .unlockedBy("has_item", has(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .save(consumer, modLoc("celestial_netherite_ingot_blasting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CSBlocks.CELESTIAL_CRAFTING_TABLE.get())
                .pattern("bnb").pattern("ncn").pattern("ooo")
                .define('b', Ingredient.of(Items.NETHERITE_INGOT)).define('n', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('c', Ingredient.of(Items.CRAFTING_TABLE)).define('o', Ingredient.of(Items.OBSIDIAN))
                .unlockedBy("has_item", has(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .save(consumer, modLoc("celestial_crafting_table"));

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CSBlocks.LUNAR_STONE.get()), RecipeCategory.MISC, CSItems.LUNAR_SCRAP.get(), 0.15F, 200)
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, modLoc("lunar_scrap_smelting"));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(CSBlocks.LUNAR_STONE.get()), RecipeCategory.MISC, CSItems.LUNAR_SCRAP.get(), 0.2F, 100)
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, modLoc("lunar_scrap_blasting"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CSItems.EYEBOMINATION.get()).requires(Items.ENDER_EYE, 4).requires(Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(Items.ENDER_EYE))
                .save(consumer, modLoc("eyebomination"));

        this.armorSetWithGold(consumer, CSBlocks.SOLAR_CRYSTAL, CSItems.SOLAR_CRYSTAL_HELMET, CSItems.SOLAR_CRYSTAL_CHESTPLATE,  CSItems.SOLAR_CRYSTAL_LEGGINGS,  CSItems.SOLAR_CRYSTAL_BOOTS);

        this.armorSetWithGold(consumer, CSBlocks.LUNAR_STONE, CSItems.LUNAR_STONE_HELMET, CSItems.LUNAR_STONE_CHESTPLATE,  CSItems.LUNAR_STONE_LEGGINGS,  CSItems.LUNAR_STONE_BOOTS);

        CelestialShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CSItems.SOLARIS.get())
                .pattern("sns").pattern("sis").pattern(" n ")
                .define('s', Ingredient.of(CSBlocks.SOLAR_CRYSTAL.get()))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlocks.SOLAR_CRYSTAL.get()))
                .save(consumer, modLoc("solaris"));

        CelestialShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CSItems.CRESCENTIA.get())
                .pattern("lll").pattern("l l").pattern("nni")
                .define('l', Ingredient.of(CSItems.LUNAR_SCRAP.get()))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlocks.LUNAR_STONE.get()))
                .save(consumer, modLoc("crescentia"));

        CelestialShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CSItems.BREEZEBREAKER.get())
                .pattern(" nz").pattern("znn").pattern("iz ")
                .define('z', Ingredient.of(CSBlocks.ZEPHYR_DEPOSIT.get()))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSBlocks.ZEPHYR_DEPOSIT.get()))
                .save(consumer, modLoc("breezebreaker"));

        CelestialShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CSItems.POLTERGEIST.get())
                .pattern("eee").pattern("ean").pattern(" ni")
                .define('e', Ingredient.of(CSItems.EYEBOMINATION.get()))
                .define('a', Ingredient.of(Items.NETHERITE_AXE))
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItems.EYEBOMINATION.get()))
                .save(consumer, modLoc("poltergeist"));

        CelestialShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CSItems.AQUAFLORA.get())
                .pattern("efi").pattern("fif").pattern("nfe")
                .define('f', Ingredient.of(ItemTags.FLOWERS))
                .define('e', Ingredient.of(Items.LILY_PAD))
                .define('n', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(Items.LILY_PAD))
                .save(consumer, modLoc("aquaflora"));

        CelestialShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CSItems.RAINFALL_SERENITY.get())
                .pattern("nfs").pattern("ibs").pattern("nfs")
                .define('b', Ingredient.of(Items.BOW))
                .define('n', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('f', Ingredient.of(CSItems.STARSTRUCK_FEATHER.get()))
                .define('s', Ingredient.of(CSItems.STARSTRUCK_SCRAP.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItems.STARSTRUCK_FEATHER.get()))
                .save(consumer, modLoc("rainfall_serenity"));

        CelestialShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CSItems.FROSTBOUND.get())
                .pattern("ssi").pattern(" ns").pattern("t  ")
                .define('n', Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()))
                .define('t', Ingredient.of(Items.IRON_INGOT))
                .define('s', Ingredient.of(CSItems.WINTEREIS_SHARD.get()))
                .define('i', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .unlockedBy("has_item", has(CSItems.WINTEREIS_SHARD.get()))
                .save(consumer, modLoc("frostbound"));

        if (CSCompatManager.checkIronsSpellbooks()) {
            ISSItemUtil.manageRecipeCompatibility(consumer, this);
        }
    }

    protected void armorSetWithGold(Consumer<FinishedRecipe> consumer, RegistryObject<? extends ItemLike> material, RegistryObject<? extends ItemLike> helmet, RegistryObject<? extends ItemLike> chestplate, RegistryObject<? extends ItemLike> leggings, RegistryObject<? extends ItemLike> boots) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet.get())
                .pattern("xgx").pattern("x x")
                .define('x', Ingredient.of(material.get())).define('g', Ingredient.of(Items.GOLD_BLOCK))
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, modLoc(material.getId().getPath() + "_helmet"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate.get())
                .pattern("x x").pattern("xgx").pattern("xxx")
                .define('x', Ingredient.of(material.get())).define('g', Ingredient.of(Items.GOLD_BLOCK))
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, modLoc(material.getId().getPath() + "_chestplate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings.get())
                .pattern("xgx").pattern("x x").pattern("x x")
                .define('x', Ingredient.of(material.get())).define('g', Ingredient.of(Items.GOLD_BLOCK))
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, modLoc(material.getId().getPath() + "_leggings"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots.get())
                .pattern("x x").pattern("xgx")
                .define('x', Ingredient.of(material.get())).define('g', Ingredient.of(Items.GOLD_BLOCK))
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, modLoc(material.getId().getPath() + "_boots"));
    }

    public TagKey<Item> csItemTag(String name) {
        return ItemTags.create(new ResourceLocation(Celestisynth.MODID,name));
    }
}
