package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.poltergeist.PoltergeistBarrierCallAttack;
import com.aqutheseal.celestisynth.common.attack.poltergeist.PoltergeistCosmicSteelAttack;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.item.base.SkilledAxeItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.UUID;

public class PoltergeistItem extends SkilledAxeItem implements CSGeoItem {
    public PoltergeistItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "poltergeist";
    }

    @Override
    public String texture(ItemStack stack) {
        if (attackController(stack).getBoolean(PoltergeistCosmicSteelAttack.IS_IMPACT_LARGE)) {
            return "poltergeist_haunted";
        } else {
            return "poltergeist";
        }
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public int getSkillsAmount() {
        return 2;
    }

    @Override
    public int getPassiveAmount() {
        return 1;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration) {
        return ImmutableList.of(
                new PoltergeistCosmicSteelAttack(player, stack),
                new PoltergeistBarrierCallAttack(player, stack)
        );
    }

    @Override
    public void addExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> map) {
        if (CSCompatManager.checkIronsSpellbooks()) {
            map.put(AttributeRegistry.ENDER_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Item ender spell power", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
            map.put(AttributeRegistry.ENDER_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Item ender resist", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.DARKNESS, 60, 0));
        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (entity instanceof Player player) {
                player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.DAMAGE_RESISTANCE, 2, 2));
            }
        }
    }
}