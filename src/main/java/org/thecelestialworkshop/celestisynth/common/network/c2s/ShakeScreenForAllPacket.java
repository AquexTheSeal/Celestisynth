package org.thecelestialworkshop.celestisynth.common.network.c2s;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.network.s2c.ShakeScreenToAllPacket;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record ShakeScreenForAllPacket(UUID playerId, int duration, int fadeOutStart, float intensity) implements CustomPacketPayload {
    public static final Type<ShakeScreenForAllPacket> TYPE = new Type<>(Celestisynth.prefix("shake_screen_for_all"));

    public static final StreamCodec<FriendlyByteBuf, ShakeScreenForAllPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, ShakeScreenForAllPacket::playerId,
            ByteBufCodecs.INT, ShakeScreenForAllPacket::duration,
            ByteBufCodecs.INT, ShakeScreenForAllPacket::fadeOutStart,
            ByteBufCodecs.FLOAT, ShakeScreenForAllPacket::intensity,
            ShakeScreenForAllPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ShakeScreenForAllPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> CSNetworkManager.sendToAll(new ShakeScreenToAllPacket(packet.playerId(), packet.duration(), packet.fadeOutStart(), packet.intensity())));
    }
}
