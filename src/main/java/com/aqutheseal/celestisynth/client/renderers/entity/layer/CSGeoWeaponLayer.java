package com.aqutheseal.celestisynth.client.renderers.entity.layer;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CSGeoWeaponLayer<T extends Item & CSGeoItem> extends GeoRenderLayer<T> {
    public final GeoItemRenderer<T> geoRenderer;

    public CSGeoWeaponLayer(GeoItemRenderer<T> entityRendererIn) {
        super(entityRendererIn);
        geoRenderer = entityRendererIn;
    }

    protected RenderType getRenderTypeEyes(T animatable) {
        return RenderType.entityTranslucentEmissive(getTextureResource(animatable));
    }

    protected RenderType getRenderTypeGlint() {
        return RenderType.entityGlint();
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderTypeEyes(animatable), bufferSource.getBuffer(getRenderTypeEyes(animatable)), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 0.5F);
        if (geoRenderer.getCurrentItemStack().isEnchanted()) {
            getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderTypeGlint(), bufferSource.getBuffer(getRenderTypeGlint()), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
