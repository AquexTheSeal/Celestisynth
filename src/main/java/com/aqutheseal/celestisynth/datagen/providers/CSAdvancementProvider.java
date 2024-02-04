package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class CSAdvancementProvider extends ForgeAdvancementProvider {

    public CSAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new CSItemAdvancements()));
    }

    public static class CSItemAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
            Advancement rootObtainedCore = Advancement.Builder.advancement().display(CSItems.CELESTIAL_CORE.get(), Component.translatable("advancement.celestisynth.obtain_celestial_core.title"), Component.translatable("advancement.celestisynth.obtain_celestial_core.description"), Celestisynth.prefix("textures/block/zephyr_deposit.png"), FrameType.TASK, false, true, false).addCriterion("obtained_core", InventoryChangeTrigger.TriggerInstance.hasItems(CSItems.CELESTIAL_CORE.get())).save(consumer, "celestisynth:root");

            Advancement getSupernalIngot = this.obtainItem(consumer, existingFileHelper, rootObtainedCore, CSItems.SUPERNAL_NETHERITE_INGOT, FrameType.TASK, false);

            Advancement obtainedCelestialIngot = this.obtainItem(consumer, existingFileHelper, getSupernalIngot, CSItems.CELESTIAL_NETHERITE_INGOT, FrameType.TASK, false);

            Advancement placeCelestialTable = this.createAdvancement(consumer, existingFileHelper, obtainedCelestialIngot, "place_celestial_table", FrameType.CHALLENGE, true, CSBlocks.CELESTIAL_CRAFTING_TABLE, EnterBlockTrigger.TriggerInstance::entersBlock);

            CSItems.ITEMS.getEntries().stream().filter(weapon -> weapon.get() instanceof CSWeapon).forEach(item -> this.obtainItem(consumer, existingFileHelper, placeCelestialTable, item, FrameType.GOAL, false));
        }

        private Advancement obtainItem(Consumer<Advancement> dataSaver, ExistingFileHelper existingFileHelper, Advancement parent, RegistryObject<? extends Item> require, FrameType frameType, boolean toChat) {
            return createAdvancement(dataSaver, existingFileHelper, parent, "obtain_" + require.getId().getPath(), frameType, toChat, require, InventoryChangeTrigger.TriggerInstance::hasItems);
        }

        private <ToCheck extends ItemLike, Trigger extends CriterionTriggerInstance> Advancement createAdvancement(Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper, Advancement parent, String saveName, FrameType frame, boolean toChat, RegistryObject<ToCheck> toCheck, Function<ToCheck, Trigger> triggerFactory) {
            return createAdvancement(consumer, existingFileHelper, parent, saveName, toCheck.get(), frame, toChat, toCheck.get(), triggerFactory);
        }

        private <ToCheck, Trigger extends CriterionTriggerInstance> Advancement createAdvancement(Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper, Advancement parent, String saveName, ItemLike displayIcon, FrameType frame, boolean toChat, ToCheck toCheck, Function<ToCheck, Trigger> triggerFactory) {
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
