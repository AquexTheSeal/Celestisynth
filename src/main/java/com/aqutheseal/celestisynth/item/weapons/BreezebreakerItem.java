package com.aqutheseal.celestisynth.item.weapons;

import com.aqutheseal.celestisynth.item.SkilledSwordItem;
import com.aqutheseal.celestisynth.item.attacks.*;
import com.aqutheseal.celestisynth.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.aqutheseal.celestisynth.registry.CSParticleRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
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

public class BreezebreakerItem extends SkilledSwordItem {
    public static final String BB_COMBO_POINTS = "cs.bbCombo";
    public static final String AT_BUFF_STATE = "cs.bbBuffState";
    public static final String BUFF_STATE_LIMITER = "cs.bbBuffStateLimiter";

    public BreezebreakerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
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
    public boolean hasPassive() {
        return true;
    }

    @Override
    public void forceTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.forceTick(itemStack, level, entity, itemSlot, isSelected);
        CompoundTag data1 = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof BreezebreakerItem)) {
            sendExpandingParticles(level, ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 1, 0.1F);
        }
        if (data1.getBoolean(AT_BUFF_STATE)) {
            if (entity instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 1));
                double radius = 1.5 + player.getBbWidth();
                double speed = 0.5;
                double offX = radius * Math.sin(speed * player.tickCount);
                double offY = player.getBbHeight() / 2;
                double offZ = radius * Math.cos(speed * player.tickCount);
                CSUtilityFunctions.sendParticle(level, CSParticleRegistry.BREEZEBROKEN.get(), player.getX() + offX, player.getY() + offY, player.getZ() + offZ);
            }

            data1.putInt(BUFF_STATE_LIMITER, data1.getInt(BUFF_STATE_LIMITER) + 1);
            if (data1.getInt(BUFF_STATE_LIMITER) >= 200) {
                data1.putBoolean(AT_BUFF_STATE, false);
                data1.putInt(BB_COMBO_POINTS, 0);
                data1.putInt(BUFF_STATE_LIMITER, 0);
            }
        }
    }

    @Override
    public void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
        if (event.getSource() == DamageSource.FALL) {
            event.setCanceled(true);
        } else {
            event.setAmount(event.getAmount() * 2.3F);
        }
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
