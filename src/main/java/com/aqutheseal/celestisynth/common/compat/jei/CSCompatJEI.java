package com.aqutheseal.celestisynth.common.compat.jei;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.gui.celestialcrafting.CelestialCraftingMenu;
import com.aqutheseal.celestisynth.client.gui.celestialcrafting.CelestialCraftingScreen;
import com.aqutheseal.celestisynth.common.compat.jei.recipecategory.CelestialCraftingRecipeCategory;
import com.aqutheseal.celestisynth.common.recipe.celestialcrafting.CelestialCraftingRecipe;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSMenuTypes;
import com.aqutheseal.celestisynth.common.registry.CSRecipeTypes;
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
    public static final mezz.jei.api.recipe.RecipeType<CelestialCraftingRecipe> CELESTIAL_CRAFTING = mezz.jei.api.recipe.RecipeType.create(Celestisynth.MODID, "celestisynth", CelestialCraftingRecipe.class);
    private static final ResourceLocation ID = Celestisynth.prefix("plugin");

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
        registration.addRecipeTransferHandler(CelestialCraftingMenu.class, CSMenuTypes.CELESTIAL_CRAFTING.get(), CELESTIAL_CRAFTING, 1, 9, 10, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CSBlocks.CELESTIAL_CRAFTING_TABLE.get()), CELESTIAL_CRAFTING);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        CategoryRecipeValidator<CelestialCraftingRecipe> validator = new CategoryRecipeValidator<>(new CelestialCraftingRecipeCategory(helper), registration.getIngredientManager(), 9);
        List<CelestialCraftingRecipe> craftingRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CSRecipeTypes.CELESTIAL_CRAFTING_TYPE.get());

        Celestisynth.LOGGER.info("[CelestialCraftingRecipe] Celestial Crafting Recipe Size: " + craftingRecipes.size());

        registration.addRecipes(CELESTIAL_CRAFTING, craftingRecipes);
    }
}
