package com.aqutheseal.celestisynth.datagen.providers.loot;

import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.datagen.providers.CSLootTableProvider;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class CSBlockLoot extends BlockLootSubProvider {

    public CSBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(CSBlocks.CELESTIAL_CRAFTING_TABLE.get());
        this.dropSelf(CSBlocks.LUNAR_STONE.get());
        this.dropSelf(CSBlocks.SOLAR_CRYSTAL.get());
        this.dropSelf(CSBlocks.ZEPHYR_DEPOSIT.get());

        this.add(CSBlocks.WINTEREIS.get(), this::createWintereisDrops);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return CSLootTableProvider.knownBlocksHelper(ForgeRegistries.BLOCKS);
    }

    protected LootTable.Builder createWintereisDrops(Block pBlock) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(pBlock).when(HAS_SILK_TOUCH)
                        .otherwise(LootItem.lootTableItem(CSItems.WINTEREIS_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
                ).when(ExplosionCondition.survivesExplosion())
        );
    }
}
