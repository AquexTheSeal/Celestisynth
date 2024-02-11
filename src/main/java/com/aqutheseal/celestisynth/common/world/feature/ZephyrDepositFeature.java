package com.aqutheseal.celestisynth.common.world.feature;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class ZephyrDepositFeature extends Feature<NoneFeatureConfiguration> {

    public ZephyrDepositFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos pos = context.origin();

        boolean large = randomsource.nextInt(5) == 0;
        int tipMin = (int) ((large ? 25 : 10) * 0.6);
        int tipRand = (int) ((large ? 35 : 20) * 0.3);
        int radiusMin = large ? 5 : 3;
        int radiusRand = large ? 3 : 1;

        int tip = tipMin + worldgenlevel.getRandom().nextInt(tipRand);
        int topX = worldgenlevel.getRandom().nextInt(tip) - tip / 2;
        int topZ = worldgenlevel.getRandom().nextInt(tip) - tip / 2;

        int radius = radiusMin + worldgenlevel.getRandom().nextInt(radiusRand);
        Vec3 to = new Vec3(pos.getX() + topX, pos.getY() + tip, pos.getZ() + topZ);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double fromCenter = Math.sqrt(x * x + z * z);
                if (fromCenter <= radius) {
                    Vec3 from = new Vec3(pos.getX() + x, pos.getY(), pos.getZ() + z);

                    if(worldgenlevel.getBlockState(posFromVec(from).below()).isAir()) {
                        continue;
                    }

                    Vec3 per = to.subtract(from).normalize();
                    Vec3 current = from.add(0, 0, 0);
                    double distance = from.distanceTo(to);

                    for (double i = 0; i < distance; i++) {
                        BlockPos targetPos = posFromVec(current);
                        if (i > 0 && i < distance / 1.3) {
                            int roll = randomsource.nextInt(3);
                            if (roll == 0) {
                                worldgenlevel.setBlock(targetPos, Blocks.DEEPSLATE.defaultBlockState(), 3);
                            } else if (roll == 1) {
                                worldgenlevel.setBlock(targetPos, Blocks.STONE.defaultBlockState(), 3);
                            } else if (roll == 2) {
                                worldgenlevel.setBlock(targetPos, CSBlocks.ZEPHYR_DEPOSIT.get().defaultBlockState(), 3);
                            }
                        } else {
                            worldgenlevel.setBlock(targetPos, CSBlocks.ZEPHYR_DEPOSIT.get().defaultBlockState(), 3);
                        }
                        if (i <= 0) {
                            BlockPos getFromTarget = targetPos;
                            while (worldgenlevel.isEmptyBlock(getFromTarget.below())) {
                                if (randomsource.nextBoolean()) {
                                    worldgenlevel.setBlock(getFromTarget, Blocks.STONE.defaultBlockState(), 3);
                                } else {
                                    worldgenlevel.setBlock(getFromTarget, Blocks.DEEPSLATE.defaultBlockState(), 3);
                                }
                                getFromTarget = getFromTarget.below();
                            }
                        }
                        current = current.add(per);
                    }
                }
            }
        }

        Celestisynth.LOGGER.info("DEPOSIT GENERATED AT: " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
        return true;
    }

    public BlockPos posFromVec(Vec3 vec3) {
        return new BlockPos((int) vec3.x(), (int) vec3.y(), (int) vec3.z());
    }
}