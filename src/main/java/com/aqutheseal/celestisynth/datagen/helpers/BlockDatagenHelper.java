package com.aqutheseal.celestisynth.datagen.helpers;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockDatagenHelper {
    public static final ResourceLocation RENDER_TYPE_CUTOUT = new ResourceLocation("cutout");
    public static final ResourceLocation RENDER_TYPE_TRANSLUCENT = new ResourceLocation("translucent");

    private final BlockStateProvider state;
    private final BlockModelProvider model;

    public BlockDatagenHelper(BlockModelProvider modelProvider, BlockStateProvider stateProvider) {
        this.state = stateProvider;
        this.model = modelProvider;
    }

    public void cubeAll(Block block) {
        baseModel(block, model.cubeAll(name(block), csLoc(name(block))));
    }

    public void cubeAll(Block block, ResourceLocation renderType) {
        baseModel(block, model.cubeAll(name(block), csLoc(name(block))).renderType(renderType));
    }

    public void crossRotatable(Block block, ResourceLocation renderType) {
        state.directionalBlock(block, model.cross(name(block), csLoc(name(block))).renderType(renderType));
    }

    public void baseModel(Block block, BlockModelBuilder model) {
        state.getVariantBuilder(block).partialState().setModels(new ConfiguredModel(model));
    }

    public static ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static String name(Block block) {
        return key(block).getPath();
    }

    public static ResourceLocation csLoc(String name) {
        return Celestisynth.prefix("block/" + name);
    }
}
