package com.aqutheseal.celestisynth.item.helpers;

import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public interface CSWeapon {

    String CS_CONTROLLER_TAG_ELEMENT = "csController";

    String ANIMATION_TIMER_KEY = "cs.animationTimer";
    String ANIMATION_BEGUN_KEY = "cs.hasAnimationBegun";
    String IS_RANGED_KEY = "cs.isRangedAttack";

    SoundEvent[] CRESENTIA_SOUNDS = {
            CSSoundRegistry.SOLARIS_1.get(),
            CSSoundRegistry.SOLARIS_2.get(),
            CSSoundRegistry.SOLARIS_3.get(),
            CSSoundRegistry.SOLARIS_5.get(),
            CSSoundRegistry.SOLARIS_6.get(),
            CSSoundRegistry.SOLARIS_4.get()
    };

    default void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = CRESENTIA_SOUNDS[new Random().nextInt(length)];
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
