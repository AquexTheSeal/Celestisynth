package org.thecelestialworkshop.celestisynth.client.renderers.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.thecelestialworkshop.celestisynth.api.item.CSGeoItem;
import org.thecelestialworkshop.celestisynth.client.models.item.CSGeoWeaponModel;
import org.thecelestialworkshop.celestisynth.client.renderers.entity.layer.CSGeoWeaponLayer;
import org.thecelestialworkshop.celestisynth.api.item.SwingParticleContainer;
import org.thecelestialworkshop.celestisynth.common.item.weapons.FrostboundItem;
import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateParticlePacket;
import org.thecelestialworkshop.celestisynth.manager.CSIntegrationManager;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.awt.*;

public class CSGeoWeaponRenderer<T extends Item & CSGeoItem> extends GeoItemRenderer<T> {

    public CSGeoWeaponRenderer() {
        super(new CSGeoWeaponModel<>());
        ((CSGeoWeaponModel<T>) getGeoModel()).renderer = this;
        this.addRenderLayer(new CSGeoWeaponLayer<>(this));
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        int color = 0xFFFFFFFF;

        if (this.getCurrentItemStack().getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getBoolean("shadow")) {
            color = 0xFF000000;
        }

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
    }

    /**
     * Better Combat integration is disabled in the NeoForge 1.21.1 port; swing
     * particles returned by CSGeoItem#getSwingContainer are only used here.
     */
    public static void particleHandler(@NotNull LivingEntity pLivingEntity, ItemStack pItemStack, @NotNull PoseStack pPoseStack) {
    }

    public void particleHandler(T animatable) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (!mc.isPaused()) {
            if (animatable instanceof FrostboundItem) {
                this.model.getBone("particle_em1").ifPresent(emitter -> {
                    Vector3d emitterPos = emitter.getWorldPosition();
                    player.level().addParticle(ParticleTypes.SNOWFLAKE, emitterPos.x(), emitterPos.y(), emitterPos.z(), 0, 0, 0);
                });
            }
        }
    }
}
