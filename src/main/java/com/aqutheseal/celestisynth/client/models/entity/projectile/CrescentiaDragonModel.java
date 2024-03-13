package com.aqutheseal.celestisynth.client.models.entity.projectile;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.projectile.CrescentiaDragon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CrescentiaDragonModel extends GeoModel<CrescentiaDragon> {

    @Override
    public ResourceLocation getModelResource(CrescentiaDragon animatable) {
        return Celestisynth.prefix("geo/projectile/crescentia_dragon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CrescentiaDragon animatable) {
        return Celestisynth.prefix("textures/entity/projectile/crescentia_dragon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CrescentiaDragon animatable) {
        return Celestisynth.prefix("animations/crescentia_dragon.animation.json");
    }
}
