package org.thecelestialworkshop.celestisynth.client.events;

import org.thecelestialworkshop.celestisynth.api.item.CSWeapon;
import org.thecelestialworkshop.celestisynth.api.mixin.PlayerMixinSupport;
import org.thecelestialworkshop.celestisynth.client.renderers.entity.layer.FrostboundGeoLayer;
import org.thecelestialworkshop.celestisynth.client.renderers.misc.CSGuiRenderer;
import org.thecelestialworkshop.celestisynth.client.renderers.misc.tooltips.CSTooltipRenderer;
import org.thecelestialworkshop.celestisynth.common.attack.aquaflora.AquafloraSlashFrenzyAttack;
import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import org.thecelestialworkshop.celestisynth.common.item.weapons.AquafloraItem;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.bus.api.SubscribeEvent;
import software.bernie.geckolib.event.GeoRenderEvent;

public class CSClientMiscEvents {

    @SubscribeEvent
    public static void onGeoEntityRender(GeoRenderEvent.Entity.CompileRenderLayers event) {
        event.addLayer(new FrostboundGeoLayer<>(event.getRenderer()));
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Opening event) {
        if (Minecraft.getInstance().player != null) {
            ItemStack itemR = Minecraft.getInstance().player.getMainHandItem();
            ItemStack itemL = Minecraft.getInstance().player.getOffhandItem();

            if (itemR.getItem() instanceof CSWeapon) {
                if (org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(itemR, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER) != null && org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(itemR, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                    event.setCanceled(true);
                }
            }
            if (itemL.getItem() instanceof CSWeapon) {
                if (org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(itemL, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER) != null && org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(itemL, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingRender(RenderLivingEvent.Pre<?, ?> event) {
        CSEntityCapabilityProvider.get(event.getEntity()).ifPresent(data -> {
            if (data.getTrueInvisibility() > 0) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {
        CSTooltipRenderer.manageTooltipColors(event);
    }

    @SubscribeEvent
    public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
        new CSGuiRenderer(event).renderGuiAdditionsPre();
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        new CSGuiRenderer(event).renderGuiAdditionsPost();
    }

    // Credits to BobMowzie for camera math
    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        PlayerMixinSupport supportedPlayer = (PlayerMixinSupport) player;

        if (!mc.options.getCameraType().isFirstPerson()) {
            checkAndSetAngle(event, player.getOffhandItem());
            checkAndSetAngle(event, player.getMainHandItem());
        }

        float delta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        float ticksExistedDelta = player.tickCount + delta;
        float intensity = supportedPlayer.getScreenShakeIntensity();
        float duration = supportedPlayer.getScreenShakeDuration();

        if (duration > 0 && !Minecraft.getInstance().isPaused() && mc.level.isClientSide()) {
            event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
            event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
            event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
        }
    }

    @SubscribeEvent
    public static void onCameraZoom(ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.getCameraType().isFirstPerson()) {
            checkAndSetFOV(event, mc.player.getOffhandItem());
            checkAndSetFOV(event, mc.player.getMainHandItem());
        }
    }

    @SubscribeEvent
    public static void onToolTipComponent(RenderTooltipEvent.GatherComponents event) {
        CSTooltipRenderer.manageCelestialTooltips(event);
    }

    private static void checkAndSetAngle(ViewportEvent.ComputeCameraAngles event, ItemStack itemStack) {
        if (itemStack.getItem() instanceof AquafloraItem) {
            CompoundTag tagElement = org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(itemStack, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER);
            if (tagElement != null && tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraSlashFrenzyAttack.ATTACK_ONGOING)) event.setPitch(90);
        }
    }

    private static void checkAndSetFOV(ViewportEvent.ComputeFov event, ItemStack itemStack) {
        CompoundTag tagElement = org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(itemStack, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER);
        if (tagElement != null && itemStack.getItem() instanceof AquafloraItem aq) {
            //if (tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraSlashFrenzyAttack.ATTACK_ONGOING)) event.setFOV(140);
        }
    }
}
