package com.aqutheseal.celestisynth.entities.helper;

import com.aqutheseal.celestisynth.entities.CSEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraftforge.common.IExtensibleEnum;

public enum CSEffectTypes {

    // Weapons
    SOLARIS_BLITZ("solaris_spin", Model.FLAT, Animation.SPIN_15,
            0, 0, 2.5, true, true, false)
    ,
    SOLARIS_BLITZ_SOUL("solaris_spin_soul", Model.FLAT, Animation.SPIN_15,
            0, 0, 3.5, true, true, false)
    ,
    SOLARIS_AIR("solaris_air", Model.FLAT, Animation.SPIN_20,
            0, 0, 2.5, true, true, false)
    ,
    SOLARIS_AIR_LARGE("solaris_air_large", "solaris_air", Model.FLAT, Animation.SPIN_20,
            0, 0, 3.5, true, true, false)
    ,
    SOLARIS_AIR_FLAT("solaris_air_flat", "solaris_air", Model.FLAT, Animation.SPIN_20,
            0, 0, 1.5, false, true, false)
    ,
    SOLARIS_AIR_MEDIUM_FLAT("solaris_air_medium_flat", "solaris_air", Model.FLAT, Animation.SPIN_20,
            0, 0, 2.5, false, true, false)
    ,
    SOLARIS_AIR_LARGE_FLAT("solaris_air_large_flat", "solaris_air", Model.FLAT, Animation.SPIN_20,
            0, 0, 3.5, false, true, false)
    ,
    CRESCENTIA_STRIKE("crescentia_strike", Model.FLAT, noneTo(12),
            6, 2, 3.5, false, true, true)
    ,
    CRESCENTIA_STRIKE_INVERTED("crescentia_strike_inverted", "crescentia_strike", Model.FLAT_INVERTED, noneTo(12),
            6, 2, 3.5, false, true, true)
    ,
    CRESCENTIA_THROW("crescentia_throw", Model.FLAT, Animation.SWEEP_RTOL,
            0, 0, 3, false, true, true)
    ,
    CRESCENTIA_THROW_INVERTED("crescentia_throw_inverted", "crescentia_throw", Model.FLAT, Animation.SWEEP_LTOR,
            0, 0, 3, false, true, true)
    ,
    BREEZEBREAKER_SLASH("breezebreaker_slash", Model.FLAT, noneTo(10),
            5, 2, 3, false, false, true)
    ,
    BREEZEBREAKER_SLASH_INVERTED("breezebreaker_slash_inverted", "breezebreaker_slash", Model.FLAT_INVERTED, noneTo(10),
            5, 2, 3, false, false, true)
    ,
    BREEZEBREAKER_WHEEL("breezebreaker_wheel", Model.FLAT_VERTICAL_FRONTFACE, noneTo(12),
            6, 2, 4, false, false, false)
    ,
    BREEZEBREAKER_WHEEL_IMPACT("breezebreaker_wheel_impact", Model.FLAT_VERTICAL_SIDEFACE,  noneTo(12),
            6, 2, 2, false, false, true)
    ,
    BREEZEBREAKER_DASH("breezebreaker_dash", Model.SIX_WAY_CROSS, Animation.STRETCH,
            16, 2, 3, false, true, false)
    ,
    BREEZEBREAKER_DASH_2("breezebreaker_dash_2", Model.FLAT_VERTICAL_SIDEFACE, noneTo(15),
            0, 0, 4, false, true, false)
    ,
    BREEZEBREAKER_DASH_3("breezebreaker_dash_3", "breezebreaker_dash_2", Model.FLAT_VERTICAL_SIDEFACE, noneTo(10),
            0, 0, 2, false, true, false)
    ,
    POLTERGEIST_WARD("poltergeist_ward", Model.FLAT_VERTICAL_SIDEFACE, Animation.GOO,
            0, 0, 1.2, false, false, false)
    ,
    POLTERGEIST_WARD_GROUND("poltergeist_ward_ground", Model.FLAT, Animation.SLOW_ROTATION,
            0, 0, 1.55, false, false, false)
    ,
    POLTERGEIST_WARD_ABSORB("poltergeist_ward_absorb", Model.FLAT, Animation.SPIN_15,
            0, 0, 4, false, true, false)
    ,
    POLTERGEIST_WARD_SUMMON_SMALL("poltergeist_ward_summon_small", "poltergeist_ward_summon", Model.WALL, Animation.ASCEND,
            0, 0, 0.95, false, true, false)
    ,
    POLTERGEIST_WARD_SUMMON("poltergeist_ward_summon", Model.WALL, Animation.ASCEND,
            0, 0, 1.3, false, true, false)
    ,
    POLTERGEIST_IMPACT_CRACK("poltergeist_impact_crack", Model.FLAT, noneTo(20),
            0, 0, 2.5, false, true, false)
    ,
    POLTERGEIST_IMPACT_CRACK_LARGE("poltergeist_impact_crack_large", "poltergeist_impact_crack", Model.FLAT, noneTo(20),
            0, 0, 4, false, true, false)
    ,
    POLTERGEIST_RETREAT("poltergeist_retreat", Model.FLAT_VERTICAL_SIDEFACE,  noneTo(15),
            0, 0, 2.25, false, true, false)
    ,
    AQUAFLORA_SLICE("aquaflora_slice", Model.FLAT, noneTo(6),
            6, 1, 1.2, false, false, false)
    ,
    AQUAFLORA_SLICE_INVERTED("aquaflora_slice_inverted", Model.FLAT, noneTo(6),
            6, 1, 1.2, false, false, false)
    ,
    AQUAFLORA_PIERCE_START("aquaflora_pierce_start", Model.FLAT_VERTICAL_SIDEFACE,  noneTo(12),
            6, 2, 1.5, false, false, true)
    ,
    AQUAFLORA_STAB("aquaflora_stab", Model.FLAT_VERTICAL_FRONTFACE,  noneTo(4),
            4, 1, 1.2, false, false, true)
    ,
    AQUAFLORA_FLOWER("aquaflora_flower", Model.FLAT, noneTo(10),
            4, 5, 3.5, false, true, false)
    ,
    AQUAFLORA_FLOWER_BIND("aquaflora_flower_bind", "aquaflora_pierce_start", Model.FLAT, noneTo(12),
            6, 2, 2 /* Special size property */, false, false, false)
    ,
    AQUAFLORA_BASH("aquaflora_bash", Model.FLAT_VERTICAL_SIDEFACE,  noneTo(12),
            6, 2, 1.75, false, true, false)
    ,
    AQUAFLORA_ASSASSINATE("aquaflora_assassinate", "aquaflora_bash", Model.FLAT,  noneTo(12),
            6, 2, 3, false, true, false)
    ,
    AQUAFLORA_DASH("aquaflora_dash", "aquaflora_pierce_start", Model.FLAT, Animation.STRETCH,
            6, 2, 2, false, true, false)
    ,
    RAINFALL_SHOOT("rainfall_shoot", Model.FLAT_VERTICAL_SIDEFACE,  noneTo(16),
            8, 2, 2.25, false, false, true)
    ;

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
    
    public static Animation noneTo(int duration){
        return Animation.create("none" + duration, "animation.cs_effect.none", duration);
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
        SIX_WAY_CROSS("cs_effect_cross"),
        WALL("cs_effect_wall")
        ;

        final String modelString;

        Model(String model) {
            this.modelString = model;
        }

        public String getModelString() {
            return modelString;
        }
    }

    public enum Animation implements IExtensibleEnum {
        SPIN_15("animation.cs_effect.spin", 15),
        SPIN_20("animation.cs_effect.spin", 20),
        ASCEND("animation.cs_effect.ascend", 20),
        SLOW_ROTATION("animation.cs_effect.slow_rotation", Integer.MAX_VALUE),
        SWEEP_RTOL("animation.cs_effect.sweep_rtol", 15),
        SWEEP_LTOR("animation.cs_effect.sweep_ltor", 15),
        STRETCH("animation.cs_effect.stretch", 15),
        GOO("animation.cs_effect.goo", Integer.MAX_VALUE);
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

        public static Animation create(String name, String animationString, int lifespan) {
            throw new IllegalStateException("Enum not extended");
        }
    }

    public static void setSpecialProperties(CSEffect animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.xRotO, animatable.getXRot()) - 165;

        switch (animatable.getEffectType()) {
            case CRESCENTIA_STRIKE, CRESCENTIA_STRIKE_INVERTED, CRESCENTIA_THROW, CRESCENTIA_THROW_INVERTED, BREEZEBREAKER_SLASH, BREEZEBREAKER_SLASH_INVERTED ->
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees(((animatable.getRotationZ() / 360.0F) * 45.0F) - 22.5F));
            case BREEZEBREAKER_WHEEL_IMPACT, AQUAFLORA_PIERCE_START, AQUAFLORA_STAB ->
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(180f + lerpBodyRot));
            case RAINFALL_SHOOT ->
                    poseStack.mulPose(Vector3f.XP.rotationDegrees(180f + lerpBodyRot - 15f));
            default -> {
            }
        }
    }
}
