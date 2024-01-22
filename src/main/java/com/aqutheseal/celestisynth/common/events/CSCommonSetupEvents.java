package com.aqutheseal.celestisynth.common.events;

import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.entity.tempestboss.TempestBoss;
import com.aqutheseal.celestisynth.common.registry.CSCapabilities;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.datagen.providers.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

public class CSCommonSetupEvents {
    
    public static class CSForgeSetupEvents {
    }

    public static class CSModSetupEvents {

        @SubscribeEvent
        public static void onRegistryCreatingEvent(NewRegistryEvent event) {
            event.create(new RegistryBuilder<CSVisualType>().setName(CSVisualTypes.VISUALS_KEY.location()).disableSaving());
        }

        @SubscribeEvent
        public static void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
            CSCapabilities.registerCapabilities();

            event.enqueueWork(() -> {
                BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(CSItems.LUNAR_SCRAP.get()), new ItemStack(CSItems.STARSTRUCK_SCRAP.get())));
                BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(Items.FEATHER), new ItemStack(CSItems.STARSTRUCK_FEATHER.get())));
            });
        }

        @SubscribeEvent
        public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
            event.put(CSEntityTypes.TEMPEST.get(), TempestBoss.createAttributes().build());
        }

        @SubscribeEvent
        public static void onGatherDataEvent(final GatherDataEvent event) {
            DataGenerator dataGenerator = event.getGenerator();
            final ExistingFileHelper efh = event.getExistingFileHelper();

            dataGenerator.addProvider(event.includeServer(), new CSBlockModelProvider(dataGenerator, efh));
            dataGenerator.addProvider(event.includeServer(), new CSBlockstateProvider(dataGenerator, efh));
            dataGenerator.addProvider(event.includeServer(), new CSItemModelProvider(dataGenerator, efh));
            dataGenerator.addProvider(event.includeServer(), new CSRecipeProvider(dataGenerator));
            dataGenerator.addProvider(event.includeServer(), new CSAdvancementProvider(dataGenerator, efh));
        }
    }
}
