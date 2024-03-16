package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Celestisynth.MODID);

    public static final RegistryObject<SimpleParticleType> BREEZEBROKEN = PARTICLE_TYPES.register("breezebroken", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RAINFALL_BEAM = PARTICLE_TYPES.register("rainfall_beam", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RAINFALL_BEAM_QUASAR = PARTICLE_TYPES.register("rainfall_beam_quasar", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RAINFALL_ENERGY = PARTICLE_TYPES.register("rainfall_energy", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RAINFALL_ENERGY_SMALL = PARTICLE_TYPES.register("rainfall_energy_small", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WATER_DROP = PARTICLE_TYPES.register("water_drop", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> KERES_OMEN = PARTICLE_TYPES.register("keres_omen", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> KERES_ASH = PARTICLE_TYPES.register("keres_ash", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SOLARIS_FLAME = PARTICLE_TYPES.register("solaris_flame", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CRESCENTIA_FIREWORK_PURPLE = PARTICLE_TYPES.register("crescentia_firework_purple", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CRESCENTIA_FIREWORK_PINK = PARTICLE_TYPES.register("crescentia_firework_pink", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> CRESCENTIA_FIREWORK_BLUE = PARTICLE_TYPES.register("crescentia_firework_blue", () -> new SimpleParticleType(true));
}
