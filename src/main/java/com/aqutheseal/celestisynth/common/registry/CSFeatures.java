package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.world.feature.LunarCraterFeature;
import com.aqutheseal.celestisynth.common.world.feature.SolarCraterFeature;
import com.aqutheseal.celestisynth.common.world.feature.ZephyrDepositFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Celestisynth.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SOLAR_CRATER = FEATURES.register("solar_crater", () -> new SolarCraterFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LUNAR_CRATER = FEATURES.register("lunar_crater", () -> new LunarCraterFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ZEPHYR_DEPOSIT = FEATURES.register("zephyr_deposit", () -> new ZephyrDepositFeature(NoneFeatureConfiguration.CODEC));

    public static final ResourceKey<ConfiguredFeature<?, ?>> SOLAR_CRATER_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Celestisynth.prefix("solar_crater"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUNAR_CRATER_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Celestisynth.prefix("lunar_crater"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ZEPHYR_DEPOSIT_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Celestisynth.prefix("zephyr_deposit"));

    public static final ResourceKey<PlacedFeature> SOLAR_CRATER_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Celestisynth.prefix("solar_crater"));
    public static final ResourceKey<PlacedFeature> LUNAR_CRATER_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Celestisynth.prefix("lunar_crater"));
    public static final ResourceKey<PlacedFeature> ZEPHYR_DEPOSIT_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Celestisynth.prefix("zephyr_deposit"));
}
