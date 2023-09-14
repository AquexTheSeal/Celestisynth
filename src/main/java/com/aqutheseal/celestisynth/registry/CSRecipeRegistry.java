package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.recipe.CelestialCraftingMenu;
import com.aqutheseal.celestisynth.recipe.CelestialCraftingRecipe;
import com.aqutheseal.celestisynth.recipe.CelestialShapedRecipe;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class CSRecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Celestisynth.MODID);

    public static final RegistryObject<RecipeType<CelestialCraftingRecipe>> CELESTIAL_CRAFTING_TYPE = RECIPE_TYPES.register("celestial_crafting", () -> new RecipeType<CelestialCraftingRecipe>() {
        public @NotNull String toString() {
            return "celestial_crafting";
        }
    });

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Celestisynth.MODID);

    public static final RegistryObject<RecipeSerializer<CelestialShapedRecipe>> SHAPED_CELESTIAL_CRAFTING = RECIPE_SERIALIZERS.register("shaped_celestial_crafting", CelestialShapedRecipe.Serializer::new);

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Celestisynth.MODID);

    public static final RegistryObject<MenuType<CelestialCraftingMenu>> CELESTIAL_CRAFTING = MENU_TYPES.register("celestial_crafting", () -> new MenuType<>(CelestialCraftingMenu::new));

}
