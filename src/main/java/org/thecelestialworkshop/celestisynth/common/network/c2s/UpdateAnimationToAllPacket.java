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
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import org.jetbrains.annotations.Nullable;

public record UpdateAnimationToAllPacket(int layerIndex, int playerId, ResourceLocation animId) implements CustomPacketPayload {
    public static final Type<UpdateAnimationToAllPacket> TYPE = new Type<>(Celestisynth.prefix("update_animation_to_all"));

    public static final StreamCodec<FriendlyByteBuf, UpdateAnimationToAllPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateAnimationToAllPacket::layerIndex,
            ByteBufCodecs.INT, UpdateAnimationToAllPacket::playerId,
            ResourceLocation.STREAM_CODEC, UpdateAnimationToAllPacket::animId,
            UpdateAnimationToAllPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateAnimationToAllPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            if (instance.level == null) return;
            var player = instance.level.getEntity(packet.playerId());
            if (player instanceof AbstractClientPlayer clientPlayer) {
                animatePlayer(packet.layerIndex(), packet.animId(), clientPlayer);
            }
        });
    }

    public static void animatePlayer(int layerIndex, ResourceLocation animId, AbstractClientPlayer player) {
        ModifierLayer<IAnimation> layer = switch (layerIndex) {
            case LayerManager.MAIN_LAYER -> CSAnimator.animationData.get(player);
            case LayerManager.LOW_PRIORITY_LAYER -> CSAnimator.otherAnimationData.get(player);
            case LayerManager.MIRRORED_LAYER -> CSAnimator.mirroredAnimationData.get(player);
            default -> throw new IllegalStateException("Invalid layer index!");
        };
        if (layer != null) {
            @Nullable PlayerAnimationContainer animation = CSPlayerAnimations.REGISTRY.get(animId);
            if (animation == null) {
                Celestisynth.LOGGER.warn("Failed to capture animation for server sync: " + animId);
            }

            if (layerIndex == LayerManager.MAIN_LAYER) AnimationManager.playAnimation(null, CSAnimator.mirroredAnimationData.get(player));
            if (layerIndex == LayerManager.MIRRORED_LAYER) AnimationManager.playAnimation(null, CSAnimator.animationData.get(player));
            AnimationManager.playAnimation(animation == null || animation == CSPlayerAnimations.CLEAR.get() ? null : animation.asAnimation(), layer);
        }
    }
}
