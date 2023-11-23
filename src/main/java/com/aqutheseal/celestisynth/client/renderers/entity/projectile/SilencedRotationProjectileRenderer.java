package com.aqutheseal.celestisynth.client.renderers.entity.projectile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

import java.util.Collections;

//TODO Refactor
public class SilencedRotationProjectileRenderer<T extends Entity & IAnimatable> extends GeoProjectilesRenderer<T> {

    public SilencedRotationProjectileRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

    // Had to bruteforce this just to silence the pre-set rotations until I find a better way, sorry GeckoLib XD
    @Override
    public void render(T animatable, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        GeoModel model = this.modelProvider.getModel(modelProvider.getModelResource(animatable));
        this.dispatchedMat = poseStack.last().pose().copy();

        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        poseStack.pushPose();

        AnimationEvent<T> predicate = new AnimationEvent<>(animatable, 0, 0, partialTick,
                false, Collections.singletonList(new EntityModelData()));

        modelProvider.setCustomAnimations(animatable, getInstanceId(animatable), predicate);
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
}
