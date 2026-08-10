package org.thecelestialworkshop.celestisynth.api.animation.player;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record PlayerAnimationContainer(@NotNull ResourceLocation animationId) {
    public KeyframeAnimation asAnimation() {
        return PlayerAnimationRegistry.getAnimation(animationId) instanceof KeyframeAnimation keyframeAnimation ? keyframeAnimation : null;
    }
}
