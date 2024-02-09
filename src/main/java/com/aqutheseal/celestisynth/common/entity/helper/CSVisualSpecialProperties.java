package com.aqutheseal.celestisynth.common.entity.helper;

import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class CSVisualSpecialProperties {
    public static void set(CSEffectEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.xRotO, animatable.getXRot()) - 165;

        //TODO Un-hardcode

        List<CSVisualType> z0 = new ArrayList<>();

        z0.add(CSVisualTypes.CRESCENTIA_STRIKE.get());
        z0.add(CSVisualTypes.CRESCENTIA_STRIKE_INVERTED.get());
        z0.add(CSVisualTypes.CRESCENTIA_THROW.get());
        z0.add(CSVisualTypes.CRESCENTIA_THROW_INVERTED.get());
        z0.add(CSVisualTypes.BREEZEBREAKER_SLASH.get());
        z0.add(CSVisualTypes.BREEZEBREAKER_SLASH_INVERTED.get());
        z0.add(CSVisualTypes.FROSTBOUND_SLASH.get());
        z0.add(CSVisualTypes.FROSTBOUND_SLASH_INVERTED.get());

        if (z0.contains(animatable.getVisualType())) poseStack.mulPose(Axis.ZP.rotationDegrees(((animatable.getRotationZ() / 360.0F) * 45.0F) - 22.5F));

        List<CSVisualType> x2 = new ArrayList<>();
        x2.add(CSVisualTypes.CRESCENTIA_STRIKE.get());
        x2.add(CSVisualTypes.CRESCENTIA_STRIKE_INVERTED.get());
        x2.add(CSVisualTypes.CRESCENTIA_THROW.get());
        x2.add(CSVisualTypes.CRESCENTIA_THROW_INVERTED.get());
        x2.add(CSVisualTypes.BREEZEBREAKER_SLASH.get());
        x2.add(CSVisualTypes.BREEZEBREAKER_SLASH_INVERTED.get());
        x2.add(CSVisualTypes.AQUAFLORA_STAB.get());
        x2.add(CSVisualTypes.AQUAFLORA_SLICE.get());
        x2.add(CSVisualTypes.AQUAFLORA_SLICE_INVERTED.get());
        x2.add(CSVisualTypes.FROSTBOUND_SLASH.get());
        x2.add(CSVisualTypes.FROSTBOUND_SLASH_INVERTED.get());

        if (x2.contains(animatable.getVisualType())) poseStack.mulPose(Axis.YP.rotationDegrees(-180));

        List<CSVisualType> x0 = new ArrayList<>();
        x0.add(CSVisualTypes.BREEZEBREAKER_WHEEL_IMPACT.get());
        x0.add(CSVisualTypes.AQUAFLORA_PIERCE_START.get());

        if (x0.contains(animatable.getVisualType()))  poseStack.mulPose(Axis.XP.rotationDegrees(180F + lerpBodyRot));

        List<CSVisualType> x3 = new ArrayList<>();
        x3.add(CSVisualTypes.AQUAFLORA_STAB.get());

        if (x3.contains(animatable.getVisualType()))  poseStack.mulPose(Axis.XP.rotationDegrees(180F - lerpBodyRot));

        List<CSVisualType> x1 = new ArrayList<>();
        x1.add(CSVisualTypes.RAINFALL_SHOOT.get());

        if (x1.contains(animatable.getVisualType())) poseStack.mulPose(Axis.XP.rotationDegrees(180F + lerpBodyRot - 15f));
    }
}
