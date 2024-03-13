package com.aqutheseal.celestisynth.client.renderers.misc;

import com.aqutheseal.celestisynth.client.models.misc.CSEffectEntityModel;
import com.aqutheseal.celestisynth.client.renderers.entity.projectile.SilencedRotationProjectileRenderer;
import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualSpecialProperties;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
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

public class CSEffectEntityRenderer extends SilencedRotationProjectileRenderer<CSEffectEntity> {

    public CSEffectEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CSEffectEntityModel());
    }

    @Override
    public void preRender(PoseStack poseStack, CSEffectEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()) - 165;
        float ageInTicks = animatable.tickCount + partialTick;
        applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);
        if (animatable.getVisualType().isRotateRandomly() && !animatable.getVisualType().hasSpecialProperties()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getRotationX()));
            poseStack.mulPose(Axis.ZP.rotationDegrees(animatable.getRotationZ()));
        }
        CSVisualSpecialProperties.set(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderFinal(PoseStack poseStack, CSEffectEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        super.renderFinal(poseStack, animatable, model, bufferSource, bufferSource.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(animatable))), partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, CSEffectEntity animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        float f = (float) animatable.getVisualType().getScale();
        super.scaleModelForRender(widthScale * f, heightScale * f, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public RenderType getRenderType(CSEffectEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture, false);
    }

    @Override
    protected int getBlockLightLevel(CSEffectEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(CSEffectEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }

    @Override
    public Color getRenderColor(CSEffectEntity animatable, float partialTick, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        float decreasingAlpha = 1.0f;
        boolean shouldHideAtFirstPerson = false;
        if (animatable.getVisualType().isFadeOut()) {
            int lifespan = animatable.getVisualType().getAnimation().getLifespan();
            decreasingAlpha = 1.0F - ((float) animatable.tickCount / lifespan);
        }
        if (!CSConfigManager.CLIENT.visibilityOnFirstPerson.get()) {
            if (mc.level != null && animatable.getOwnerUuid() != null && mc.player != null) {
                if (mc.options.getCameraType().isFirstPerson() && animatable.getOwnerUuid() == mc.player.getUUID()) {
                    shouldHideAtFirstPerson = true;
                }
            }
        }
        return Color.ofRGBA(1, 1, 1, shouldHideAtFirstPerson ? 0F : decreasingAlpha);
    }
}
