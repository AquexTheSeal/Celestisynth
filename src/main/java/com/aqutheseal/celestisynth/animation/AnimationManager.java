package com.aqutheseal.celestisynth.animation;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.network.CSNetwork;
import com.aqutheseal.celestisynth.network.animation.SetAnimationServerPacket;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class AnimationManager {
    //public static final ResourceLocation DEFAULT_LAYER = new ResourceLocation(Celestisynth.MODID, "csanimation");

    public enum AnimationsList {
        CLEAR(null, 0),
        ANIM_SOLARIS_SPIN("cs_solaris_spin", 1),
        ANIM_CRESCENTIA_STRIKE("cs_crescentia_strike", 2),
        ANIM_CRESCENTIA_THROW("cs_crescentia_throw", 3);

        final String path;
        final int id;
        AnimationsList(String file, int animId) {
            path = file;
            id = animId;
        }

        public KeyframeAnimation getAnimation() {
            return PlayerAnimationRegistry.getAnimation(new ResourceLocation(Celestisynth.MODID, getPath()));
        }

        public String getPath() {
            return path;
        }

        public int getId() {
            return id;
        }
    }

    public static void playAnimation(AnimationsList animation) {
        CSNetwork.sendToServer(new SetAnimationServerPacket(animation.getId()));
    }

    public static void playAnimation(KeyframeAnimation animation, ModifierLayer<IAnimation> layer) {
        layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, Ease.LINEAR), new KeyframeAnimationPlayer(animation)
                .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                .setFirstPersonConfiguration(new FirstPersonConfiguration()
                        .setShowRightArm(true)
                        .setShowLeftItem(true)
                ), true
        );
    }

    public static void cancelConcurrentAnimation(AbstractClientPlayer player, ModifierLayer<IAnimation> layer) {
        if (layer.getAnimation() != null) {
            layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(0, Ease.LINEAR), null);
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
