package com.aqutheseal.celestisynth.common.attack.base;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class WeaponAttackInstance implements CSWeaponUtil {
    protected final Player player;
    protected final ItemStack stack;
    protected final int heldDuration;
    public static final SoundEvent[] BASE_WEAPON_EFFECTS = {
            CSSoundEvents.CS_SWORD_SWING.get(),
            CSSoundEvents.CS_SWORD_SWING_FIRE.get(),
            CSSoundEvents.CS_AIR_SWING.get(),
            CSSoundEvents.CS_SWORD_CLASH.get(),
            CSSoundEvents.CS_FIRE_SHOOT.get(),
            CSSoundEvents.CS_IMPACT_HIT.get()
    };

    public WeaponAttackInstance(Player player, ItemStack stack, int heldDuration) {
        this.player = player;
        this.stack = stack;
        this.heldDuration = heldDuration;
    }

    public WeaponAttackInstance(Player player, ItemStack stack) {
        this(player, stack, 0);
    }

    public abstract AnimationManager.AnimationsList getAnimation();

    public abstract int getCooldown();

    public abstract int getAttackStopTime();

    public abstract boolean getCondition();

    public abstract void startUsing();

    public abstract void tickAttack();

    public abstract void stopUsing();

    public void baseStop() {
        stopUsing();
        getTagController().putInt("cs.AttackIndex", -1);
        getTagController().putInt(ANIMATION_TIMER_KEY, 0);
        getTagController().putBoolean(ANIMATION_BEGUN_KEY, false);
    }

    public void baseTickSkill() {
        tickAttack();

        if (getTimerProgress() >= getAttackStopTime()) baseStop();
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getHeldDuration() {
        return heldDuration;
    }

    public int getTimerProgress() {
        return getTagController().getInt(ANIMATION_TIMER_KEY);
    }

    public CompoundTag getTagController() {
        return stack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
    }

    public CompoundTag getTagExtras() {
        return stack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
    }

    public static void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = BASE_WEAPON_EFFECTS[entity.level.getRandom().nextInt(length)];

        entity.playSound(randomSound, 0.35F, 0.5F + entity.level.getRandom().nextFloat());
    }
}
