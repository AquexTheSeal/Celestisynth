package org.thecelestialworkshop.celestisynth.common.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.thecelestialworkshop.celestisynth.Celestisynth;

public class CSMobSpawns {

    // Structure-Specific Spawn Modifiers
    public static final ResourceKey<StructureModifier> NETHER_FORTRESS_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.STRUCTURE_MODIFIERS, Celestisynth.prefix("nether_fortress_spawns"));
}
