package com.aqutheseal.celestisynth.api.item;

import com.aqutheseal.celestisynth.common.compat.spellbooks.ISSBArmorUtil;
import com.aqutheseal.celestisynth.common.registry.CSAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.fml.ModList;

import java.util.UUID;

public class CSArmorItem extends ArmorItem {
    public CSArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == this.type.getSlot() ? specialAttributes() : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    public Multimap<Attribute, AttributeModifier> specialAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> additional = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        additional.putAll(this.defaultModifiers);
        additional.put(CSAttributes.CELESTIAL_DAMAGE.get(), new AttributeModifier(uuid, "Armor celestial damage", 0.07, AttributeModifier.Operation.MULTIPLY_BASE));
        if (ModList.get().isLoaded("irons_spellbooks")) {
            ISSBArmorUtil.addSpellbookAttributesOnArmor(additional, uuid, material);
        }
        return additional.build();
    }
}
