package com.aqutheseal.celestisynth.client.renderers.item;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.models.item.FrostboundModel;
import com.aqutheseal.celestisynth.common.item.weapons.FrostboundItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FrostboundRenderer extends GeoItemRenderer<FrostboundItem> {

    public FrostboundRenderer() {
        super(new FrostboundModel());
    }

    @Override
    public void renderRecursively(PoseStack poseStack, FrostboundItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (!mc.isPaused()) {
            if (bone.getName().equals("weapon")) {
                var emitterPos = bone.getLocalPosition();
                Celestisynth.LOGGER.info("X:" +  (int) emitterPos.x + ", Y:" + (int) emitterPos.y + ", Z:" + (int) emitterPos.z);
                player.level().addParticle(ParticleTypes.SNOWFLAKE, emitterPos.x(), emitterPos.y(), emitterPos.z(), 0, 0, 0);
            }
        }
    }

    public void particleHandler() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (!mc.isPaused()) {
            this.model.getBone("weapon").ifPresent(emitter -> {
                Vector3d emitterPos = emitter.getModelPosition();
                Celestisynth.LOGGER.info("X:" +  (int) emitterPos.x + ", Y:" + (int) emitterPos.y + ", Z:" + (int) emitterPos.z);
                player.level().addParticle(ParticleTypes.SNOWFLAKE, emitterPos.x(), emitterPos.y(), emitterPos.z(), 0, 0, 0);
            });
        }
    }
}
