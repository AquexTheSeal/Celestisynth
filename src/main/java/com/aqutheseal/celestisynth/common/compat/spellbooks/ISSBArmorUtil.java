package com.aqutheseal.celestisynth.common.compat.spellbooks;

import com.aqutheseal.celestisynth.api.item.CSArmorMaterials;
import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;

import java.util.UUID;

public class ISSBArmorUtil {
    public static void addSpellbookAttributesOnArmor(ImmutableMultimap.Builder<Attribute, AttributeModifier> map, UUID armorUuid, ArmorMaterial armorMaterial) {
        if (armorMaterial.equals(CSArmorMaterials.SOLAR_CRYSTAL)) {
            map.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(armorUuid, "Armor max mana", 50, AttributeModifier.Operation.ADDITION));
            map.put(AttributeRegistry.FIRE_MAGIC_RESIST.get(), new AttributeModifier(armorUuid, "Armor fire magic resist", 1.5, AttributeModifier.Operation.ADDITION));
            map.put(AttributeRegistry.FIRE_SPELL_POWER.get(), new AttributeModifier(armorUuid, "Armor fire spell power", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }
}
