package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.google.common.collect.Sets;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CSLootTableProvider extends LootTableProvider {
    protected static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

    public CSLootTableProvider(PackOutput pOutput) {
        super(pOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BlockHandler::new, LootContextParamSets.BLOCK)
        ));
    }

    public static class BlockHandler extends BlockLootSubProvider {

        protected BlockHandler() {
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
            return knownBlocksHelper(ForgeRegistries.BLOCKS);
        }

        protected LootTable.Builder createWintereisDrops(Block pBlock) {
            return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(pBlock).when(HAS_SILK_TOUCH)
                            .otherwise(LootItem.lootTableItem(CSItems.WINTEREIS_SHARD.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
                    ).when(ExplosionCondition.survivesExplosion())
            );
        }
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
        final var modLootTableIds = BuiltInLootTables.all().stream().filter(lootTable -> lootTable.getNamespace().equals(Celestisynth.MODID)).collect(Collectors.toSet());
        for (final var id : Sets.difference(modLootTableIds, map.keySet())) {
            context.reportProblem("Missing mod loot table: " + id);
        }
        map.forEach((id, lootTable) -> lootTable.validate(context.setParams(lootTable.getParamSet()).enterElement("{" + id + "}", new LootDataId<>(LootDataType.TABLE, id))));
    }

    public static <T> Set<T> knownBlocksHelper(final IForgeRegistry<T> registry) {
        return StreamSupport
                .stream(registry.spliterator(), false)
                .filter(entry -> Optional.ofNullable(registry.getKey(entry))
                        .filter(key -> key.getNamespace().equals(Celestisynth.MODID))
                        .isPresent()).collect(Collectors.toSet());
    }
}
