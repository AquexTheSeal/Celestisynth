package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.entities.renderer.CSEffectRenderer;
import com.aqutheseal.celestisynth.entities.renderer.CrescentiaRangedRenderer;
import com.aqutheseal.celestisynth.item.SolarisItem;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CSClientRegistries {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CSEntityRegistry.CS_EFFECT.get(), CSEffectRenderer::new);
        event.registerEntityRenderer(CSEntityRegistry.CRESCENTIA_RANGED.get(), CrescentiaRangedRenderer::new);
    }

    @SubscribeEvent
    public static void setupItemPredicates(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(CSItemRegistry.SOLARIS.get(),
                    new ResourceLocation(Celestisynth.MODID, "soul"), (stack, level, living, id) ->
                            living != null && stack.getOrCreateTagElement("csController").getInt(SolarisItem.DIRECTION_INDEX_KEY) == 2 ? 1.0F : 0.0F);
        });
    }
}
