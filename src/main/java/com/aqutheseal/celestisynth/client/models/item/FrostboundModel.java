package com.aqutheseal.celestisynth.client.models.item;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;

public class FrostboundModel<T extends Item & CSGeoItem> extends GeoModel<T> {

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Celestisynth.prefix("geo/item/" + animatable.model() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Celestisynth.prefix("textures/item/" + animatable.texture() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return Celestisynth.prefix("animations/weapon.animation.json");
    }
}
