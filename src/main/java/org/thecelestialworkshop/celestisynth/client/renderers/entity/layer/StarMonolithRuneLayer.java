package org.thecelestialworkshop.celestisynth.client.renderers.entity.layer;

import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class StarMonolithRuneLayer extends GeoRenderLayer<StarMonolith> {
    public StarMonolithRuneLayer(GeoRenderer<StarMonolith> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, StarMonolith animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getRune().runeTexture != null) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(animatable.getRune().runeTexture));
            poseStack.pushPose();
            float alpha = animatable.isDeadOrDying() ? 0F : .5F + (Mth.sin(animatable.tickCount * 0.25F) * .5F);
            this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, net.minecraft.util.FastColor.ARGB32.colorFromFloat(1.0F, alpha, alpha, alpha));
            poseStack.popPose();
        }
    }
}
