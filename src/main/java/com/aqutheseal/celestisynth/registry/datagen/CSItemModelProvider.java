package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

public class CSItemModelProvider extends ItemModelProvider {

    public CSItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.defaultItem(CSItemRegistry.ITEMS.getEntries());
        this.csSinglePredicatedModel(CSItemRegistry.SOLARIS, "item/long_blade", csLoc("soul"), "item/long_blade");
        this.csCustomModel(CSItemRegistry.CRESCENTIA, "item/long_blade");
    }

    public void defaultItem(Collection<RegistryObject<Item>> items) {
        for (RegistryObject<Item> item : items) {
            String name = item.getId().getPath();
            Item getItem = item.get();
            ResourceLocation datagenLoc = new ResourceLocation(Celestisynth.MODID, "item/" + name);
            ModelFile.ExistingModelFile modelType = getItem instanceof DiggerItem || getItem instanceof SwordItem ? getMcLoc("item/handheld") : getMcLoc("item/generated");

            if (!existingFileHelper.exists(datagenLoc, TEXTURE) || existingFileHelper.exists(datagenLoc, MODEL))
                continue;

            this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name);
            Celestisynth.LOGGER.info("Generate Item Successful: " + item.getId());
        }
    }

    public void csCustomModel(RegistryObject<Item> item, String modelPath) {
        String name = item.getId().getPath();
        ResourceLocation datagenLoc = new ResourceLocation(Celestisynth.MODID, "item/" + name);
        ModelFile.ExistingModelFile modelType = getCSLoc(modelPath);

        if (!existingFileHelper.exists(datagenLoc, TEXTURE) || existingFileHelper.exists(datagenLoc, MODEL)) {
            this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name);
            Celestisynth.LOGGER.info("Generate Item Successful: " + item.getId());
        }
    }

    public void csSinglePredicatedModel(RegistryObject<Item> item, String modelPath, ResourceLocation predicate, String predicatedModelPath) {
        String name = item.getId().getPath();
        ResourceLocation datagenLoc = new ResourceLocation(Celestisynth.MODID, "item/" + name);
        ModelFile.ExistingModelFile modelType = getCSLoc(modelPath);
        ModelFile.ExistingModelFile predModelType = getCSLoc(predicatedModelPath);

        if (!existingFileHelper.exists(datagenLoc, TEXTURE) || existingFileHelper.exists(datagenLoc, MODEL)) {
            this.getBuilder(name).parent(modelType).texture("layer0", ITEM_FOLDER + "/" + name).override()
                    .predicate(predicate, 1).model(getBuilder(name + "_" + predicate.getPath()));

            this.getBuilder(name + "_" + predicate.getPath()).parent(predModelType).texture("layer0", ITEM_FOLDER + "/" + name + "_" + predicate.getPath());
            Celestisynth.LOGGER.info("Generate Item Successful: " + item.getId());
        }
    }

    public ModelFile.ExistingModelFile getMcLoc(String mcModel) {
        return getExistingFile(mcLoc(mcModel));
    }
    public ModelFile.ExistingModelFile getCSLoc(String csModel) {
        return getExistingFile(csLoc(csModel));
    }

    public ResourceLocation csLoc(String name) {
        return new ResourceLocation(Celestisynth.MODID, name);
    }
}
