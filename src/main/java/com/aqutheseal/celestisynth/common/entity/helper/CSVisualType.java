package com.aqutheseal.celestisynth.common.entity.helper;

import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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

        List<CSVisualType> z0 = new ArrayList<>();
        z0.add(CSVisualTypes.CRESCENTIA_STRIKE.get());
        z0.add(CSVisualTypes.CRESCENTIA_STRIKE_INVERTED.get());
        z0.add(CSVisualTypes.CRESCENTIA_THROW.get());
        z0.add(CSVisualTypes.CRESCENTIA_THROW_INVERTED.get());
        z0.add(CSVisualTypes.BREEZEBREAKER_SLASH.get());
        z0.add(CSVisualTypes.BREEZEBREAKER_SLASH_INVERTED.get());

        if (z0.contains(animatable.getVisualType())) poseStack.mulPose(Axis.ZP.rotationDegrees(((animatable.getRotationZ() / 360.0F) * 45.0F) - 22.5F));

        List<CSVisualType> x2 = new ArrayList<>();
        x2.add(CSVisualTypes.CRESCENTIA_STRIKE.get());
        x2.add(CSVisualTypes.CRESCENTIA_STRIKE_INVERTED.get());
        x2.add(CSVisualTypes.CRESCENTIA_THROW.get());
        x2.add(CSVisualTypes.CRESCENTIA_THROW_INVERTED.get());
        x2.add(CSVisualTypes.BREEZEBREAKER_SLASH.get());
        x2.add(CSVisualTypes.BREEZEBREAKER_SLASH_INVERTED.get());

        if (x2.contains(animatable.getVisualType())) poseStack.mulPose(Axis.YP.rotationDegrees(-180));

        List<CSVisualType> x0 = new ArrayList<>();
        x0.add(CSVisualTypes.BREEZEBREAKER_WHEEL_IMPACT.get());
        x0.add(CSVisualTypes.AQUAFLORA_PIERCE_START.get());
        x0.add(CSVisualTypes.AQUAFLORA_STAB.get());

        if (x0.contains(animatable.getVisualType()))  poseStack.mulPose(Axis.XP.rotationDegrees(180F + lerpBodyRot));

        List<CSVisualType> x1 = new ArrayList<>();
        x1.add(CSVisualTypes.RAINFALL_SHOOT.get());

        if (x1.contains(animatable.getVisualType())) poseStack.mulPose(Axis.XP.rotationDegrees(180F + lerpBodyRot - 15f));
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
