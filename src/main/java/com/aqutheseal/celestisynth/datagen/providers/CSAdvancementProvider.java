package com.aqutheseal.celestisynth.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CSAdvancementProvider extends ForgeAdvancementProvider {

    public CSAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
        super(output, registries, existingFileHelper, subProviders);
    }

/**
    public CSAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement rootObtainedCore = Advancement.Builder.advancement().display(CSItems.CELESTIAL_CORE.get(), Component.translatable("advancement.celestisynth.obtain_celestial_core.title"), Component.translatable("advancement.celestisynth.obtain_celestial_core.description"), Celestisynth.prefix("textures/block/zephyr_deposit.png"), FrameType.TASK, false, true, false).addCriterion("obtained_core", InventoryChangeTrigger.TriggerInstance.hasItems(CSItems.CELESTIAL_CORE.get())).save(consumer, "celestisynth:root");

        Advancement getSupernalIngot = this.obtainItem(consumer, rootObtainedCore, CSItems.SUPERNAL_NETHERITE_INGOT, FrameType.TASK, false);

        Advancement obtainedCelestialIngot = this.obtainItem(consumer, getSupernalIngot, CSItems.CELESTIAL_NETHERITE_INGOT, FrameType.TASK, false);

        Advancement placeCelestialTable = this.createAdvancement(consumer, obtainedCelestialIngot, "place_celestial_table", FrameType.CHALLENGE, true, CSBlocks.CELESTIAL_CRAFTING_TABLE, EnterBlockTrigger.TriggerInstance::entersBlock);

        CSItems.ITEMS.getEntries().stream().filter(weapon -> weapon.get() instanceof CSWeapon)
                .forEach(item -> this.obtainItem(consumer, placeCelestialTable, item, FrameType.GOAL, false));
    }


    private Advancement obtainItem(Consumer<Advancement> dataSaver, Advancement parent, RegistryObject<? extends Item> require, FrameType frameType, boolean toChat) {
        return createAdvancement(dataSaver, parent, "obtain_" + require.getId().getPath(), frameType, toChat, require, InventoryChangeTrigger.TriggerInstance::hasItems);
    }

    private <ToCheck extends ItemLike, Trigger extends CriterionTriggerInstance> Advancement createAdvancement(Consumer<Advancement> consumer, Advancement parent, String saveName, FrameType frame, boolean toChat, RegistryObject<ToCheck> toCheck, Function<ToCheck, Trigger> triggerFactory) {
        return createAdvancement(consumer, parent, saveName, toCheck.get(), frame, toChat, toCheck.get(), triggerFactory);
    }

    private <ToCheck, Trigger extends CriterionTriggerInstance> Advancement createAdvancement(Consumer<Advancement> consumer, Advancement parent, String saveName, ItemLike displayIcon, FrameType frame, boolean toChat, ToCheck toCheck, Function<ToCheck, Trigger> triggerFactory) {
        ResourceLocation fileName = Celestisynth.prefix(saveName);
        String languageKey = fileName.toLanguageKey("advancement");
        return Advancement.Builder
                .advancement()
                .parent(parent)
                .display(displayIcon, Component.translatable(languageKey + ".title"), Component.translatable(languageKey + ".description"), null, frame, true, toChat, false)
                .addCriterion("criteria", triggerFactory.apply(toCheck))
                .save(consumer, fileName, fileH);
    }
    **/
}
