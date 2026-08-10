package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CSAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Celestisynth.MODID);

    public static final DeferredHolder<Attribute, Attribute> CELESTIAL_DAMAGE = createDefaultRangedAttribute("celestial_damage", 1);
    public static final DeferredHolder<Attribute, Attribute> CELESTIAL_DAMAGE_REDUCTION = createDefaultRangedAttribute("celestial_damage_reduction", 1);

    @SuppressWarnings("unchecked")
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach((entity) -> {
            for (DeferredHolder<Attribute, ? extends Attribute> attribute : ATTRIBUTES.getEntries()) {
                event.add(entity, (net.minecraft.core.Holder<Attribute>) (net.minecraft.core.Holder<?>) attribute);
            }
        });
    }

    public static DeferredHolder<Attribute, Attribute> createDefaultRangedAttribute(String id, double defaultValue) {
        return ATTRIBUTES.register(id, () -> new RangedAttribute("attribute.celestisynth." + id, defaultValue, -1024.0, 1024.0));
    }
}
