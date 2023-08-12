package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSEntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Celestisynth.MODID);

    public static final RegistryObject<EntityType<CSEffect>> CS_EFFECT = ENTITY_TYPES.register("cs_effect", () -> EntityType.Builder.of(CSEffect::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(32).build(new ResourceLocation(Celestisynth.MODID, "cs_effect").toString())
    );
    public static final RegistryObject<EntityType<SkillCastCrescentiaRanged>> CRESCENTIA_RANGED = ENTITY_TYPES.register("crescentia_ranged", () -> EntityType.Builder.of(SkillCastCrescentiaRanged::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(new ResourceLocation(Celestisynth.MODID, "crescentia_ranged").toString())
    );
    public static final RegistryObject<EntityType<SkillCastBreezebreakerTornado>> BREEZEBREAKER_TORNADO = ENTITY_TYPES.register("breezebreaker_tornado", () -> EntityType.Builder.of(SkillCastBreezebreakerTornado::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(new ResourceLocation(Celestisynth.MODID, "breezebreaker_tornado").toString())
    );
    public static final RegistryObject<EntityType<SkillCastPoltergeistWard>> POLTERGEIST_WARD = ENTITY_TYPES.register("poltergeist_ward", () -> EntityType.Builder.of(SkillCastPoltergeistWard::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16).build(new ResourceLocation(Celestisynth.MODID, "poltergeist_ward").toString())
    );

    public static final RegistryObject<EntityType<UtilRainfallArrow>> RAINFALL_ARROW = ENTITY_TYPES.register("rainfall_arrow", () -> EntityType.Builder.<UtilRainfallArrow>of(UtilRainfallArrow::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(32).updateInterval(20).build(new ResourceLocation(Celestisynth.MODID, "rainfall_arrow").toString())
    );

}
