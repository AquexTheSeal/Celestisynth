package com.aqutheseal.celestisynth.common.compat.jei.recipecategory;

import com.aqutheseal.celestisynth.common.compat.jei.CSCompatJEI;
import com.aqutheseal.celestisynth.common.recipe.celestialcrafting.CelestialCraftingRecipe;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CelestialCraftingRecipeCategory implements IRecipeCategory<CelestialCraftingRecipe> {
    public static final int width = 116;
    public static final int height = 54;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public CelestialCraftingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
        this.background = guiHelper.createDrawable(location, 0, 60, width, height);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(CSBlocks.CELESTIAL_CRAFTING_TABLE.get()));
        this.localizedName = Component.translatable(CSBlocks.CELESTIAL_CRAFTING_TABLE.get().getDescriptionId());
    }

    public RecipeType<CelestialCraftingRecipe> getRecipeType() {
        return CSCompatJEI.CELESTIAL_CRAFTING;
    }

    public Component getTitle() {
        return this.localizedName;
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CelestialCraftingRecipe recipe, IFocusGroup focuses) {
        ItemStack resultItem = recipe.getResultItem(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));

        int width = getWidth();
        int height = getHeight();
        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19);

        outputSlot.addIngredients(VanillaTypes.ITEM_STACK, List.of(resultItem));

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                IRecipeSlotBuilder inputSlots = builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1);
                inputSlots.addIngredients(recipe.getIngredients().get(y * 3 + x));
            }
        }
    }
}
