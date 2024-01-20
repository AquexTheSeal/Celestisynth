package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.item.misc.CelestialCoreItem;
import com.aqutheseal.celestisynth.common.item.weapons.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Celestisynth.MODID);

    public static final RegistryObject<Item> CELESTIAL_CORE = ITEMS.register("celestial_core", () -> new CelestialCoreItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> CELESTIAL_CORE_HEATED = ITEMS.register("celestial_core_heated", () -> new CelestialCoreItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> SUPERNAL_NETHERITE_INGOT = ITEMS.register("supernal_netherite_ingot", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> CELESTIAL_NETHERITE_INGOT = ITEMS.register("celestial_netherite_ingot", () -> new Item(new Item.Properties().rarity(Rarity.RARE).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> LUNAR_SCRAP = ITEMS.register("lunar_scrap", () -> new Item(new Item.Properties().tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> EYEBOMINATION = ITEMS.register("eyebomination", () -> new Item(new Item.Properties().tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> STARSTRUCK_SCRAP = ITEMS.register("starstruck_scrap", () -> new Item(new Item.Properties().tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> STARSTRUCK_FEATHER = ITEMS.register("starstruck_feather", () -> new Item(new Item.Properties().tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> TEMPEST_SPAWN_EGG = ITEMS.register("tempest_spawn_egg", () -> new ForgeSpawnEggItem(CSEntityTypes.TEMPEST, 0, 0, new Item.Properties()));

    public static final RegistryObject<Item> SOLARIS = ITEMS.register("solaris", () -> new SolarisItem(Tiers.NETHERITE,  7 - 4, -2.5F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> CRESCENTIA = ITEMS.register("crescentia", () -> new CrescentiaItem(Tiers.NETHERITE, 8 - 4, -2.7F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> BREEZEBREAKER = ITEMS.register("breezebreaker", () -> new BreezebreakerItem(Tiers.NETHERITE, 5 - 4, -2.0F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> POLTERGEIST = ITEMS.register("poltergeist", () -> new PoltergeistItem(Tiers.NETHERITE, 10 - 4, -3.1F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> AQUAFLORA = ITEMS.register("aquaflora", () -> new AquafloraItem(Tiers.NETHERITE, 2 - 4, -1.1F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> RAINFALL_SERENITY = ITEMS.register("rainfall_serenity", () -> new RainfallSerenityItem((new Item.Properties()).fireResistant().durability(1200).rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabs.CELESTISYNTH)));
    public static final RegistryObject<Item> FROSTBOUND = ITEMS.register("frostbound", () -> new FrostboundItem(Tiers.NETHERITE,  9 - 4, -2.7F, (new Item.Properties()).fireResistant().rarity(CSRarityTypes.CELESTIAL).tab(CSCreativeTabs.CELESTISYNTH)));

}
