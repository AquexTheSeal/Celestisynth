package com.aqutheseal.celestisynth.common.entity.renderer;

import com.aqutheseal.celestisynth.common.entity.CSEffect;
import com.aqutheseal.celestisynth.common.entity.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.common.entity.model.CSEffectModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CSEffectRenderer extends GeoEntityRenderer<CSEffect> {

    public CSEffectRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CSEffectModel());
        //this.addRenderLayer(new CSEffectGlowLayer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, CSEffect animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()) - 165;
        float ageInTicks = animatable.tickCount + partialTick;
        applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);
        
        if (animatable.getEffectType().isRotateRandomly() && !animatable.getEffectType().hasSpecialProperties()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getRotationX()));
            poseStack.mulPose(Axis.ZP.rotationDegrees(animatable.getRotationZ()));
        }
        CSEffectTypes.setSpecialProperties(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, CSEffect animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float f = (float) animatable.getEffectType().getScale();
        super.scaleModelForRender(widthScale * f, heightScale * f, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public RenderType getRenderType(CSEffect animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentEmissive(texture);
    }

    @Override
    protected int getBlockLightLevel(CSEffect p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(CSEffect p_114509_, BlockPos p_114510_) {
        return 15;
    }

    @Override
    public Color getRenderColor(CSEffect animatable, float partialTick, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        float decreasingAlpha = 1.0f;
        boolean shouldHideAtFirstPerson = false;
        if (animatable.getEffectType().isFadeOut()) {
            int lifespan = animatable.getEffectType().getAnimation().getLifespan();
            decreasingAlpha = 1.0F - ((float) animatable.tickCount / lifespan);
        }
        //if (!CSConfig.CLIENT.visibilityOnFirstPerson.get()) {
            if (mc.level != null && animatable.getOwnerUuid() != null && mc.player != null) {
                if (mc.options.getCameraType().isFirstPerson() && animatable.getOwnerUuid() == mc.player.getUUID()) {
                    shouldHideAtFirstPerson = true;
                }
            }
        //}
        return Color.ofRGBA(1, 1, 1, shouldHideAtFirstPerson ? 0F : decreasingAlpha);
    }
}
