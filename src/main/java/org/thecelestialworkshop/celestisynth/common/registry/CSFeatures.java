package org.thecelestialworkshop.celestisynth.common.registry;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.world.feature.LunarCraterFeature;
import org.thecelestialworkshop.celestisynth.common.world.feature.SolarCraterFeature;
import org.thecelestialworkshop.celestisynth.common.world.feature.WintereisSpikesFeature;
import org.thecelestialworkshop.celestisynth.common.world.feature.ZephyrDepositFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CSFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, Celestisynth.MODID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> SOLAR_CRATER = FEATURES.register("solar_crater", () -> new SolarCraterFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> LUNAR_CRATER = FEATURES.register("lunar_crater", () -> new LunarCraterFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> ZEPHYR_DEPOSIT = FEATURES.register("zephyr_deposit", () -> new ZephyrDepositFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> WINTEREIS_SPIKES = FEATURES.register("wintereis_spikes", () -> new WintereisSpikesFeature(NoneFeatureConfiguration.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> SOLAR_CRATER_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Celestisynth.prefix("solar_crater"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUNAR_CRATER_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Celestisynth.prefix("lunar_crater"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ZEPHYR_DEPOSIT_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Celestisynth.prefix("zephyr_deposit"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> WINTEREIS_SPIKES_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Celestisynth.prefix("wintereis_spikes"));

    public static final ResourceKey<PlacedFeature> SOLAR_CRATER_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Celestisynth.prefix("solar_crater"));
    public static final ResourceKey<PlacedFeature> LUNAR_CRATER_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Celestisynth.prefix("lunar_crater"));
    public static final ResourceKey<PlacedFeature> ZEPHYR_DEPOSIT_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Celestisynth.prefix("zephyr_deposit"));
    public static final ResourceKey<PlacedFeature> WINTEREIS_SPIKES_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Celestisynth.prefix("wintereis_spikes"));

    public static final ResourceKey<BiomeModifier> SOLAR_CRATER_MODIFIER = ResourceKey.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIERS, Celestisynth.prefix("solar_crater"));
    public static final ResourceKey<BiomeModifier> LUNAR_CRATER_MODIFIER = ResourceKey.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIERS, Celestisynth.prefix("lunar_crater"));
    public static final ResourceKey<BiomeModifier> ZEPHYR_DEPOSIT_MODIFIER = ResourceKey.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIERS, Celestisynth.prefix("zephyr_deposit"));
    public static final ResourceKey<BiomeModifier> WINTEREIS_SPIKES_MODIFIER = ResourceKey.create(net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.BIOME_MODIFIERS, Celestisynth.prefix("wintereis_spikes"));
}
