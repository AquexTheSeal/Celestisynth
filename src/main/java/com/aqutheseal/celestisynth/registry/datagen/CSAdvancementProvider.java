package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Function;

public class CSAdvancementProvider extends AdvancementProvider {

    public CSAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement rootObtainedCore = Advancement.Builder.advancement().display(CSItemRegistry.CELESTIAL_CORE.get(), Component.translatable("advancements.celestisynth.obtained_core.title"), Component.translatable("advancements.celestisynth.obtained_core.description"), new ResourceLocation(Celestisynth.MODID, "textures/block/zephyr_deposit.png"), FrameType.TASK, false, true, false).addCriterion("obtained_core", InventoryChangeTrigger.TriggerInstance.hasItems(CSItemRegistry.CELESTIAL_CORE.get())).save(consumer, "celestisynth:root");

        Advancement getSupernalIngot = this.obtainItem(consumer, rootObtainedCore, CSItemRegistry.SUPERNAL_NETHERITE_INGOT, FrameType.TASK, false);

        Advancement obtainedCelestialIngot = this.obtainItem(consumer, getSupernalIngot, CSItemRegistry.CELESTIAL_NETHERITE_INGOT, FrameType.TASK, false);

        Advancement placeCelestialTable = this.createAdvancement(consumer, obtainedCelestialIngot, "place_celestial_table", FrameType.CHALLENGE, true, CSBlockRegistry.CELESTIAL_CRAFTING_TABLE, PlacedBlockTrigger.TriggerInstance::placedBlock);

        CSItemRegistry.ITEMS.getEntries().stream().filter(weapon -> weapon.get() instanceof CSWeapon)
                .forEach(item -> this.obtainItem(consumer, placeCelestialTable, item, FrameType.GOAL, true));
    }


    private Advancement obtainItem(Consumer<Advancement> dataSaver, Advancement parent, RegistryObject<? extends Item> require, FrameType frameType, boolean toChat) {
        return createAdvancement(dataSaver, parent, "obtain_" + require.getId().getPath(), frameType, toChat, require, InventoryChangeTrigger.TriggerInstance::hasItems);
    }

    private <ToCheck extends ItemLike, Trigger extends CriterionTriggerInstance> Advancement createAdvancement(Consumer<Advancement> consumer, Advancement parent, String saveName, FrameType frame, boolean toChat, RegistryObject<ToCheck> toCheck, Function<ToCheck, Trigger> triggerFactory) {
        return createAdvancement(consumer, parent, saveName, toCheck.get(), frame, toChat, toCheck.get(), triggerFactory);
    }

    private <ToCheck, Trigger extends CriterionTriggerInstance> Advancement createAdvancement(Consumer<Advancement> consumer, Advancement parent, String saveName, ItemLike displayIcon, FrameType frame, boolean toChat, ToCheck toCheck, Function<ToCheck, Trigger> triggerFactory) {
        ResourceLocation fileName = new ResourceLocation(Celestisynth.MODID, saveName);
        String languageKey = fileName.toLanguageKey("advancement");
        return Advancement.Builder
                .advancement()
                .parent(parent)
                .display(displayIcon, Component.translatable(languageKey + ".title"), Component.translatable(languageKey + ".description"), null, frame, true, toChat, false)
                .addCriterion("criteria", triggerFactory.apply(toCheck))
                .save(consumer, fileName, super.fileHelper);
    }
}
