package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.entities.tempestboss.TempestBoss;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
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

    @SubscribeEvent
    public static void registerMobAttributes(EntityAttributeCreationEvent event) {
        event.put(CSEntityRegistry.TEMPEST.get(), TempestBoss.createAttributes().build());
    }
}
