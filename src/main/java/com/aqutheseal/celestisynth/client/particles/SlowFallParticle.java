package com.aqutheseal.celestisynth.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SlowFallParticle extends SnowflakeParticle {
    protected SlowFallParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pSprites);
        this.quadSize = 0.05F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public final SpriteSet sprites;
        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SlowFallParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
        }
    }
    @Override
    public void tick() {
        super.tick();
        float progress = (float) this.age / this.lifetime;
        this.alpha = Mth.lerp(progress, 1, 0);
    }
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Ash extends SlowFallParticle {
        private final float rotSpeed;
        private final float startQuadSize;
        protected Ash(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
            super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pSprites);
            this.rotSpeed = ((float)Math.random() - 0.5F) * 0.025F;
            this.startQuadSize = 0.3F + (float) this.random.nextGaussian() * 0.25F;
        }

        @Override
        public void tick() {
            super.tick();
            this.oRoll = this.roll;
            this.roll += (float)Math.PI * this.rotSpeed * 2.0F;
            float progress = (float) this.age / this.lifetime;
            this.quadSize = Mth.lerp(progress, startQuadSize, 0);
            this.alpha = Mth.lerp(progress, 0.85F, 0);
        }

        @OnlyIn(Dist.CLIENT)
        public static class Provider extends SlowFallParticle.Provider {
            public Provider(SpriteSet pSprites) {
                super(pSprites);
            }
            public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
                return new SlowFallParticle.Ash(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
            }
        }
    }
}
