package com.aqutheseal.celestisynth.entities.helper;

import com.aqutheseal.celestisynth.entities.CSEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public enum CSEffectTypes {

    // Weapons
    SOLARIS_BLITZ("solaris_spin", Model.FLAT, Animation.SPIN_20, 11, 2, 2.5, true, true, false),
    SOLARIS_BLITZ_SOUL("solaris_spin_soul", Model.FLAT, Animation.SPIN_20, 11, 2, 3.5, true, true, false),
    SOLARIS_AIR("solaris_air", Model.FLAT, Animation.SPIN_20, 0, 0, 2.5, true, true, false),
    SOLARIS_AIR_LARGE("solaris_air_large", "solaris_air", Model.FLAT, Animation.SPIN_20, 0, 0, 3.5, true, true, false),
    SOLARIS_AIR_FLAT("solaris_air_flat", "solaris_air", Model.FLAT, Animation.SPIN_20, 0, 0, 1.5, false, true, false),
    SOLARIS_AIR_MEDIUM_FLAT("solaris_air_medium_flat", "solaris_air", Model.FLAT, Animation.SPIN_20, 0, 0, 2.5, false, true, false),
    SOLARIS_AIR_LARGE_FLAT("solaris_air_large_flat", "solaris_air", Model.FLAT, Animation.SPIN_20, 0, 0, 3.5, false, true, false),
    CRESCENTIA_STRIKE("crescentia_strike", Model.FLAT, Animation.SWEEP_RTOL, 0, 0, 2.5, false, false, true),
    CRESCENTIA_STRIKE_INVERTED("crescentia_strike_inverted", "crescentia_strike", Model.FLAT, Animation.SWEEP_LTOR, 0, 0, 2.5, false, false, true),
    CRESCENTIA_THROW("crescentia_throw", Model.FLAT, Animation.SWEEP_RTOL, 0, 0, 3, false, true, true),
    CRESCENTIA_THROW_INVERTED("crescentia_throw_inverted", "crescentia_throw", Model.FLAT, Animation.SWEEP_LTOR, 0, 0, 3, false, true, true),
    BREEZEBREAKER_SLASH("breezebreaker_slash", Model.FLAT, Animation.SWEEP_RTOL, 0, 0, 3, false, true, true),
    BREEZEBREAKER_SLASH_INVERTED("breezebreaker_slash_inverted", "breezebreaker_slash", Model.FLAT, Animation.SWEEP_LTOR, 0, 0, 3, false, true, true),
    BREEZEBREAKER_WHEEL("breezebreaker_slash_vertical", "breezebreaker_wheel", Model.FLAT_VERTICAL_FRONTFACE, Animation.SWEEP_RTOL, 0, 0, 4, false, true, false),
    BREEZEBREAKER_DASH("breezebreaker_dash", Model.CROSS, Animation.STRETCH, 0, 0, 3, false, true, false);

    final String name;
    final String texture;
    final Model model;
    final Animation animation;
    final int frames;
    final int framesSpeed;
    final double scale;
    final boolean rotateRandomly;
    final boolean fadeOut;
    final boolean specialProperties;

    CSEffectTypes(String name, String texture, Model model, Animation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
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

    CSEffectTypes(String texture, Model model, Animation animation, int frames, int framesSpeed, double scale, boolean rotateRandomly, boolean fadeOut, boolean specialProperties) {
        this(texture, texture, model, animation, frames, framesSpeed, scale, rotateRandomly, fadeOut, specialProperties);
    }

    public String getName() {
        return name;
    }

    public String getTexture() {
        return texture;
    }

    public Model getModel() {
        return model;
    }

    public Animation getAnimation() {
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

    public enum Model {

        FLAT("cs_effect_flat"),
        FLAT_INVERTED("cs_effect_flat_inverted"),
        FLAT_VERTICAL_SIDEFACE("cs_effect_flat_vertical"),
        FLAT_VERTICAL_FRONTFACE("cs_effect_flat_vertical_side"),
        IMPACT("cs_effect_impact"),
        CROSS("cs_effect_cross")
        ;

        final String modelString;

        Model(String model) {
            this.modelString = model;
        }

        public String getModelString() {
            return modelString;
        }
    }

    public enum Animation {

        NONE_10("animation.cs_effect.none", 10),
        NONE_15("animation.cs_effect.none", 15),
        NONE_20("animation.cs_effect.none", 20),
        SPIN_15("animation.cs_effect.spin", 15),
        SPIN_20("animation.cs_effect.spin", 20),
        SMASH("animation.cs_effect.smash", 20),
        ASCEND("animation.cs_effect.ascend", 20),
        SLOW_ROTATION("animation.cs_effect.slow_rotation", Integer.MAX_VALUE),
        SWEEP_RTOL("animation.cs_effect.sweep_rtol", 15),
        SWEEP_LTOR("animation.cs_effect.sweep_ltor", 15),
        STRETCH("animation.cs_effect.stretch", 15);

        final String animationString;
        final int lifespan;

        Animation(String animation, int lifespan) {
            this.animationString = animation;
            this.lifespan = lifespan;
        }

        public String getAnimationString() {
            return animationString;
        }

        public int getLifespan() {
            return lifespan;
        }
    }

    public static void setSpecialProperties(PoseStack poseStack, CSEffect animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        switch (animatable.getEffectType()) {
            case CRESCENTIA_STRIKE, CRESCENTIA_STRIKE_INVERTED, CRESCENTIA_THROW, CRESCENTIA_THROW_INVERTED ->
                    poseStack.mulPose(Axis.ZP.rotationDegrees(((animatable.getRotationZ() / 360.0F) * 90.0F) - 45.0F));
            case BREEZEBREAKER_SLASH, BREEZEBREAKER_SLASH_INVERTED ->
                    poseStack.mulPose(Axis.ZP.rotationDegrees(((animatable.getRotationZ() / 360.0F) * 45.0F) - 22.5F));
            default -> {
            }
        }
    }
}
