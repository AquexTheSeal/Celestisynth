package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.world.feature.LunarCraterFeature;
import com.aqutheseal.celestisynth.world.feature.SolarCraterFeature;
import com.aqutheseal.celestisynth.world.feature.ZephyrDepositFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.MatchingBlockTagPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class CSFeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Celestisynth.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SOLAR_CRATER = FEATURES.register("solar_crater",
            () -> new SolarCraterFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LUNAR_CRATER = FEATURES.register("lunar_crater",
            () -> new LunarCraterFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ZEPHYR_DEPOSIT = FEATURES.register("zephyr_deposit",
            () -> new ZephyrDepositFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Celestisynth.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> SOLAR_CRATER_CONFIGURED = CONFIGURED_FEATURES.register("solar_crater_configured",
            () -> new ConfiguredFeature<>(CSFeatureRegistry.SOLAR_CRATER.get(), new NoneFeatureConfiguration())
    );

    public static final RegistryObject<ConfiguredFeature<?, ?>> LUNAR_CRATER_CONFIGURED = CONFIGURED_FEATURES.register("lunar_crater_configured",
            () -> new ConfiguredFeature<>(CSFeatureRegistry.LUNAR_CRATER.get(), new NoneFeatureConfiguration())
    );

    public static final RegistryObject<ConfiguredFeature<?, ?>> ZEPHYR_DEPOSIT_CONFIGURED = CONFIGURED_FEATURES.register("zephyr_deposit_configured",
            () -> new ConfiguredFeature<>(CSFeatureRegistry.ZEPHYR_DEPOSIT.get(), new NoneFeatureConfiguration())
    );

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Celestisynth.MODID);

    public static final RegistryObject<PlacedFeature> SOLAR_CRATER_PLACED = PLACED_FEATURES.register("solar_crater_placed",
            () -> new PlacedFeature(CSFeatureRegistry.SOLAR_CRATER_CONFIGURED.getHolder().get(),
                    List.of(RarityFilter.onAverageOnceEvery(50), CountOnEveryLayerPlacement.of(1), BiomeFilter.biome()))
    );

    public static final RegistryObject<PlacedFeature> LUNAR_CRATER_PLACED = PLACED_FEATURES.register("lunar_crater_placed",
            () -> new PlacedFeature(CSFeatureRegistry.LUNAR_CRATER_CONFIGURED.getHolder().get(),
                    List.of(RarityFilter.onAverageOnceEvery(13),
                            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(30))),
                            EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), MatchingBlockTagPredicate.ONLY_IN_AIR_PREDICATE, 12),
                            BiomeFilter.biome()
                    )
            )
    );

    public static final RegistryObject<PlacedFeature> ZEPHYR_DEPOSIT_PLACED = PLACED_FEATURES.register("zephyr_deposit_placed",
            () -> new PlacedFeature(CSFeatureRegistry.ZEPHYR_DEPOSIT_CONFIGURED.getHolder().get(),
                    List.of(RarityFilter.onAverageOnceEvery(7),
                            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(100), VerticalAnchor.absolute(320))),
                            BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(CSBlockRegistry.SOLAR_CRYSTAL.get().defaultBlockState(), BlockPos.ZERO)),
                            BiomeFilter.biome()
                    )
            )
    );
}
