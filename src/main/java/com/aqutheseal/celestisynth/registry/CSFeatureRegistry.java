package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.world.feature.LunarCraterFeature;
import com.aqutheseal.celestisynth.world.feature.SolarCraterFeature;
import com.aqutheseal.celestisynth.world.feature.ZephyrDepositFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
}
