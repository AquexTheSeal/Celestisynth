package com.aqutheseal.celestisynth.client;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.animation.CSAnimator;
import com.aqutheseal.celestisynth.common.item.CSItems;
import com.aqutheseal.celestisynth.common.item.SolarisItem;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CelestisynthForgeClientEventsHandler {


    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(event::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void registerAnimationLayer(FMLClientSetupEvent event) {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(CSAnimator::registerPlayerAnimation);
        event.enqueueWork(() -> {
            ItemProperties.register(CSItems.SOLARIS.get(),
                    new ResourceLocation(Celestisynth.MODID, "soul"), (stack, level, living, id) ->
                            living != null && stack.getOrCreateTagElement("csController").getInt(SolarisItem.DIRECTION_INDEX_KEY) == 2 ? 1.0F : 0.0F);
        });
    }
    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    //    event.registerLayerDefinition(BoggordEntityModel.LAYER_LOCATION, BoggordEntityModel::createBodyLayer);
    }

}