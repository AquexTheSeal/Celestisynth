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
}
