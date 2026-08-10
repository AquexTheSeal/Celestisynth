package org.thecelestialworkshop.celestisynth.datagen.helpers;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.minecraft.core.registries.BuiltInRegistries;

public class BlockDatagenHelper {
    public static final ResourceLocation RENDER_TYPE_CUTOUT = ResourceLocation.parse("cutout");
    public static final ResourceLocation RENDER_TYPE_TRANSLUCENT = ResourceLocation.parse("translucent");

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
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String name(Block block) {
        return key(block).getPath();
    }

    public static ResourceLocation csLoc(String name) {
        return Celestisynth.prefix("block/" + name);
    }
}
