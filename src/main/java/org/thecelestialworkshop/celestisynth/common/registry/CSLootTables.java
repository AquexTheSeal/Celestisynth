package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class CSLootTables {
    public static final CSLootTables UNDERGROUND_DUNGEONS = new CSLootTables("underground_dungeons");
    public static final CSLootTables VANILLA_NETHER_STRUCTURES = new CSLootTables("vanilla_nether_structures");

    public final ResourceLocation location;
    public final ResourceKey<LootTable> key;

    private CSLootTables(String path) {
        this.location = Celestisynth.prefix(String.format("chests/%s", path));
        this.key = ResourceKey.create(Registries.LOOT_TABLE, this.location);
    }
}
