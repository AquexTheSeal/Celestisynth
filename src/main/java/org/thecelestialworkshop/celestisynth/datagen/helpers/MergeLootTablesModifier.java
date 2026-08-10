package org.thecelestialworkshop.celestisynth.datagen.helpers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MergeLootTablesModifier extends LootModifier {
    public static final Supplier<MapCodec<MergeLootTablesModifier>> CODEC =
            Suppliers.memoize(() -> RecordCodecBuilder.mapCodec((inst) ->
                    codecStart(inst)
                    .and(ResourceLocation.CODEC.fieldOf("loot_table").forGetter((m) -> m.lootTableLocation))
                    .apply(inst, MergeLootTablesModifier::new)
            ));
    private final ResourceLocation lootTableLocation;

    public MergeLootTablesModifier(LootItemCondition[] conditions, ResourceLocation lootTableLocation) {
        super(conditions);
        this.lootTableLocation = lootTableLocation;
    }

    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
        LootTable lootTable = context.getLevel().getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, this.lootTableLocation));
        ObjectArrayList<ItemStack> lootContent = new ObjectArrayList<>();
        lootTable.getRandomItemsRaw(context, lootContent::add);
        loot.addAll(lootContent);
        return loot;
    }

    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
