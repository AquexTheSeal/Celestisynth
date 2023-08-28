package com.aqutheseal.celestisynth.entities.model;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.tempestboss.TempestBoss;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TempestBossModel extends AnimatedGeoModel<TempestBoss> {

    @Override
    public ResourceLocation getModelResource(TempestBoss animatable) {
        return new ResourceLocation(Celestisynth.MODID, "geo/tempest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TempestBoss animatable) {
        return new ResourceLocation(Celestisynth.MODID, "textures/entity/tempestboss/tempest.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TempestBoss animatable) {
        return new ResourceLocation(Celestisynth.MODID, "animations/tempest.animation.json");
    }
}
