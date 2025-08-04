package org.thecelestialworkshop.celestisynth.client.models.entity.mob;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Veilguard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class VeilguardModel extends GeoModel<Veilguard> {

    @Override
    public ResourceLocation getModelResource(Veilguard animatable) {
        return Celestisynth.prefix("geo/mob/veilguard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Veilguard animatable) {
        return Celestisynth.prefix("textures/entity/mob/veilguard.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Veilguard animatable) {
        return Celestisynth.prefix("animations/mob/veilguard.animation.json");
    }

    @Override
    public void setCustomAnimations(Veilguard animatable, long instanceId, AnimationState<Veilguard> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animatable.hurtTime > 0) {
            this.getBone("body").ifPresent(part -> {
                part.setRotZ(-Mth.cos(animatable.hurtTime * 0.1F) * 0.15F);
            });
        }
    }
}
