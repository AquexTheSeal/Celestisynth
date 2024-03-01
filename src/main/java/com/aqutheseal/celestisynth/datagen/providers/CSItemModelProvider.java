package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSItemModelProvider extends ItemModelProvider {
    private static final List<RegistryObject<Item>> exemptions = new ArrayList<>();

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

        exemptions.add(CSItems.TEMPEST_SPAWN_EGG);

        this.defaultItem(CSItems.ITEMS.getEntries());
        this.defaultItem(CSItems.SOLAR_CRYSTAL_HELMET);
        this.defaultItem(CSItems.SOLAR_CRYSTAL_CHESTPLATE);
        this.defaultItem(CSItems.SOLAR_CRYSTAL_LEGGINGS);
        this.defaultItem(CSItems.SOLAR_CRYSTAL_BOOTS);

        this.csCustomModel(CSBlocks.SOLAR_CRYSTAL.get().asItem(), getMcLoc("item/generated"));
        this.block(CSBlocks.LUNAR_STONE);
        this.block(CSBlocks.ZEPHYR_DEPOSIT);
        this.block(CSBlocks.WINTEREIS);

        this.csCustomModel(CSItems.CELESTIAL_DEBUGGER.get(), getMcLoc("item/handheld"));
    }

    public void defaultItem(Collection<RegistryObject<Item>> items) {
        for (RegistryObject<Item> item : items) {

            if (exemptions.contains(item)) {
                return;
            }

            String name = item.getId().getPath();
            Item getItem = item.get();
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

    public void defaultItem(RegistryObject<Item> item) {
        String name = item.getId().getPath();
        Item getItem = item.get();
        ModelFile.ExistingModelFile modelType = getItem instanceof DiggerItem || getItem instanceof SwordItem ? getMcLoc("item/handheld") : getMcLoc("item/generated");
        this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name);
    }

    public void block(RegistryObject<Block> blockItem) {
        String name = blockItem.getId().getPath();
        this.getBuilder(name).parent(getCSLoc("block/" + name));
    }

    public void csCustomModel(RegistryObject<Item> item, ModelFile.ExistingModelFile modelPath) {
        csCustomModel(item.get(), modelPath);
    }

    public void csCustomModel(Item item, ModelFile.ExistingModelFile modelType) {
        String name = ForgeRegistries.ITEMS.getKey(item).getPath();
        this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name);
    }

    public void csSinglePredicatedModel(RegistryObject<Item> item, String modelPath, ResourceLocation predicate, String predicatedModelPath) {
        String name = item.getId().getPath();
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
