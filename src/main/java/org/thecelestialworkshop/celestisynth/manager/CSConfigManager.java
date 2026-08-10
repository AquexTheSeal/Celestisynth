package org.thecelestialworkshop.celestisynth.manager;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.config.client.CSClientConfig;
import org.thecelestialworkshop.celestisynth.config.common.CSCommonConfig;

public final class CSConfigManager {
    public static final ModConfigSpec COMMON_SPEC;
    public static final CSCommonConfig COMMON;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final CSClientConfig CLIENT;

    static {
        final Pair<CSCommonConfig, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(CSCommonConfig::new);
        final Pair<CSClientConfig, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(CSClientConfig::new);

        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static void registerConfigs() {
        var container = ModLoadingContext.get().getActiveContainer();
        container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, Celestisynth.MODID + "/celestisynth-client.toml");
        container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, Celestisynth.MODID + "/celestisynth-common.toml");
    }
}
