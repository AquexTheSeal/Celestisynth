package com.aqutheseal.celestisynth.common.compat.spellbooks;

import com.aqutheseal.celestisynth.api.item.CSArmorMaterials;
import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;

import java.util.UUID;

public class ISSArmorUtil {
    public static void addSpellbookAttributesOnArmor(ImmutableMultimap.Builder<Attribute, AttributeModifier> map, UUID armorUuid, ArmorMaterial armorMaterial) {
        if (armorMaterial.equals(CSArmorMaterials.SOLAR_CRYSTAL)) {
            map.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(armorUuid, "Armor max mana", 10, AttributeModifier.Operation.ADDITION));
            map.put(AttributeRegistry.FIRE_MAGIC_RESIST.get(), new AttributeModifier(armorUuid, "Armor fire magic resist", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        if (armorMaterial.equals(CSArmorMaterials.LUNAR_STONE)) {
            map.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(armorUuid, "Armor max mana", 10, AttributeModifier.Operation.ADDITION));
            map.put(AttributeRegistry.SPELL_RESIST.get(), new AttributeModifier(armorUuid, "Armor spell resist", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }
}
