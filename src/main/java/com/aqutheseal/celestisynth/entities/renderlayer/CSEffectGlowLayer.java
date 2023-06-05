package com.aqutheseal.celestisynth.entities.renderlayer;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CSEffectGlowLayer extends GeoRenderLayer<CSEffect> {
    public CSEffectGlowLayer(GeoRenderer<CSEffect> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, CSEffect animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.eyes(getTexture(animatable));

        poseStack.pushPose();
        float f = 1;
        f = (float) ((double) f * animatable.getEffectType().getScale());
        poseStack.scale(f, f, f);

        if (animatable.getEffectType().isRotateRandomly()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getRotationX()));
            poseStack.mulPose(Axis.ZP.rotationDegrees(animatable.getRotationZ()));
        }
        poseStack.popPose();

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }

    public ResourceLocation getTexture(CSEffect animatable) {
        if (animatable.getEffectType().getFrames() > 0) {
            return new ResourceLocation(Celestisynth.MODID, "textures/entity/" + animatable.getEffectType().getTexture() + "_" + animatable.getFrameLevel() + ".png");
        } else {
            return new ResourceLocation(Celestisynth.MODID, "textures/entity/" + animatable.getEffectType().getTexture() + ".png");
        }
    }
}
