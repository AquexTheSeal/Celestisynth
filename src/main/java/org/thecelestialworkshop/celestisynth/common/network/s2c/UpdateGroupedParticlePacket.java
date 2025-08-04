package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class UpdateGroupedParticlePacket {
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

    public <T extends ParticleType<?>> UpdateGroupedParticlePacket(T pParticle, boolean pOverrideLimiter, double pX, double pY, double pZ, float pXDist, float pYDist, float pZDist, float xSpeed, float ySpeed, float zSpeed, int pCount) {
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

    public UpdateGroupedParticlePacket(FriendlyByteBuf buffer) {
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

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) return false;
        if (count == 0) {
            try {
                minecraft.level.addAlwaysVisibleParticle((ParticleOptions) particle, true, x, y, z, xSpeed, ySpeed, zSpeed);
            } catch (Throwable throwable1) {
                Celestisynth.LOGGER.warn("Could not spawn particle effect {}", particle);
            }
        } else {
            for (int i = 0; i < count; ++i) {
                double xOffset = minecraft.level.random.nextGaussian() * xDist;
                double yOffset = minecraft.level.random.nextGaussian() * yDist;
                double zOffset = minecraft.level.random.nextGaussian() * zDist;

                try {
                    minecraft.level.addAlwaysVisibleParticle((ParticleOptions) particle, true, x + xOffset, y + yOffset, z + zOffset, xSpeed, ySpeed, zSpeed);
                } catch (Throwable throwable) {
                    Celestisynth.LOGGER.warn("Could not spawn particle effect {}", particle);
                }
            }
        }
        return true;
    }
}
