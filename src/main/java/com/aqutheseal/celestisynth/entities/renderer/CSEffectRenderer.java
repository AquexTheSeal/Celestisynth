package com.aqutheseal.celestisynth.entities.renderer;

import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.entities.model.CSEffectModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Collections;

public class CSEffectRenderer extends GeoProjectilesRenderer<CSEffect> {

    public CSEffectRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CSEffectModel());
        //this.addRenderLayer(new CSEffectGlowLayer(this));
    }

    @Override
    public void renderEarly(CSEffect animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yRotO, animatable.getYRot()) - 165;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180f - lerpBodyRot));

        if (animatable.getEffectType().isRotateRandomly() && !animatable.getEffectType().hasSpecialProperties()) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(animatable.getRotationX()));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(animatable.getRotationZ()));
        }
        CSEffectTypes.setSpecialProperties(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);

        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    // Had to bruteforce this just to prevent the effect from rendering like a projectile, sorry GeckoLib XD
    @Override
    public void render(CSEffect animatable, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        GeoModel model = this.modelProvider.getModel(modelProvider.getModelResource(animatable));
        this.dispatchedMat = poseStack.last().pose().copy();

        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        poseStack.pushPose();
        //poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) - 90));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot())));

        AnimationEvent<CSEffect> predicate = new AnimationEvent<>(animatable, 0, 0, partialTick,
                false, Collections.singletonList(new EntityModelData()));

        modelProvider.setLivingAnimations(animatable, getInstanceId(animatable), predicate); // TODO change to setCustomAnimations in 1.20+
        RenderSystem.setShaderTexture(0, getTextureLocation(animatable));

        Color renderColor = getRenderColor(animatable, partialTick, poseStack, bufferSource, null, packedLight);
        RenderType renderType = getRenderType(animatable, partialTick, poseStack, bufferSource, null, packedLight,
                getTextureLocation(animatable));

        if (!animatable.isInvisibleTo(Minecraft.getInstance().player)) {
            render(model, animatable, partialTick, renderType, poseStack, bufferSource, null, packedLight,
                    getPackedOverlay(animatable, 0), renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                    renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
        }

        poseStack.popPose();
        var renderNameTagEvent = new net.minecraftforge.client.event.RenderNameTagEvent(animatable, animatable.getDisplayName(), this, poseStack, bufferSource, packedLight, partialTick);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
        if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(animatable))) {
            this.renderNameTag(animatable, renderNameTagEvent.getContent(), poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public float getHeightScale(CSEffect entity) {
        float f = (float) animatable.getEffectType().getScale();
        return super.getHeightScale(entity) * f;
    }

    @Override
    public float getWidthScale(CSEffect animatable) {
         float f = (float) animatable.getEffectType().getScale();
        return super.getWidthScale(animatable) * f;
    }

    @Override
    public RenderType getRenderType(CSEffect animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucentEmissive(texture);
    }

    @Override
    protected int getBlockLightLevel(CSEffect p_114496_, BlockPos p_114497_) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(CSEffect p_114509_, BlockPos p_114510_) {
        return 15;
    }

    @Override
    public Color getRenderColor(CSEffect animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        float decreasingAlpha = 1.0f;
        boolean shouldHideAtFirstPerson = false;
        if (animatable.getEffectType().isFadeOut()) {
            int lifespan = animatable.getEffectType().getAnimation().getLifespan();
            decreasingAlpha = 1.0F - ((float) animatable.tickCount / lifespan);
        }
        if (!CSConfig.CLIENT.visibilityOnFirstPerson.get()) {
            if (mc.level != null && animatable.getOwnerUuid() != null && mc.player != null) {
                if (mc.options.getCameraType().isFirstPerson() && animatable.getOwnerUuid() == mc.player.getUUID()) {
                    shouldHideAtFirstPerson = true;
                }
            }
        }
        return Color.ofRGBA(1, 1, 1, shouldHideAtFirstPerson ? 0F : decreasingAlpha);
    }
}
