package com.aqutheseal.celestisynth.api.mixin;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface PlayerMixinSupport {

    int getScreenShakeDuration();

    void setScreenShakeDuration(int duration);

    int getScreenShakeFadeoutBegin();

    void setScreenShakeFadeoutBegin(int duration);

    float getScreenShakeIntensity();

    void setScreenShakeIntensity(float intensity);
}
