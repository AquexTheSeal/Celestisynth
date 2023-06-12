package com.aqutheseal.celestisynth.mixin;

import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Set;

@Mixin(ConfigTracker.class)
public interface ConfigTrackerAccessor {

    @Invoker("openConfig")
    void invokeOpenConfig(final ModConfig config, final Path configBasePath);

    @Accessor
    EnumMap<ModConfig.Type, Set<ModConfig>> getConfigSets();

}
