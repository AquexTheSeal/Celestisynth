package org.thecelestialworkshop.celestisynth.common.registry;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CSParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Celestisynth.MODID);

    public static final Supplier<SimpleParticleType> BREEZEBROKEN = PARTICLE_TYPES.register("breezebroken", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> RAINFALL_BEAM = PARTICLE_TYPES.register("rainfall_beam", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> RAINFALL_BEAM_QUASAR = PARTICLE_TYPES.register("rainfall_beam_quasar", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> RAINFALL_ENERGY = PARTICLE_TYPES.register("rainfall_energy", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> RAINFALL_ENERGY_SMALL = PARTICLE_TYPES.register("rainfall_energy_small", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> WATER_DROP = PARTICLE_TYPES.register("water_drop", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> KERES_OMEN = PARTICLE_TYPES.register("keres_omen", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> KERES_ASH = PARTICLE_TYPES.register("keres_ash", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> SOLARIS_FLAME = PARTICLE_TYPES.register("solaris_flame", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> CRESCENTIA_FIREWORK_PURPLE = PARTICLE_TYPES.register("crescentia_firework_purple", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> CRESCENTIA_FIREWORK_PINK = PARTICLE_TYPES.register("crescentia_firework_pink", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> CRESCENTIA_FIREWORK_BLUE = PARTICLE_TYPES.register("crescentia_firework_blue", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> PULSATION = PARTICLE_TYPES.register("pulsation", () -> new SimpleParticleType(true));
}
