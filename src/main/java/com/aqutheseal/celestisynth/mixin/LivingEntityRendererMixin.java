package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    @Shadow protected M model;

    @Shadow protected abstract boolean isBodyVisible(T pLivingEntity);

    @Shadow
    public static int getOverlayCoords(LivingEntity pLivingEntity, float pU) {
        return 0;
    }

    @Shadow protected abstract float getWhiteOverlayProgress(T pLivingEntity, float pPartialTicks);

    // Dummy Constructor
    protected LivingEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Inject(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V", ordinal = 0, shift = At.Shift.AFTER))
    public void celestisynth$render(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        pEntity.getCapability(CSEntityCapabilityProvider.CAPABILITY).ifPresent(data -> {
            if (data.getFrostbound() > 0) {
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.eyes(this.getTextureLocation(pEntity)));
                int i = getOverlayCoords(pEntity, this.getWhiteOverlayProgress(pEntity, pPartialTicks));
                this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, i, 0F, 0.3F, 1.0F, 1.0F);
            }
        });
    }
}
