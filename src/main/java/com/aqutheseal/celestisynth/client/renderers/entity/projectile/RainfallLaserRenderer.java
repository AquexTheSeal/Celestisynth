package com.aqutheseal.celestisynth.client.renderers.entity.projectile;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.entity.projectile.RainfallLaserModel;
import com.aqutheseal.celestisynth.common.entity.projectile.RainfallLaserMarker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RainfallLaserRenderer extends EntityRenderer<RainfallLaserMarker> {
    private static final ResourceLocation ARROW_TEXTURE = Celestisynth.prefix("textures/entity/projectile/rainfall_arrow.png");
    private final RainfallLaserModel<RainfallLaserMarker> model;

    public RainfallLaserRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RainfallLaserModel<>(context.bakeLayer(RainfallLaserModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(RainfallLaserMarker pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(RainfallLaserMarker entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        float scale = 0.4f;
        float progress = (float) entity.tickCount / 5;
        float size = Mth.clampedLerp(scale, 0, progress);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - (90.0F)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + (90.0F)));
        poseStack.scale(size, scale, size);
        {
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(ARROW_TEXTURE));
            this.model.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 1, 1,  entity.isQuasar() ? 1 : 0, 1);
            VertexConsumer consumer2 = buffer.getBuffer(RenderType.eyes(ARROW_TEXTURE));
            this.model.renderToBuffer(poseStack, consumer2, light, OverlayTexture.NO_OVERLAY, 1, 1,  entity.isQuasar() ? 1 : 0, 0.5F);
        }
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(RainfallLaserMarker entity) {
        return ARROW_TEXTURE;
    }
}
