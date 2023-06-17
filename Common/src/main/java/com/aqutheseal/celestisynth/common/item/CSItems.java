package com.aqutheseal.celestisynth.common.item;

import com.aqutheseal.celestisynth.common.block.CSBlocks;
import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.item.helpers.CSRarityTypes;
import com.aqutheseal.celestisynth.reg.RegistrationProvider;
import com.aqutheseal.celestisynth.reg.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class CSItems {

    public static final RegistrationProvider<Item> ITEM = RegistrationProvider.get(Registries.ITEM, Celestisynth.MODID);

    public static final RegistryObject<Item> SOLARIS = createItem(() -> new SolarisItem(Tiers.NETHERITE,  7 - 4, -2.5F, (new Item.Properties()).fireResistant().rarity(Rarity.EPIC)), "solaris");
    public static final RegistryObject<Item> CRESCENTIA = createItem(() ->  new CrescentiaItem(Tiers.NETHERITE, 8 - 4, -2.7F, (new Item.Properties()).fireResistant().rarity(Rarity.EPIC)), "crescentia");
    public static final RegistryObject<Item> BREEZEBREAKER = createItem(() -> new BreezebreakerItem(Tiers.NETHERITE, 5 - 4, -2.0F, (new Item.Properties()).fireResistant().rarity(Rarity.EPIC)), "breezebreaker");

    public static final RegistryObject<Item> LUNAR_STONE = createItem(CSBlocks.LUNAR_STONE);
    public static final RegistryObject<Item> SOLAR_CRYSTAL = createItem(CSBlocks.SOLAR_CRYSTAL);
    public static RegistryObject<Item> createItem(RegistryObject<? extends Block> block) {
        return block == null ? null : createItem(() -> new BlockItem(block.get(), new Item.Properties()), block);
    }


    public static <T extends Item> RegistryObject<T> createItem(Supplier<? extends T> item, RegistryObject<? extends Block> block) {
        return createItem(item, block.getId().getPath());
    }

    public static <T extends Item> RegistryObject<T> createItem(Supplier<? extends T> item, String id) {
        return ITEM.register(id, item);
    }

    public static void init() {

    }
}
