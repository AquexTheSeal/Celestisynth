package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Celestisynth.MODID);

    public static final RegistryObject<Attribute> CELESTIAL_DAMAGE = createDefaultRangedAttribute("celestial_damage", 1);
    public static final RegistryObject<Attribute> CELESTIAL_DAMAGE_REDUCTION = createDefaultRangedAttribute("celestial_damage_reduction", 1);

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach((entity) -> {
            for (RegistryObject<Attribute> attribute : ATTRIBUTES.getEntries()) {
                event.add(entity, attribute.get());
            }
        });
    }

    public static RegistryObject<Attribute> createDefaultRangedAttribute(String id, double defaultValue) {
        return ATTRIBUTES.register(id, () -> new RangedAttribute("attribute.celestisynth." + id, defaultValue, -1024.0, 1024.0));
    }
}
