package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
public abstract class EntityRendererMixin<T extends Entity> {

    private EntityRendererMixin() {
        throw new IllegalAccessError("Attempted to instantiate a Mixin Class!");
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void celestisynth$render(T pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
       if (pEntity instanceof LivingEntity living) {
           CSEntityCapabilityProvider.get(living).ifPresent(data -> {
               if (data.getPhantomTagSource() != null) {
                   renderPhantomFlame(pPoseStack, pBuffer, pEntity);
               }
           });
       }
    }

    private static void renderPhantomFlame(PoseStack pMatrixStack, MultiBufferSource pBuffer, Entity pEntity) {
        TextureAtlasSprite fireSprite0 = ModelBakery.FIRE_0.sprite();
        TextureAtlasSprite fireSprite1 = ModelBakery.FIRE_1.sprite();

        pMatrixStack.pushPose();

        float offsetBBWidth = pEntity.getBbWidth() * 1.4F;

        pMatrixStack.scale(offsetBBWidth, offsetBBWidth, offsetBBWidth);

        float xFireVertex = 0.5F;
        float offsetBBHeight = pEntity.getBbHeight() / offsetBBWidth;
        float yFireVertexOffset = 0.0F;

        pMatrixStack.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().getYRot()));
        pMatrixStack.translate(0.0D, 0.0D, -0.3F + offsetBBHeight * 0.02F);

        float zFireVertex = 0.0F;
        int stackIdx = 0;
        VertexConsumer blockVc = pBuffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));

        for (PoseStack.Pose lastPose = pMatrixStack.last(); offsetBBHeight > 0.0F; stackIdx++) {
            TextureAtlasSprite curFireSprite = stackIdx % 2 == 0 ? fireSprite0 : fireSprite1;
            float curFireSpriteU0 = curFireSprite.getU0();
            float curFireSpriteV0 = curFireSprite.getV0();
            float curFireSpriteU1 = curFireSprite.getU1();
            float curFireSpriteV1 = curFireSprite.getV1();

            if (stackIdx / 2 % 2 == 0) {
                float cachedU1 = curFireSpriteU1;

                curFireSpriteU1 = curFireSpriteU0;
                curFireSpriteU0 = cachedU1;
            }

            fireVertex(lastPose, blockVc, xFireVertex - 0.0F, 0.0F - yFireVertexOffset, zFireVertex, curFireSpriteU1, curFireSpriteV1);
            fireVertex(lastPose, blockVc, -xFireVertex - 0.0F, 0.0F - yFireVertexOffset, zFireVertex, curFireSpriteU0, curFireSpriteV1);
            fireVertex(lastPose, blockVc, -xFireVertex - 0.0F, 1.4F - yFireVertexOffset, zFireVertex, curFireSpriteU0, curFireSpriteV0);
            fireVertex(lastPose, blockVc, xFireVertex - 0.0F, 1.4F - yFireVertexOffset, zFireVertex, curFireSpriteU1, curFireSpriteV0);

            offsetBBHeight -= 0.45F;
            yFireVertexOffset -= 0.45F;
            xFireVertex *= 0.9F;
            zFireVertex += 0.03F;
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
