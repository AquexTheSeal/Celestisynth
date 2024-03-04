package com.aqutheseal.celestisynth.common.world.feature;

import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nullable;

public class SolarCraterFeature extends Feature<NoneFeatureConfiguration> {

    public SolarCraterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos blockpos = context.origin();

        int[] layerRadii = {7, 7, 6, 6, 4, 2};
        for (int i = 0; i < layerRadii.length; i++) {
            int radiuss = layerRadii[i] + randomsource.nextInt(5);
            createLayer(radiuss, null, worldgenlevel, randomsource, blockpos.below(i + 1));
            if (i != (layerRadii.length - 1)) {
                createLayer(radiuss - 1, Blocks.AIR.defaultBlockState(), worldgenlevel, randomsource, blockpos.below(i + 1));
            }
            createCrystalLayer(radiuss, worldgenlevel, randomsource, blockpos.below(i));
        }
        return true;
    }

    public void createLayer(int radius, @Nullable BlockState block, WorldGenLevel level, RandomSource random, BlockPos origin) {
        for (int sx = -radius; sx <= radius; sx++) {
            for (int sz = -radius; sz <= radius; sz++) {
                if (sx * sx + sz * sz <= radius * radius) {
                    BlockPos pos = origin.offset(sx, 0, sz);
                    if (!level.getBlockState(pos).isAir()) {
                        if (block == null) {
                            int randomInt = random.nextInt(3);
                            BlockState blockToPut;
                            if (randomInt == 0) {
                                blockToPut = Blocks.GLOWSTONE.defaultBlockState();
                            } else if (randomInt == 1) {
                                blockToPut = level.getBlockState(pos);
                            } else {
                                blockToPut = Blocks.NETHER_GOLD_ORE.defaultBlockState();
                            }
                            this.setBlock(level, pos, blockToPut);
                        } else {
                            this.setBlock(level, pos, block);
                        }
                    }
                }
            }
        }
    }

    public void createCrystalLayer(int radius, WorldGenLevel level, RandomSource random, BlockPos origin) {
        for (int sx = -radius; sx <= radius; sx++) {
            for (int sz = -radius; sz <= radius; sz++) {
                if (sx * sx + sz * sz <= radius * radius) {
                    BlockPos pos = origin.offset(sx, 0, sz);
                    if (!level.getBlockState(pos.below()).isAir() && level.getBlockState(pos).isAir()) {
                        int randomInt = random.nextInt(5);
                        if (randomInt == 0) {
                            this.setBlock(level, pos, CSBlocks.SOLAR_CRYSTAL.get().defaultBlockState());
                        }
                    }
                }
            }
        }
    }
}
