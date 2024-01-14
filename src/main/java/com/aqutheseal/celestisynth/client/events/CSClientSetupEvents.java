package com.aqutheseal.celestisynth.client.events;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.client.gui.celestialcrafting.CelestialCraftingScreen;
import com.aqutheseal.celestisynth.client.particles.BreezebrokenParticle;
import com.aqutheseal.celestisynth.client.particles.RainfallBeamParticle;
import com.aqutheseal.celestisynth.client.particles.RainfallEnergyParticle;
import com.aqutheseal.celestisynth.client.renderers.blockentity.CelestialCraftingTableBlockEntityRenderer;
import com.aqutheseal.celestisynth.client.renderers.entity.boss.TempestBossRenderer;
import com.aqutheseal.celestisynth.client.renderers.entity.projectile.RainfallArrowRenderer;
import com.aqutheseal.celestisynth.client.renderers.misc.CSEffectEntityRenderer;
import com.aqutheseal.celestisynth.client.renderers.misc.NullRenderer;
import com.aqutheseal.celestisynth.common.attack.aquaflora.AquafloraAttack;
import com.aqutheseal.celestisynth.common.attack.poltergeist.PoltergeistCosmicSteelAttack;
import com.aqutheseal.celestisynth.common.attack.solaris.SolarisSoulDashAttack;
import com.aqutheseal.celestisynth.common.item.weapons.RainfallSerenityItem;
import com.aqutheseal.celestisynth.common.registry.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CSClientSetupEvents {

    @SubscribeEvent
    public static void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CSEntityTypes.TEMPEST.get(), TempestBossRenderer::new);

        event.registerEntityRenderer(CSEntityTypes.CS_EFFECT.get(), CSEffectEntityRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.CRESCENTIA_RANGED.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.BREEZEBREAKER_TORNADO.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.POLTERGEIST_WARD.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.RAINFALL_RAIN.get(), NullRenderer::new);

        event.registerEntityRenderer(CSEntityTypes.RAINFALL_ARROW.get(), RainfallArrowRenderer::new);

        event.registerBlockEntityRenderer(CSBlockEntityTypes.CELESTIAL_CRAFTING_TABLE_TILE.get(), CelestialCraftingTableBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(CSItems.SOLARIS.get(),
                    Celestisynth.prefix("soul"), (stack, level, living, id) ->
                            living != null && stack.hasTag() && stack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT) != null && stack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(SolarisSoulDashAttack.STARTED) ? 1.0F : 0.0F);
            ItemProperties.register(CSItems.POLTERGEIST.get(),
                    Celestisynth.prefix("haunted"), (stack, level, living, id) ->
                            living != null && stack.hasTag() && stack.getTagElement(CSWeapon.CS_EXTRAS_ELEMENT) != null && stack.getTagElement(CSWeapon.CS_EXTRAS_ELEMENT).getBoolean(PoltergeistCosmicSteelAttack.IS_IMPACT_LARGE) ? 1.0F : 0.0F);
            ItemProperties.register(CSItems.AQUAFLORA.get(),
                    Celestisynth.prefix("blooming"), (stack, level, living, id) ->
                            living != null && stack.hasTag() && stack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT) != null && stack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(AquafloraAttack.CHECK_PASSIVE) ? 1.0F : 0.0F);

            ItemProperties.register(CSItems.RAINFALL_SERENITY.get(), new ResourceLocation("pull"), (stack, level, living, id) ->
                    living == null || !(stack.getItem() instanceof RainfallSerenityItem) ? 0.0F : living.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - living.getUseItemRemainingTicks()) / ((RainfallSerenityItem)stack.getItem()).getDrawSpeed(stack));
            ItemProperties.register(CSItems.RAINFALL_SERENITY.get(), new ResourceLocation("pulling"), (stack, level, living, id) ->
                    living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F);

            MenuScreens.register(CSMenuTypes.CELESTIAL_CRAFTING.get(), CelestialCraftingScreen::new);
        });
    }

    @SubscribeEvent
    public static void onRegisterParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
        event.register(CSParticleTypes.BREEZEBROKEN.get(), BreezebrokenParticle.Provider::new);
        event.register(CSParticleTypes.RAINFALL_BEAM.get(), RainfallBeamParticle.Provider::new);
        event.register(CSParticleTypes.RAINFALL_BEAM_QUASAR.get(), RainfallBeamParticle.Quasar.Provider::new);
        event.register(CSParticleTypes.RAINFALL_ENERGY.get(), RainfallEnergyParticle.Provider::new);
        event.register(CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), RainfallEnergyParticle.Small.Provider::new);
    }

}
