package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.datagen.helpers.BlockDatagenHelper;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CSBlockstateProvider extends BlockStateProvider {
    private final BlockDatagenHelper helper = new BlockDatagenHelper(models(), this);

    public CSBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Celestisynth.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        helper.cross(CSBlocks.SOLAR_CRYSTAL.get(), BlockDatagenHelper.RENDER_TYPE_CUTOUT);
        helper.cubeAll(CSBlocks.LUNAR_STONE.get());
        helper.cubeAll(CSBlocks.ZEPHYR_DEPOSIT.get());
        helper.cubeAll(CSBlocks.WINTEREIS.get(), BlockDatagenHelper.RENDER_TYPE_TRANSLUCENT);
    }
}
