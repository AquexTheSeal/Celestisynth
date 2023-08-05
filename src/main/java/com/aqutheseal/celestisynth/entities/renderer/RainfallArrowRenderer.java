package com.aqutheseal.celestisynth.entities.renderer;

import com.aqutheseal.celestisynth.entities.UtilRainfallArrow;
import com.aqutheseal.celestisynth.entities.model.RainfallArrowModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class RainfallArrowRenderer extends GeoProjectilesRenderer<UtilRainfallArrow> {

    public RainfallArrowRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RainfallArrowModel());
    }

    @Override
    public RenderType getRenderType(UtilRainfallArrow animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.eyes(getTextureLocation(animatable));
    }
}
