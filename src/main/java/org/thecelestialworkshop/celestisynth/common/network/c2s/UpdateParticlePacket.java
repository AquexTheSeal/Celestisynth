package org.thecelestialworkshop.celestisynth.common.network.c2s;

import org.thecelestialworkshop.celestisynth.common.network.s2c.UpdateGroupedParticlePacket;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class UpdateParticlePacket {
    private final double x;
    private final double y;
    private final double z;
    private final float xSpeed;
    private final float ySpeed;
    private final float zSpeed;
    private final ParticleType<?> particle;

    public <T extends ParticleType<?>> UpdateParticlePacket(T pParticle, double pX, double pY, double pZ, float xSpeed, float ySpeed, float zSpeed) {
        this.particle = pParticle;
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }

    public UpdateParticlePacket(FriendlyByteBuf buffer) {
        ParticleType<?> particletype = ForgeRegistries.PARTICLE_TYPES.getValue(buffer.readResourceLocation());
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.xSpeed = buffer.readFloat();
        this.ySpeed = buffer.readFloat();
        this.zSpeed = buffer.readFloat();
        this.particle = particletype;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(ForgeRegistries.PARTICLE_TYPES.getKey(particle));
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeFloat(xSpeed);
        buffer.writeFloat(ySpeed);
        buffer.writeFloat(zSpeed);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            CSNetworkManager.sendToAll(new UpdateGroupedParticlePacket(particle, particle.getOverrideLimiter(), x, y, z, 0, 0, 0, xSpeed, ySpeed, zSpeed, 1));
        });
        return true;
    }
}
