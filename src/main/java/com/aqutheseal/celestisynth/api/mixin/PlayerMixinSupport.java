package com.aqutheseal.celestisynth.api.mixin;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface PlayerMixinSupport {

    @OnlyIn(Dist.CLIENT)
    int getScreenShakeDuration();

    @OnlyIn(Dist.CLIENT)
    void setScreenShakeDuration(int duration);

    @OnlyIn(Dist.CLIENT)
    int getScreenShakeFadeoutBegin();

    @OnlyIn(Dist.CLIENT)
    void setScreenShakeFadeoutBegin(int duration);

    @OnlyIn(Dist.CLIENT)
    float getScreenShakeIntensity();

    @OnlyIn(Dist.CLIENT)
    void setScreenShakeIntensity(float intensity);

    int getCameraAngleOrdinal();

    void setCameraAngleOrdinal(int ordinal);
}
