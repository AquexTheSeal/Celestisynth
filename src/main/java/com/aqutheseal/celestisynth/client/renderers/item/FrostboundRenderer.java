package com.aqutheseal.celestisynth.client.renderers.item;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.client.models.item.FrostboundModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FrostboundRenderer<T extends Item & CSGeoItem> extends GeoItemRenderer<T> {

    public FrostboundRenderer() {
        super(new FrostboundModel<>());
    }

    @Override
    public void renderFinal(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        particleHandler();
    }

    public void particleHandler() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (!mc.isPaused()) {
            this.model.getBone("particle_em1").ifPresent(emitter -> {
                Vector3d emitterPos = emitter.getWorldPosition();
                Celestisynth.LOGGER.info("X:" +  (int) emitterPos.x + ", Y:" + (int) emitterPos.y + ", Z:" + (int) emitterPos.z);
                player.level().addParticle(ParticleTypes.SNOWFLAKE, emitterPos.x(), emitterPos.y(), emitterPos.z(), 0, 0, 0);
            });
        }
    }
}
