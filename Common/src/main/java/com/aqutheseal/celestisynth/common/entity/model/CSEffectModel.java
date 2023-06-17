package com.aqutheseal.celestisynth.common.entity.model;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.CSEffect;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CSEffectModel extends GeoModel<CSEffect> {

    @Override
    public ResourceLocation getModelResource(CSEffect animatable) {
        return new ResourceLocation(Celestisynth.MODID, "geo/" + animatable.getEffectType().getModel().getModelString() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CSEffect animatable) {
        if (animatable.getEffectType().getFrames() > 0) {
            return new ResourceLocation(Celestisynth.MODID, "textures/entity/" + animatable.getEffectType().getTexture() + "_" + animatable.getFrameLevel() + ".png");
        } else {
            return new ResourceLocation(Celestisynth.MODID, "textures/entity/" + animatable.getEffectType().getTexture() + ".png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(CSEffect animatable) {
        return new ResourceLocation(Celestisynth.MODID, "animations/cs_effect.animation.json");
    }
}
