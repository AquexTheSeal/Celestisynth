package org.thecelestialworkshop.celestisynth.common.compat.spellbooks;

import org.thecelestialworkshop.celestisynth.common.item.weapons.*;
import org.thecelestialworkshop.celestisynth.common.registry.CSAttributes;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import org.thecelestialworkshop.celestisynth.datagen.providers.CSRecipeProvider;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.ItemAttributeModifierEvent;

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

    public static void manageCompatAttributes(ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.MAINHAND) {
            if (event.getItemStack().getItem() instanceof SolarisItem) {
                event.addModifier(AttributeRegistry.FIRE_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("35371358-49c8-4aba-acce-549e043c8c9d"), "SolFir", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
                event.addModifier(AttributeRegistry.FIRE_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("e486e288-2376-4b00-a50b-012b263907fa"), "SolFirDef", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (event.getItemStack().getItem() instanceof CrescentiaItem) {
                event.addModifier(AttributeRegistry.SPELL_RESIST.get(), new AttributeModifier(UUID.fromString("8b51ffee-4bb4-48df-9e65-8f2f5d990cc0"), "CreDef", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (event.getItemStack().getItem() instanceof BreezebreakerItem) {
                event.addModifier(AttributeRegistry.NATURE_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("80696156-ad32-4c5a-8bc1-5050d2cad359"), "BreNat", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (event.getItemStack().getItem() instanceof PoltergeistItem) {
                event.addModifier(AttributeRegistry.ENDER_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("7195a5a6-2396-41b3-b495-a08b1fb5daea"), "PolEnd", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
                event.addModifier(AttributeRegistry.ENDER_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("ac6aa20f-31cd-40f4-baf7-bb2363e52ab7"), "PolEndDef", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (event.getItemStack().getItem() instanceof AquafloraItem) {
                event.addModifier(AttributeRegistry.NATURE_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("b0f22b25-ce68-472f-8ad4-6499b150568a"), "AquNat", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
                event.addModifier(AttributeRegistry.NATURE_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("a0c35b8d-bc6d-4140-8cc6-c53ed78adc03"), "AquNatDef", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (event.getItemStack().getItem() instanceof RainfallSerenityItem) {
                event.addModifier(AttributeRegistry.SPELL_POWER.get(), new AttributeModifier(UUID.fromString("fe8c7205-2828-4df1-86a1-3a25ffcc65f2"), "RaiPow", 0.075, AttributeModifier.Operation.MULTIPLY_BASE));
                event.addModifier(AttributeRegistry.MANA_REGEN.get(), new AttributeModifier(UUID.fromString("6cae1528-8914-45c5-91b0-3e2d318932d3"), "RaiManaReg", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (event.getItemStack().getItem() instanceof FrostboundItem) {
                event.addModifier(AttributeRegistry.ICE_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("96a455d6-ca69-4214-b080-8edb8b556981"), "FroIce", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
                event.addModifier(AttributeRegistry.ICE_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("70330fba-9592-47f8-9b0c-b9831822824a"), "FroIceDef", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            if (event.getItemStack().getItem() instanceof KeresItem) {
                event.addModifier(AttributeRegistry.BLOOD_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("5ba770e7-b158-4f5c-8ae0-e0a5672939e2"), "KerBlo", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
                event.addModifier(AttributeRegistry.BLOOD_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("b7de9dc2-46f9-4ed8-9ca4-2048883c5878"), "KerBloDef", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
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
