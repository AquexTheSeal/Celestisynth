package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.cresentia.CrescentiaBarrageAttack;
import com.aqutheseal.celestisynth.common.attack.cresentia.CrescentiaDragonAttack;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CrescentiaItem extends SkilledSwordItem implements CSGeoItem {
    public static HumanoidModel.ArmPose CRESCENTIA_POSE = HumanoidModel.ArmPose.create("CRESCENTIA", false, (model, entity, arm) -> {
        float rotation = (entity.isSprinting() ? 30 : -10) + Mth.sin((float) entity.tickCount / 22.5F) * 10;
        float running = entity.isSprinting() ? 40 + (Mth.cos((float) entity.tickCount / 8) * 5) : 0;
        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.zRot = (float) Math.toRadians(30 + rotation);
            model.rightArm.xRot = (float) Math.toRadians(30 + running);
        } else {
            model.leftArm.zRot = -((float) Math.toRadians(30 + rotation));
            model.leftArm.xRot = (float) Math.toRadians(30 + running);
        }
    });

    public CrescentiaItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "crescentia";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new CrescentiaBarrageAttack(player, stack),
                new CrescentiaDragonAttack(player, stack)
        );
    }

    @Override
    public HumanoidModel.ArmPose getArmPose() {
        return CRESCENTIA_POSE;
    }

    @Override
    public void addExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> map) {
        if (CSCompatManager.checkIronsSpellbooks()) {
            map.put(AttributeRegistry.SPELL_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Item spell resist", 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
        }
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
        entity.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));

        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof CrescentiaItem)) player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.DAMAGE_RESISTANCE, 2, 0));
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
    }

    public static void createCrescentiaFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z, boolean isBig) {
        Random random = new Random();
        ItemStack star = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag compoundtag = star.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        DyeColor[] allowedColors = new DyeColor[]{DyeColor.LIGHT_BLUE, DyeColor.WHITE, DyeColor.BLUE, DyeColor.MAGENTA, DyeColor.YELLOW, DyeColor.ORANGE};
        list.add(allowedColors[random.nextInt(allowedColors.length)].getFireworkColor());
        compoundtag.putIntArray("Colors", list);
        compoundtag.putByte("Type", (byte)(isBig ? FireworkRocketItem.Shape.LARGE_BALL.getId() : FireworkRocketItem.Shape.SMALL_BALL.getId()));
        CompoundTag itemCompound = itemStack.getOrCreateTagElement("Fireworks");
        ListTag listtag = new ListTag();
        CompoundTag starCompound = star.getTagElement("Explosion");
        if (starCompound != null) {
            listtag.add(starCompound);
        }
        itemCompound.putByte("Flight", (byte) 3);
        if (!listtag.isEmpty()) {
            itemCompound.put("Explosions", listtag);
        }
        level.createFireworks(x, y, z, 0.01, 0.01, 0.01, itemCompound);
        player.playSound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 1.0F, 0.5F + random.nextFloat());
    }
}
