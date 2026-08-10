package org.thecelestialworkshop.celestisynth.common.registry;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CSSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Celestisynth.MODID);

    public static final Supplier<SoundEvent> STEP = createSound("step");
    public static final Supplier<SoundEvent> HOP = createSound("hop");
    public static final Supplier<SoundEvent> SWORD_SWING = createSound("sword_swing");
    public static final Supplier<SoundEvent> SWORD_SWING_FIRE = createSound("sword_swing_fire");
    public static final Supplier<SoundEvent> AIR_SWING = createSound("air_swing");
    public static final Supplier<SoundEvent> IMPACT_HIT = createSound("impact_hit");
    public static final Supplier<SoundEvent> SWORD_CLASH = createSound("sword_clash");
    public static final Supplier<SoundEvent> FIRE_SHOOT = createSound("fire_shoot");
    public static final Supplier<SoundEvent> WIND_STRIKE = createSound("wind_strike");
    public static final Supplier<SoundEvent> WHIRLWIND = createSound("whirlwind");
    public static final Supplier<SoundEvent> LOUD_IMPACT = createSound("loud_impact");
    public static final Supplier<SoundEvent> BLING = createSound("bling");
    public static final Supplier<SoundEvent> LASER_SHOOT = createSound("laser_shoot");
    public static final Supplier<SoundEvent> VANISH = createSound("vanish");
    public static final Supplier<SoundEvent> FROZEN_SLASH = createSound("frozen_slash");
    public static final Supplier<SoundEvent> ICE_CAST = createSound("ice_cast");
    public static final Supplier<SoundEvent> GROUND_IMPACT_WATER = createSound("ground_impact_water");
    public static final Supplier<SoundEvent> SLASH_WATER = createSound("slash_water");
    public static final Supplier<SoundEvent> WATER_CAST = createSound("water_cast");
    public static final Supplier<SoundEvent> BASS_DROP = createSound("bass_drop");
    public static final Supplier<SoundEvent> BASS_PULSE = createSound("bass_pulse");
    public static final Supplier<SoundEvent> HEARTBEAT = createSound("heartbeat");

    public static final Supplier<SoundEvent> TRAVERSER_DEATH = createSound("traverser_death");
    public static final Supplier<SoundEvent> TRAVERSER_HURT = createSound("traverser_hurt");
    public static final Supplier<SoundEvent> TRAVERSER_STEP = createSound("traverser_step");

    public static Supplier<SoundEvent> createSound(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Celestisynth.prefix(name)));
    }
}
