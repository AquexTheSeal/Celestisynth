package com.aqutheseal.celestisynth.common.compat.spellbooks;

import com.aqutheseal.celestisynth.common.registry.CSAttributes;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.datagen.providers.CSRecipeProvider;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.UUID;
import java.util.function.Consumer;

public class ISSItemUtil {

    public static Multimap<Attribute, AttributeModifier> createCelestialSpellbookAttributes() {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        map.put(CSAttributes.CELESTIAL_DAMAGE.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
        map.put(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
        map.put(AttributeRegistry.MANA_REGEN.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
        return map;
    }

    public static void manageRecipeCompatibility(Consumer<FinishedRecipe> consumer, CSRecipeProvider provider) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ISSCompatItems.CELESTIAL_SPELLBOOK.get())
                .pattern("xcx").pattern("xbp").pattern("xcx")
                .define('x', Ingredient.of(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .define('c', Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get()))
                .define('b', Ingredient.of(ItemRegistry.RUINED_BOOK.get()))
                .define('p', Ingredient.of(Items.PAPER))
                .unlockedBy("has_item", has(CSItems.CELESTIAL_NETHERITE_INGOT.get()))
                .save(consumer, provider.modLoc("celestial_spellbook"));
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike) {
        return new InventoryChangeTrigger.TriggerInstance(
                ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
                new ItemPredicate[]{
                        ItemPredicate.Builder.item().of(pItemLike).build()
                }
        );
    }
}
