package com.aqutheseal.celestisynth.client.particles;

import com.aqutheseal.celestisynth.common.registry.CSParticleRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class RainfallEnergyParticle extends SimpleAnimatedParticle {
    protected float rotSpeed;
    protected float startQuadSize;

    RainfallEnergyParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pSprites, 0.0F);
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.lifetime = 20;
        this.rotSpeed = ((float)Math.random() - 0.5F) * 0.3F;
        this.startQuadSize = 1.5F;

        setFadeColor(15916745);
        setSpriteFromAge(pSprites);
    }

    @Override
    public void tick() {
        super.tick();
        float endQuadSize = 0.0F;

        if (this.age < this.lifetime) {
            float progress = (float) this.age / this.lifetime;
            this.quadSize = startQuadSize + progress * (endQuadSize - startQuadSize);
            changeAlpha(quadSize);
        } else this.quadSize = endQuadSize;

        this.oRoll = this.roll;
        this.roll += (float)Math.PI * this.rotSpeed * 2.0F;
    }

    public void changeAlpha(float value) {
        this.alpha = Mth.clamp(value, 0, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CSParticleRenderTypes.PARTICLE_SHEET_TRANSLUCENT_LIT;
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
            return new RainfallEnergyParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
        }
    }

    public static class Small extends RainfallEnergyParticle {

        Small(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
            super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pSprites);
            this.rotSpeed = ((float)Math.random() - 0.5F) * 0.15F;
            this.startQuadSize = 0.7F;
        }

        public static class Provider extends RainfallEnergyParticle.Provider {

            public Provider(SpriteSet pSprites) { super(pSprites); }

            @Override
            public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
                return new Small(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites);
            }
        }
    }
}
