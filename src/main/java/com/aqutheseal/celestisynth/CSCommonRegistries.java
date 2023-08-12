package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.particles.RainfallBeamParticle;
import com.aqutheseal.celestisynth.particles.RainfallEnergyParticle;
import com.aqutheseal.celestisynth.registry.CSParticleRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CSCommonRegistries {

    public static ParticleRenderType PARTICLE_SHEET_TRANSLUCENT_LIT = new ParticleRenderType() {
        public void begin(BufferBuilder p_107455_, TextureManager p_107456_) {
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            p_107455_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public void end(Tesselator p_107458_) {
            p_107458_.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT_LIT";
        }
    };

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.register(CSParticleRegistry.RAINFALL_BEAM.get(), RainfallBeamParticle.Provider::new);
        event.register(CSParticleRegistry.RAINFALL_BEAM_QUASAR.get(), RainfallBeamParticle.Quasar.Provider::new);
        event.register(CSParticleRegistry.RAINFALL_ENERGY.get(), RainfallEnergyParticle.Provider::new);
        event.register(CSParticleRegistry.RAINFALL_ENERGY_SMALL.get(), RainfallEnergyParticle.Small.Provider::new);
    }
}
