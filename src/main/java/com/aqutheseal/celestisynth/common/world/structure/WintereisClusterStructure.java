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
        builder.addPiece(new WintereisClusterPiece(context.random(), placePos, Direction.NORTH, WintereisClusterPiece.CENTER_PIECE));
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            builder.addPiece(new WintereisClusterPiece(context.random(), placePos.relative(direction, 48), direction, WintereisClusterPiece.CONNECTOR_FOUR_WAY));
            builder.addPiece(new WintereisClusterPiece(context.random(), placePos.relative(direction, 48 * 2), direction.getOpposite(), WintereisClusterPiece.CONNECTOR_END));
        }
        builder.addPiece(new WintereisClusterPiece(context.random(), placePos.offset(48, 0, -48), Direction.SOUTH, WintereisClusterPiece.CONNECTOR_CORNER));
        builder.addPiece(new WintereisClusterPiece(context.random(), placePos.offset(48, 0, 48), Direction.WEST, WintereisClusterPiece.CONNECTOR_CORNER));
        builder.addPiece(new WintereisClusterPiece(context.random(), placePos.offset(-48, 0, 48), Direction.NORTH, WintereisClusterPiece.CONNECTOR_CORNER));
        builder.addPiece(new WintereisClusterPiece(context.random(), placePos.offset(-48, 0, -48), Direction.EAST, WintereisClusterPiece.CONNECTOR_CORNER));
    }

    public @NotNull StructureType<?> type() {
        return CSStructures.WINTEREIS_CLUSTER_TYPE.get();
    }
}
