package com.aqutheseal.celestisynth.compat;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.recipe.CelestialCraftingMenu;
import com.aqutheseal.celestisynth.recipe.CelestialCraftingRecipe;
import com.aqutheseal.celestisynth.recipe.CelestialCraftingScreen;
import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
import com.aqutheseal.celestisynth.registry.CSRecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import mezz.jei.library.plugins.vanilla.RecipeBookGuiHandler;
import mezz.jei.library.plugins.vanilla.crafting.CategoryRecipeValidator;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class CSCompatJEI implements IModPlugin {
    public static final mezz.jei.api.recipe.RecipeType<CelestialCraftingRecipe> CELESTIAL_CRAFTING = mezz.jei.api.recipe.RecipeType.create(Celestisynth.MODID, "hunter_weapon", CelestialCraftingRecipe.class);
    private static final ResourceLocation ID = new ResourceLocation(Celestisynth.MODID, "plugin");

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }


    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CelestialCraftingRecipeCategory(helper));
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(CelestialCraftingScreen.class, new RecipeBookGuiHandler<>());
    }

    @Override
    public void registerRecipeTransferHandlers(@NotNull IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(CelestialCraftingMenu.class, CSRecipeRegistry.CELESTIAL_CRAFTING.get(), CELESTIAL_CRAFTING, 1, 9, 10, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CSBlockRegistry.CELESTIAL_CRAFTING_TABLE.get()), CELESTIAL_CRAFTING);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        CategoryRecipeValidator<CelestialCraftingRecipe> validator = new CategoryRecipeValidator<>(new CelestialCraftingRecipeCategory(helper), registration.getIngredientManager(), 9);
        List<CelestialCraftingRecipe> craftingRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CSRecipeRegistry.CELESTIAL_CRAFTING_TYPE.get());
        Celestisynth.LOGGER.info("[CelestialCraftingRecipe] Celestial Crafting Recipe Size: " + craftingRecipes.size());
        registration.addRecipes(CELESTIAL_CRAFTING, craftingRecipes);
    }
}
