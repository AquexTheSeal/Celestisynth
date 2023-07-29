package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.item.*;
import com.aqutheseal.celestisynth.item.helpers.CSRarityTypes;
import com.aqutheseal.celestisynth.item.weapons.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Celestisynth.MODID);

    public static final RegistryObject<Item> CELESTIAL_CORE = ITEMS.register("celestial_core", () -> new CelestialCoreItem(new Item.Properties().tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> CELESTIAL_CORE_HEATED = ITEMS.register("celestial_core_heated", () -> new CelestialCoreItem(new Item.Properties().tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> SUPERNAL_NETHERITE_INGOT = ITEMS.register("supernal_netherite_ingot", () -> new Item(new Item.Properties().tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> LUNAR_SCRAP = ITEMS.register("lunar_scrap", () -> new Item(new Item.Properties().tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> EYEBOMINATION = ITEMS.register("eyebomination", () -> new Item(new Item.Properties().tab(CSCreativeTabRegistry.CELESTISYNTH)));

    public static final RegistryObject<Item> SOLARIS = ITEMS.register("solaris", () -> new SolarisItem(Tiers.NETHERITE,  7 - 4, -2.5F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> CRESCENTIA = ITEMS.register("crescentia", () -> new CrescentiaItem(Tiers.NETHERITE, 8 - 4, -2.7F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> BREEZEBREAKER = ITEMS.register("breezebreaker", () -> new BreezebreakerItem(Tiers.NETHERITE, 5 - 4, -2.0F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> POLTERGEIST = ITEMS.register("poltergeist", () -> new PoltergeistItem(Tiers.NETHERITE, 10 - 4, -3.1F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabRegistry.CELESTISYNTH)));
    public static final RegistryObject<Item> AQUAFLORA = ITEMS.register("aquaflora", () -> new AquafloraItem(Tiers.NETHERITE, 2 - 4, -1.1F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabRegistry.CELESTISYNTH)));

}
