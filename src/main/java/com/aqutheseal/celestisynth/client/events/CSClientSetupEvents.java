package com.aqutheseal.celestisynth.client.events;

import com.aqutheseal.celestisynth.client.gui.celestialcrafting.CelestialCraftingScreen;
import com.aqutheseal.celestisynth.client.models.entity.projectile.FrostboundShardModel;
import com.aqutheseal.celestisynth.client.models.entity.projectile.RainfallLaserModel;
import com.aqutheseal.celestisynth.client.models.entity.projectile.SolarisBombModel;
import com.aqutheseal.celestisynth.client.particles.*;
import com.aqutheseal.celestisynth.client.renderers.blockentity.CelestialCraftingTableBlockEntityRenderer;
import com.aqutheseal.celestisynth.client.renderers.entity.boss.TempestBossRenderer;
import com.aqutheseal.celestisynth.client.renderers.entity.projectile.CrescentiaDragonRenderer;
import com.aqutheseal.celestisynth.client.renderers.entity.projectile.FrostboundShardRenderer;
import com.aqutheseal.celestisynth.client.renderers.entity.projectile.RainfallLaserRenderer;
import com.aqutheseal.celestisynth.client.renderers.entity.projectile.SolarisBombRenderer;
import com.aqutheseal.celestisynth.client.renderers.misc.CSEffectEntityRenderer;
import com.aqutheseal.celestisynth.client.renderers.misc.NullRenderer;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.compat.spellbooks.ISSCompatItems;
import com.aqutheseal.celestisynth.common.registry.CSBlockEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSMenuTypes;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CSClientSetupEvents {

    @SubscribeEvent
    public static void onRegisterRenderersEvent(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CSEntityTypes.TEMPEST.get(), TempestBossRenderer::new);

        event.registerEntityRenderer(CSEntityTypes.CS_EFFECT.get(), CSEffectEntityRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.CRESCENTIA_RANGED.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.BREEZEBREAKER_TORNADO.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.POLTERGEIST_WARD.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.RAINFALL_RAIN.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.FROSTBOUND_ICE_CAST.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.RAINFALL_LASER_MARKER.get(), RainfallLaserRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.RAINFALL_ARROW.get(), NullRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.FROSTBOUND_SHARD.get(), FrostboundShardRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.SOLARIS_BOMB.get(), SolarisBombRenderer::new);
        event.registerEntityRenderer(CSEntityTypes.CRESCENTIA_DRAGON.get(), CrescentiaDragonRenderer::new);
        event.registerBlockEntityRenderer(CSBlockEntityTypes.CELESTIAL_CRAFTING_TABLE_TILE.get(), context -> new CelestialCraftingTableBlockEntityRenderer());
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitionsEvent(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FrostboundShardModel.LAYER_LOCATION, FrostboundShardModel::createBodyLayer);
        event.registerLayerDefinition(RainfallLaserModel.LAYER_LOCATION, RainfallLaserModel::createBodyLayer);
        event.registerLayerDefinition(SolarisBombModel.LAYER_LOCATION, SolarisBombModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(CSMenuTypes.CELESTIAL_CRAFTING.get(), CelestialCraftingScreen::new);
        });

        if (CSCompatManager.checkIronsSpellbooks()) {
            ISSCompatItems.SPELLBOOKS_ITEMS.getEntries().stream().filter(item -> item.get() instanceof SpellBook).forEach((item) ->
                    CuriosRendererRegistry.register(item.get(), SpellBookCurioRenderer::new)
            );
        }
    }

    @SubscribeEvent
    public static void onRegisterParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CSParticleTypes.BREEZEBROKEN.get(), BreezebrokenParticle.Provider::new);
        event.registerSpriteSet(CSParticleTypes.RAINFALL_BEAM.get(), RainfallBeamParticle.Provider::new);
        event.registerSpriteSet(CSParticleTypes.RAINFALL_BEAM_QUASAR.get(), RainfallBeamParticle.Quasar.Provider::new);
        event.registerSpriteSet(CSParticleTypes.RAINFALL_ENERGY.get(), RainfallEnergyParticle.Provider::new);
        event.registerSpriteSet(CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), RainfallEnergyParticle.Small.Provider::new);
        event.registerSpriteSet(CSParticleTypes.WATER_DROP.get(), WaterDropParticle.Provider::new);
        event.registerSpriteSet(CSParticleTypes.KERES_OMEN.get(), SlowFallParticle.Provider::new);
        event.registerSpriteSet(CSParticleTypes.KERES_ASH.get(), SlowFallParticle.Ash.Provider::new);
        event.registerSpriteSet(CSParticleTypes.SOLARIS_FLAME.get(), SlowFallParticle.Provider::new);
    }

}
