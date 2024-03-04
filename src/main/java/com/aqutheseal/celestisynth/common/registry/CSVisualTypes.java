package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualAnimation;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualModel;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.entity.helper.skinset.FrostboundSlashSkinSet;
import com.aqutheseal.celestisynth.common.item.weapons.FrostboundItem;
import com.aqutheseal.celestisynth.common.item.weapons.RainfallSerenityItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CSVisualTypes {
    public static final ResourceKey<Registry<CSVisualType>> VISUALS_KEY = ResourceKey.createRegistryKey(Celestisynth.prefix("cs_visuals"));

    public static final DeferredRegister<CSVisualType> VISUALS = DeferredRegister.create(VISUALS_KEY, Celestisynth.MODID);

    public static final RegistryObject<CSVisualType> SOLARIS_BLITZ = addVisual(new CSVisualType("solaris_spin", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 2.5, true, true, false));
    public static final RegistryObject<CSVisualType> SOLARIS_BLITZ_SOUL = addVisual(new CSVisualType("solaris_spin_soul", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 3.5, true, true, false));
    public static final RegistryObject<CSVisualType> SOLARIS_AIR = addVisual(new CSVisualType("solaris_air", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 2.5, true, true, false));
    public static final RegistryObject<CSVisualType> SOLARIS_AIR_LARGE = addVisual(new CSVisualType("solaris_air_large", "solaris_air", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 3.5, true, true, false));
    public static final RegistryObject<CSVisualType> SOLARIS_AIR_FLAT = addVisual(new CSVisualType("solaris_air_flat", "solaris_air", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 1.5, false, true, false));
    public static final RegistryObject<CSVisualType> SOLARIS_AIR_MEDIUM_FLAT = addVisual(new CSVisualType("solaris_air_medium_flat", "solaris_air", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 2.5, false, true, false));
    public static final RegistryObject<CSVisualType> SOLARIS_AIR_LARGE_FLAT = addVisual(new CSVisualType("solaris_air_large_flat", "solaris_air", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 3.5, false, true, false));

    public static final RegistryObject<CSVisualType> CRESCENTIA_STRIKE = addVisual(new CSVisualType("crescentia_strike", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(12), 6, 2, 3.5, false, true, true));
    public static final RegistryObject<CSVisualType> CRESCENTIA_STRIKE_INVERTED = addVisual(new CSVisualType("crescentia_strike_inverted", "crescentia_strike", CSVisualModel.FLAT_INVERTED, CSVisualAnimation.noAnimWithLifespan(12), 6, 2, 3.5, false, true, true));
    public static final RegistryObject<CSVisualType> CRESCENTIA_THROW = addVisual(new CSVisualType("crescentia_throw", CSVisualModel.FLAT, CSVisualAnimation.SWEEP_RTOL, 0, 0, 3, false, true, true));
    public static final RegistryObject<CSVisualType> CRESCENTIA_THROW_INVERTED = addVisual(new CSVisualType("crescentia_throw_inverted", "crescentia_throw", CSVisualModel.FLAT, CSVisualAnimation.SWEEP_LTOR, 0, 0, 3, false, true, true));

    public static final RegistryObject<CSVisualType> BREEZEBREAKER_SLASH = addVisual(new CSVisualType("breezebreaker_slash", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(10), 5, 2, 3, false, false, true));
    public static final RegistryObject<CSVisualType> BREEZEBREAKER_SLASH_INVERTED = addVisual(new CSVisualType("breezebreaker_slash_inverted", "breezebreaker_slash", CSVisualModel.FLAT_INVERTED, CSVisualAnimation.noAnimWithLifespan(10), 5, 2, 3, false, false, true));
    public static final RegistryObject<CSVisualType> BREEZEBREAKER_WHEEL = addVisual(new CSVisualType("breezebreaker_wheel", CSVisualModel.FLAT_VERTICAL_SIDEFACE, CSVisualAnimation.noAnimWithLifespan(12), 6, 2, 4, false, false, false));
    public static final RegistryObject<CSVisualType> BREEZEBREAKER_WHEEL_IMPACT = addVisual(new CSVisualType("breezebreaker_wheel_impact", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(12), 6, 2, 2, false, false, true));
    public static final RegistryObject<CSVisualType> BREEZEBREAKER_DASH = addVisual(new CSVisualType("breezebreaker_dash", CSVisualModel.SIX_WAY_CROSS, CSVisualAnimation.STRETCH, 16, 2, 3, false, true, false));
    public static final RegistryObject<CSVisualType> BREEZEBREAKER_DASH_2 = addVisual(new CSVisualType("breezebreaker_dash_2", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(15), 0, 0, 4, false, true, false));
    public static final RegistryObject<CSVisualType> BREEZEBREAKER_DASH_3 = addVisual(new CSVisualType("breezebreaker_dash_3", "breezebreaker_dash_2", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(10), 0, 0, 2, false, true, false));

    public static final RegistryObject<CSVisualType> POLTERGEIST_WARD = addVisual(new CSVisualType("poltergeist_ward", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.GOO, 0, 0, 1.2, false, false, false));
    public static final RegistryObject<CSVisualType> POLTERGEIST_WARD_GROUND = addVisual(new CSVisualType("poltergeist_ward_ground", CSVisualModel.FLAT, CSVisualAnimation.SLOW_ROTATION, 0, 0, 1.55, false, false, false));
    public static final RegistryObject<CSVisualType> POLTERGEIST_WARD_ABSORB = addVisual(new CSVisualType("poltergeist_ward_absorb", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 4, false, true, false));
    public static final RegistryObject<CSVisualType> POLTERGEIST_WARD_SUMMON_SMALL = addVisual(new CSVisualType("poltergeist_ward_summon_small", "poltergeist_ward_summon", CSVisualModel.WALL, CSVisualAnimation.ASCEND, 0, 0, 0.95, false, true, false));
    public static final RegistryObject<CSVisualType> POLTERGEIST_WARD_SUMMON = addVisual(new CSVisualType("poltergeist_ward_summon", CSVisualModel.WALL, CSVisualAnimation.ASCEND, 0, 0, 1.3, false, true, false));
    public static final RegistryObject<CSVisualType> POLTERGEIST_IMPACT_CRACK = addVisual(new CSVisualType("poltergeist_impact_crack", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(20), 0, 0, 2.5, false, true, false));
    public static final RegistryObject<CSVisualType> POLTERGEIST_IMPACT_CRACK_LARGE = addVisual(new CSVisualType("poltergeist_impact_crack_large", "poltergeist_impact_crack", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(20), 0, 0, 3.5, false, true, false));
    public static final RegistryObject<CSVisualType> POLTERGEIST_RETREAT = addVisual(new CSVisualType("poltergeist_retreat", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(15), 0, 0, 2.25, false, true, false));

    public static final RegistryObject<CSVisualType> AQUAFLORA_SLICE = addVisual(new CSVisualType("aquaflora_slice", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(6), 6, 1, 1.2, false, false, false));
    public static final RegistryObject<CSVisualType> AQUAFLORA_SLICE_INVERTED = addVisual(new CSVisualType("aquaflora_slice_inverted", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(6), 6, 1, 1.2, false, false, false));
    public static final RegistryObject<CSVisualType> AQUAFLORA_PIERCE_START = addVisual(new CSVisualType("aquaflora_pierce_start", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(12), 6, 2, 1.5, false, false, true));
    public static final RegistryObject<CSVisualType> AQUAFLORA_STAB = addVisual(new CSVisualType("aquaflora_stab", CSVisualModel.FLAT_VERTICAL_SIDEFACE, CSVisualAnimation.noAnimWithLifespan(4), 4, 1, 1.2, false, false, true));
    public static final RegistryObject<CSVisualType> AQUAFLORA_FLOWER = addVisual(new CSVisualType("aquaflora_flower", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(10), 4, 5, 3.5, false, true, false));
    public static final RegistryObject<CSVisualType> AQUAFLORA_FLOWER_BIND = addVisual(new CSVisualType("aquaflora_flower_bind", "aquaflora_pierce_start", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(12),  6, 2, 2 /* Special size property */, false, false, false));
    public static final RegistryObject<CSVisualType> AQUAFLORA_BASH = addVisual(new CSVisualType("aquaflora_bash", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(12),  6, 2, 1.75, false, true, false));
    public static final RegistryObject<CSVisualType> AQUAFLORA_ASSASSINATE = addVisual(new CSVisualType("aquaflora_assassinate", "aquaflora_bash", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(12), 6, 2, 3, false, true, false));
    public static final RegistryObject<CSVisualType> AQUAFLORA_DASH = addVisual(new CSVisualType("aquaflora_dash", "aquaflora_pierce_start", CSVisualModel.FLAT, CSVisualAnimation.STRETCH, 6, 2, 2, false, true, false));

    public static final RegistryObject<CSVisualType> RAINFALL_SHOOT = addVisual(new CSVisualType("rainfall_shoot", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(16), 8, 2, 2.25, false, false, true));
    public static final RegistryObject<CSVisualType> RAINFALL_VANISH = addVisual(new CSVisualType("rainfall_vanish", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.noAnimWithLifespan(20), 0, 0, 2.25, false, true, false));
    public static final RegistryObject<CSVisualType> RAINFALL_VANISH_CIRCLE = addVisual(new CSVisualType("rainfall_vanish_circle", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 4, false, true, false));
    public static final RegistryObject<CSVisualType> RAINFALL_RAIN = addVisual(new CSVisualType("rainfall_rain", CSVisualModel.FLAT, RainfallSerenityItem.SPECIAL_RAINFALL, 0, 0, 2.5, false, true, false));

    public static final RegistryObject<CSVisualType> FROSTBOUND_SLASH = addVisual(FrostboundSlashSkinSet.FROSTBOUND_SLASH);
    public static final RegistryObject<CSVisualType> FROSTBOUND_SLASH_INVERTED = addVisual(FrostboundSlashSkinSet.FROSTBOUND_SLASH_INVERTED);
    public static final RegistryObject<CSVisualType> FROSTBOUND_SLASH_LARGE = addVisual(FrostboundSlashSkinSet.FROSTBOUND_SLASH_LARGE);
    public static final RegistryObject<CSVisualType> FROSTBOUND_IMPACT_CRACK = addVisual(FrostboundSlashSkinSet.FROSTBOUND_IMPACT_CRACK);
    public static final RegistryObject<CSVisualType> FROSTBOUND_ICE_CAST = addVisual(new CSVisualType("frostbound_ice_cast", CSVisualModel.WALL_CROSS, FrostboundItem.SPECIAL_ICE_CAST, 0, 0, 1, false, true, false));
    public static final RegistryObject<CSVisualType> FROSTBOUND_SHARD_PULSE = addVisual(FrostboundSlashSkinSet.FROSTBOUND_SHARD_PULSE);

    public static final RegistryObject<CSVisualType> FROSTBOUND_SLASH_SEABR = addVisual(CSVisualType.createSkin("seabreeze", FrostboundSlashSkinSet.FROSTBOUND_SLASH));
    public static final RegistryObject<CSVisualType> FROSTBOUND_SLASH_INVERTED_SEABR = addVisual(CSVisualType.createSkin("seabreeze", FrostboundSlashSkinSet.FROSTBOUND_SLASH_INVERTED));
    public static final RegistryObject<CSVisualType> FROSTBOUND_SLASH_LARGE_SEABR = addVisual(CSVisualType.createSkin("seabreeze", FrostboundSlashSkinSet.FROSTBOUND_SLASH_LARGE));
    public static final RegistryObject<CSVisualType> FROSTBOUND_IMPACT_CRACK_SEABR = addVisual(CSVisualType.createSkin("seabreeze", FrostboundSlashSkinSet.FROSTBOUND_IMPACT_CRACK));
    public static final RegistryObject<CSVisualType> FROSTBOUND_SHARD_PULSE_SEABR = addVisual(CSVisualType.createSkin("seabreeze", FrostboundSlashSkinSet.FROSTBOUND_SHARD_PULSE));

    public static RegistryObject<CSVisualType> addVisual(CSVisualType type) {
        return VISUALS.register(type.getName(), () -> type);
    }
}
