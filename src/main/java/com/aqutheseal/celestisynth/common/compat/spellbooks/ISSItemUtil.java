package com.aqutheseal.celestisynth.common.compat.spellbooks;

import com.aqutheseal.celestisynth.common.registry.CSAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class ISSItemUtil {

    public static Multimap<Attribute, AttributeModifier> createCelestialSpellbookAttributes() {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        map.put(CSAttributes.CELESTIAL_DAMAGE.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
        map.put(AttributeRegistry.COOLDOWN_REDUCTION.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        map.put(AttributeRegistry.MANA_REGEN.get(), new AttributeModifier(UUID.randomUUID(), "Weapon modifier", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        return map;
    }
}
