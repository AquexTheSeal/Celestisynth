package org.thecelestialworkshop.celestisynth.common.network.c2s;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.api.animation.player.AnimationManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.CSAnimator;
import org.thecelestialworkshop.celestisynth.api.animation.player.LayerManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class UpdateAnimationToAllPacket {
    private final int layerIndex;
    private final int playerId;
    private final ResourceLocation animId;

    public UpdateAnimationToAllPacket(int layerIndex, int playerId, ResourceLocation animId) {
        this.layerIndex = layerIndex;
        this.playerId = playerId;
        this.animId = animId;
    }

    public UpdateAnimationToAllPacket(FriendlyByteBuf buf) {
        this.layerIndex = buf.readInt();
        this.playerId = buf.readInt();
        this.animId = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(layerIndex);
        buf.writeInt(playerId);
        buf.writeResourceLocation(animId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            var player = instance.level.getEntity(playerId);
            animatePlayer(layerIndex, animId, (AbstractClientPlayer) player);
        });
        return true;
    }

    public static void animatePlayer(int layerIndex, ResourceLocation animId, AbstractClientPlayer player) {
        ModifierLayer<IAnimation> layer = switch (layerIndex) {
            case LayerManager.MAIN_LAYER -> CSAnimator.animationData.get(player);
            case LayerManager.LOW_PRIORITY_LAYER -> CSAnimator.otherAnimationData.get(player);
            case LayerManager.MIRRORED_LAYER -> CSAnimator.mirroredAnimationData.get(player);
            default -> throw new IllegalStateException("Invalid layer index!");
        };
        if (layer != null) {
            @Nullable PlayerAnimationContainer animation = RegistryManager.ACTIVE.getRegistry(CSPlayerAnimations.ANIMATIONS_KEY).getValue(animId);
            if (animation == null) {
                Celestisynth.LOGGER.warn("Failed to capture animation for server sync: " + animId);
            }

            if (layerIndex == LayerManager.MAIN_LAYER) AnimationManager.playAnimation(null, CSAnimator.mirroredAnimationData.get(player));
            if (layerIndex == LayerManager.MIRRORED_LAYER) AnimationManager.playAnimation(null, CSAnimator.animationData.get(player));
            AnimationManager.playAnimation(animation == null || animation == CSPlayerAnimations.CLEAR.get() ? null : animation.asAnimation(), layer);
        }
    }
}
