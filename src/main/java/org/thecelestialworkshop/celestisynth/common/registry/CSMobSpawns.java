package org.thecelestialworkshop.celestisynth.common.registry;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.world.StructureModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import org.thecelestialworkshop.celestisynth.Celestisynth;

public class CSMobSpawns {

    // Structure-Specific Spawn Modifiers
    public static final ResourceKey<StructureModifier> NETHER_FORTRESS_SPAWNS = ResourceKey.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.STRUCTURE_MODIFIERS, Celestisynth.prefix("nether_fortress_spawns"));
}
