package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class CSBlockModelProvider extends BlockModelProvider {
    public CSBlockModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.cross(CSBlockRegistry.SOLAR_CRYSTAL.get());
        this.cubeAll(CSBlockRegistry.LUNAR_STONE.get());
        this.cubeAll(CSBlockRegistry.ZEPHYR_DEPOSIT.get());
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
        return new ResourceLocation(Celestisynth.MODID, "block/" + name);
    }
}
