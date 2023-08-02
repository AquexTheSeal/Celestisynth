package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import com.mojang.datafixers.util.Pair;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CSAdvancementProvider extends AdvancementProvider {

    public CSAdvancementProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement obtained_core = Advancement.Builder.advancement().display(CSItemRegistry.CELESTIAL_CORE.get(),
                        Component.translatable("advancements.celestisynth.obtained_core.title"),
                        Component.translatable("advancements.celestisynth.obtained_core.description"),
                        new ResourceLocation(Celestisynth.MODID, "textures/block/zephyr_deposit.png"),
                        FrameType.TASK, false, true, false).addCriterion("obtained_core",
                        InventoryChangeTrigger.TriggerInstance.hasItems(CSItemRegistry.CELESTIAL_CORE.get()))
                .save(consumer, "celestisynth/root");

        Advancement obtained_supernal_ingot =
                simplified(consumer, "obtained_supernal_ingot", obtained_core, FrameType.TASK, false,
                        InventoryChangeTrigger.TriggerInstance.hasItems(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()),  CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get());

        Advancement obtained_celestial_ingot =
                simplified(consumer, "obtained_celestial_ingot", obtained_supernal_ingot, FrameType.TASK, false,
                        InventoryChangeTrigger.TriggerInstance.hasItems(CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get()),  CSItemRegistry.CELESTIAL_NETHERITE_INGOT.get());

        Advancement placed_celestial_table =
                simplified(consumer, "placed_celestial_table", obtained_celestial_ingot, FrameType.CHALLENGE, true,
                        PlacedBlockTrigger.TriggerInstance.placedBlock(CSBlockRegistry.CELESTIAL_CRAFTING_TABLE.get()),  CSBlockRegistry.CELESTIAL_CRAFTING_TABLE.get());

        List<Pair<Advancement, Item>> weaponAdvancements = new ArrayList<>();
        for (RegistryObject<Item> weapons : CSItemRegistry.ITEMS.getEntries()) {
            if (weapons.get() instanceof CSWeapon) {
                weaponAdvancements.add(Pair.of(byWeapon(consumer, placed_celestial_table, weapons), weapons.get()));
            }
        }
    }

    public Advancement getFromWeapon(List<Pair<Advancement, Item>> candidates, RegistryObject<Item> weapon) {
        for (Pair<Advancement, Item> pairs : candidates) {
            if (pairs.getSecond() == weapon.get()) {
                return pairs.getFirst();
            }
        }
        throw new IllegalStateException("Cannot find matching Advancement for weapon: " + weapon.getId().getPath());
    }

    public Advancement byWeapon(Consumer<Advancement> consumer, Advancement parent, RegistryObject<Item> celestisynthWeapon) {
        return simplified(consumer, "obtained_" + celestisynthWeapon.getId().getPath(), parent, FrameType.GOAL, true,
                InventoryChangeTrigger.TriggerInstance.hasItems(celestisynthWeapon.get()), celestisynthWeapon.get());
    }

    public Advancement simplified(Consumer<Advancement> consumer, String title, Advancement parent, FrameType frameType, boolean announceToChat, CriterionTriggerInstance trigger, ItemLike display) {
        return Advancement.Builder.advancement().parent(parent).display(display,
                        Component.translatable("advancements.celestisynth." + title + ".title"),
                        Component.translatable("advancements.celestisynth." + title + ".description"), null,
                        frameType, false, announceToChat, false).addCriterion(title, trigger)
                .save(consumer, "celestisynth/" + title);
    }
}
