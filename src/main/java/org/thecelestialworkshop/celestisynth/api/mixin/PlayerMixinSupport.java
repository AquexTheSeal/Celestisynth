package org.thecelestialworkshop.celestisynth.api.mixin;

public interface PlayerMixinSupport {

    int getPulseScale();
    void setPulseScale(int scale);

    String getChantMessage();
    void setChantMessage(String message);
    int getChantMark();
    void setChantMark(int time);
    int getChantColor();
    void setChantColor(int color);

    int getTexturePulseMark();
    void setTexturePulseMark(int time);
    String getTexturePulseImage();
    void setTexturePulseImage(String order);

    int getPulseTimeSpeed();
    void setPulseTimeSpeed(int speed);

    int getScreenShakeDuration();
    void setScreenShakeDuration(int duration);
    int getScreenShakeFadeoutBegin();
    void setScreenShakeFadeoutBegin(int duration);
    float getScreenShakeIntensity();
    void setScreenShakeIntensity(float intensity);
}
