package com.aqutheseal.celestisynth.client.renderers.item;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.client.models.item.CSGeoWeaponModel;
import com.aqutheseal.celestisynth.client.renderers.entity.layer.CSGeoWeaponLayer;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.compat.bettercombat.SwingParticleContainer;
import com.aqutheseal.celestisynth.common.item.weapons.FrostboundItem;
import com.aqutheseal.celestisynth.common.network.util.C2SParticlePacket;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.bettercombat.api.MinecraftClient_BetterCombat;
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
import software.bernie.geckolib.renderer.GeoItemRenderer;

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

    public static void particleHandler(@NotNull LivingEntity pLivingEntity, ItemStack pItemStack, @NotNull PoseStack pPoseStack) {
        if (CSCompatManager.checkBetterCombat() && !Minecraft.getInstance().isPaused()) {
            if (pLivingEntity instanceof Player player && player.getUUID() == Minecraft.getInstance().player.getUUID() && pItemStack.getItem() instanceof CSGeoItem item) {
                if (item.getSwingContainer() != null && Minecraft.getInstance() instanceof MinecraftClient_BetterCombat bcPlayer) {
                    if (bcPlayer.isWeaponSwingInProgress()) {
                        SwingParticleContainer swingContainer = item.getSwingContainer();
                        CameraType cameraType = Minecraft.getInstance().options.getCameraType();
                        Matrix4f localMatrix = new Matrix4f(pPoseStack.last().pose());
                        double xR = cameraType == CameraType.THIRD_PERSON_FRONT ? Math.toRadians(pLivingEntity.getXRot()) : -Math.toRadians(pLivingEntity.getXRot());
                        double yR = cameraType == CameraType.THIRD_PERSON_FRONT ? -Math.toRadians(pLivingEntity.getYRot()) : -Math.toRadians(pLivingEntity.getYRot() - 180);
                        Vector4f relativePosition = new Vector4f().mulProject(localMatrix).rotateX((float) xR).rotateY((float) yR).mul(swingContainer.sizeMult());
                        double x = pLivingEntity.getX() + relativePosition.x();
                        double y = pLivingEntity.getY() + relativePosition.y() + 2 + ((swingContainer.sizeMult() - 1.5) * 0.3333);
                        double z = pLivingEntity.getZ() + relativePosition.z();
                        Vec3 lookAngle;
                        if (cameraType == CameraType.THIRD_PERSON_BACK) {
                            lookAngle = pLivingEntity.getLookAngle().scale(swingContainer.sizeMult() * 4);
                            x -= lookAngle.x();
                            y -= lookAngle.y();
                            z -= lookAngle.z();
                        } else if (cameraType == CameraType.THIRD_PERSON_FRONT) {
                            lookAngle = pLivingEntity.getLookAngle().scale((swingContainer.sizeMult() * 4.3333) - (swingContainer.sizeMult() - 3));
                            x += lookAngle.x();
                            y += lookAngle.y();
                            z += lookAngle.z();
                        }
                        Vector3d positions = new Vector3d(x, y, z);
                        CSNetworkManager.sendToServer(new C2SParticlePacket(swingContainer.particleType(), positions.x(), positions.y(), positions.z(), 0, 0, 0));
                    }
                }
            }
        }
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
