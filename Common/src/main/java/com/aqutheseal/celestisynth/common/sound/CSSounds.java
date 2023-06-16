package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSSoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "your_mod_id");

    public static final RegistryObject<SoundEvent> CS_STEP = SOUND_EVENTS.register("cs_step", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "cs_step")));
    public static final RegistryObject<SoundEvent> CS_SWORD_SWING = SOUND_EVENTS.register("solaris_1", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_1")));
    public static final RegistryObject<SoundEvent> CS_SWORD_SWING_FIRE = SOUND_EVENTS.register("solaris_2", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_2")));
    public static final RegistryObject<SoundEvent> CS_AIR_SWING = SOUND_EVENTS.register("solaris_3", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_3")));
    public static final RegistryObject<SoundEvent> CS_IMPACT_HIT = SOUND_EVENTS.register("solaris_4", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_4")));
    public static final RegistryObject<SoundEvent> CS_SWORD_CLASH = SOUND_EVENTS.register("solaris_5", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_5")));
    public static final RegistryObject<SoundEvent> CS_FIRE_SHOOT = SOUND_EVENTS.register("solaris_6", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_6")));
    public static final RegistryObject<SoundEvent> CS_WIND_STRIKE = SOUND_EVENTS.register("cs_wind_strike", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "cs_wind_strike")));
    public static final RegistryObject<SoundEvent> CS_WHIRLWIND = SOUND_EVENTS.register("cs_whirlwind", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "cs_whirlwind")));
}
