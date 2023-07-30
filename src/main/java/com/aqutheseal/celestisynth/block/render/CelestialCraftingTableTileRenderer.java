package com.aqutheseal.celestisynth.block.render;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.block.CelestialCraftingTableTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CelestialCraftingTableTileRenderer extends GeoBlockRenderer<CelestialCraftingTableTileEntity> {
    public static final ResourceLocation GLOW_LAYER = new ResourceLocation(Celestisynth.MODID, "textures/block/celestial_crafting_table_glow.png");

    public CelestialCraftingTableTileRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new CelestialCraftingTableModel());
    }

    @Override
    public RenderType getRenderType(CelestialCraftingTableTileEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void render(GeoModel model, CelestialCraftingTableTileEntity animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        super.render(model, animatable, partialTick, RenderType.eyes(GLOW_LAYER), poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
