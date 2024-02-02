package com.aqutheseal.celestisynth.client.models.item;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.item.weapons.FrostboundItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FrostboundModel extends GeoModel<FrostboundItem> {

    @Override
    public ResourceLocation getModelResource(FrostboundItem animatable) {
        return Celestisynth.prefix("geo/item/frostbound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FrostboundItem animatable) {
        return Celestisynth.prefix("textures/item/frostbound.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FrostboundItem animatable) {
        return Celestisynth.prefix("animations/weapon.animation.json");
    }
}
