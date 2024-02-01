package com.aqutheseal.celestisynth.client.renderers.entity.boss;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.entity.boss.TempestBossModel;
import com.aqutheseal.celestisynth.common.entity.tempestboss.TempestBoss;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TempestBossRenderer extends GeoEntityRenderer<TempestBoss> {
    public static final ResourceLocation GLOW_LAYER = Celestisynth.prefix("textures/entity/tempestboss/tempest_glow.png");

    public TempestBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TempestBossModel());
    }

    @Override
    public void renderFinal(PoseStack poseStack, TempestBoss animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        super.renderFinal(poseStack, animatable, model, bufferSource, bufferSource.getBuffer(RenderType.eyes(GLOW_LAYER)), partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
