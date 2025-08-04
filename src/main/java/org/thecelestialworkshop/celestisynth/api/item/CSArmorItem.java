package org.thecelestialworkshop.celestisynth.api.item;

import org.thecelestialworkshop.celestisynth.common.compat.spellbooks.ISSArmorUtil;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSAttributes;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import org.thecelestialworkshop.celestisynth.manager.CSIntegrationManager;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    public static void hurtWearer(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        int solarCount = getSameArmorCount(entity, CSArmorMaterials.SOLAR_CRYSTAL);
        if (solarCount > 0) {
            CSEffectEntity.createInstance(entity, entity, CSVisualTypes.SOLAR_EXPLOSION.get(), 0, 0.75, 0);
            entity.level().playSound(null, entity, CSSoundEvents.SWORD_SWING_FIRE.get(), SoundSource.PLAYERS, 0.2F, 1.0F);
            for (int i = 0; i < 22.5; i++) {
                Vec3 delta = new Vec3(0, 0, 0).add(Mth.sin(i), 0, Mth.cos(i)).scale(0.25);
                ParticleUtil.sendParticle(entity.level(), CSParticleTypes.SOLARIS_FLAME.get(), entity.position().add(0, 1.5, 0), delta);
            }
            for (LivingEntity targets : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(2, 0, 2)).stream().filter(en -> en != entity).toList()) {
                targets.hurt(entity.damageSources().onFire(), solarCount * 1.2F);
                targets.setSecondsOnFire(1 + solarCount);
            }
        }

        int lunarCount = getSameArmorCount(entity, CSArmorMaterials.LUNAR_STONE);
        if (lunarCount > 0) {
            if (entity.getRandom().nextInt(6) == 0) {
                entity.level().playSound(null, entity, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 0.5F, 1.0F);
                for (int i = 0; i < 22.5; i++) {
                    Vec3 delta = new Vec3(0, 0, 0).add(Mth.sin(i), 0, Mth.cos(i)).scale(0.25);
                    ParticleUtil.sendParticle(entity.level(), ParticleTypes.ENCHANT, entity.position().add(0, 1.5, 0), delta);
                }
                event.setAmount(event.getAmount() - lunarCount * 1.5F);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (material == CSArmorMaterials.SOLAR_CRYSTAL) {
            pTooltipComponents.add(Component.translatable("item.celestisynth.solar_crystal_armor_bonus").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }

        if (material == CSArmorMaterials.LUNAR_STONE) {
            pTooltipComponents.add(Component.translatable("item.celestisynth.lunar_stone_armor_bonus").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
    }

    public static int getSameArmorCount(LivingEntity wearer, ArmorMaterial armorMaterial) {
        int i = 0;
        for (var slot : wearer.getArmorSlots()) {
            if (slot.getItem() instanceof ArmorItem armor) {
                if (armor.getMaterial() == armorMaterial) {
                    i++;
                }
            }
        }
        return i;
    }

    public Multimap<Attribute, AttributeModifier> modifiedAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> additional = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        additional.putAll(this.defaultModifiers);
        this.createExtraAttributes(additional, uuid);
        if (CSIntegrationManager.checkIronsSpellbooks()) {
            ISSArmorUtil.addSpellbookAttributesOnArmor(additional, uuid, material);
        }
        return additional.build();
    }
}
