package com.aqutheseal.celestisynth.common.sound;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.reg.RegistrationProvider;
import com.aqutheseal.celestisynth.reg.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class CSSounds {

    public static final RegistrationProvider<SoundEvent> SOUND_EVENTS = RegistrationProvider.get(Registries.SOUND_EVENT, Celestisynth.MODID);

    public static final RegistryObject<SoundEvent> CS_STEP = SOUND_EVENTS.register("cs_step", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "cs_step")));
    public static final RegistryObject<SoundEvent> CS_SWORD_SWING = SOUND_EVENTS.register("solaris_1", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_1")));
    public static final RegistryObject<SoundEvent> CS_SWORD_SWING_FIRE = SOUND_EVENTS.register("solaris_2", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_2")));
    public static final RegistryObject<SoundEvent> CS_AIR_SWING = SOUND_EVENTS.register("solaris_3", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_3")));
    public static final RegistryObject<SoundEvent> CS_IMPACT_HIT = SOUND_EVENTS.register("solaris_4", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_4")));
    public static final RegistryObject<SoundEvent> CS_SWORD_CLASH = SOUND_EVENTS.register("solaris_5", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_5")));
    public static final RegistryObject<SoundEvent> CS_FIRE_SHOOT = SOUND_EVENTS.register("solaris_6", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "solaris_6")));
    public static final RegistryObject<SoundEvent> CS_WIND_STRIKE = SOUND_EVENTS.register("cs_wind_strike", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "cs_wind_strike")));
    public static final RegistryObject<SoundEvent> CS_WHIRLWIND = SOUND_EVENTS.register("cs_whirlwind", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Celestisynth.MODID, "cs_whirlwind")));

    public static void init(){

    }
}
