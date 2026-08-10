package org.thecelestialworkshop.celestisynth.datagen.providers.loot;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CSBlockLoot extends BlockLootSubProvider {

    public CSBlockLoot(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        this.dropSelf(CSBlocks.CELESTIAL_CRAFTING_TABLE.get());
        this.dropSelf(CSBlocks.STARLIT_FACTORY.get());
        this.dropSelf(CSBlocks.LUNAR_STONE.get());
        this.dropSelf(CSBlocks.SOLAR_CRYSTAL.get());
        this.dropSelf(CSBlocks.ZEPHYR_DEPOSIT.get());

        this.add(CSBlocks.WINTEREIS.get(), this::createWintereisDrops);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return StreamSupport.stream(BuiltInRegistries.BLOCK.spliterator(), false)
                .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(Celestisynth.MODID))
                .collect(Collectors.toSet());
    }

    protected LootTable.Builder createWintereisDrops(Block pBlock) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(pBlock).when(this.hasSilkTouch())
                        .otherwise(LootItem.lootTableItem(CSItems.WINTEREIS_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
                ).when(ExplosionCondition.survivesExplosion())
        );
    }
}
