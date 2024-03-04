package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.datagen.providers.loot.CSBlockLoot;
import com.aqutheseal.celestisynth.datagen.providers.loot.CSChestLoot;
import com.google.common.collect.Sets;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CSLootTableProvider extends LootTableProvider {

    public CSLootTableProvider(PackOutput pOutput) {
        super(pOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(CSBlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(CSChestLoot::new, LootContextParamSets.CHEST)
        ));
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
