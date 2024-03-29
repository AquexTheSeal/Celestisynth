package com.aqutheseal.celestisynth.client.renderers.blockentity;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.blockentity.CelestialCraftingTableModel;
import com.aqutheseal.celestisynth.common.block.CelestialCraftingTableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CelestialCraftingTableBlockEntityRenderer extends GeoBlockRenderer<CelestialCraftingTableBlockEntity> {
    private static final ResourceLocation GLOW_LAYER = Celestisynth.prefix("textures/block/celestial_crafting_table_glow.png");

    public CelestialCraftingTableBlockEntityRenderer() {
        super(new CelestialCraftingTableModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, CelestialCraftingTableBlockEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, bufferSource.getBuffer(RenderType.eyes(GLOW_LAYER)), isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
