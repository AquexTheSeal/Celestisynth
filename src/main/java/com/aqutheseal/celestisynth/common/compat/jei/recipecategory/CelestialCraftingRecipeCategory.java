package com.aqutheseal.celestisynth.common.compat.jei.recipecategory;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.recipe.celestialcrafting.CelestialCraftingRecipe;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.common.Constants;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.recipes.ExtendableRecipeCategoryHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class CelestialCraftingRecipeCategory implements IRecipeCategory<CelestialCraftingRecipe> {
    public static final RecipeType<CelestialCraftingRecipe> CELESTIAL_CRAFTING = RecipeType.create(Celestisynth.MODID, "celestial_crafting", CelestialCraftingRecipe.class);
    public static final int width = 116;
    public static final int height = 54;
    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final ICraftingGridHelper craftingGridHelper;
    private final ExtendableRecipeCategoryHelper<Recipe<?>, ICraftingCategoryExtension> extendableHelper = new ExtendableRecipeCategoryHelper<>(CelestialCraftingRecipe.class);

    public CelestialCraftingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
        this.background = guiHelper.createDrawable(location, 0, 60, width, height);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(CSBlocks.CELESTIAL_CRAFTING_TABLE.get()));
        this.localizedName = Component.translatable(CSBlocks.CELESTIAL_CRAFTING_TABLE.get().getDescriptionId());
        this.craftingGridHelper = guiHelper.createCraftingGridHelper();
    }

    public RecipeType<CelestialCraftingRecipe> getRecipeType() {
        return CELESTIAL_CRAFTING;
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

    public void setRecipe(IRecipeLayoutBuilder builder, CelestialCraftingRecipe recipe, IFocusGroup focuses) {
        ICraftingCategoryExtension recipeExtension = this.extendableHelper.getRecipeExtension(recipe);
        recipeExtension.setRecipe(builder, this.craftingGridHelper, focuses);
    }

    public void draw(CelestialCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtension(recipe);
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();
        extension.drawInfo(recipeWidth, recipeHeight, guiGraphics, mouseX, mouseY);
    }

    public List<Component> getTooltipStrings(CelestialCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtension(recipe);
        return extension.getTooltipStrings(mouseX, mouseY);
    }

    public boolean handleInput(CelestialCraftingRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        ICraftingCategoryExtension extension = this.extendableHelper.getRecipeExtension(recipe);
        return extension.handleInput(mouseX, mouseY, input);
    }

    public boolean isHandled(CelestialCraftingRecipe recipe) {
        return this.extendableHelper.getOptionalRecipeExtension(recipe).isPresent();
    }

    public <R extends CelestialCraftingRecipe> void addCategoryExtension(Class<? extends R> recipeClass, Function<R, ? extends ICraftingCategoryExtension> extensionFactory) {
        ErrorUtil.checkNotNull(recipeClass, "recipeClass");
        ErrorUtil.checkNotNull(extensionFactory, "extensionFactory");
        this.extendableHelper.addRecipeExtensionFactory(recipeClass, null, extensionFactory);
    }

    public <R extends CelestialCraftingRecipe> void addCategoryExtension(Class<? extends R> recipeClass, Predicate<R> extensionFilter, Function<R, ? extends ICraftingCategoryExtension> extensionFactory) {
        ErrorUtil.checkNotNull(recipeClass, "recipeClass");
        ErrorUtil.checkNotNull(extensionFilter, "extensionFilter");
        ErrorUtil.checkNotNull(extensionFactory, "extensionFactory");
        this.extendableHelper.addRecipeExtensionFactory(recipeClass, extensionFilter, extensionFactory);
    }

    public ResourceLocation getRegistryName(CelestialCraftingRecipe recipe) {
        ErrorUtil.checkNotNull(recipe, "recipe");
        Optional var10000 = this.extendableHelper.getOptionalRecipeExtension(recipe).flatMap((extension) -> Optional.ofNullable(extension.getRegistryName()));
        Objects.requireNonNull(recipe);
        return (ResourceLocation)var10000.orElseGet(recipe::getId);
    }
}
