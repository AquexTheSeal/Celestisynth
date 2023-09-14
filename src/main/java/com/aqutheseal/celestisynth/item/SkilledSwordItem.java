package com.aqutheseal.celestisynth.item;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class SkilledSwordItem extends SwordItem implements CSWeapon {
    public static final String ATTACK_INDEX_KEY = "cs.AttackIndex";

    public SkilledSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public abstract ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        CompoundTag data = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (!player.getCooldowns().isOnCooldown(itemstack.getItem()) && !data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (getUseDuration(itemstack) <= 0) {
                int index = 0;
                for (WeaponAttackInstance attack : getPossibleAttacks(player, itemstack, 0)) {
                    if (attack.getCondition()) {
                        data.putBoolean(ANIMATION_BEGUN_KEY, true);
                        AnimationManager.playAnimation(level, attack.getAnimation());
                        setAttackIndex(itemstack, index);
                        attack.startUsing();
                        player.getCooldowns().addCooldown(itemstack.getItem(), attack.getCooldown());
                        break;
                    }
                    index++;
                }
            } else {
                if (player.getCooldowns().isOnCooldown(itemstack.getItem()) || data.getBoolean(ANIMATION_BEGUN_KEY)) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    player.startUsingItem(interactionHand);
                    return InteractionResultHolder.consume(itemstack);
                }
            }
        }
        return InteractionResultHolder.success(itemstack);
    }

    public void releaseUsing(ItemStack itemstack, @NotNull Level level, @NotNull LivingEntity entity, int i) {
        CompoundTag data = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        int dur = this.getUseDuration(itemstack) - i;
        if (entity instanceof Player player) {
            int index = 0;
            for (WeaponAttackInstance attack : getPossibleAttacks(player, itemstack, dur)) {
                if (attack.getCondition()) {
                    data.putBoolean(ANIMATION_BEGUN_KEY, true);
                    AnimationManager.playAnimation(level, attack.getAnimation());
                    setAttackIndex(itemstack, index);
                    attack.startUsing();
                    player.getCooldowns().addCooldown(itemstack.getItem(), attack.getCooldown());
                    break;
                }
                index++;
            }
        }
    }

    @Override
    public void forceTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (entity instanceof Player player && data.getBoolean(ANIMATION_BEGUN_KEY)) {
            int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
            data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
            int index = 0;
            for (WeaponAttackInstance attack : getPossibleAttacks(player, itemStack, 0)) {
                if (getAttackIndex(itemStack) == index) {
                    attack.baseTickSkill();
                }
                index++;
            }
        }
    }

    public static int getAttackIndex(ItemStack stack) {
        CompoundTag data = stack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        return data.getInt(ATTACK_INDEX_KEY);
    }

    public static void setAttackIndex(ItemStack stack, int value) {
        CompoundTag data = stack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        data.putInt(ATTACK_INDEX_KEY, value);
    }
}