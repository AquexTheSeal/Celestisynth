package com.aqutheseal.celestisynth.common.item.base;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class SkilledAxeItem extends AxeItem implements CSWeapon {
    public static final String ATTACK_INDEX_KEY = "cs.AttackIndex";

    public SkilledAxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    public abstract ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack heldStack = player.getItemInHand(interactionHand);
        CompoundTag data = heldStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (!player.getCooldowns().isOnCooldown(heldStack.getItem()) && !data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (getUseDuration(heldStack) <= 0) {
                int index = 0;
                for (WeaponAttackInstance attack : getPossibleAttacks(player, heldStack, 0)) {
                    if (attack.getCondition()) {
                        data.putBoolean(ANIMATION_BEGUN_KEY, true);
                        AnimationManager.playAnimation(level, attack.getAnimation());
                        setAttackIndex(heldStack, index);
                        attack.baseStart();
                        player.getCooldowns().addCooldown(heldStack.getItem(), attack.getCooldown());
                        break;
                    }
                    index++;
                }
            } else {
                if (player.getCooldowns().isOnCooldown(heldStack.getItem()) || data.getBoolean(ANIMATION_BEGUN_KEY)) {
                    return InteractionResultHolder.fail(heldStack);
                } else {
                    player.startUsingItem(interactionHand);
                    return InteractionResultHolder.consume(heldStack);
                }
            }
        }
        return InteractionResultHolder.success(heldStack);
    }

    @Override
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
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (entity instanceof Player player && data.getBoolean(ANIMATION_BEGUN_KEY)) {
            int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
            data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
            int index = 0;
            for (WeaponAttackInstance attack : getPossibleAttacks(player, itemStack, 0)) {
                if (getAttackIndex(itemStack) == index) attack.baseTickSkill();

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
