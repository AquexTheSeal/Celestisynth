package org.thecelestialworkshop.celestisynth.common.item.base;

import org.thecelestialworkshop.celestisynth.api.item.CSWeapon;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents;
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
        super(pTier, pProperties.attributes(AxeItem.createAttributes(pTier, pAttackDamageModifier, pAttackSpeedModifier)));
    }

    public abstract ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack heldStack = player.getItemInHand(interactionHand);
        CompoundTag data = CSDataComponents.getOrCreateLiveTag(heldStack, CSDataComponents.CS_CONTROLLER);

        if (!player.getCooldowns().isOnCooldown(heldStack.getItem()) && !data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (getUseDuration(heldStack, player) <= 0) {
                int index = 0;
                for (WeaponAttackInstance attack : getPossibleAttacks(player, heldStack, 0)) {
                    if (attack.getCondition()) {
                        data.putBoolean(ANIMATION_BEGUN_KEY, true);
                        this.executeAnimation(level, attack.getAnimation(), attack, interactionHand);
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
        CompoundTag data = CSDataComponents.getOrCreateLiveTag(itemstack, CSDataComponents.CS_CONTROLLER);
        int dur = this.getUseDuration(itemstack, entity) - i;
        if (entity instanceof Player player) {
            int index = 0;
            for (WeaponAttackInstance attack : getPossibleAttacks(player, itemstack, dur)) {
                if (attack.getCondition()) {
                    data.putBoolean(ANIMATION_BEGUN_KEY, true);
                    this.executeAnimation(level, attack.getAnimation(), attack, entity.getOffhandItem() == itemstack ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                    setAttackIndex(itemstack, index);
                    attack.baseStart();
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
        CompoundTag data = CSDataComponents.getOrCreateLiveTag(itemStack, CSDataComponents.CS_CONTROLLER);
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

    public int getAttackIndex(ItemStack stack) {
        return attackController(stack).getInt(ATTACK_INDEX_KEY);
    }

    public void setAttackIndex(ItemStack stack, int value) {
        attackController(stack).putInt(ATTACK_INDEX_KEY, value);
    }
}
