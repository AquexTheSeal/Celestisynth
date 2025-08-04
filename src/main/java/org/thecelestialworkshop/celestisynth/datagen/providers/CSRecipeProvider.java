package org.thecelestialworkshop.celestisynth.datagen.providers;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.compat.spellbooks.ISSItemUtil;
import org.thecelestialworkshop.celestisynth.common.recipe.StarlitFactoryRecipeBuilder;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import org.thecelestialworkshop.celestisynth.manager.CSIntegrationManager;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, CSBlocks.STARLIT_FACTORY.get())
                .requires(CSBlocks.CELESTIAL_CRAFTING_TABLE.get())
                .unlockedBy("has_item", has(Blocks.CRAFTING_TABLE))
                .save(consumer, modLoc("starlit_factory_update_fix"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, CSBlocks.STARLIT_FACTORY.get())
                .pattern("ini")
                .pattern("scb")
                .pattern("ooo")
                .define('n', Ingredient.of(Items.IRON_BLOCK))
                .define('i', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('c', Ingredient.of(Items.CRAFTING_TABLE))
                .define('b', Ingredient.of(Items.BLAST_FURNACE))
                .define('s', Ingredient.of(Items.SMITHING_TABLE))
                .define('o', Ingredient.of(Items.OBSIDIAN))
                .unlockedBy("has_item", has(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .save(consumer, modLoc("starlit_factory"));

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

        StarlitFactoryRecipeBuilder.starlitFactory(
                Ingredient.of(CSBlocks.SOLAR_CRYSTAL.get()), Ingredient.of(CSBlocks.SOLAR_CRYSTAL.get()), Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()),
                Ingredient.of(Items.IRON_SWORD), Ingredient.of(Items.FIRE_CHARGE), Ingredient.of(Items.BLAZE_ROD),
                CSItems.SOLARIS.get(), 500
        ).save(consumer, modLoc("solaris"));

        StarlitFactoryRecipeBuilder.starlitFactory(
                Ingredient.of(CSItems.LUNAR_SCRAP.get()), Ingredient.of(CSItems.LUNAR_SCRAP.get()), Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()),
                Ingredient.of(Items.TRIDENT), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.IRON_INGOT),
                CSItems.CRESCENTIA.get(), 500
        ).save(consumer, modLoc("crescentia"));

        StarlitFactoryRecipeBuilder.starlitFactory(
                    Ingredient.of(CSBlocks.ZEPHYR_DEPOSIT.get()), Ingredient.of(CSBlocks.ZEPHYR_DEPOSIT.get()), Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()),
                Ingredient.of(Items.GHAST_TEAR), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.IRON_INGOT),
                CSItems.BREEZEBREAKER.get(), 700
        ).save(consumer, modLoc("breezebreaker"));

        StarlitFactoryRecipeBuilder.starlitFactory(
                Ingredient.of(CSItems.EYEBOMINATION.get()), Ingredient.of(CSItems.EYEBOMINATION.get()), Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()),
                Ingredient.of(Items.ANVIL), Ingredient.of(Items.ANVIL), Ingredient.of(Items.IRON_AXE),
                CSItems.POLTERGEIST.get(), 700
        ).save(consumer, modLoc("poltergeist"));

        StarlitFactoryRecipeBuilder.starlitFactory(
                Ingredient.of(ItemTags.FLOWERS), Ingredient.of(Items.LILY_PAD), Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()),
                Ingredient.of(Items.PEARLESCENT_FROGLIGHT), Ingredient.of(Items.OCHRE_FROGLIGHT), Ingredient.of(Items.VERDANT_FROGLIGHT),
                CSItems.AQUAFLORA.get(), 400
        ).save(consumer, modLoc("aquaflora"));

        StarlitFactoryRecipeBuilder.starlitFactory(
                Ingredient.of(CSItems.STARSTRUCK_SCRAP.get()), Ingredient.of(Items.BOW), Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()),
                Ingredient.of(CSItems.STARSTRUCK_FEATHER.get()), Ingredient.of(CSItems.STARSTRUCK_FEATHER.get()), Ingredient.of(CSItems.STARSTRUCK_SCRAP.get()),
                CSItems.RAINFALL_SERENITY.get(), 825
        ).save(consumer, modLoc("rainfall_serenity"));

        StarlitFactoryRecipeBuilder.starlitFactory(
                Ingredient.of(CSItems.WINTEREIS_SHARD.get()), Ingredient.of(Items.DIAMOND_HOE), Ingredient.of(CSItems.SUPERNAL_NETHERITE_INGOT.get()),
                Ingredient.of(CSItems.WINTEREIS_SHARD.get()), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.PACKED_ICE),
                CSItems.FROSTBOUND.get(), 400
        ).save(consumer, modLoc("frostbound"));

        StarlitFactoryRecipeBuilder.starlitFactory(
                Ingredient.of(CSItems.CRISMSON_PIECE.get()), Ingredient.of(CSItems.CRISMSON_PIECE.get()), Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()),
                Ingredient.of(Items.NETHERITE_INGOT), Ingredient.of(Items.NETHER_WART_BLOCK), Ingredient.of(Items.NETHERITE_INGOT),
                CSItems.KERES.get(), 800
        ).save(consumer, modLoc("keres"));

        if (CSIntegrationManager.checkIronsSpellbooks()) {
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
