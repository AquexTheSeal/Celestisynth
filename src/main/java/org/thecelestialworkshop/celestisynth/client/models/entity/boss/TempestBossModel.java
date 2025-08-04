package org.thecelestialworkshop.celestisynth.client.models.entity.boss;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.tempestboss_scrapped.TempestBoss;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TempestBossModel extends GeoModel<TempestBoss> {

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
