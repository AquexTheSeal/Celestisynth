package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Celestisynth.MODID);

    public static final RegistryObject<Attribute> CELESTIAL_DAMAGE = ATTRIBUTES.register("celestial_damage", () ->
            new RangedAttribute("attribute.celestisynth.celestial_damage", 1, -1024.0, 1024.0)
    );

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach((entity) -> {
            event.add(entity, CELESTIAL_DAMAGE.get());
        });
    }
}
