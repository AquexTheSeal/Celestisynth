package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpdateGroupedParticlePacket(ParticleType<?> particle, boolean overrideLimiter, double x, double y, double z, float xDist, float yDist, float zDist, float xSpeed, float ySpeed, float zSpeed, int count) implements CustomPacketPayload {
    public static final Type<UpdateGroupedParticlePacket> TYPE = new Type<>(Celestisynth.prefix("update_grouped_particle"));

    public static final StreamCodec<FriendlyByteBuf, UpdateGroupedParticlePacket> STREAM_CODEC = StreamCodec.of(
            (buffer, packet) -> {
                buffer.writeResourceLocation(BuiltInRegistries.PARTICLE_TYPE.getKey(packet.particle()));
                buffer.writeBoolean(packet.overrideLimiter());
                buffer.writeDouble(packet.x());
                buffer.writeDouble(packet.y());
                buffer.writeDouble(packet.z());
                buffer.writeFloat(packet.xDist());
                buffer.writeFloat(packet.yDist());
                buffer.writeFloat(packet.zDist());
                buffer.writeFloat(packet.xSpeed());
                buffer.writeFloat(packet.ySpeed());
                buffer.writeFloat(packet.zSpeed());
                buffer.writeInt(packet.count());
            },
            (buffer) -> new UpdateGroupedParticlePacket(
                    BuiltInRegistries.PARTICLE_TYPE.get(buffer.readResourceLocation()),
                    buffer.readBoolean(),
                    buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                    buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                    buffer.readInt()
            )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateGroupedParticlePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.level == null) return;
            if (packet.count() == 0) {
                try {
                    minecraft.level.addAlwaysVisibleParticle((ParticleOptions) packet.particle(), true, packet.x(), packet.y(), packet.z(), packet.xSpeed(), packet.ySpeed(), packet.zSpeed());
                } catch (Throwable throwable1) {
                    Celestisynth.LOGGER.warn("Could not spawn particle effect {}", packet.particle());
                }
            } else {
                for (int i = 0; i < packet.count(); ++i) {
                    double xOffset = minecraft.level.random.nextGaussian() * packet.xDist();
                    double yOffset = minecraft.level.random.nextGaussian() * packet.yDist();
                    double zOffset = minecraft.level.random.nextGaussian() * packet.zDist();

                    try {
                        minecraft.level.addAlwaysVisibleParticle((ParticleOptions) packet.particle(), true, packet.x() + xOffset, packet.y() + yOffset, packet.z() + zOffset, packet.xSpeed(), packet.ySpeed(), packet.zSpeed());
                    } catch (Throwable throwable) {
                        Celestisynth.LOGGER.warn("Could not spawn particle effect {}", packet.particle());
                    }
                }
            }
        });
    }
}
