package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.item.BreezebreakerItem;
import com.aqutheseal.celestisynth.item.CrescentiaItem;
import com.aqutheseal.celestisynth.item.SolarisItem;
import com.aqutheseal.celestisynth.item.helpers.CSRarityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Celestisynth.MODID);

    public static final RegistryObject<Item> SOLARIS = ITEMS.register("solaris", () -> new SolarisItem(Tiers.NETHERITE,  7 - 4, -2.5F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL)));
    public static final RegistryObject<Item> CRESCENTIA = ITEMS.register("crescentia", () -> new CrescentiaItem(Tiers.NETHERITE, 8 - 4, -2.7F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL)));
    public static final RegistryObject<Item> BREEZEBREAKER = ITEMS.register("breezebreaker", () -> new BreezebreakerItem(Tiers.NETHERITE, 5 - 4, -2.0F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL)));

}
