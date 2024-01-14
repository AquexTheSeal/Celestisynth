package com.aqutheseal.celestisynth.client.models.misc;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CSEffectEntityModel extends AnimatedGeoModel<CSEffectEntity> {

    @Override
    public ResourceLocation getModelResource(CSEffectEntity animatable) {
        return Celestisynth.prefix("geo/" + animatable.getVisualType().getModel().getModelName() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CSEffectEntity animatable) {
        return animatable.getVisualType().getFrames() > 0 ? Celestisynth.prefix("textures/entity/" + animatable.getVisualType().getTexture() + "/" + animatable.getVisualType().getTexture() + animatable.getFrameLevel() + ".png") : Celestisynth.prefix("textures/entity/" + animatable.getVisualType().getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(CSEffectEntity animatable) {
        return Celestisynth.prefix("animations/cs_effect.animation.json");
    }
}
