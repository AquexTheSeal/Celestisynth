package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.resources.ResourceLocation;

public class CSLootTables {
    public static final CSLootTables UNDERGROUND_DUNGEONS = new CSLootTables("underground_dungeons");
    public static final CSLootTables VANILLA_NETHER_STRUCTURES = new CSLootTables("vanilla_nether_structures");

    public final ResourceLocation location;

    private CSLootTables(String path) {
        this.location = Celestisynth.prefix(String.format("chests/%s", path));
    }
}
