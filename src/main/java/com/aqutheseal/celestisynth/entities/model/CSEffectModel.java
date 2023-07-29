package com.aqutheseal.celestisynth.entities.model;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CSEffectModel extends AnimatedGeoModel<CSEffect> {

    @Override
    public ResourceLocation getModelResource(CSEffect animatable) {
        return new ResourceLocation(Celestisynth.MODID, "geo/" + animatable.getEffectType().getModel().getModelString() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CSEffect animatable) {
        if (animatable.getEffectType().getFrames() > 0) {
            return new ResourceLocation(Celestisynth.MODID, "textures/entity/" + animatable.getEffectType().getTexture() + "/" + animatable.getEffectType().getTexture() + animatable.getFrameLevel() + ".png");
        } else {
            return new ResourceLocation(Celestisynth.MODID, "textures/entity/" + animatable.getEffectType().getTexture() + ".png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(CSEffect animatable) {
        return new ResourceLocation(Celestisynth.MODID, "animations/cs_effect.animation.json");
    }
}
