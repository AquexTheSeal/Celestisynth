package org.thecelestialworkshop.celestisynth.datagen.providers;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.datagen.providers.loot.CSBlockLoot;
import org.thecelestialworkshop.celestisynth.datagen.providers.loot.CSChestLoot;
import org.thecelestialworkshop.celestisynth.datagen.providers.loot.CSEntityLoot;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

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
                new LootTableProvider.SubProviderEntry(CSChestLoot::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(CSEntityLoot::new, LootContextParamSets.ENTITY)
        ));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext context) {
        map.forEach((id, table) -> table.validate(context.setParams(table.getParamSet()).enterElement("{" + id + "}", new LootDataId<>(LootDataType.TABLE, id))));
    }

    public static <T> Set<T> knownSet(final IForgeRegistry<T> registry) {
        return StreamSupport
                .stream(registry.spliterator(), false)
                .filter(entry -> Optional.ofNullable(registry.getKey(entry))
                        .filter(key -> key.getNamespace().equals(Celestisynth.MODID))
                        .isPresent()).collect(Collectors.toSet());
    }
}
