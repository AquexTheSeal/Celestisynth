package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.solaris.SolarisFullRoundAttack;
import com.aqutheseal.celestisynth.common.attack.solaris.SolarisSoulDashAttack;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.UUID;

public class SolarisItem extends SkilledSwordItem implements CSGeoItem {
    public static HumanoidModel.ArmPose SOLARIS_POSE = HumanoidModel.ArmPose.create("SOLARIS", false, (model, entity, arm) -> {
        float rotation = (entity.isSprinting() ? 20 : 0) + Mth.sin((float) entity.tickCount / 10) * 5;
        float running = entity.isSprinting() ? 40 + (Mth.cos((float) entity.tickCount / 4) * 5) : 0;
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.zRot = (float) Math.toRadians(15 + rotation);
            model.rightArm.xRot = (float) Math.toRadians(30 + running);
        } else {
            model.leftArm.zRot = -((float) Math.toRadians(15 + rotation));
            model.leftArm.xRot = (float) Math.toRadians(30 + running);
        }
    });

    public SolarisItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "solaris";
    }

    @Override
    public String texture(ItemStack stack) {
        if (attackController(stack) != null && attackController(stack).getBoolean(SolarisSoulDashAttack.STARTED)) {
            return "solaris_soul";
        } else {
            return "solaris";
        }
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public HumanoidModel.ArmPose getArmPose() {
        return SOLARIS_POSE;
    }

    @Override
    public void addExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> map) {
        if (CSCompatManager.checkIronsSpellbooks()) {
            map.put(AttributeRegistry.FIRE_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Item fire spell power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
            map.put(AttributeRegistry.FIRE_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Item fire resist", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new SolarisFullRoundAttack(player, stack),
                new SolarisSoulDashAttack(player, stack)
        );
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
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.setSecondsOnFire(5);
        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof SolarisItem)) player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.FIRE_RESISTANCE, 2, 0));
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
    }
}
