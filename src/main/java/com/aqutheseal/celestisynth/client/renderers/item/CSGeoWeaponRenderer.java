package com.aqutheseal.celestisynth.client.renderers.item;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.client.models.item.CSGeoWeaponModel;
import com.aqutheseal.celestisynth.common.item.weapons.FrostboundItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CSGeoWeaponRenderer<T extends Item & CSGeoItem> extends GeoItemRenderer<T> {

    public CSGeoWeaponRenderer() {
        super(new CSGeoWeaponModel<>());
        ((CSGeoWeaponModel<T>) getGeoModel()).renderer = this;
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucentEmissive(texture, false);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        //particleHandler(animatable);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void particleHandler(T animatable) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (!mc.isPaused()) {
            if (animatable instanceof FrostboundItem) {
                this.model.getBone("particle_em1").ifPresent(emitter -> {
                    Vector3d emitterPos = emitter.getWorldPosition();
                    Celestisynth.LOGGER.info("X:" + (int) emitterPos.x + ", Y:" + (int) emitterPos.y + ", Z:" + (int) emitterPos.z);
                    player.level().addParticle(ParticleTypes.SNOWFLAKE, emitterPos.x(), emitterPos.y(), emitterPos.z(), 0, 0, 0);
                });
            }
        }
    }
}
