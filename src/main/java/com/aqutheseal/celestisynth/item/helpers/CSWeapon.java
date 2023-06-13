package com.aqutheseal.celestisynth.item.helpers;

import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Random;

public interface CSWeapon {

    String CS_CONTROLLER_TAG_ELEMENT = "csController";
    String ANIMATION_TIMER_KEY = "cs.animationTimer";
    String ANIMATION_BEGUN_KEY = "cs.hasAnimationBegun";

    SoundEvent[] BASE_WEAPON_EFFECTS = {
            CSSoundRegistry.CS_SWORD_SWING.get(),
            CSSoundRegistry.CS_SWORD_SWING_FIRE.get(),
            CSSoundRegistry.CS_AIR_SWING.get(),
            CSSoundRegistry.CS_SWORD_CLASH.get(),
            CSSoundRegistry.CS_FIRE_SHOOT.get(),
            CSSoundRegistry.CS_IMPACT_HIT.get()
    };

    default void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
    }

    default void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = BASE_WEAPON_EFFECTS[new Random().nextInt(length)];
        entity.playSound(randomSound, 0.55F, 0.5F + new Random().nextFloat());
    }

    default void constantAttack(Player holder, LivingEntity target, float damage) {
        double preAttribute = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
        target.invulnerableTime = 0;
        target.hurt(holder.damageSources().playerAttack(holder), damage);
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(preAttribute);
    }

    default void setDeltaPlayer(Player player, double x, double y, double z) {
        player.setDeltaMovement(0, 0, 0);
        player.hurtMarked = true;
    }

    default double calculateXLook(Player player) {
        return -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
    }

    default double calculateYLook(Player player) {
        return -Mth.sin(player.getXRot() * ((float) Math.PI / 180F));
    }

    default double calculateZLook(Player player) {
        return Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
    }
}
