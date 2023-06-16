package com.aqutheseal.celestisynth.common.packet.network;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class CSSpawnParticlePacket {

    public <T extends ParticleType<?>> CSSpawnParticlePacket(T pParticle, boolean pOverrideLimiter, double pX, double pY, double pZ, float pXDist, float pYDist, float pZDist, float xSpeed, float ySpeed, float zSpeed, int pCount) {
    }

    public CSSpawnParticlePacket(FriendlyByteBuf buffer) {
    }

    public void toBytes(FriendlyByteBuf buffer) {
    }


    public boolean isOverrideLimiter() {
        return false;
    }

    public double getX() {
        return 0;
    }

    public double getY() {
        return 0;
    }

    public double getZ() {
        return 0;
    }

    public float getXDist() {
        return 0;
    }

    public float getYDist() {
        return 0;
    }

    public float getZDist() {
        return 0;
    }

    public float getXSpeed() {
        return 0;
    }

    public float getYSpeed() {
        return 0;
    }

    public float getZSpeed() {
        return 0;
    }

    public int getCount() {
        return 0;
    }

    public ParticleType<?> getParticle() {
        return null;
    }
}
