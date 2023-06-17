package com.aqutheseal.celestisynth.client.animation;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.packet.network.CSNetwork;
import com.aqutheseal.celestisynth.common.packet.network.animation.SetAnimationServerPacket;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class AnimationManager {
    public static int animIndex;

    public enum AnimationsList {
        CLEAR(null),
        ANIM_SOLARIS_SPIN("cs_solaris_spin"),
        ANIM_CRESCENTIA_STRIKE("cs_crescentia_strike"),
        ANIM_CRESCENTIA_THROW("cs_crescentia_throw"),
        ANIM_BREEZEBREAKER_NORMAL_SINGLE("cs_breezebreaker_normal_single"),
        ANIM_BREEZEBREAKER_NORMAL_DOUBLE("cs_breezebreaker_normal_double"),
        ANIM_BREEZEBREAKER_SHIFT_RIGHT("cs_breezebreaker_shift_right"),
        ANIM_BREEZEBREAKER_SHIFT_LEFT("cs_breezebreaker_shift_left"),
        ANIM_BREEZEBREAKER_JUMP("cs_breezebreaker_jump"),
        ANIM_BREEZEBREAKER_JUMP_ATTACK("cs_breezebreaker_jump_attack"),
        ANIM_BREEZEBREAKER_SPRINT_ATTACK("cs_breezebreaker_sprint_attack");

        final @Nullable String path;
        final int id;

        AnimationsList(@Nullable String file) {
            path = file;
            id = animIndex++;
        }

        public @Nullable KeyframeAnimation getAnimation() {
            if (getPath() != null) {
                return PlayerAnimationRegistry.getAnimation(new ResourceLocation(Celestisynth.MODID, getPath()));
            } else {
                return null;
            }
        }

        public @Nullable String getPath() {
            return path;
        }

        public int getId() {
            return id;
        }
    }

    public static void playAnimation(Level level, AnimationsList animation) {
        if (level.isClientSide()) {
            playAnimation(animation, false);
        }
    }

    public static void playAnimation(AnimationsList animation) {
        playAnimation(animation, false);
    }

    public static void playAnimation(AnimationsList animation, boolean isOtherLayer) {
        CSNetwork.sendToServer(new SetAnimationServerPacket(isOtherLayer, animation.getId()));
    }

    public static void playAnimation(@Nullable KeyframeAnimation animation, ModifierLayer<IAnimation> layer) {
        if (animation == null) {
            layer.setAnimation(null);
        } else {
            if (CSAnimator.animationData.containsValue(layer)) {
                layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, Ease.LINEAR), new KeyframeAnimationPlayer(animation)
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration()
                                .setShowRightArm(true).setShowRightItem(true)
                                .setShowLeftArm(true).setShowLeftItem(true)
                        ), true
                );
            }
            if (CSAnimator.otherAnimationData.containsValue(layer)) {
                layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, Ease.LINEAR), new KeyframeAnimationPlayer(animation)
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration()
                                .setShowRightArm(false).setShowRightItem(true)
                                .setShowLeftArm(false).setShowLeftItem(true)
                        ), true
                );
            }
        }
    }

    public static AnimationsList getAnimFromId(int id) {
        for (AnimationsList anim : AnimationsList.values()) {
            if (anim.id == id) {
                return anim;
            }
        }
        throw new IllegalStateException("Animation ID is invalid: " + id);
    }
}
