package org.thecelestialworkshop.celestisynth.common.network.c2s;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.network.s2c.UpdateGroupedParticlePacket;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateParticlePacket(ParticleType<?> particle, double x, double y, double z, float xSpeed, float ySpeed, float zSpeed) implements CustomPacketPayload {
    public static final Type<UpdateParticlePacket> TYPE = new Type<>(Celestisynth.prefix("update_particle"));

    public static final StreamCodec<FriendlyByteBuf, UpdateParticlePacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> {
                buffer.writeResourceLocation(BuiltInRegistries.PARTICLE_TYPE.getKey(packet.particle()));
                buffer.writeDouble(packet.x());
                buffer.writeDouble(packet.y());
                buffer.writeDouble(packet.z());
                buffer.writeFloat(packet.xSpeed());
                buffer.writeFloat(packet.ySpeed());
                buffer.writeFloat(packet.zSpeed());
            },
            (buffer) -> new UpdateParticlePacket(
                    BuiltInRegistries.PARTICLE_TYPE.get(buffer.readResourceLocation()),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readFloat(), buffer.readFloat(), buffer.readFloat()
            )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateParticlePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            CSNetworkManager.sendToAll(new UpdateGroupedParticlePacket(packet.particle(), packet.particle().getOverrideLimiter(), packet.x(), packet.y(), packet.z(), 0, 0, 0, packet.xSpeed(), packet.ySpeed(), packet.zSpeed(), 1));
        });
    }
}
