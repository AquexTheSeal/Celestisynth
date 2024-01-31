package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.world.structure.WintereisClusterPiece;
import com.aqutheseal.celestisynth.common.world.structure.WintereisClusterStructure;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CSStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, Celestisynth.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE = DeferredRegister.create(Registries.STRUCTURE_PIECE, Celestisynth.MODID);

    public static RegistryObject<StructureType<WintereisClusterStructure>> WINTEREIS_CLUSTER_STRUCTURE = STRUCTURE_TYPE.register("wintereis_cluster_structure", () -> explicitStructureTypeTyping(WintereisClusterStructure.CODEC));
    public static final RegistryObject<StructurePieceType> WINTEREIS_CLUSTER_PIECE = STRUCTURE_PIECE.register("wintereis_cluster_piece", () -> (StructurePieceType.ContextlessType) WintereisClusterPiece::new);

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }
}
