package com.aqutheseal.celestisynth.client.models.item;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CSGeoWeaponModel<T extends Item & CSGeoItem> extends GeoModel<T> {
    public GeoItemRenderer<T> renderer;

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Celestisynth.prefix("geo/item/" + (renderer != null ? animatable.model(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Celestisynth.prefix("textures/item/" + (renderer != null ? animatable.texture(renderer.getCurrentItemStack()) : animatable.geoIdentifier()) + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return Celestisynth.prefix("animations/weapon.animation.json");
    }
}
