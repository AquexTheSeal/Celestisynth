package com.aqutheseal.celestisynth.client.renderers.entity.projectile;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.entity.projectile.RainfallLaserModel;
import com.aqutheseal.celestisynth.common.entity.projectile.RainfallLaserMarker;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

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
//        poseStack.pushPose();
//        poseStack.translate(0, 1, 0);
//        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
//        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
//
//        Vec3 to = new Vec3(entity.getX(), entity.getY(), entity.getZ());
//        Vec3 from = new Vec3(entity.getOrigin().getX(), entity.getOrigin().getY(), entity.getOrigin().getZ());
//        double distance = to.distanceTo(from);
//        for (float i = 0; i < distance; i += 0.1F) {
//            poseStack.translate(0, i, 0);
//            VertexConsumer VertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
//            this.model.renderToBuffer(poseStack, VertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//            VertexConsumer VertexConsumer2 = buffer.getBuffer(RenderType.eyes(this.getTextureLocation(entity)));
//            this.model.renderToBuffer(poseStack, VertexConsumer2, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//        }
//        poseStack.popPose();
        poseStack.pushPose();
        float scale = 0.4f;
        float length = 32 * scale * scale;
        float f = entity.tickCount + partialTicks;
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot() - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getXRot() - 90));
        poseStack.scale(scale, scale, scale);

        Vec3 to = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        Vec3 from = new Vec3(entity.getOrigin().getX(), entity.getOrigin().getY(), entity.getOrigin().getZ());
        double distance = to.distanceTo(from);

        VertexConsumer consumer = buffer.getBuffer(RenderType.eyes(ARROW_TEXTURE));
        for (float i = 0; i < distance * 4; i += length) {
            poseStack.translate(0, length, 0);
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(f * 5));
            this.model.renderToBuffer(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            poseStack.popPose();
        }
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(RainfallLaserMarker entity) {
        return ARROW_TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(RainfallLaserMarker pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(RainfallLaserMarker pEntity, BlockPos pPos) {
        return 15;
    }
}
