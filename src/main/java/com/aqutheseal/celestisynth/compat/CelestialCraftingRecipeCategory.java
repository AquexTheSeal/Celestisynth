package com.aqutheseal.celestisynth.compat;

import com.aqutheseal.celestisynth.recipe.CelestialCraftingRecipe;
import com.aqutheseal.celestisynth.registry.CSBlockRegistry;
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CelestialCraftingRecipeCategory implements IRecipeCategory<CelestialCraftingRecipe> {
    public static final int WIDTH = 116;
    public static final int HEIGHT = 54;
    private final @NotNull Component localizedName;
    private final @NotNull IDrawable background;
    private final @NotNull IDrawable icon;

    CelestialCraftingRecipeCategory(@NotNull IGuiHelper guiHelper) {
        this.localizedName = Component.translatable(CSBlockRegistry.CELESTIAL_CRAFTING_TABLE.get().getDescriptionId());
        this.background = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 0, 60, WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CSBlockRegistry.CELESTIAL_CRAFTING_TABLE.get()));
    }

    @NotNull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @NotNull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public @NotNull RecipeType<CelestialCraftingRecipe> getRecipeType() {
        return CSCompatJEI.CELESTIAL_CRAFTING;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CelestialCraftingRecipe recipe, IFocusGroup focuses) {
        ItemStack resultItem = recipe.getResultItem();

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
