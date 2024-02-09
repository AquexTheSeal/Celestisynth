package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.breezebreaker.*;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public class BreezebreakerItem extends SkilledSwordItem implements CSGeoItem {
    public static final String BB_COMBO_POINTS = "cs.bbCombo";
    public static final String AT_BUFF_STATE = "cs.bbBuffState";
    public static final String BUFF_STATE_LIMITER = "cs.bbBuffStateLimiter";

    public BreezebreakerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "breezebreaker";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration) {
        return ImmutableList.of(
                new BreezebreakerGalestormAttack(player, stack, useDuration),
                new BreezebreakerDualGalestormAttack(player, stack, useDuration),
                new BreezebreakerWheelAttack(player, stack, useDuration),
                new BreezebreakerWhirlwindAttack(player, stack, useDuration),
                new BreezebreakerWindRoarAttack(player, stack, useDuration)
        );
    }

    @Override
    public int getSkillsAmount() {
        return 5;
    }

    @Override
    public int getPassiveAmount() {
        return 2;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity owner, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, level, owner, itemSlot, isSelected);

        CompoundTag extrasData = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);

        if (owner instanceof Player playerOwner && (isSelected || playerOwner.getOffhandItem().getItem() instanceof BreezebreakerItem)) sendExpandingParticles(level, ParticleTypes.END_ROD, owner.getX(), owner.getY(), owner.getZ(), 1, 0.1F);
        if (extrasData.getBoolean(AT_BUFF_STATE)) {
            if (owner instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 1));
                double radius = 1.5 + player.getBbWidth();
                double speed = 0.5;
                double offX = radius * Math.sin(speed * player.tickCount);
                double offY = player.getBbHeight() / 2;
                double offZ = radius * Math.cos(speed * player.tickCount);

                ParticleUtil.sendParticle(level, CSParticleTypes.BREEZEBROKEN.get(), player.getX() + offX, player.getY() + offY, player.getZ() + offZ);
            }

            extrasData.putInt(BUFF_STATE_LIMITER, extrasData.getInt(BUFF_STATE_LIMITER) + 1);
            if (extrasData.getInt(BUFF_STATE_LIMITER) >= 200) {
                extrasData.putBoolean(AT_BUFF_STATE, false);
                extrasData.putInt(BB_COMBO_POINTS, 0);
                extrasData.putInt(BUFF_STATE_LIMITER, 0);
            }
        }
    }

    @Override
    public void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
        if (event.getSource() == event.getEntity().damageSources().fall()) event.setCanceled(true);
        else event.setAmount(event.getAmount() * 2.3F);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }
}
