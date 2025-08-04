package org.thecelestialworkshop.celestisynth.client.renderers.entity.projectile;

import org.thecelestialworkshop.celestisynth.client.models.entity.projectile.SimpleGeoModel;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresRend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KeresRendRenderer extends GeoEntityRenderer<KeresRend> {
    public KeresRendRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleGeoModel<>("keres_rend", "projectile/"));
    }

    @Override
    public RenderType getRenderType(KeresRend animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    protected int getBlockLightLevel(KeresRend pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(KeresRend pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void render(KeresRend entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(2, 2, 2);
        poseStack.translate(0, 1.5, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTick, entity.xRotO, entity.getXRot())));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
