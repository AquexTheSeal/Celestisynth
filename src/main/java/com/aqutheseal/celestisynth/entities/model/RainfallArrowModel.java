package com.aqutheseal.celestisynth.entities.model;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.UtilRainfallArrow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class RainfallArrowModel extends AnimatedGeoModel<UtilRainfallArrow> {

    @Override
    public ResourceLocation getModelResource(UtilRainfallArrow object) {
        return new ResourceLocation(Celestisynth.MODID, "geo/rainfall_arrow.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(UtilRainfallArrow object) {
        return new ResourceLocation(Celestisynth.MODID, "textures/entity/projectile/rainfall_arrow.png");
    }

    @Override
    public ResourceLocation getAnimationResource(UtilRainfallArrow animatable) {
        return  new ResourceLocation(Celestisynth.MODID, "animations/rainfall_arrow.animation.json");
    }
}
