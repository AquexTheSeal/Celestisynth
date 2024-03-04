package com.aqutheseal.celestisynth.datagen.providers.loot;

import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSLootTables;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class CSChestLoot implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> pOutput) {
        pOutput.accept(CSLootTables.UNDERGROUND_DUNGEONS.location,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 3))
                                .add(LootItem.lootTableItem(CSBlocks.LUNAR_STONE.get()))
                        )
        );
        pOutput.accept(CSLootTables.VANILLA_NETHER_STRUCTURES.location,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(3, 6))
                                .add(LootItem.lootTableItem(CSBlocks.SOLAR_CRYSTAL.get()))
                        )
        );
    }
}
