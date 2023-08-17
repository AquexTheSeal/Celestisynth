package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.particles.RainfallBeamParticle;
import com.aqutheseal.celestisynth.particles.RainfallEnergyParticle;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CSCommonRegistries {

    @SubscribeEvent
    public static void registerBrewingRecipes(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(CSItemRegistry.LUNAR_SCRAP.get()), new ItemStack(CSItemRegistry.STARSTRUCK_SCRAP.get()))
            );
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(Items.FEATHER), new ItemStack(CSItemRegistry.STARSTRUCK_FEATHER.get()))
            );
        });
    }
}
