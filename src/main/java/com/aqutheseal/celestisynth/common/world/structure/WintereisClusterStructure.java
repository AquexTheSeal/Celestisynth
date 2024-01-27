package com.aqutheseal.celestisynth.common.world.structure;

import com.aqutheseal.celestisynth.common.registry.CSStructures;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class WintereisClusterStructure extends Structure {
    public static final Codec<WintereisClusterStructure> CODEC = simpleCodec(WintereisClusterStructure::new);

    public WintereisClusterStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    public @NotNull Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> {
            generatePieces(builder, context);
        });
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        BlockPos placePos = context.chunkPos().getMiddleBlockPosition(70);
        builder.addPiece(new WintereisClusterPiece(context.random(), placePos, 10, 0.4, Direction.NORTH, WintereisClusterPiece.CENTER_PIECE));
        for (int i = 0; i < 10; i++) {
            builder.addPiece(new WintereisClusterPiece(context.random(), placePos.offset(bounded(40), bounded(10), bounded(40)),
                    4 + context.random().nextInt(3), 0.4, Direction.NORTH, WintereisClusterPiece.CONNECTOR_STRAIGHT)
            );
        }
    }

    public @NotNull StructureType<?> type() {
        return CSStructures.WINTEREIS_CLUSTER_STRUCTURE.get();
    }

    public int bounded(int value) {
        return (-value) + (new Random().nextInt(value * 2));
    }
}
