package com.aqutheseal.celestisynth.client.models.misc;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.base.CSEffect;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CSEffectModel extends AnimatedGeoModel<CSEffect> {

    @Override
    public ResourceLocation getModelResource(CSEffect animatable) {
        return Celestisynth.prefix("geo/" + animatable.getEffectType().getModel().getModelName() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CSEffect animatable) {
        return animatable.getEffectType().getFrames() > 0 ? Celestisynth.prefix("textures/entity/" + animatable.getEffectType().getTexture() + "/" + animatable.getEffectType().getTexture() + animatable.getFrameLevel() + ".png") : Celestisynth.prefix("textures/entity/" + animatable.getEffectType().getTexture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(CSEffect animatable) {
        return Celestisynth.prefix("animations/cs_effect.animation.json");
    }
}
