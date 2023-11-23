package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class CSBlockModelProvider extends BlockModelProvider {
    public CSBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Celestisynth.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.cross(CSBlocks.SOLAR_CRYSTAL.get());
        this.cubeAll(CSBlocks.LUNAR_STONE.get());
        this.cubeAll(CSBlocks.ZEPHYR_DEPOSIT.get());
    }

    public void cross(Block block) {
        this.cross(name(block), csLoc(name(block)));
    }

    public void cubeAll(Block block) {
        this.cubeAll(name(block), csLoc(name(block)));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    public ResourceLocation csLoc(String name) {
        return Celestisynth.prefix("block/" + name);
    }
}
