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
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TempestBossRenderer extends GeoEntityRenderer<TempestBoss> {
    public static final ResourceLocation GLOW_LAYER = Celestisynth.prefix("textures/entity/tempestboss/tempest_glow.png");

    public TempestBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TempestBossModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, TempestBoss animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, bufferSource.getBuffer(RenderType.eyes(GLOW_LAYER)), isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
