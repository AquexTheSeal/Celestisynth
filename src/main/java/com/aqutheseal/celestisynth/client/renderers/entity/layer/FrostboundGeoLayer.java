package com.aqutheseal.celestisynth.client.renderers.entity.layer;

import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FrostboundGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

    public FrostboundGeoLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    protected RenderType getRenderType(T animatable) {
        return RenderType.eyes(getTextureResource(animatable));
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable instanceof LivingEntity living) {
            CSEntityCapabilityProvider.get(living).ifPresent(data -> {
                if (data.getFrostbound() > 0) {
                    getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderType(animatable), bufferSource.getBuffer(getRenderType(animatable)), partialTick, packedLight, packedOverlay, 0F, 0.3F, 1.0F, 1.0F);
                }
            });
        }
    }
}
