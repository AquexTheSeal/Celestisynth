package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.projectile.FrostboundShard;
import com.aqutheseal.celestisynth.common.entity.projectile.RainfallArrow;
import com.aqutheseal.celestisynth.common.entity.projectile.RainfallLaserMarker;
import com.aqutheseal.celestisynth.common.entity.skill.*;
import com.aqutheseal.celestisynth.common.entity.tempestboss.TempestBoss;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Celestisynth.MODID);

    public static final RegistryObject<EntityType<TempestBoss>> TEMPEST = ENTITY_TYPES.register("tempest", () -> EntityType.Builder.of(TempestBoss::new, MobCategory.MONSTER)
            .sized(0.7F, 1.95F).clientTrackingRange(8).build(Celestisynth.prefix("tempest").toString()));

    public static final RegistryObject<EntityType<CSEffectEntity>> CS_EFFECT = ENTITY_TYPES.register("cs_effect", () -> EntityType.Builder.of(CSEffectEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(32).build(Celestisynth.prefix("cs_effect").toString())
    );
    public static final RegistryObject<EntityType<SkillCastCrescentiaRanged>> CRESCENTIA_RANGED = ENTITY_TYPES.register("crescentia_ranged", () -> EntityType.Builder.of(SkillCastCrescentiaRanged::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(Celestisynth.prefix("crescentia_ranged").toString())
    );
    public static final RegistryObject<EntityType<SkillCastBreezebreakerTornado>> BREEZEBREAKER_TORNADO = ENTITY_TYPES.register("breezebreaker_tornado", () -> EntityType.Builder.of(SkillCastBreezebreakerTornado::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(Celestisynth.prefix("breezebreaker_tornado").toString())
    );
    public static final RegistryObject<EntityType<SkillCastPoltergeistWard>> POLTERGEIST_WARD = ENTITY_TYPES.register("poltergeist_ward", () -> EntityType.Builder.of(SkillCastPoltergeistWard::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(Celestisynth.prefix("poltergeist_ward").toString())
    );
    public static final RegistryObject<EntityType<SkillCastRainfallRain>> RAINFALL_RAIN = ENTITY_TYPES.register("rainfall_rain", () -> EntityType.Builder.of(SkillCastRainfallRain::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(Celestisynth.prefix("rainfall_rain").toString())
    );
    public static final RegistryObject<EntityType<SkillCastFrostboundIceCast>> FROSTBOUND_ICE_CAST = ENTITY_TYPES.register("frostbound_ice_cast", () -> EntityType.Builder.of(SkillCastFrostboundIceCast::new, MobCategory.MISC)
            .sized(0.3F, 0.3F).clientTrackingRange(16).build(Celestisynth.prefix("rainfall_rain").toString())
    );
    public static final RegistryObject<EntityType<RainfallLaserMarker>> RAINFALL_LASER_MARKER = ENTITY_TYPES.register("rainfall_laser_marker", () -> EntityType.Builder.of(RainfallLaserMarker::new, MobCategory.MISC)
            .sized(0.0F, 0.0F).clientTrackingRange(16).build(Celestisynth.prefix("rainfall_laser_marker").toString())
    );

    public static final RegistryObject<EntityType<RainfallArrow>> RAINFALL_ARROW = ENTITY_TYPES.register("rainfall_arrow", () -> EntityType.Builder.<RainfallArrow>of(RainfallArrow::new, MobCategory.MISC)
            .sized(1F, 1F).clientTrackingRange(32).updateInterval(20).build(Celestisynth.prefix("rainfall_arrow").toString())
    );
    public static final RegistryObject<EntityType<FrostboundShard>> FROSTBOUND_SHARD = ENTITY_TYPES.register("frostbound_shard", () -> EntityType.Builder.<FrostboundShard>of(FrostboundShard::new, MobCategory.MISC)
            .sized(3F, 3F).clientTrackingRange(32).updateInterval(20).build(Celestisynth.prefix("frostbound_shard").toString())
    );
}
