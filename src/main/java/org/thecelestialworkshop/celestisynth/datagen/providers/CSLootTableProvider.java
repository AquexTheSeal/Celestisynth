package org.thecelestialworkshop.celestisynth.datagen.providers;

import org.thecelestialworkshop.celestisynth.datagen.providers.loot.CSBlockLoot;
import org.thecelestialworkshop.celestisynth.datagen.providers.loot.CSChestLoot;
import org.thecelestialworkshop.celestisynth.datagen.providers.loot.CSEntityLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CSLootTableProvider extends LootTableProvider {

    public CSLootTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(CSBlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(CSChestLoot::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(CSEntityLoot::new, LootContextParamSets.ENTITY)
        ), pRegistries);
    }
}
