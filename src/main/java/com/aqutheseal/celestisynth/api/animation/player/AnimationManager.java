package com.aqutheseal.celestisynth.api.animation.player;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.network.animation.SetAnimationServerPacket;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public class AnimationManager {
    public static int animIndex;

    public static void playAnimation(Level level, AnimationsList animation) {
        if (level.isClientSide()) {
            playAnimation(animation, false);
        }
    }

    public static void playAnimation(AnimationsList animation) {
        playAnimation(animation, false);
    }

    public static void playAnimation(AnimationsList animation, boolean isOtherLayer) {
        CSNetworkManager.sendToServer(new SetAnimationServerPacket(isOtherLayer, animation.getId()));
    }

    public static void playAnimation(@Nullable KeyframeAnimation animation, ModifierLayer<IAnimation> layer) {
        boolean isFirstPersonModelLoaded = ModList.get().isLoaded("firstpersonmod");

        if (animation == null) {
            layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(3, Ease.OUTCIRC), null);
        } else {
            if (CSAnimator.animationData.containsValue(layer)) {
                layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(3, Ease.OUTCIRC), new KeyframeAnimationPlayer(animation)
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration()
                                .setShowRightArm(!isFirstPersonModelLoaded && CSConfigManager.CLIENT.showRightArmOnAnimate.get()).setShowRightItem(!isFirstPersonModelLoaded)
                                .setShowLeftArm(!isFirstPersonModelLoaded && CSConfigManager.CLIENT.showLeftArmOnAnimate.get()).setShowLeftItem(!isFirstPersonModelLoaded)
                        ), false
                );
            }
            if (CSAnimator.otherAnimationData.containsValue(layer)) {
                layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(20, Ease.OUTCIRC), new KeyframeAnimationPlayer(animation)
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration()
                                .setShowRightArm(!isFirstPersonModelLoaded && CSConfigManager.CLIENT.showRightArmOnAnimate.get()).setShowRightItem(!isFirstPersonModelLoaded)
                                .setShowLeftArm(!isFirstPersonModelLoaded && CSConfigManager.CLIENT.showLeftArmOnAnimate.get()).setShowLeftItem(!isFirstPersonModelLoaded)
                        ), false
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

    public enum AnimationsList implements IExtensibleEnum {
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
        ANIM_BREEZEBREAKER_SPRINT_ATTACK("cs_breezebreaker_sprint_attack"),
        ANIM_POLTERGEIST_SMASH("cs_poltergeist_smash"),
        ANIM_POLTERGEIST_RETREAT("cs_poltergeist_retreat"),
        ANIM_AQUAFLORA_PIERCE_RIGHT("cs_aquaflora_pierce_right"),
        ANIM_AQUAFLORA_PIERCE_LEFT("cs_aquaflora_pierce_left"),
        ANIM_AQUAFLORA_BASH("cs_aquaflora_bash"),
        ANIM_AQUAFLORA_ASSASSINATE("cs_aquaflora_assassinate"),
        ANIM_RAINFALL_AIM_LEFT("cs_rainfall_aim_left"),
        ANIM_RAINFALL_AIM_RIGHT("cs_rainfall_aim_right");

        final @Nullable String path;
        final int id;

        AnimationsList(@Nullable String file) {
            path = file;
            id = animIndex++;
        }

        public static AnimationsList create(String name, String file) {
            throw new IllegalStateException("Enum not extended");
        }

        public @Nullable KeyframeAnimation getAnimation() {
            if (getPath() != null) {
                return PlayerAnimationRegistry.getAnimation(Celestisynth.prefix(getPath()));
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
}
