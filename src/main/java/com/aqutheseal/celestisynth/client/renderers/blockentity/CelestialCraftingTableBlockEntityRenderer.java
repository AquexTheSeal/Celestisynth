package com.aqutheseal.celestisynth.client.renderers.blockentity;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.blockentity.CelestialCraftingTableModel;
import com.aqutheseal.celestisynth.common.block.CelestialCraftingTableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CelestialCraftingTableBlockEntityRenderer extends GeoBlockRenderer<CelestialCraftingTableBlockEntity> {
    private static final ResourceLocation GLOW_LAYER = Celestisynth.prefix("textures/block/celestial_crafting_table_glow.png");

    public CelestialCraftingTableBlockEntityRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new CelestialCraftingTableModel());
    }

    @Override
    public RenderType getRenderType(CelestialCraftingTableBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void render(GeoModel model, CelestialCraftingTableBlockEntity animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        super.render(model, animatable, partialTick, RenderType.eyes(GLOW_LAYER), poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
