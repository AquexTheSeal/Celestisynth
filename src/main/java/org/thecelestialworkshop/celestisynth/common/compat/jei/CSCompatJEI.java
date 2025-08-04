package org.thecelestialworkshop.celestisynth.common.compat.jei;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryMenu;
import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryScreen;
import org.thecelestialworkshop.celestisynth.common.compat.jei.guihandler.StarlitFactoryGuiHandler;
import org.thecelestialworkshop.celestisynth.common.compat.jei.recipecategory.StarlitFactoryRecipeCategory;
import org.thecelestialworkshop.celestisynth.common.recipe.StarlitFactoryRecipe;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import org.thecelestialworkshop.celestisynth.common.registry.CSMenuTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class CSCompatJEI implements IModPlugin {
   public static final RecipeType<StarlitFactoryRecipe> STARLIT_FACTORY = RecipeType.create(Celestisynth.MODID, "starlit_factory", StarlitFactoryRecipe.class);
    private static final ResourceLocation ID = Celestisynth.prefix("plugin");

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new StarlitFactoryRecipeCategory(helper));
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(StarlitFactoryScreen.class, new StarlitFactoryGuiHandler());
    }

    @Override
    public void registerRecipeTransferHandlers(@NotNull IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(StarlitFactoryMenu.class, CSMenuTypes.STARLIT_FACTORY.get(), STARLIT_FACTORY, 0, 7, 8, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CSBlocks.STARLIT_FACTORY.get()), STARLIT_FACTORY);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        List<StarlitFactoryRecipe> starlitFactoryRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CSRecipeTypes.STARLIT_FACTORY_TYPE.get());
        registration.addRecipes(STARLIT_FACTORY, starlitFactoryRecipes);
    }
}
