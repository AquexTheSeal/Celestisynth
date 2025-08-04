package org.thecelestialworkshop.celestisynth.client.renderers.entity.projectile;

import org.thecelestialworkshop.celestisynth.client.models.entity.RainfallTurretModel;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.RainfallTurret;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RainfallTurretRenderer extends GeoEntityRenderer<RainfallTurret> {
    public RainfallTurretRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RainfallTurretModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, RainfallTurret animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (animatable.createBowFromData().isEnchanted()) {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, bufferSource.getBuffer(RenderType.entityGlint()), isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }
}
