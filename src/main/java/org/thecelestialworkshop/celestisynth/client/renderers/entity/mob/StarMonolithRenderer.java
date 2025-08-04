package org.thecelestialworkshop.celestisynth.client.renderers.entity.mob;

import org.thecelestialworkshop.celestisynth.client.models.entity.mob.StarMonolithModel;
import org.thecelestialworkshop.celestisynth.client.renderers.entity.layer.StarMonolithRuneLayer;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StarMonolithRenderer extends GeoEntityRenderer<StarMonolith> {

    public StarMonolithRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StarMonolithModel());
        this.addRenderLayer(new StarMonolithRuneLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(StarMonolith animatable) {
        return 0;
    }

    @Override
    public int getPackedOverlay(StarMonolith animatable, float u, float partialTick) {
        return OverlayTexture.NO_OVERLAY;
    }
}
