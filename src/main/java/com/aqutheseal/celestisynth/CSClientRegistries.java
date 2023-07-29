package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.entities.renderer.CSEffectRenderer;
import com.aqutheseal.celestisynth.entities.renderer.NullRenderer;
import com.aqutheseal.celestisynth.item.weapons.AquafloraItem;
import com.aqutheseal.celestisynth.item.weapons.PoltergeistItem;
import com.aqutheseal.celestisynth.item.weapons.SolarisItem;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
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
        event.registerEntityRenderer(CSEntityRegistry.CRESCENTIA_RANGED.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityRegistry.BREEZEBREAKER_TORNADO.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityRegistry.POLTERGEIST_WARD.get(), NullRenderer::new);
    }

    @SubscribeEvent
    public static void setupItemPredicates(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(CSItemRegistry.SOLARIS.get(),
                    new ResourceLocation(Celestisynth.MODID, "soul"), (stack, level, living, id) ->
                            living != null && stack.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getInt(SolarisItem.DIRECTION_INDEX_KEY) == 2 ? 1.0F : 0.0F);
            ItemProperties.register(CSItemRegistry.POLTERGEIST.get(),
                    new ResourceLocation(Celestisynth.MODID, "haunted"), (stack, level, living, id) ->
                            living != null && stack.getOrCreateTagElement(CSWeapon.CS_EXTRAS_ELEMENT).getBoolean(PoltergeistItem.IS_IMPACT_LARGE) ? 1.0F : 0.0F);
            ItemProperties.register(CSItemRegistry.AQUAFLORA.get(),
                    new ResourceLocation(Celestisynth.MODID, "blooming"), (stack, level, living, id) ->
                            living != null && stack.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(AquafloraItem.CHECK_PASSIVE) ? 1.0F : 0.0F);
        });
    }
}
