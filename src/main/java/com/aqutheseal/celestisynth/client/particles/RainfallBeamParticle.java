package com.aqutheseal.celestisynth.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class RainfallBeamParticle extends SimpleAnimatedParticle {

    RainfallBeamParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pSprites, 0.0F);
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.lifetime = 10;

        setFadeColor(15916745);
        setSpriteFromAge(pSprites);
    }

    @Override
    public void tick() {
        super.tick();
        float startQuadSize = 1.0F;
        float endQuadSize = 0.0F;

        if (this.age < this.lifetime) {
            float progress = (float) this.age / this.lifetime;
            this.quadSize = Mth.lerp(progress, startQuadSize * 2, endQuadSize);
            this.alpha = Mth.lerp(progress, startQuadSize, endQuadSize);
        } else this.quadSize = endQuadSize;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void move(double pX, double pY, double pZ) {
        setBoundingBox(this.getBoundingBox().move(pX, pY, pZ));
        setLocationFromBoundingbox();
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new RainfallBeamParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
        }
    }

    public static class Quasar extends RainfallBeamParticle {

        Quasar(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
            super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pSprites);
        }

        public static class Provider extends RainfallBeamParticle.Provider {

            public Provider(SpriteSet pSprites) { super(pSprites); }

            @Override
            public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
                return new Quasar(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
            }
        }
    }
}
