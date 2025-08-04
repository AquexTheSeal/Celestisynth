package org.thecelestialworkshop.celestisynth.client.models.entity.projectile;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;

public class SimpleGeoModel<T extends GeoEntity> extends GeoModel<T> {
    public final String modelIdentifier;
    public final String modelExtension;

    public SimpleGeoModel(String modelIdentifier, String modelExtension) {
        this.modelIdentifier = modelIdentifier;
        this.modelExtension = modelExtension;
    }

    public SimpleGeoModel(String modelIdentifier) {
        this(modelIdentifier, "/");
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Celestisynth.prefix("geo/" + modelExtension + modelIdentifier + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return Celestisynth.prefix("textures/entity/"  + modelExtension + modelIdentifier + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return Celestisynth.prefix("animations/" + modelIdentifier + ".animation.json");
    }
}
