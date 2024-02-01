package com.aqutheseal.celestisynth.client.renderers.entity.projectile;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

//TODO Refactor
public class SilencedRotationProjectileRenderer<T extends Entity & GeoAnimatable> extends GeoEntityRenderer<T> {

    public SilencedRotationProjectileRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> modelProvider) {
        super(renderManager, modelProvider);
    }

}
