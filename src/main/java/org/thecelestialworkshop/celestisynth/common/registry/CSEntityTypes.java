package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.RainfallTurret;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Veilguard;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.*;
import org.thecelestialworkshop.celestisynth.common.entity.skillcast.*;
import org.thecelestialworkshop.celestisynth.common.entity.tempestboss_scrapped.TempestBoss;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Celestisynth.MODID);

    public static final RegistryObject<EntityType<TempestBoss>> TEMPEST = ENTITY_TYPES.register("tempest", () -> EntityType.Builder.of(TempestBoss::new, MobCategory.MONSTER)
            .sized(0.7F, 1.95F).clientTrackingRange(8).build(Celestisynth.prefix("tempest").toString()));

    public static final RegistryObject<EntityType<Traverser>> TRAVERSER = ENTITY_TYPES.register("traverser", () -> EntityType.Builder.of(Traverser::new, MobCategory.MONSTER)
            .sized(1F, 2F).clientTrackingRange(64).build(Celestisynth.prefix("traverser").toString()));

    public static final RegistryObject<EntityType<Veilguard>> VEILGUARD = ENTITY_TYPES.register("veilguard", () -> EntityType.Builder.of(Veilguard::new, MobCategory.MONSTER)
            .sized(1.8F, 4F).clientTrackingRange(64).build(Celestisynth.prefix("veilguard").toString()));

    public static final RegistryObject<EntityType<CSEffectEntity>> CS_EFFECT = ENTITY_TYPES.register("cs_effect", () -> EntityType.Builder.of(CSEffectEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(64).build(Celestisynth.prefix("cs_effect").toString())
    );
    public static final RegistryObject<EntityType<SkillCastCrescentiaRanged>> CRESCENTIA_RANGED = ENTITY_TYPES.register("crescentia_ranged", () -> EntityType.Builder.of(SkillCastCrescentiaRanged::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(Celestisynth.prefix("crescentia_ranged").toString())
    );
    public static final RegistryObject<EntityType<SkillCastBreezebreakerTornado>> BREEZEBREAKER_TORNADO = ENTITY_TYPES.register("breezebreaker_tornado", () -> EntityType.Builder.of(SkillCastBreezebreakerTornado::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(Celestisynth.prefix("breezebreaker_tornado").toString())
    );
    public static final RegistryObject<EntityType<SkillCastPoltergeistWard>> POLTERGEIST_WARD = createBasicEntity("poltergeist_ward", SkillCastPoltergeistWard::new);
    public static final RegistryObject<EntityType<SkillCastRainfallRain>> RAINFALL_RAIN = createBasicEntity("rainfall_rain", SkillCastRainfallRain::new);
    public static final RegistryObject<EntityType<SkillCastFrostboundIceCast>> FROSTBOUND_ICE_CAST = createBasicEntity("frostbound_ice_cast", SkillCastFrostboundIceCast::new);
    public static final RegistryObject<EntityType<RainfallLaserMarker>> RAINFALL_LASER_MARKER = createBasicEntity("rainfall_laser_marker", RainfallLaserMarker::new);
    public static final RegistryObject<EntityType<SkillCastKeresSmash>> KERES_SMASH = createBasicEntity("keres_smash", SkillCastKeresSmash::new);
    public static final RegistryObject<EntityType<SkillCastKeresSlashWave>> KERES_SLASH_WAVE = createBasicEntity("keres_slash_wave", SkillCastKeresSlashWave::new);
    public static final RegistryObject<EntityType<SkillCastAquafloraCamera>> AQUAFLORA_CAMERA = createBasicEntity("aquaflora_camera", SkillCastAquafloraCamera::new);

    public static final RegistryObject<EntityType<RainfallTurret>> RAINFALL_TURRET = ENTITY_TYPES.register("rainfall_turret", () -> EntityType.Builder.of(RainfallTurret::new, MobCategory.MISC)
            .sized(0.8F, 1.2F).clientTrackingRange(64).build(Celestisynth.prefix("rainfall_turret").toString())
    );
    public static final RegistryObject<EntityType<StarMonolith>> STAR_MONOLITH = ENTITY_TYPES.register("star_monolith", () -> EntityType.Builder.of(StarMonolith::new, MobCategory.MONSTER)
            .sized(1F, 2.8F).clientTrackingRange(64).build(Celestisynth.prefix("star_monolith").toString())
    );

    public static final RegistryObject<EntityType<RainfallArrow>> RAINFALL_ARROW = ENTITY_TYPES.register("rainfall_arrow", () -> EntityType.Builder.<RainfallArrow>of(RainfallArrow::new, MobCategory.MISC)
            .sized(1F, 1F).clientTrackingRange(32).updateInterval(20).build(Celestisynth.prefix("rainfall_arrow").toString())
    );
    public static final RegistryObject<EntityType<FrostboundShard>> FROSTBOUND_SHARD = ENTITY_TYPES.register("frostbound_shard", () -> EntityType.Builder.<FrostboundShard>of(FrostboundShard::new, MobCategory.MISC)
            .sized(3F, 3F).clientTrackingRange(32).updateInterval(20).build(Celestisynth.prefix("frostbound_shard").toString())
    );
    public static final RegistryObject<EntityType<SolarisBomb>> SOLARIS_BOMB = ENTITY_TYPES.register("solaris_bomb", () -> EntityType.Builder.<SolarisBomb>of(SolarisBomb::new, MobCategory.MISC)
            .sized(1F, 1F).clientTrackingRange(32).updateInterval(20).build(Celestisynth.prefix("solaris_bomb").toString())
    );
    public static final RegistryObject<EntityType<CrescentiaDragon>> CRESCENTIA_DRAGON = ENTITY_TYPES.register("crescentia_dragon", () -> EntityType.Builder.<CrescentiaDragon>of(CrescentiaDragon::new, MobCategory.MISC)
            .sized(1.5F, 1.5F).clientTrackingRange(64).updateInterval(20).build(Celestisynth.prefix("crescentia_dragon").toString())
    );
    public static final RegistryObject<EntityType<KeresShadow>> KERES_SHADOW = ENTITY_TYPES.register("keres_shadow", () -> EntityType.Builder.<KeresShadow>of(KeresShadow::new, MobCategory.MISC)
            .sized(1.5F, 1.5F).clientTrackingRange(64).updateInterval(20).build(Celestisynth.prefix("keres_shadow").toString())
    );
    public static final RegistryObject<EntityType<KeresRend>> KERES_REND = ENTITY_TYPES.register("keres_rend", () -> EntityType.Builder.<KeresRend>of(KeresRend::new, MobCategory.MISC)
            .sized(2.5F, 10F).clientTrackingRange(64).updateInterval(20).build(Celestisynth.prefix("keres_rend").toString())
    );
    public static final RegistryObject<EntityType<KeresSlash>> KERES_SLASH = ENTITY_TYPES.register("keres_slash", () -> EntityType.Builder.<KeresSlash>of(KeresSlash::new, MobCategory.MISC)
            .sized(2.5F, 2.5F).clientTrackingRange(64).updateInterval(20).build(Celestisynth.prefix("keres_slash").toString())
    );

    public static <T extends Entity> RegistryObject<EntityType<T>> createBasicEntity(String id, EntityType. EntityFactory<T> factory) {
        return ENTITY_TYPES.register(id, () -> EntityType.Builder.of(factory, MobCategory.MISC).sized(0.2F, 0.2F).clientTrackingRange(16).build(Celestisynth.prefix(id).toString()));
    }
}
