package com.aqutheseal.celestisynth.client.renderers.entity.projectile;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.entity.projectile.SolarisBombModel;
import com.aqutheseal.celestisynth.common.entity.projectile.SolarisBomb;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SolarisBombRenderer extends EntityRenderer<SolarisBomb> {
    private final SolarisBombModel<SolarisBomb> model;

    public SolarisBombRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new SolarisBombModel<>(pContext.bakeLayer(SolarisBombModel.LAYER_LOCATION));
    }

    public void render(SolarisBomb entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.translate(0, -1 , 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.tickCount * 32));
        VertexConsumer VertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, VertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SolarisBomb pEntity) {
        return Celestisynth.prefix("textures/entity/projectile/solaris_bomb.png");
    }
}
