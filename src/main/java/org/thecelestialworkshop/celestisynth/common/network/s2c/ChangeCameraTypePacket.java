package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChangeCameraTypePacket(int playerTarget, int enumID) implements CustomPacketPayload {
    public static final Type<ChangeCameraTypePacket> TYPE = new Type<>(Celestisynth.prefix("change_camera_type"));

    public static final StreamCodec<FriendlyByteBuf, ChangeCameraTypePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ChangeCameraTypePacket::playerTarget,
            ByteBufCodecs.INT, ChangeCameraTypePacket::enumID,
            ChangeCameraTypePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChangeCameraTypePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            if (instance.player != null && instance.player.getId() == packet.playerTarget()) {
                instance.options.setCameraType(CameraType.values()[packet.enumID()]);
            }
        });
    }
}
