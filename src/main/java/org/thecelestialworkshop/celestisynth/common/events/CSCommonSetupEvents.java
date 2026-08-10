package org.thecelestialworkshop.celestisynth.common.events;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.RainfallTurret;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Veilguard;
import org.thecelestialworkshop.celestisynth.common.entity.tempestboss_scrapped.TempestBoss;
import org.thecelestialworkshop.celestisynth.common.registry.*;
import org.thecelestialworkshop.celestisynth.datagen.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CSCommonSetupEvents {

    public static class CSForgeSetupEvents {

        @SubscribeEvent
        public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
            event.getBuilder().addRecipe(new BrewingRecipe(Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(CSItems.LUNAR_SCRAP.get()), new ItemStack(CSItems.STARSTRUCK_SCRAP.get())));
            event.getBuilder().addRecipe(new BrewingRecipe(Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(Items.FEATHER), new ItemStack(CSItems.STARSTRUCK_FEATHER.get())));
        }
    }

    public static class CSModSetupEvents {

        @SubscribeEvent
        public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
            CSAttributes.modifyEntityAttributes(event);
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onRegistryCreatingEvent(NewRegistryEvent event) {
            event.register(CSVisualTypes.REGISTRY);
            event.register(CSPlayerAnimations.REGISTRY);
        }

        @SubscribeEvent
        public static void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
        }

        @SubscribeEvent
        public static void onSpawnPlacementRegisterEvent(RegisterSpawnPlacementsEvent event) {
            event.register(CSEntityTypes.STAR_MONOLITH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StarMonolith::canSpawn, RegisterSpawnPlacementsEvent.Operation.OR);
        }

        @SubscribeEvent
        public static void onEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
            event.put(CSEntityTypes.RAINFALL_TURRET.get(), RainfallTurret.createAttributes().build());
            event.put(CSEntityTypes.STAR_MONOLITH.get(), StarMonolith.createAttributes().build());
            event.put(CSEntityTypes.TRAVERSER.get(), Traverser.createAttributes().build());
            event.put(CSEntityTypes.VEILGUARD.get(), Veilguard.createAttributes().build());
            event.put(CSEntityTypes.TEMPEST.get(), TempestBoss.createAttributes().build());
        }

        @SubscribeEvent
        public static void onGatherDataEvent(final GatherDataEvent event) {
            DataGenerator dataGenerator = event.getGenerator();
            final ExistingFileHelper efh = event.getExistingFileHelper();
            final PackOutput output = event.getGenerator().getPackOutput();
            final CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

            dataGenerator.addProvider(event.includeServer(), new CSLootTableProvider(output, lookup));
            dataGenerator.addProvider(event.includeServer(), new CSBlockstateProvider(output, efh));
            dataGenerator.addProvider(event.includeServer(), new CSItemModelProvider(output, efh));
            dataGenerator.addProvider(event.includeServer(), new CSRecipeProvider(output, lookup));
            dataGenerator.addProvider(event.includeServer(), new CSAdvancementProvider(output, lookup, efh));

            CSTagsProvider.BlockHandler blockTagProvider = new CSTagsProvider.BlockHandler(output, lookup, efh);
            dataGenerator.addProvider(event.includeServer(), blockTagProvider);
            dataGenerator.addProvider(event.includeServer(), new CSTagsProvider.ItemHandler(output, lookup, blockTagProvider.contentsGetter(), efh));
            dataGenerator.addProvider(event.includeServer(), new CSTagsProvider.EntityTypeHandler(output, lookup, efh));
            dataGenerator.addProvider(event.includeServer(), new CSTagsProvider.BiomeHandler(output, lookup, efh));
            dataGenerator.addProvider(event.includeServer(), new CSTagsProvider.StructureHandler(output, lookup, efh));
            dataGenerator.addProvider(event.includeServer(), new CSGlobalLootModifiersProvider(output, lookup));

            otherProviders(output, lookup, efh).forEach(provider -> dataGenerator.addProvider(event.includeServer(), provider));
        }

        public static List<DataProvider> otherProviders(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper efh) {
            RegistrySetBuilder builder = new RegistrySetBuilder()
                    .add(Registries.DAMAGE_TYPE, CSDamageTypeProvider::bootstrap)
                    .add(Registries.ENCHANTMENT, CSEnchantmentProvider::bootstrap)
                    .add(Registries.CONFIGURED_FEATURE, CSFeatureProvider.ConfiguredFeatures::bootstrap)
                    .add(Registries.PLACED_FEATURE, CSFeatureProvider.PlacedFeatures::bootstrap)
                    .add(Registries.STRUCTURE, CSStructureProvider.Structures::bootstrap)
                    .add(Registries.STRUCTURE_SET, CSStructureProvider.StructureSets::bootstrap)
                    .add(NeoForgeRegistries.Keys.STRUCTURE_MODIFIERS, CSMobSpawnProvider.StructureModifiers::bootstrap)
                    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ctx -> {
                        CSFeatureProvider.BiomeModifiers.bootstrap(ctx);
                        CSMobSpawnProvider.BiomeModifiers.bootstrap(ctx);
                    })
                    ;
            return List.of(
                    new DatapackBuiltinEntriesProvider(output, lookup, builder, Set.of(Celestisynth.MODID)),
                    new CSTagsProvider.DamageTypeHandler(output, net.minecraft.data.registries.RegistryPatchGenerator.createLookup(lookup, builder).thenApply(patched -> patched.full()), efh)
            );
        }
    }
}
