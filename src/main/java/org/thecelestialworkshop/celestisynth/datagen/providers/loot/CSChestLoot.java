package org.thecelestialworkshop.celestisynth.datagen.providers.loot;

import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import org.thecelestialworkshop.celestisynth.common.registry.CSLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class CSChestLoot implements LootTableSubProvider {

    public CSChestLoot(HolderLookup.Provider pRegistries) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> pOutput) {
        pOutput.accept(CSLootTables.UNDERGROUND_DUNGEONS.key,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 3))
                                .add(LootItem.lootTableItem(CSBlocks.LUNAR_STONE.get()))
                        )
        );
        pOutput.accept(CSLootTables.VANILLA_NETHER_STRUCTURES.key,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(3, 6))
                                .add(LootItem.lootTableItem(CSBlocks.SOLAR_CRYSTAL.get()))
                        )
        );
    }
}
