package com.aqutheseal.celestisynth.common.network.util;

import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class C2SParticlePacket {

    private final double x;
    private final double y;
    private final double z;
    private final float xSpeed;
    private final float ySpeed;
    private final float zSpeed;
    private final ParticleType<?> particle;

    public <T extends ParticleType<?>> C2SParticlePacket(T pParticle, double pX, double pY, double pZ, float xSpeed, float ySpeed, float zSpeed) {
        this.particle = pParticle;
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }

    public C2SParticlePacket(FriendlyByteBuf buffer) {
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

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getXSpeed() {
        return this.xSpeed;
    }

    public float getYSpeed() {
        return this.ySpeed;
    }

    public float getZSpeed() {
        return this.zSpeed;
    }

    public ParticleType<?> getParticle() {
        return this.particle;
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() -> {
            CSNetworkManager.sendToAll(new S2CGroupedParticlePacket(particle, particle.getOverrideLimiter(), x, y, z, 0, 0, 0, xSpeed, ySpeed, zSpeed, 1));
        });
        return true;
    }
}
