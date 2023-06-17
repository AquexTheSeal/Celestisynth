package com.aqutheseal.celestisynth.common.world.feature;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.reg.RegistrationProvider;
import com.aqutheseal.celestisynth.reg.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CSFeatures {
    public static final RegistrationProvider<Feature<?>> FEATURES = RegistrationProvider.get(Registries.FEATURE, Celestisynth.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SOLAR_CRATER = FEATURES.register("solar_crater",
            () -> new SolarCraterFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LUNAR_CRATER = FEATURES.register("lunar_crater",
            () -> new LunarCraterFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ZEPHYR_DEPOSIT = FEATURES.register("zephyr_deposit",
            () -> new ZephyrDepositFeature(NoneFeatureConfiguration.CODEC)
    );

    public static void init(){

    }
}
