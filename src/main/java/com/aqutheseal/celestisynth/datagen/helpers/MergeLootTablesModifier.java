package com.aqutheseal.celestisynth.datagen.helpers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MergeLootTablesModifier extends LootModifier {
    public static final Supplier<Codec<MergeLootTablesModifier>> CODEC =
            Suppliers.memoize(() -> RecordCodecBuilder.create((inst) ->
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
        LootTable lootTable = context.getLevel().getServer().getLootData().getLootTable(this.lootTableLocation);
        ObjectArrayList<ItemStack> lootContent = new ObjectArrayList<>();
        lootTable.getRandomItemsRaw(context, lootContent::add);
        loot.addAll(lootContent);
        return loot;
    }

    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
