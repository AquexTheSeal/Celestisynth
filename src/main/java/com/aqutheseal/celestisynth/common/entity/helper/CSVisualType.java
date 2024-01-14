package com.aqutheseal.celestisynth.common.entity.helper;

import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public static void setSpecialProperties(CSEffectEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.xRotO, animatable.getXRot()) - 165;

        //TODO Un-hardcode

        List<CSVisualType> rotatableEffectsZ = new ArrayList<>();
        rotatableEffectsZ.add(CSVisualTypes.CRESCENTIA_STRIKE.get());
        rotatableEffectsZ.add(CSVisualTypes.CRESCENTIA_STRIKE_INVERTED.get());
        rotatableEffectsZ.add(CSVisualTypes.CRESCENTIA_THROW.get());
        rotatableEffectsZ.add(CSVisualTypes.CRESCENTIA_THROW_INVERTED.get());
        rotatableEffectsZ.add(CSVisualTypes.BREEZEBREAKER_SLASH.get());
        rotatableEffectsZ.add(CSVisualTypes.BREEZEBREAKER_SLASH_INVERTED.get());

        if (rotatableEffectsZ.contains(animatable.getVisualType())) poseStack.mulPose(Vector3f.ZP.rotationDegrees(((animatable.getRotationZ() / 360.0F) * 45.0F) - 22.5F));

        List<CSVisualType> rotatableEffectsX = new ArrayList<>();
        rotatableEffectsX.add(CSVisualTypes.BREEZEBREAKER_WHEEL_IMPACT.get());
        rotatableEffectsX.add(CSVisualTypes.AQUAFLORA_PIERCE_START.get());
        rotatableEffectsX.add(CSVisualTypes.AQUAFLORA_STAB.get());

        if (rotatableEffectsX.contains(animatable.getVisualType()))  poseStack.mulPose(Vector3f.XP.rotationDegrees(180F + lerpBodyRot));

        List<CSVisualType> men = new ArrayList<>();
        men.add(CSVisualTypes.RAINFALL_SHOOT.get());

        if (men.contains(animatable.getVisualType())) poseStack.mulPose(Vector3f.XP.rotationDegrees(180F + lerpBodyRot - 15f));
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
