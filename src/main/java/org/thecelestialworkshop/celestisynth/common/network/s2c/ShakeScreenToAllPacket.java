package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.api.mixin.PlayerMixinSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record ShakeScreenToAllPacket(UUID playerId, int duration, int fadeOutStart, float intensity) implements CustomPacketPayload {
    public static final Type<ShakeScreenToAllPacket> TYPE = new Type<>(Celestisynth.prefix("shake_screen_to_all"));

    public static final StreamCodec<FriendlyByteBuf, ShakeScreenToAllPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, ShakeScreenToAllPacket::playerId,
            ByteBufCodecs.INT, ShakeScreenToAllPacket::duration,
            ByteBufCodecs.INT, ShakeScreenToAllPacket::fadeOutStart,
            ByteBufCodecs.FLOAT, ShakeScreenToAllPacket::intensity,
            ShakeScreenToAllPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ShakeScreenToAllPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            if (instance.level == null) return;
            var player = instance.level.getPlayerByUUID(packet.playerId());
            if (player instanceof PlayerMixinSupport pms) {
                pms.setScreenShakeDuration(packet.duration());
                pms.setScreenShakeFadeoutBegin(packet.fadeOutStart());
                pms.setScreenShakeIntensity(packet.intensity());
            }
        });
    }
}
