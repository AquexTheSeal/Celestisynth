package com.aqutheseal.celestisynth.common.world.structure;

import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class WintereisClusterPiece extends BlockPlacerPiece {
    public static final int CENTER_PIECE = 0, CONNECTOR_STRAIGHT = 1, CONNECTOR_THREE_WAY = 2, CONNECTOR_END = 3;

    public int radius;
    public double heightMod;
    public int type;

    public WintereisClusterPiece(RandomSource pRandom, BlockPos pos, int radius, double heightMod, Direction orientation, int type) {
        super(CSStructures.WINTEREIS_CLUSTER_PIECE.get(), pos.getX(), pos.getY(), pos.getZ(), 48, 48, 48, orientation);
        this.radius = radius;
        this.heightMod = heightMod;
        this.type = type;
    }

    public WintereisClusterPiece(CompoundTag pTag) {
        super(CSStructures.WINTEREIS_CLUSTER_PIECE.get(), pTag);
        radius = pTag.getInt("Radius");
        heightMod = pTag.getDouble("HeightMod");
        type = pTag.getInt("Type");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        super.addAdditionalSaveData(pContext, pTag);
        pTag.putInt("Radius", radius);
        pTag.putDouble("HeightMod", heightMod);
        pTag.putInt("Type", type);
    }

    @Override
    public void generatePiece(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        generateDiamond(level, random, pos, box, radius, heightMod);

        if (type == CENTER_PIECE) {
            for (int cr = 0; cr <= 360; cr++) {
                putBlock(level, Blocks.SNOW_BLOCK.defaultBlockState(), pos.offset(Math.sin(cr) * 20, -1, Math.cos(cr) * 20), box);
                putBlock(level, Blocks.SNOW_BLOCK.defaultBlockState(), pos.offset(Math.sin(cr) * 16, -2, Math.cos(cr) * 16), box);
                putBlock(level, Blocks.SNOW_BLOCK.defaultBlockState(), pos.offset(Math.sin(cr) * 12, -3, Math.cos(cr) * 12), box);
            }
            for (int sx = -24; sx <= 24; sx++) {
                for (int sz = -2; sz <= 2; sz++) {
                    putBlock(level, Blocks.CRYING_OBSIDIAN.defaultBlockState(), pos.offset(sx, 0, sz), box);
                }
            }
            for (int sz = -24; sz <= 24; sz++) {
                for (int sx = -2; sx <= 2; sx++) {
                    putBlock(level, Blocks.CRYING_OBSIDIAN.defaultBlockState(), pos.offset(sx, 0, sz), box);
                }
            }
        }
    }

    public void generateDiamond(WorldGenLevel level, RandomSource random, BlockPos pos, BoundingBox box, int radius, double heightMod) {
        for (int sy = -64; sy <= 64; sy++) {
            for (int sx = -radius; sx <= radius; sx++) {
                for (int sz = -radius; sz <= radius; sz++) {
                    if (Math.abs(sx) + Math.abs(sz) + Math.abs(sy * heightMod) <= radius) {
                        putBlock(level, Blocks.CRYING_OBSIDIAN.defaultBlockState(), pos.offset(sx, sy, sz), box);
                    }
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
}
