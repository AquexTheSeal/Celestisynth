package com.aqutheseal.celestisynth.world.feature;

import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class LunarCraterFeature extends Feature<NoneFeatureConfiguration> {

    public LunarCraterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos blockpos = context.origin();

        int[] layerRadii = {7, 7, 6, 6, 4, 2};
        for (int i = 0; i < layerRadii.length; i++) {
            int radiuss = layerRadii[i] + 3 + randomsource.nextInt(7);
            createLayer(radiuss, null, worldgenlevel, randomsource, blockpos.below(i + 1));
            if (i != (layerRadii.length - 1)) {
                BlockState filler = context.origin().getY() > -50 ? Fluids.WATER.defaultFluidState().createLegacyBlock() : Blocks.AIR.defaultBlockState();
                createLayer(radiuss - 1, filler, worldgenlevel, randomsource, blockpos.below(i + 1));
            }
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
                            int randomInt = random.nextInt(4);
                            BlockState blockToPut;
                            if (randomInt == 0) {
                                blockToPut = level.getBlockState(pos) == Blocks.DEEPSLATE.defaultBlockState() ? Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState() : Blocks.LAPIS_ORE.defaultBlockState();
                            } else if (randomInt == 1) {
                                blockToPut = level.getBlockState(pos);
                            } else if (randomInt == 2) {
                                blockToPut = CSBlockRegistry.LUNAR_STONE.get().defaultBlockState();
                            } else {
                                blockToPut = Blocks.SEA_LANTERN.defaultBlockState();
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
}
