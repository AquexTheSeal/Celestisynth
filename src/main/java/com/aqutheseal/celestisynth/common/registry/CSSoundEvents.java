package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Celestisynth.MODID);

    public static final RegistryObject<SoundEvent> CS_STEP = SOUND_EVENTS.register("step", () -> new SoundEvent(Celestisynth.prefix("step")));
    public static final RegistryObject<SoundEvent> CS_HOP = SOUND_EVENTS.register("hop", () -> new SoundEvent(Celestisynth.prefix("hop")));
    public static final RegistryObject<SoundEvent> CS_SWORD_SWING = SOUND_EVENTS.register("sword_swing", () -> new SoundEvent(Celestisynth.prefix("sword_swing")));
    public static final RegistryObject<SoundEvent> CS_SWORD_SWING_FIRE = SOUND_EVENTS.register("sword_swing_fire", () -> new SoundEvent(Celestisynth.prefix("sword_swing_fire")));
    public static final RegistryObject<SoundEvent> CS_AIR_SWING = SOUND_EVENTS.register("air_swing", () -> new SoundEvent(Celestisynth.prefix("air_swing")));
    public static final RegistryObject<SoundEvent> CS_IMPACT_HIT = SOUND_EVENTS.register("impact_hit", () -> new SoundEvent(Celestisynth.prefix("impact_hit")));
    public static final RegistryObject<SoundEvent> CS_SWORD_CLASH = SOUND_EVENTS.register("sword_clash", () -> new SoundEvent(Celestisynth.prefix("sword_clash")));
    public static final RegistryObject<SoundEvent> CS_FIRE_SHOOT = SOUND_EVENTS.register("fire_shoot", () -> new SoundEvent(Celestisynth.prefix("fire_shoot")));
    public static final RegistryObject<SoundEvent> CS_WIND_STRIKE = SOUND_EVENTS.register("wind_strike", () -> new SoundEvent(Celestisynth.prefix("wind_strike")));
    public static final RegistryObject<SoundEvent> CS_WHIRLWIND = SOUND_EVENTS.register("whirlwind", () -> new SoundEvent(Celestisynth.prefix("whirlwind")));
    public static final RegistryObject<SoundEvent> CS_LOUD_IMPACT = SOUND_EVENTS.register("loud_impact", () -> new SoundEvent(Celestisynth.prefix("loud_impact")));
    public static final RegistryObject<SoundEvent> CS_BLING = SOUND_EVENTS.register("bling", () -> new SoundEvent(Celestisynth.prefix("bling")));
    public static final RegistryObject<SoundEvent> CS_LASER_SHOOT = SOUND_EVENTS.register("laser_shoot", () -> new SoundEvent(Celestisynth.prefix("laser_shoot")));
    public static final RegistryObject<SoundEvent> CS_VANISH = SOUND_EVENTS.register("vanish", () -> new SoundEvent(Celestisynth.prefix("vanish")));

}
