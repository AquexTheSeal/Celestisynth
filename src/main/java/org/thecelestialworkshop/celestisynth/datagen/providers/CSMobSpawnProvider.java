package org.thecelestialworkshop.celestisynth.datagen.providers;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.StructureModifier;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSMobSpawns;
import org.thecelestialworkshop.celestisynth.common.registry.CSStructureModifiers;
import org.thecelestialworkshop.celestisynth.common.registry.CSTags;

import java.util.List;

public class CSMobSpawnProvider {
    public static class StructureModifiers {
        public static void bootstrap(BootstapContext<StructureModifier> ctx) {
            final HolderGetter<Structure> structureRegistry = ctx.lookup(Registries.STRUCTURE);

            ctx.register(CSMobSpawns.NETHER_FORTRESS_SPAWNS, new CSStructureModifiers.AddSpawnsStructureTagModifier(
                    CSTags.Structures.NETHER_MONOLITH_SPAWN, List.of(
                            new MobSpawnSettings.SpawnerData(CSEntityTypes.STAR_MONOLITH.get(), 5, 1, 1)
                    ))
            );
        }
    }

    public static class BiomeModifiers {
        public static void bootstrap(BootstapContext<BiomeModifier> ctx) {
        }
    }
}
