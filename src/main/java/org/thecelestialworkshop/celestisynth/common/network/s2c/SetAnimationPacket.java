package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateAnimationToAllPacket;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetAnimationPacket(int layerIndex, ResourceLocation animId) implements CustomPacketPayload {
    public static final Type<SetAnimationPacket> TYPE = new Type<>(Celestisynth.prefix("set_animation"));

    public static final StreamCodec<FriendlyByteBuf, SetAnimationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SetAnimationPacket::layerIndex,
            ResourceLocation.STREAM_CODEC, SetAnimationPacket::animId,
            SetAnimationPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetAnimationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer sender) {
                CSNetworkManager.sendToPlayersNearbyAndSelf(new UpdateAnimationToAllPacket(packet.layerIndex(), sender.getId(), packet.animId()), sender);
            }
        });
    }
}
