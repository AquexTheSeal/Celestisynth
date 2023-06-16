package com.aqutheseal.celestisynth.client.animation;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.IdentityHashMap;
import java.util.Map;

public class CSAnimator {
    public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> animationData = new IdentityHashMap<>();
    public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> otherAnimationData = new IdentityHashMap<>();

    public static void registerPlayerAnimation(AbstractClientPlayer player, AnimationStack stack) {
        var layer = new ModifierLayer<>();
        stack.addAnimLayer(6900, layer);
        CSAnimator.animationData.put(player, layer);

        var layerOther = new ModifierLayer<>();
        stack.addAnimLayer(4200, layerOther);
        CSAnimator.otherAnimationData.put(player, layerOther);
    }
}
