package org.thecelestialworkshop.celestisynth.datagen.providers;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSItemModelProvider extends ItemModelProvider {
    private static final List<Supplier<Item>> exemptions = new ArrayList<>();

    public CSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Celestisynth.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        exemptions.add(CSItems.SOLARIS);
        exemptions.add(CSItems.CRESCENTIA);
        exemptions.add(CSItems.BREEZEBREAKER);
        exemptions.add(CSItems.POLTERGEIST);
        exemptions.add(CSItems.AQUAFLORA);
        exemptions.add(CSItems.RAINFALL_SERENITY);
        exemptions.add(CSItems.FROSTBOUND);
        exemptions.add(CSItems.TRAVERSER_SPAWN_EGG);
        exemptions.add(CSItems.TEMPEST_SPAWN_EGG);

        this.defaultItem(CSItems.ITEMS.getEntries());

        this.defaultItem(CSItems.SOLAR_CRYSTAL_HELMET);
        this.defaultItem(CSItems.SOLAR_CRYSTAL_CHESTPLATE);
        this.defaultItem(CSItems.SOLAR_CRYSTAL_LEGGINGS);
        this.defaultItem(CSItems.SOLAR_CRYSTAL_BOOTS);
        this.defaultItem(CSItems.LUNAR_STONE_HELMET);
        this.defaultItem(CSItems.LUNAR_STONE_CHESTPLATE);
        this.defaultItem(CSItems.LUNAR_STONE_LEGGINGS);
        this.defaultItem(CSItems.LUNAR_STONE_BOOTS);

        this.spawnEgg(CSItems.TRAVERSER_SPAWN_EGG);

        this.csCustomModel(CSBlocks.SOLAR_CRYSTAL.get().asItem(), getMcLoc("item/generated"));
        this.block(CSBlocks.LUNAR_STONE);
        this.block(CSBlocks.ZEPHYR_DEPOSIT);
        this.block(CSBlocks.WINTEREIS);

        this.csCustomModel(CSItems.CELESTIAL_DEBUGGER.get(), getMcLoc("item/handheld"));

    }

    public void defaultItem(Collection<? extends Supplier<? extends Item>> items) {
        for (Supplier<? extends Item> item : items) {

            if (exemptions.contains(item)) {
                return;
            }

            Item getItem = item.get();
            String name = BuiltInRegistries.ITEM.getKey(getItem).getPath();
            ResourceLocation datagenLoc = Celestisynth.prefix("item/" + name);
            ModelFile.ExistingModelFile modelType = getItem instanceof DiggerItem || getItem instanceof SwordItem ? getMcLoc("item/handheld") : getMcLoc("item/generated");

            if (getItem instanceof BlockItem) {
                return;
            }

            if (existingFileHelper.exists(datagenLoc, TEXTURE) || !existingFileHelper.exists(datagenLoc, MODEL)) {
                this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name);
            }
        }
    }

    public void defaultItem(Supplier<Item> item) {
        Item getItem = item.get();
        String name = BuiltInRegistries.ITEM.getKey(getItem).getPath();
        ModelFile.ExistingModelFile modelType = getItem instanceof DiggerItem || getItem instanceof SwordItem ? getMcLoc("item/handheld") : getMcLoc("item/generated");
        this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name);
    }

    public void spawnEgg(Supplier<Item> item) {
        Item getItem = item.get();
        String name = BuiltInRegistries.ITEM.getKey(getItem).getPath();
        this.getBuilder(name).parent(getMcLoc("item/template_spawn_egg"));
    }

    public void block(Supplier<Block> blockItem) {
        String name = BuiltInRegistries.BLOCK.getKey(blockItem.get()).getPath();
        this.getBuilder(name).parent(getCSLoc("block/" + name));
    }

    public void csCustomModel(Supplier<Item> item, ModelFile.ExistingModelFile modelPath) {
        csCustomModel(item.get(), modelPath);
    }

    public void csCustomModel(Item item, ModelFile.ExistingModelFile modelType) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name);
    }

    public void csSinglePredicatedModel(Supplier<Item> item, String modelPath, ResourceLocation predicate, String predicatedModelPath) {
        String name = BuiltInRegistries.ITEM.getKey(item.get()).getPath();
        ModelFile.ExistingModelFile modelType = getCSLoc(modelPath);
        ModelFile.ExistingModelFile predModelType = getCSLoc(predicatedModelPath);
        this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name).override()
                .predicate(predicate, 1).model(getBuilder(name + "_" + predicate.getPath()));

        this.getBuilder(name + "_" + predicate.getPath()).parent(predModelType).texture("layer0", ITEM_FOLDER + "/" + name + "_" + predicate.getPath());
    }

    public ModelFile.ExistingModelFile getMcLoc(String mcModel) {
        return getExistingFile(mcLoc(mcModel));
    }

    public ModelFile.ExistingModelFile getCSLoc(String csModel) {
        return getExistingFile(csLoc(csModel));
    }

    public ResourceLocation csLoc(String name) {
        return Celestisynth.prefix(name);
    }
}
