package com.aqutheseal.celestisynth.datagen.providers;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class CSStructureProvider {

    public static class Structures {
        public static void bootstrap(BootstapContext<Structure> ctx) {
            HolderGetter<Biome> biomeRegistry = ctx.lookup(Registries.BIOME);

//            ctx.register(CSStructures.WINTEREIS_CLUSTER, new WintereisClusterStructure(new Structure.StructureSettings(
//                    biomeRegistry.getOrThrow(BiomeTags.IS_END),
//                    Map.of(),
//                    GenerationStep.Decoration.SURFACE_STRUCTURES,
//                    TerrainAdjustment.NONE
//            )));
        }
    }

    public static class StructureSets {
        public static void bootstrap(BootstapContext<StructureSet> ctx) {
            HolderGetter<Structure> structureRegistry = ctx.lookup(Registries.STRUCTURE);

//            ctx.register(CSStructures.WINTEREIS_CLUSTER_SET, new StructureSet(
//                    structureRegistry.getOrThrow(CSStructures.WINTEREIS_CLUSTER),
//                    new RandomSpreadStructurePlacement(2200, 1500, RandomSpreadType.LINEAR, 1407818372))
//            );
        }
    }
}
