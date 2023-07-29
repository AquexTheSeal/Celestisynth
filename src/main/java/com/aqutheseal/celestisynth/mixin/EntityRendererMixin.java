package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.LivingMixinSupport;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Inject(method = "render", at = @At("TAIL"))
    public void render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if (pEntity instanceof LivingMixinSupport lms) {
            if (lms.getPhantomTagger() != null) {
                renderPhantomFlame(pPoseStack, pBuffer, pEntity);
            }
        }
    }

    private static void renderPhantomFlame(PoseStack pMatrixStack, MultiBufferSource pBuffer, Entity pEntity) {
        TextureAtlasSprite textureatlassprite = ModelBakery.FIRE_0.sprite();
        TextureAtlasSprite textureatlassprite1 = ModelBakery.FIRE_1.sprite();
        pMatrixStack.pushPose();
        float f = pEntity.getBbWidth() * 1.4F;
        pMatrixStack.scale(f, f, f);
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = pEntity.getBbHeight() / f;
        float f4 = 0.0F;
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getYRot()));
        pMatrixStack.translate(0.0D, 0.0D, (double)(-0.3F + (float)((int)f3) * 0.02F));
        float f5 = 0.0F;
        int i = 0;
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));

        for(PoseStack.Pose posestack$pose = pMatrixStack.last(); f3 > 0.0F; ++i) {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            float f6 = textureatlassprite2.getU0();
            float f7 = textureatlassprite2.getV0();
            float f8 = textureatlassprite2.getU1();
            float f9 = textureatlassprite2.getV1();
            if (i / 2 % 2 == 0) {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, f8, f9);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, f6, f9);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, f6, f7);
            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, f8, f7);
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 += 0.03F;
        }

        pMatrixStack.popPose();
    }

    private static void fireVertex(PoseStack.Pose pMatrixEntry, VertexConsumer pBuffer, float pX, float pY, float pZ, float pTexU, float pTexV) {
        pBuffer.vertex(pMatrixEntry.pose(), pX, pY, pZ)
                .color(0, 20, 0, 70)
                .uv(pTexU, pTexV).overlayCoords(0, 10).uv2(240)
                .normal(pMatrixEntry.normal(), 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}
