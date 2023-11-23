package com.aqutheseal.celestisynth.common.network.util;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CSSpawnParticlePacket {

    private final double x;
    private final double y;
    private final double z;
    private final float xDist;
    private final float yDist;
    private final float zDist;
    private final float xSpeed;
    private final float ySpeed;
    private final float zSpeed;
    private final int count;
    private final boolean overrideLimiter;
    private final ParticleType<?> particle;

    public <T extends ParticleType<?>> CSSpawnParticlePacket(T pParticle, boolean pOverrideLimiter, double pX, double pY, double pZ, float pXDist, float pYDist, float pZDist, float xSpeed, float ySpeed, float zSpeed, int pCount) {
        this.particle = pParticle;
        this.overrideLimiter = pOverrideLimiter;
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.xDist = pXDist;
        this.yDist = pYDist;
        this.zDist = pZDist;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        this.count = pCount;
    }

    public CSSpawnParticlePacket(FriendlyByteBuf buffer) {
        ParticleType<?> particletype = ForgeRegistries.PARTICLE_TYPES.getValue(buffer.readResourceLocation());
        this.overrideLimiter = buffer.readBoolean();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.xDist = buffer.readFloat();
        this.yDist = buffer.readFloat();
        this.zDist = buffer.readFloat();
        this.xSpeed = buffer.readFloat();
        this.ySpeed = buffer.readFloat();
        this.zSpeed = buffer.readFloat();
        this.count = buffer.readInt();
        this.particle = particletype;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(ForgeRegistries.PARTICLE_TYPES.getKey(particle));
        buffer.writeBoolean(overrideLimiter);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeFloat(xDist);
        buffer.writeFloat(yDist);
        buffer.writeFloat(zDist);
        buffer.writeFloat(xSpeed);
        buffer.writeFloat(ySpeed);
        buffer.writeFloat(zSpeed);
        buffer.writeInt(count);
    }

    public boolean isOverrideLimiter() {
        return this.overrideLimiter;
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

    public float getXDist() {
        return this.xDist;
    }

    public float getYDist() {
        return this.yDist;
    }

    public float getZDist() {
        return this.zDist;
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

    public int getCount() {
        return this.count;
    }

    public ParticleType<?> getParticle() {
        return this.particle;
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return false;
        }
        if (getCount() == 0) {
            double d0 = getXSpeed();
            double d2 = getYSpeed();
            double d4 = getZSpeed();
            try {
                minecraft.level.addParticle((ParticleOptions) getParticle(), isOverrideLimiter(), getX(), getY(), getZ(), d0, d2, d4);
            } catch (Throwable throwable1) {
                Celestisynth.LOGGER.warn("Could not spawn particle effect {}", getParticle());
            }
        } else {
            for (int i = 0; i < getCount(); ++i) {
                double d1 = minecraft.level.random.nextGaussian() * (double) getXDist();
                double d3 = minecraft.level.random.nextGaussian() * (double) getYDist();
                double d5 = minecraft.level.random.nextGaussian() * (double) getZDist();
                double d6 = getXSpeed();
                double d7 = getYSpeed();
                double d8 = getZSpeed();

                try {
                    minecraft.level.addParticle((ParticleOptions) getParticle(), isOverrideLimiter(), getX() + d1, getY() + d3, getZ() + d5, d6, d7, d8);
                } catch (Throwable throwable) {
                    Celestisynth.LOGGER.warn("Could not spawn particle effect {}", getParticle());
                }
            }
        }
        return true;
    }
}
