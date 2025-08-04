package org.thecelestialworkshop.celestisynth.client.models.entity.mob;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StarMonolithModel extends GeoModel<StarMonolith> {
    @Override
    public ResourceLocation getModelResource(StarMonolith animatable) {
        return Celestisynth.prefix("geo/mob/star_monolith.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StarMonolith animatable) {
        if (animatable.getVariant() == StarMonolith.VARIANT_END) {
            return Celestisynth.prefix("textures/entity/mob/star_monolith/star_monolith_end.png");
        }
        if (animatable.getVariant() == StarMonolith.VARIANT_NETHER) {
            return Celestisynth.prefix("textures/entity/mob/star_monolith/star_monolith_nether.png");
        }
        return Celestisynth.prefix("textures/entity/mob/star_monolith/star_monolith_overworld.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StarMonolith animatable) {
        return Celestisynth.prefix("animations/mob/star_monolith.animation.json");
    }
}
