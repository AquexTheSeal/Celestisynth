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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TempestBossRenderer extends GeoEntityRenderer<TempestBoss> {
    public static final ResourceLocation GLOW_LAYER = Celestisynth.prefix("textures/entity/tempestboss/tempest_glow.png");

    public TempestBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TempestBossModel());
    }

    @Override
    public void render(GeoModel model, TempestBoss animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        super.render(model, animatable, partialTick, RenderType.eyes(GLOW_LAYER), poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
