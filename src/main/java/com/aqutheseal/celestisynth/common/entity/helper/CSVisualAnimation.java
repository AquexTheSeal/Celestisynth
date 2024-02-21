package com.aqutheseal.celestisynth.common.entity.helper;

public class CSVisualAnimation {
    public static CSVisualAnimation SPIN = new CSVisualAnimation("animation.cs_effect.spin", 15);
    public static CSVisualAnimation ASCEND = new CSVisualAnimation("animation.cs_effect.ascend", 20);
    public static CSVisualAnimation SLOW_ROTATION = new CSVisualAnimation("animation.cs_effect.slow_rotation", Integer.MAX_VALUE);
    public static CSVisualAnimation SWEEP_RTOL = new CSVisualAnimation("animation.cs_effect.sweep_rtol", 15);
    public static CSVisualAnimation SWEEP_LTOR = new CSVisualAnimation("animation.cs_effect.sweep_ltor", 15);
    public static CSVisualAnimation STRETCH = new CSVisualAnimation("animation.cs_effect.stretch", 15);
    public static CSVisualAnimation GOO = new CSVisualAnimation("animation.cs_effect.goo", Integer.MAX_VALUE);
    public static CSVisualAnimation SPIN_EXPAND = new CSVisualAnimation("animation.cs_effect.spin_expand", 15);

    private final String animName;
    private final int lifespan;

    public CSVisualAnimation(String animation, int lifespan) {
        this.animName = animation;
        this.lifespan = lifespan;
    }

    public String getAnimName() {
        return animName;
    }

    public int getLifespan() {
        return lifespan;
    }

    public static CSVisualAnimation noAnimWithLifespan(int duration) {
        return new CSVisualAnimation("animation.cs_effect.none", duration);
    }
}
