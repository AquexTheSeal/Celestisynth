package org.thecelestialworkshop.celestisynth.client.renderers.entity.mob;

import org.thecelestialworkshop.celestisynth.client.models.entity.mob.TraverserModel;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TraverserRenderer extends GeoEntityRenderer<Traverser> {

    public TraverserRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TraverserModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, Traverser animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
