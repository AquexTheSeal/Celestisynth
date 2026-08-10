package org.thecelestialworkshop.celestisynth.client.renderers.entity.projectile;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.client.models.entity.projectile.RainfallLaserModel;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.RainfallArrow;
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

public class RainfallLaserRenderer extends EntityRenderer<RainfallArrow> {
    private static final ResourceLocation ARROW_TEXTURE = Celestisynth.prefix("textures/entity/projectile/rainfall_arrow.png");
    private final RainfallLaserModel<RainfallArrow> model;

    public RainfallLaserRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new RainfallLaserModel<>(context.bakeLayer(RainfallLaserModel.LAYER_LOCATION));
    }

    @Override
    public boolean shouldRender(RainfallArrow pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(RainfallArrow entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {

        float extension = 5;

        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() - (90.0F)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot() + (90.0F)));
        poseStack.translate(0, -extension * 4, 0);

        float scale = 0.5f;
        poseStack.pushPose();
        poseStack.scale(scale, scale + extension, scale);
        VertexConsumer consumer = buffer.getBuffer(RenderType.eyes(ARROW_TEXTURE));
        this.model.renderToBuffer(poseStack, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.scale(scale + .25F,  scale + extension, scale + .25F);
        VertexConsumer consumer1 = buffer.getBuffer(RenderType.eyes(ARROW_TEXTURE));
        this.model.renderToBuffer(poseStack, consumer1, light, OverlayTexture.NO_OVERLAY, net.minecraft.util.FastColor.ARGB32.colorFromFloat(1F, entity.isImbueQuasar() ? 0F : 1F, entity.isImbueQuasar() ? 0.3F : 1F, entity.isImbueQuasar() ? 1F : 0F));
        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    protected int getSkyLightLevel(RainfallArrow pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getBlockLightLevel(RainfallArrow pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(RainfallArrow entity) {
        return ARROW_TEXTURE;
    }
}
