package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.common.registry.CSFeatures;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.MatchingBlockTagPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;

import java.util.List;

public class CSFeatureProvider {

    public static class ConfiguredFeatures {
        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
            ctx.register(CSFeatures.SOLAR_CRATER_CONFIGURED, new ConfiguredFeature<>(CSFeatures.SOLAR_CRATER.get(), new NoneFeatureConfiguration()));
            ctx.register(CSFeatures.LUNAR_CRATER_CONFIGURED, new ConfiguredFeature<>(CSFeatures.LUNAR_CRATER.get(), new NoneFeatureConfiguration()));
            ctx.register(CSFeatures.ZEPHYR_DEPOSIT_CONFIGURED, new ConfiguredFeature<>(CSFeatures.ZEPHYR_DEPOSIT.get(), new NoneFeatureConfiguration()));
            ctx.register(CSFeatures.WINTEREIS_SPIKES_CONFIGURED, new ConfiguredFeature<>(CSFeatures.WINTEREIS_SPIKES.get(), new NoneFeatureConfiguration()));
        }
    }

    public static class PlacedFeatures {
        public static void bootstrap(BootstapContext<PlacedFeature> ctx) {
            ctx.register(CSFeatures.SOLAR_CRATER_PLACED, new PlacedFeature(ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CSFeatures.SOLAR_CRATER_CONFIGURED),
                    List.of(
                            RarityFilter.onAverageOnceEvery(85), InSquarePlacement.spread(), BiomeFilter.biome()
                    )
            ));
            ctx.register(CSFeatures.LUNAR_CRATER_PLACED, new PlacedFeature(ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CSFeatures.LUNAR_CRATER_CONFIGURED),
                    List.of(
                            RarityFilter.onAverageOnceEvery(17),
                            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(30))),
                            EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), MatchingBlockTagPredicate.ONLY_IN_AIR_PREDICATE, 12),
                            BiomeFilter.biome()
                    )
            ));
            ctx.register(CSFeatures.ZEPHYR_DEPOSIT_PLACED, new PlacedFeature(ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CSFeatures.ZEPHYR_DEPOSIT_CONFIGURED),
                    List.of(
                            RarityFilter.onAverageOnceEvery(7),
                            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(100), VerticalAnchor.absolute(320))),
                            BiomeFilter.biome()
                    )
            ));
            ctx.register(CSFeatures.WINTEREIS_SPIKES_PLACED, new PlacedFeature(ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CSFeatures.WINTEREIS_SPIKES_CONFIGURED),
                    List.of(
                            CountPlacement.of(2),
                            HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(60))),
                            EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), MatchingBlockTagPredicate.ONLY_IN_AIR_PREDICATE, 12),
                            BiomeFilter.biome()
                    )
            ));
        }
    }

    public static class BiomeModifiers {
        public static void bootstrap(BootstapContext<BiomeModifier> ctx) {
            final HolderGetter<PlacedFeature> featureRegistry = ctx.lookup(Registries.PLACED_FEATURE);
            final HolderGetter<Biome> biomeRegistry = ctx.lookup(Registries.BIOME);

            ctx.register(CSFeatures.SOLAR_CRATER_MODIFIER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    biomeRegistry.getOrThrow(BiomeTags.IS_NETHER),
                    HolderSet.direct(featureRegistry.getOrThrow(CSFeatures.SOLAR_CRATER_PLACED)),
                    GenerationStep.Decoration.SURFACE_STRUCTURES
                    )
            );
            ctx.register(CSFeatures.LUNAR_CRATER_MODIFIER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    biomeRegistry.getOrThrow(BiomeTags.IS_OVERWORLD),
                    HolderSet.direct(featureRegistry.getOrThrow(CSFeatures.LUNAR_CRATER_PLACED)),
                    GenerationStep.Decoration.UNDERGROUND_STRUCTURES
                    )
            );
            ctx.register(CSFeatures.ZEPHYR_DEPOSIT_MODIFIER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                    biomeRegistry.getOrThrow(Tags.Biomes.IS_MOUNTAIN),
                    HolderSet.direct(featureRegistry.getOrThrow(CSFeatures.ZEPHYR_DEPOSIT_PLACED)),
                    GenerationStep.Decoration.SURFACE_STRUCTURES
                    )
            );
            ctx.register(CSFeatures.WINTEREIS_SPIKES_MODIFIER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                            biomeRegistry.getOrThrow(Tags.Biomes.IS_COLD_OVERWORLD),
                            HolderSet.direct(featureRegistry.getOrThrow(CSFeatures.WINTEREIS_SPIKES_PLACED)),
                            GenerationStep.Decoration.UNDERGROUND_STRUCTURES
                    )
            );
        }
    }
}
