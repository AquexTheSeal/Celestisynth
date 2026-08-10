package org.thecelestialworkshop.celestisynth.client.renderers.entity.projectile;

import org.thecelestialworkshop.celestisynth.client.models.entity.projectile.SimpleGeoModel;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresSlash;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KeresSlashRenderer extends GeoEntityRenderer<KeresSlash> {
    public KeresSlashRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleGeoModel<>("keres_slash", "projectile/"));
    }

    @Override
    public RenderType getRenderType(KeresSlash animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    protected int getBlockLightLevel(KeresSlash pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(KeresSlash pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void renderFinal(PoseStack poseStack, KeresSlash animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void render(KeresSlash entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 1.25, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 180));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getRoll()));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
