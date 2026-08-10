package org.thecelestialworkshop.celestisynth.client.renderers.entity.projectile;

import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresShadow;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class KeresShadowRenderer extends EntityRenderer<KeresShadow> {
    private final ItemRenderer itemRenderer;

    public KeresShadowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    public void render(KeresShadow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.scale(2.5F, 2.5F, 2.5F);
        poseStack.translate(0.0D, 0.33D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 90.0F));
        ItemStack item = new ItemStack(CSItems.KERES.get());
        net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, item, tag -> tag.putBoolean("shadow", true));
        this.itemRenderer.renderStatic(item, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(KeresShadow pEntity) {
        return null;
    }
}
