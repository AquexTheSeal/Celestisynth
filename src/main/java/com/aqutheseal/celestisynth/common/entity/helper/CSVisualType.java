package com.aqutheseal.celestisynth.common.entity.helper;

public class CSVisualType {
    private final String name;
    private final String texture;
    private final CSVisualModel model;
    private final CSVisualAnimation animation;
    private final int frames;
    private final int framesSpeed;
    private final double scale;
    private final boolean rotateRandomly;
    private final boolean fadeOut;
    private final boolean specialProperties;

    public CSVisualType(String name, String texture, CSVisualModel model, CSVisualAnimation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this.name = name;
        this.texture = texture;
        this.model = model;
        this.animation = animation;
        this.frames = frames;
        this.framesSpeed = framesSpeed;
        this.scale = scale;
        this.rotateRandomly = rotateRandomly;
        this.fadeOut = fadeOut;
        this.specialProperties = specialProperties;
    }

    public CSVisualType(String texture, CSVisualModel model, CSVisualAnimation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this(texture, texture, model, animation, frames, framesSpeed, scale, rotateRandomly, fadeOut, specialProperties);
    }

    public static CSVisualType createSkin(String skin, CSVisualType parent) {
        return new CSVisualType(parent.name + "_" + skin, "skin/" + parent.texture + "_" + skin, parent.model, parent.animation, parent.frames, parent.framesSpeed, parent.scale, parent.rotateRandomly, parent.fadeOut, parent.specialProperties);
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
