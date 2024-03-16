package com.aqutheseal.celestisynth.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CrescentiaFireworkParticle extends SlowFallParticle {
    protected CrescentiaFireworkParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pSprites);
        this.lifetime = 50;
        this.gravity = 0.1F;
        this.quadSize = 0.2F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Purple extends SlowFallParticle.Provider {
        public Purple(SpriteSet pSprites) {
            super(pSprites);
        }
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            Particle particle = new CrescentiaFireworkParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
            particle.setColor(1.0F, 1.0F, 1.0F);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Pink extends SlowFallParticle.Provider {
        public Pink(SpriteSet pSprites) {
            super(pSprites);
        }
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            Particle particle = new CrescentiaFireworkParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
            particle.setColor(1.0F, 1.0F, 1.0F);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Blue extends SlowFallParticle.Provider {
        public Blue(SpriteSet pSprites) {
            super(pSprites);
        }
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            Particle particle = new CrescentiaFireworkParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
            particle.setColor(1.0F, 1.0F, 1.0F);
            return particle;
        }
    }
}
