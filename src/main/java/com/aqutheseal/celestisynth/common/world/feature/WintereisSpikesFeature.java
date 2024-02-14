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
import net.minecraft.world.phys.Vec3;

public class WintereisSpikesFeature extends Feature<NoneFeatureConfiguration> {

    public WintereisSpikesFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos pTarget = context.origin();
        WorldGenLevel pLevel = context.level();
        RandomSource pRandom = context.random();

        if (isBigCave(pLevel, pTarget, 10)) {
            int tip = 13 + pRandom.nextInt(7);
            int topX = -10 + pRandom.nextInt(10);
            int topZ = -10 + pRandom.nextInt(10);
            int radius = 1 + pRandom.nextInt(3);

            Vec3 to = new Vec3(pTarget.getX() + topX, pTarget.getY() + tip, pTarget.getZ() + topZ);

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double fromCenter = Math.sqrt(x * x + z * z);
                    if (fromCenter <= radius) {
                        Vec3 from = new Vec3(pTarget.getX() + x, pTarget.getY(), pTarget.getZ() + z);

                        Vec3 per = to.subtract(from).normalize();
                        Vec3 current = from.add(0, 0, 0);
                        double distance = from.distanceTo(to);

                        for (double i = 0; i < distance; i++) {
                            BlockPos targetPos = posFromVec(current);
                            if (i >= 0 && i < distance / (4 - pRandom.nextDouble())) {
                                setBlockConditioned(pLevel, targetPos, Blocks.DEEPSLATE.defaultBlockState());
                            } else {
                                setBlockConditioned(pLevel, targetPos, CSBlocks.WINTEREIS.get().defaultBlockState());
                            }
                            current = current.add(per);

                            if (i <= 0) {
                                BlockPos getFromTarget = targetPos;
                                while (pLevel.isEmptyBlock(getFromTarget.below())) {
                                    setBlockConditioned(pLevel, getFromTarget, Blocks.DEEPSLATE.defaultBlockState());
                                    getFromTarget = getFromTarget.below();
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isBigCave(WorldGenLevel level, BlockPos pos, int height) {
        for (int i = 1; i < height; i++) {
            if (!level.getBlockState(pos.above(i)).isAir()) {
                return false;
            }
        }
        return true;
    }

    protected void setBlockConditioned(WorldGenLevel pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.getBlockState(pPos).isAir()) {
            super.setBlock(pLevel, pPos, pState);
        }
    }

    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }
}
