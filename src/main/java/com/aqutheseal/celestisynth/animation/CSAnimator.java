package com.aqutheseal.celestisynth.animation;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.IdentityHashMap;
import java.util.Map;

public class CSAnimator {
    public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> animationData = new IdentityHashMap<>();
    public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> otherAnimationData = new IdentityHashMap<>();

    public static void registerAnimationLayer(FMLClientSetupEvent event) {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(CSAnimator::registerPlayerAnimation);
    }

    public static void registerPlayerAnimation(AbstractClientPlayer player, AnimationStack stack) {
        var layer = new ModifierLayer<>();
        stack.addAnimLayer(6900, layer);
        CSAnimator.animationData.put(player, layer);

        var layerOther = new ModifierLayer<>();
        stack.addAnimLayer(4200, layerOther);
        CSAnimator.otherAnimationData.put(player, layerOther);
    }
}
