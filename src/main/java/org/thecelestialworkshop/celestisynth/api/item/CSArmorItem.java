package org.thecelestialworkshop.celestisynth.api.item;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSAttributes;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CSArmorItem extends ArmorItem implements CSWeaponUtil {
    public CSArmorItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers base = super.getDefaultAttributeModifiers();
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        for (ItemAttributeModifiers.Entry entry : base.modifiers()) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }
        this.createExtraAttributes(builder);
        return builder.build();
    }

    public void createExtraAttributes(ItemAttributeModifiers.Builder additional) {
        EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(this.type.getSlot());
        ResourceLocation bonusId = Celestisynth.prefix("armor_bonus_" + this.type.getName());
        if (this.material.is(CSArmorMaterials.SOLAR_CRYSTAL.getId())) {
            additional.add(CSAttributes.CELESTIAL_DAMAGE, new AttributeModifier(bonusId, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), slotGroup);
            additional.add(CSAttributes.CELESTIAL_DAMAGE_REDUCTION, new AttributeModifier(bonusId, 0.025, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), slotGroup);
        }
        if (this.material.is(CSArmorMaterials.LUNAR_STONE.getId())) {
            additional.add(CSAttributes.CELESTIAL_DAMAGE, new AttributeModifier(bonusId, 0.025, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), slotGroup);
            additional.add(CSAttributes.CELESTIAL_DAMAGE_REDUCTION, new AttributeModifier(bonusId, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), slotGroup);
        }
    }

    public static void hurtWearer(LivingIncomingDamageEvent event) {
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
                targets.igniteForSeconds(1 + solarCount);
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
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
        if (this.material.is(CSArmorMaterials.SOLAR_CRYSTAL.getId())) {
            pTooltipComponents.add(Component.translatable("item.celestisynth.solar_crystal_armor_bonus").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }

        if (this.material.is(CSArmorMaterials.LUNAR_STONE.getId())) {
            pTooltipComponents.add(Component.translatable("item.celestisynth.lunar_stone_armor_bonus").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
    }

    public static int getSameArmorCount(LivingEntity wearer, Holder<ArmorMaterial> armorMaterial) {
        int i = 0;
        for (var slot : wearer.getArmorSlots()) {
            if (slot.getItem() instanceof ArmorItem armor) {
                if (armor.getMaterial().is(armorMaterial.unwrapKey().orElseThrow())) {
                    i++;
                }
            }
        }
        return i;
    }
}
