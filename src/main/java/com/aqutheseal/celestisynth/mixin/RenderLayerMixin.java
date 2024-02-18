package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLayer.class)
public class RenderLayerMixin {
    @Inject(method = "renderColoredCutoutModel", at = @At("TAIL"))
    private static <T extends LivingEntity> void renderColoredCutoutModel(EntityModel<T> pModel, ResourceLocation pTextureLocation, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pEntity, float pRed, float pGreen, float pBlue, CallbackInfo ci) {
        CSEntityCapabilityProvider.get(pEntity).ifPresent(data -> {
            if (data.getFrostbound() > 0) {
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.eyes(pTextureLocation));
                int i = LivingEntityRenderer.getOverlayCoords(pEntity, 0.0F);
                pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, i, 0F, 0.3F, 1.0F, 1.0F);
            }
        });
    }
}
