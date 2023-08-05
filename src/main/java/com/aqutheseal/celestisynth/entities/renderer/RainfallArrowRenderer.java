package com.aqutheseal.celestisynth.entities.renderer;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.UtilRainfallArrow;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RainfallArrowRenderer extends ArrowRenderer<UtilRainfallArrow> {
    public RainfallArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(UtilRainfallArrow pEntity) {
        return new ResourceLocation(Celestisynth.MODID, "textures/entity/projectile/rainfall_arrow.png");
    }

    @Override
    public void render(UtilRainfallArrow pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.scale(2F, 2F, 2F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    protected int getBlockLightLevel(UtilRainfallArrow pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(UtilRainfallArrow pEntity, BlockPos pPos) {
        return 15;
    }
}
