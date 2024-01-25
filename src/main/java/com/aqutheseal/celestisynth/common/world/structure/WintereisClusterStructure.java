package com.aqutheseal.celestisynth.common.world.structure;

import com.aqutheseal.celestisynth.common.registry.CSStructures;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WintereisClusterStructure extends Structure {
    public static final Codec<WintereisClusterStructure> CODEC = simpleCodec(WintereisClusterStructure::new);

    public WintereisClusterStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    private static boolean extraSpawningChecks(Structure.GenerationContext context) {
        LevelHeightAccessor height = context.heightAccessor();

        return height.getHeight() >= height.getMinBuildHeight() + 60 && height.getHeight() <= height.getMaxBuildHeight() - 60;
    }

    public @NotNull Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        if (!extraSpawningChecks(context)) {
            return Optional.empty();
        }

        ChunkPos chunkpos = context.chunkPos();
        int i = chunkpos.getMiddleBlockX();
        int j = chunkpos.getMiddleBlockZ();
        return Optional.of(new Structure.GenerationStub(new BlockPos(i, context.heightAccessor().getHeight(), j), (builder) -> {
            generatePieces(builder, context);
        }));
    }

    private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        builder.addPiece(new WintereisClusterPiece(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ()));
    }

    public @NotNull StructureType<?> type() {
        return CSStructures.WINTEREIS_CLUSTER_STRUCTURE.get();
    }
}
