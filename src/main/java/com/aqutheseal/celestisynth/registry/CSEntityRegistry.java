package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.CrescentiaRanged;
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
    public static final RegistryObject<EntityType<CrescentiaRanged>> CRESCENTIA_RANGED = ENTITY_TYPES.register("crescentia_ranged", () -> EntityType.Builder.of(CrescentiaRanged::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(1).build(new ResourceLocation(Celestisynth.MODID, "crescentia_ranged").toString())
    );
}
