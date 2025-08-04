package org.thecelestialworkshop.celestisynth.client.renderers.blockentity;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryMenu;
import org.thecelestialworkshop.celestisynth.client.models.blockentity.StarlitFactoryModel;
import org.thecelestialworkshop.celestisynth.common.block.StarlitFactoryBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StarlitFactoryRenderer extends GeoBlockRenderer<StarlitFactoryBlockEntity> {
    private static final ResourceLocation GLOW_LAYER = Celestisynth.prefix("textures/block/starlit_factory_glow.png");

    public StarlitFactoryRenderer() {
        super(new StarlitFactoryModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, StarlitFactoryBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        assert Minecraft.getInstance().player != null;
        float modifiedAlpha = 0.6F + Mth.sin(Minecraft.getInstance().player.tickCount * 0.25F) * 0.4F;
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, bufferSource.getBuffer(RenderType.eyes(GLOW_LAYER)), isReRender, partialTick, packedLight, packedOverlay, modifiedAlpha, modifiedAlpha, modifiedAlpha, alpha);
    }

    @Override
    public void renderFinal(PoseStack poseStack, StarlitFactoryBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        ItemStack stack = animatable.getItem(StarlitFactoryMenu.RESULT_SLOT);
        poseStack.pushPose();
        poseStack.translate(0.5, 1.2, 0.45);
        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.mulPose(Axis.YP.rotationDegrees(Minecraft.getInstance().player.tickCount * 10));
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(stack, Minecraft.getInstance().level, null, 0);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, bakedmodel);
        poseStack.popPose();
    }

    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        switch (facing.getOpposite()) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(0.0F));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            case DOWN -> poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
        }
    }
}
