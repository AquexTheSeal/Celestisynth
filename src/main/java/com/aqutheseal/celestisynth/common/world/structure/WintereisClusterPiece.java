package com.aqutheseal.celestisynth.common.world.structure;

import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;

public class WintereisClusterPiece extends ScatteredFeaturePiece {

    public WintereisClusterPiece(RandomSource pRandom, int pX, int pZ) {
        super(CSStructures.WINTEREIS_CLUSTER_PIECE.get(), pX, 64, pZ, 64, 64, 64, getRandomHorizontalDirection(pRandom));
    }

    public WintereisClusterPiece(CompoundTag pTag) {
        super(CSStructures.WINTEREIS_CLUSTER_PIECE.get(), pTag);
    }

    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        createPillar(level, pos, 3 + random.nextInt(3), 25 + random.nextInt(7), box);
        for (int i = 1; i < 4; i++) {
            createOrbit(level, random, pos, 5 * (i * 2), 5 + (i * 3), bounded(random, (60 + (i * 20))), i < 3, box);
        }
    }

    public void createOrbit(WorldGenLevel level, RandomSource random, BlockPos pos, int radius, int heightVar, int rotationOffset, boolean isInnerLayers, BoundingBox pBox) {
        for (int i = 0; i <= 90 * (radius * 4); i++) {
            double y = (heightVar * (Math.sin(rotationOffset + i) * Math.cos(0.05 * i)));
            this.placeBlock(level, getBlock(random, isInnerLayers).defaultBlockState(), (int) (Math.sin(i) * radius), (int) y, (int) (Math.cos(i) * radius), pBox);
        }
    }

    public void drawVerticalLine(WorldGenLevel level, BlockPos pos, int length, BoundingBox pBox) {
        for (int sy = -length; sy <= length; sy++) {
            this.placeBlock(level, CSBlocks.WINTEREIS.get().defaultBlockState(), 0, sy, 0, pBox);
        }
    }

    public void createPillar(WorldGenLevel level, BlockPos pos, int radius, int length, BoundingBox pBox) {
        for (int sx = -radius; sx <= radius; sx++) {
            for (int sz = -radius; sz <= radius; sz++) {
                if (sx * sx + sz * sz <= radius * radius) {
                    BlockPos place = pos.offset(sx, 0, sz);
                    Vec3i toVector = new Vec3i(place.getX(), place.getY(), place.getZ());
                    drawVerticalLine(level, place, length - (int) (pos.distSqr(toVector) * (radius / 4)), pBox);
                }
            }
        }
    }

    public Block getBlock(RandomSource random, boolean isInnerLayers) {
        switch (random.nextInt(3)) {
            case 1: return Blocks.BLUE_ICE;
            case 2: {
                if (isInnerLayers) {
                    return CSBlocks.WINTEREIS.get();
                } else {
                    return Blocks.ICE;
                }
            }
            default: {
                return Blocks.ICE;
            }
        }
    }

    public int bounded(RandomSource random, int value) {
        return -value + random.nextInt(value);
    }
}
