package com.aqutheseal.celestisynth.client.models.entity.boss;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.tempestboss.TempestBoss;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TempestBossModel extends AnimatedGeoModel<TempestBoss> {

    @Override
    public ResourceLocation getModelResource(TempestBoss animatable) {
        return Celestisynth.prefix("geo/tempest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TempestBoss animatable) {
        return Celestisynth.prefix("textures/entity/tempestboss/tempest.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TempestBoss animatable) {
        return Celestisynth.prefix("animations/tempest.animation.json");
    }
}
