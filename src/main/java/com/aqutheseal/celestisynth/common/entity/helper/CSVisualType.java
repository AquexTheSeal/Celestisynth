package com.aqutheseal.celestisynth.common.entity.helper;

import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

public class CSVisualType {
    private final String name;
    private final String texture;
    private final CSVisualModel model;
    private final CSVisualAnimation animation;
    @Nullable private final SoundEvent effectSound;
    private final int frames;
    private final int framesSpeed;
    private final double scale;
    private final boolean rotateRandomly;
    private final boolean fadeOut;
    private final boolean specialProperties;

    public CSVisualType(String name, String texture, CSVisualModel model, CSVisualAnimation animation, @Nullable SoundEvent effectSound, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this.name = name;
        this.texture = texture;
        this.model = model;
        this.animation = animation;
        this.effectSound = effectSound;
        this.frames = frames;
        this.framesSpeed = framesSpeed;
        this.scale = scale;
        this.rotateRandomly = rotateRandomly;
        this.fadeOut = fadeOut;
        this.specialProperties = specialProperties;
    }

    public CSVisualType(String name, String texture, CSVisualModel model, CSVisualAnimation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this(name, texture, model, animation, null, frames, framesSpeed, scale, rotateRandomly, fadeOut, specialProperties);
    }

    public CSVisualType(String texture, CSVisualModel model, CSVisualAnimation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this(texture, texture, model, animation, null, frames, framesSpeed, scale, rotateRandomly, fadeOut, specialProperties);
    }

    public String getName() {
        return name;
    }

    public String getTexture() {
        return texture;
    }

    public CSVisualModel getModel() {
        return model;
    }

    public CSVisualAnimation getAnimation() {
        return animation;
    }

    public SoundEvent getEffectSound() {
        return effectSound;
    }

    public int getFrames() {
        return frames;
    }

    public int getFramesSpeed() {
        return framesSpeed;
    }

    public double getScale() {
        return scale;
    }

    public boolean isRotateRandomly() {
        return rotateRandomly;
    }

    public boolean isFadeOut() {
        return fadeOut;
    }

    public boolean hasSpecialProperties() {
        return specialProperties;
    }
}
