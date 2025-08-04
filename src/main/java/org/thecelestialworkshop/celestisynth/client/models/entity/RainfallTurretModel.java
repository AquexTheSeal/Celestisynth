package org.thecelestialworkshop.celestisynth.client.models.entity;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.RainfallTurret;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RainfallTurretModel extends GeoModel<RainfallTurret> {

    @Override
    public ResourceLocation getModelResource(RainfallTurret animatable) {
        return Celestisynth.prefix("geo/rainfall_turret.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RainfallTurret animatable) {
        return Celestisynth.prefix("textures/entity/rainfall_turret.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RainfallTurret animatable) {
        return Celestisynth.prefix("animations/rainfall_turret.animation.json");
    }

    @Override
    public void setCustomAnimations(RainfallTurret animatable, long instanceId, AnimationState<RainfallTurret> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        CoreGeoBone subhead = getAnimationProcessor().getBone("subhead");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (head != null) {
            head.setRotY((entityData.netHeadYaw() - 90) * Mth.DEG_TO_RAD);
        }
        if (subhead != null) {
            subhead.setRotZ(animatable.getXSyncedRot() * Mth.DEG_TO_RAD);
        }
    }
}
