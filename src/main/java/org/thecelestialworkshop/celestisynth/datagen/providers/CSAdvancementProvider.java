package org.thecelestialworkshop.celestisynth.datagen.providers;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.api.item.CSWeapon;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.AdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class CSAdvancementProvider extends AdvancementProvider {

    public CSAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new CSItemAdvancements()));
    }

    public static class CSItemAdvancements implements AdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            AdvancementHolder rootObtainedCore = Advancement.Builder.advancement().display(CSItems.CELESTIAL_CORE.get(), Component.translatable("advancement.celestisynth.obtain_celestial_core.title"), Component.translatable("advancement.celestisynth.obtain_celestial_core.description"), Celestisynth.prefix("textures/block/zephyr_deposit.png"), AdvancementType.TASK, false, true, false).addCriterion("obtained_core", InventoryChangeTrigger.TriggerInstance.hasItems(CSItems.CELESTIAL_CORE.get())).save(consumer, ResourceLocation.parse("celestisynth:root"), existingFileHelper);

            AdvancementHolder getSupernalIngot = this.obtainItem(consumer, existingFileHelper, rootObtainedCore, CSItems.SUPERNAL_NETHERITE_INGOT, AdvancementType.TASK, false);

            AdvancementHolder obtainedCelestialIngot = this.obtainItem(consumer, existingFileHelper, getSupernalIngot, CSItems.CELESTIAL_NETHERITE_INGOT, AdvancementType.TASK, false);

            AdvancementHolder placeCelestialTable = this.createAdvancement(consumer, existingFileHelper, obtainedCelestialIngot, "place_celestial_table", AdvancementType.CHALLENGE, true, CSBlocks.CELESTIAL_CRAFTING_TABLE, EnterBlockTrigger.TriggerInstance::entersBlock);

            CSItems.ITEMS.getEntries().stream().filter(weapon -> weapon.get() instanceof CSWeapon).forEach(item -> this.obtainItem(consumer, existingFileHelper, placeCelestialTable, item, AdvancementType.GOAL, false));
        }

        private AdvancementHolder obtainItem(Consumer<AdvancementHolder> dataSaver, ExistingFileHelper existingFileHelper, AdvancementHolder parent, Supplier<? extends Item> require, AdvancementType frameType, boolean toChat) {
            String path = BuiltInRegistries.ITEM.getKey(require.get()).getPath();
            return createAdvancement(dataSaver, existingFileHelper, parent, "obtain_" + path, frameType, toChat, require, InventoryChangeTrigger.TriggerInstance::hasItems);
        }

        private <ToCheck extends ItemLike> AdvancementHolder createAdvancement(Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper, AdvancementHolder parent, String saveName, AdvancementType frame, boolean toChat, Supplier<? extends ToCheck> toCheck, Function<ToCheck, Criterion<?>> triggerFactory) {
            return createAdvancement(consumer, existingFileHelper, parent, saveName, toCheck.get(), frame, toChat, toCheck.get(), triggerFactory);
        }

        private <ToCheck> AdvancementHolder createAdvancement(Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper, AdvancementHolder parent, String saveName, ItemLike displayIcon, AdvancementType frame, boolean toChat, ToCheck toCheck, Function<ToCheck, Criterion<?>> triggerFactory) {
            ResourceLocation fileName = Celestisynth.prefix(saveName);
            String languageKey = fileName.toLanguageKey("advancement");
            return Advancement.Builder
                    .advancement()
                    .parent(parent)
                    .display(displayIcon, Component.translatable(languageKey + ".title"), Component.translatable(languageKey + ".description"), null, frame, true, toChat, false)
                    .addCriterion("criteria", triggerFactory.apply(toCheck))
                    .save(consumer, fileName, existingFileHelper);
        }
    }
}
