package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class CSBlockstateProvider extends BlockStateProvider {
    private static final ResourceLocation RENDER_TYPE_CUTOUT = new ResourceLocation("cutout");

    public CSBlockstateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.crossBlock(CSBlockRegistry.SOLAR_CRYSTAL.get());
        this.simpleBlock(CSBlockRegistry.LUNAR_STONE.get());
    }

    public void crossBlock(Block block) {
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(models().cross(name(block), blockTexture(block)).renderType(RENDER_TYPE_CUTOUT)));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
}
