package com.aqutheseal.celestisynth.common.entity;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.reg.RegistrationProvider;
import com.aqutheseal.celestisynth.reg.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class CSEntities {
    public static final RegistrationProvider<EntityType<?>> PROVIDER = RegistrationProvider.get(Registries.ENTITY_TYPE, Celestisynth.MODID);
    public static final RegistryObject<EntityType<CSEffect>> CS_EFFECT = register("cs_effect", EntityType.Builder.of(CSEffect::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(32));
    public static final RegistryObject<EntityType<CrescentiaRanged>> CRESCENTIA_RANGED = register("crescentia_ranged", EntityType.Builder.of(CrescentiaRanged::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16));
    public static final RegistryObject<EntityType<BreezebreakerTornado>> BREEZEBREAKER_TORNADO = register("breezebreaker_tornado", EntityType.Builder.of(BreezebreakerTornado::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(16));
    public static <E extends Entity> RegistryObject<EntityType<E>> register(String id, EntityType.Builder<E> entityType) {
        return PROVIDER.register(id, () -> entityType.build(Celestisynth.MODID + ":" + id));
    }
    public static void init() {}
}
