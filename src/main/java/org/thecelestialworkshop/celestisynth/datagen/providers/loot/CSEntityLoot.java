package org.thecelestialworkshop.celestisynth.datagen.providers.loot;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CSEntityLoot extends EntityLootSubProvider {
    public CSEntityLoot(HolderLookup.Provider pRegistries) {
        super(FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    public void generate() {
        this.add(CSEntityTypes.TEMPEST.get(), LootTable.lootTable());
        this.add(CSEntityTypes.STAR_MONOLITH.get(), LootTable.lootTable());
        this.add(CSEntityTypes.TRAVERSER.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(CSItems.CRISMSON_PIECE.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                        )
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                )
        );
        this.add(CSEntityTypes.VEILGUARD.get(), LootTable.lootTable());
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return StreamSupport.stream(BuiltInRegistries.ENTITY_TYPE.spliterator(), false)
                .filter(type -> BuiltInRegistries.ENTITY_TYPE.getKey(type).getNamespace().equals(Celestisynth.MODID));
    }
}
