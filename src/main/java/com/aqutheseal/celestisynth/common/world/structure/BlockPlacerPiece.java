package com.aqutheseal.celestisynth.common.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.FluidState;

import java.util.Random;

public abstract class BlockPlacerPiece extends ScatteredFeaturePiece {
    protected BlockPlacerPiece(StructurePieceType pType, int pX, int pY, int pZ, int xSize, int ySize, int zSize, Direction pOrientation) {
        super(pType, pX, pY, pZ, xSize, ySize, zSize, pOrientation);
    }

    protected BlockPlacerPiece(StructurePieceType pType, CompoundTag pTag) {
        super(pType, pTag);
    }

    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        generatePiece(level, structureManager, generator, random, box, chunkPos, getWSPos(0, 0, 0).immutable());
    }

    public abstract void generatePiece(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos);

    public void putBlock(WorldGenLevel pLevel, BlockState pBlockstate, BlockPos blockpos, BoundingBox pBoundingbox) {
        if (pBoundingbox.isInside(blockpos)) {
            pLevel.setBlock(blockpos, pBlockstate, 2);
            FluidState fluidstate = pLevel.getFluidState(blockpos);
            if (!fluidstate.isEmpty()) {
                pLevel.scheduleTick(blockpos, fluidstate.getType(), 0);
            }
        }
    }

    protected BlockPos.MutableBlockPos getWSPos(int pX, int pY, int pZ) {
        return new BlockPos.MutableBlockPos(this.getWSX(pX), this.getWSY(pY), this.getWSZ(pZ));
    }

    protected int getWSX(int pX) {
        return this.boundingBox.getCenter().getX() + pX;
    }

    protected int getWSY(int pY) {
        return this.boundingBox.minY() + pY;
    }

    protected int getWSZ(int pZ) {
        return this.boundingBox.getCenter().getZ() + pZ;
    }

    public int bounded(int value) {
        return (-value) + (new Random().nextInt(value * 2));
    }
}
