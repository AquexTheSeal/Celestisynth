package com.aqutheseal.celestisynth.client.renderers.misc;

import com.aqutheseal.celestisynth.client.models.misc.CSEffectModel;
import com.aqutheseal.celestisynth.client.renderers.entity.projectile.SilencedRotationProjectileRenderer;
import com.aqutheseal.celestisynth.common.entity.base.CSEffect;
import com.aqutheseal.celestisynth.common.entity.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.util.Color;

public class CSEffectRenderer extends SilencedRotationProjectileRenderer<CSEffect> {

    public CSEffectRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CSEffectModel());
    }

    @Override
    public void renderEarly(CSEffect animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()) - 165;

        poseStack.mulPose(Vector3f.YP.rotationDegrees(180f - lerpBodyRot));

        if (animatable.getEffectType().isRotateRandomly() && !animatable.getEffectType().hasSpecialProperties()) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(animatable.getRotationX()));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(animatable.getRotationZ()));
        }

        CSEffectTypes.setSpecialProperties(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }


    @Override
    public float getHeightScale(CSEffect entity) {
        float effectScale = (float) animatable.getEffectType().getScale();

        return super.getHeightScale(entity) * animatable.getCustomizableSize() * effectScale;
    }

    @Override
    public float getWidthScale(CSEffect animatable) {
        float effectScale = (float) animatable.getEffectType().getScale();

        return super.getWidthScale(animatable) * animatable.getCustomizableSize() * effectScale;
    }

    @Override
    public RenderType getRenderType(CSEffect animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    protected int getBlockLightLevel(CSEffect p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(CSEffect p_114509_, BlockPos p_114510_) {
        return 7;
    }

    @Override
    public Color getRenderColor(CSEffect animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        float decreasingAlpha = 1.0f;
        boolean shouldHideAtFirstPerson = false;

        if (animatable.getEffectType().isFadeOut()) {
            int lifespan = animatable.getEffectType().getAnimation().getLifespan();
            decreasingAlpha = 1.0F - ((float) animatable.tickCount / lifespan);
        }

        if (!CSConfigManager.CLIENT.visibilityOnFirstPerson.get()) {
            if (mc.level != null && animatable.getOwnerUuid() != null && mc.player != null && mc.options.getCameraType().isFirstPerson() && animatable.getOwnerUuid() == mc.player.getUUID()) shouldHideAtFirstPerson = true;
        }

        return Color.ofRGBA(1, 1, 1, shouldHideAtFirstPerson ? 0F : decreasingAlpha);
    }
}
