package com.aqutheseal.celestisynth.common.world.structure;

import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSStructures;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.ArrayList;
import java.util.List;

public class WintereisClusterPiece extends BlockPlacerPiece {
    public static final int CENTER_PIECE = 0, CONNECTOR_FOUR_WAY = 1, CONNECTOR_CORNER = 3, CONNECTOR_END = 4;
    public int type;

    public WintereisClusterPiece(RandomSource pRandom, BlockPos pos, Direction orientation, int type) {
        super(CSStructures.WINTEREIS_CLUSTER_PIECE.get(), pos.getX(), pos.getY(), pos.getZ(), 48, 48, 48, orientation);
        this.type = type;
    }

    public WintereisClusterPiece(CompoundTag pTag) {
        super(CSStructures.WINTEREIS_CLUSTER_PIECE.get(), pTag);
        type = pTag.getInt("Type");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        super.addAdditionalSaveData(pContext, pTag);
        pTag.putInt("Type", type);
    }

    @Override
    public void generatePiece(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        if (type == CENTER_PIECE) {
            generateDiamond(level, CSBlocks.WINTEREIS.get().defaultBlockState(), pos, box, 10, 0.4, false);
            for (Pair<Integer, Integer> corner : get8WayPoses(4)) {
                generateDiamond(level, CSBlocks.WINTEREIS.get().defaultBlockState(), pos.offset(corner.getFirst(), 0, corner.getSecond()), box, 4, 0.25, false);
            }

            for (Pair<Integer, Integer> corner : getCornerPoses(20)) {
                generateDiamond(level, Blocks.PRISMARINE_BRICKS.defaultBlockState(), pos.offset(corner.getFirst(), 16, corner.getSecond()), box, 4, 0.3, true);
            }

            for (int cr = 0; cr <= 360; cr++) {
                putBlock(level, Blocks.PRISMARINE_BRICKS.defaultBlockState(), pos.offset(Math.sin(cr) * 20, 0, Math.cos(cr) * 20), box);
            }
            generateConnector(level,  Blocks.PRISMARINE_BRICKS.defaultBlockState(), pos, box, 16);
        }
        if (type == CONNECTOR_FOUR_WAY) {
            generateDiamond(level,  Blocks.ICE.defaultBlockState(), pos, box, 8, 0.5, false);
            generateConnector(level,  Blocks.PRISMARINE_BRICKS.defaultBlockState(), pos, box, 12);
        }
        if (type == CONNECTOR_CORNER) {
            generateDiamond(level,  Blocks.ICE.defaultBlockState(), pos, box, 4, 0.5, false);
            generateConnector(level,  Blocks.PRISMARINE_BRICKS.defaultBlockState(), pos, box, 8);

            for (Pair<Integer, Integer> corner : getCornerPoses(16)) {
                generateDiamond(level, Blocks.PRISMARINE_BRICKS.defaultBlockState(), pos.offset(corner.getFirst(), 0, corner.getSecond()), box, 4, 0.3, true);
            }
        }
        if (type == CONNECTOR_END) {
            generateDiamond(level,  Blocks.ICE.defaultBlockState(), pos, box, 3, 0.5, false);
            generateConnector(level,  Blocks.PRISMARINE_BRICKS.defaultBlockState(), pos, box, 4);
        }
    }

    public void generateConnector(WorldGenLevel level, BlockState blockState, BlockPos pos, BoundingBox box, int radius) {
        for (int cr = 0; cr <= 360; cr++) {
            putBlock(level, blockState, pos.offset(Math.sin(cr) * radius, 0, Math.cos(cr) * radius), box);
        }
        if (type == CENTER_PIECE || type == CONNECTOR_FOUR_WAY) {
            for (int sx = -24; sx <= 24; sx++) {
                if (sx > (radius - 1) || sx < -(radius - 1)) {
                    for (int sz = -2; sz <= 2; sz++) {
                        putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                    }
                }
            }
            for (int sz = -24; sz <= 24; sz++) {
                if (sz > (radius - 1) || sz < -(radius - 1)) {
                    for (int sx = -2; sx <= 2; sx++) {
                        putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                    }
                }
            }
        }
        if (type == CONNECTOR_CORNER) {
            if (getOrientation() != null) {
                switch (getOrientation()) {
                    case WEST: {
                        for (int sz = -radius; sz >= -24; sz--) {
                            for (int sx = -2; sx <= 2; sx++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        for (int sx = -radius; sx >= -24; sx--) {
                                for (int sz = -2; sz <= 2; sz++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                    case EAST: {
                        for (int sz = radius; sz <= 24; sz++) {
                            for (int sx = -2; sx <= 2; sx++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        for (int sx = radius; sx <= 24; sx++) {
                            for (int sz = -2; sz <= 2; sz++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                    case NORTH: {
                        for (int sx = radius; sx <= 24; sx++) {
                            for (int sz = -2; sz <= 2; sz++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        for (int sz = -radius; sz >= -24; sz--) {
                            for (int sx = -2; sx <= 2; sx++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                    case SOUTH: {
                        for (int sx = -radius; sx >= -24; sx--) {
                            for (int sz = -2; sz <= 2; sz++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        for (int sz = radius; sz <= 24; sz++) {
                            for (int sx = -2; sx <= 2; sx++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                }
            }
        }
        if (type == CONNECTOR_END) {
            if (getOrientation() != null) {
                switch (getOrientation()) {
                    case WEST: {
                        for (int sx = -radius; sx >= -24; sx--) {
                            for (int sz = -2; sz <= 2; sz++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                    case EAST: {
                        for (int sx = radius; sx <= 24; sx++) {
                            for (int sz = -2; sz <= 2; sz++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                    case NORTH: {
                        for (int sz = -radius; sz >= -24; sz--) {
                            for (int sx = -2; sx <= 2; sx++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                    case SOUTH: {
                        for (int sz = radius; sz <= 24; sz++) {
                            for (int sx = -2; sx <= 2; sx++) putBlock(level, blockState, pos.offset(sx, 0, sz), box);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void generateDiamond(WorldGenLevel level, BlockState blockState, BlockPos pos, BoundingBox box, int radius, double heightMod, boolean generateBelow) {
        for (int sy = -64; sy <= 64; sy++) {
            for (int sx = -radius; sx <= radius; sx++) {
                for (int sz = -radius; sz <= radius; sz++) {
                    if (Math.abs(sx) + Math.abs(sz) + Math.abs(sy * heightMod) <= radius) {
                        BlockPos.MutableBlockPos offset = pos.mutable().move(sx, sy, sz);
                        if (generateBelow) {
                            for (int ty = offset.getY(); ty > level.getMinBuildHeight(); ty--) {
                                putBlock(level, blockState, offset, box);
                                offset.move(Direction.DOWN);
                            }
                        } else {
                            putBlock(level, blockState, offset, box);
                        }
                    }
                }
            }
        }
    }

    public ImmutableList<Pair<Integer, Integer>> get8WayPoses(int multiplier) {
        ArrayList<Pair<Integer, Integer>> list = new ArrayList<>(List.of(
                Pair.of(multiplier * 2, 0),
                Pair.of(0, -multiplier * 2),
                Pair.of(-multiplier * 2, 0),
                Pair.of(0, multiplier * 2)
        ));
        list.addAll(getCornerPoses(multiplier));
        return ImmutableList.copyOf(list);
    }

    public ImmutableList<Pair<Integer, Integer>> getCornerPoses(int multiplier) {
        return ImmutableList.of(
                Pair.of(multiplier, multiplier),
                Pair.of(multiplier, -multiplier),
                Pair.of(-multiplier, multiplier),
                Pair.of(-multiplier, -multiplier)
        );
    }
}
