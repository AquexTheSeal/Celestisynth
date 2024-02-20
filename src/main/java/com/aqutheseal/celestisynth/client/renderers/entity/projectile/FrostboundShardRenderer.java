package com.aqutheseal.celestisynth.client.renderers.entity.projectile;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.entity.projectile.FrostboundShardModel;
import com.aqutheseal.celestisynth.common.entity.projectile.FrostboundShard;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FrostboundShardRenderer extends EntityRenderer<FrostboundShard> {
    private final FrostboundShardModel<FrostboundShard> model;

    public FrostboundShardRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new FrostboundShardModel<>(pContext.bakeLayer(FrostboundShardModel.LAYER_LOCATION));
    }

    public void render(FrostboundShard entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 1, 0);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot()) + 90.0F));
        VertexConsumer VertexConsumer = bufferIn.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entityIn)));
        this.model.renderToBuffer(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FrostboundShard pEntity) {
        return Celestisynth.prefix("textures/entity/projectile/frostbound_shard.png");
    }
}
