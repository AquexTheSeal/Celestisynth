package org.thecelestialworkshop.celestisynth.client.renderers.entity.mob;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.client.models.entity.mob.VeilguardModel;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Veilguard;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class VeilguardRenderer extends GeoEntityRenderer<Veilguard> {

    public VeilguardRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VeilguardModel());
        this.addRenderLayer(new VeilguardGlowLayer(this));
    }

    @Override
    public void renderRecursively(PoseStack poseStack, Veilguard animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderFinal(PoseStack poseStack, Veilguard animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (animatable.getAction() == Veilguard.ACTION_MELEE || animatable.getAction() == Veilguard.ACTION_RAGE) {
            this.model.getBone("palmleft").ifPresent(part -> {
                RandomSource rand = animatable.getRandom();
                Vector3d pos = part.getWorldPosition();
                animatable.getCommandSenderWorld().addParticle(CSParticleTypes.RAINFALL_BEAM.get(), pos.x(), pos.y(), pos.z(), rand.nextGaussian() * 0.05, rand.nextGaussian() * 0.05, rand.nextGaussian() * 0.05);
            });
        }

        if (animatable.getAction() == Veilguard.ACTION_SMASH || animatable.getAction() == Veilguard.ACTION_RAGE) {
            this.model.getBone("palmright").ifPresent(part -> {
                RandomSource rand = animatable.getRandom();
                Vector3d pos = part.getWorldPosition();
                animatable.getCommandSenderWorld().addParticle(CSParticleTypes.RAINFALL_BEAM.get(), pos.x(), pos.y(), pos.z(), rand.nextGaussian() * 0.05, rand.nextGaussian() * 0.05, rand.nextGaussian() * 0.05);
            });
        }
    }

    public static class VeilguardGlowLayer extends GeoRenderLayer<Veilguard> {
        public static final ResourceLocation GLOW = Celestisynth.prefix("textures/entity/mob/veilguard_glow.png");

        public VeilguardGlowLayer(GeoRenderer<Veilguard> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, Veilguard animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            RenderType type = RenderType.eyes(GLOW);
            float r = 0.5F + Mth.sin(animatable.tickCount * 0.4F) * 0.5F;
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, type,
                    bufferSource.getBuffer(type), partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                    r, r, r, 1);
        }
    }
}
