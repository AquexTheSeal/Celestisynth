package com.aqutheseal.celestisynth.api.item;

import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.compat.spellbooks.ISSArmorUtil;
import com.aqutheseal.celestisynth.common.registry.CSAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CSArmorItem extends ArmorItem implements CSWeaponUtil {
    public CSArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == this.type.getSlot() ? modifiedAttributes() : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    public void createExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> additional, UUID uuid) {
        if (material == CSArmorMaterials.SOLAR_CRYSTAL) {
            additional.put(CSAttributes.CELESTIAL_DAMAGE.get(), new AttributeModifier(uuid, "Armor celestial damage", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
            additional.put(CSAttributes.CELESTIAL_DAMAGE_REDUCTION.get(), new AttributeModifier(uuid, "Armor celestial damage reduction", 0.025, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        if (material == CSArmorMaterials.LUNAR_STONE) {
            additional.put(CSAttributes.CELESTIAL_DAMAGE.get(), new AttributeModifier(uuid, "Armor celestial damage", 0.025, AttributeModifier.Operation.MULTIPLY_BASE));
            additional.put(CSAttributes.CELESTIAL_DAMAGE_REDUCTION.get(), new AttributeModifier(uuid, "Armor celestial damage reduction", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));;
        }
    }

    public void hurtWearer(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (material == CSArmorMaterials.SOLAR_CRYSTAL) {
            if (event.getSource().getEntity() != null && event.getSource().getEntity() != entity) {
                entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 0.5F, Level.ExplosionInteraction.NONE);
            }
        }
        if (material == CSArmorMaterials.LUNAR_STONE) {
            if (event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
                event.setAmount(event.getAmount() * 0.9F);
            }
        }
    }

    public Multimap<Attribute, AttributeModifier> modifiedAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> additional = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        additional.putAll(this.defaultModifiers);
        this.createExtraAttributes(additional, uuid);
        if (CSCompatManager.checkIronsSpellbooks()) {
            ISSArmorUtil.addSpellbookAttributesOnArmor(additional, uuid, material);
        }
        return additional.build();
    }
}
