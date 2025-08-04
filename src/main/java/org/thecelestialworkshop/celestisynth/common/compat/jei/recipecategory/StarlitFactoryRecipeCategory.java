package org.thecelestialworkshop.celestisynth.common.compat.jei.recipecategory;

import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryScreen;
import org.thecelestialworkshop.celestisynth.common.compat.jei.CSCompatJEI;
import org.thecelestialworkshop.celestisynth.common.compat.jei.drawable.StarlitFactoryDrawable;
import org.thecelestialworkshop.celestisynth.common.recipe.StarlitFactoryRecipe;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.core.object.Color;

public class StarlitFactoryRecipeCategory implements IRecipeCategory<StarlitFactoryRecipe> {
    private final IDrawable background;
    private final IDrawable icon;
    protected final IDrawableStatic staticForge;
    protected final IDrawableAnimated animatedForge;

    public StarlitFactoryRecipeCategory(IGuiHelper guiHelper) {
        this.background = new StarlitFactoryDrawable();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(CSBlocks.STARLIT_FACTORY.get()));
        this.staticForge = guiHelper.createDrawable(StarlitFactoryScreen.FACTORY_GUI, 208, 0, 44, 75);
        this.animatedForge = guiHelper.createAnimatedDrawable(staticForge, 100, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public RecipeType<StarlitFactoryRecipe> getRecipeType() {
        return CSCompatJEI.STARLIT_FACTORY;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.celestisynth.starlit_factory");
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(StarlitFactoryRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        animatedForge.draw(guiGraphics, 62, 3);

        Component experienceString = Component.literal( recipe.getForgeTime() / 20 + "s");
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        float motion = Mth.sin(minecraft.player.tickCount * 0.2F) * 1;
        Color color = Color.ofRGBA(0.75F + (motion * 0.25F), 0.75F + (motion * 0.25F), 1F, 1F);
        fontRenderer.drawInBatch8xOutline(experienceString.getVisualOrderText(), 7, 5, color.argbInt(), color.darker(5F).getColor(), guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, StarlitFactoryRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 17).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 17).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 53).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 108, 17).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 108, 35).addIngredients(recipe.getIngredients().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 108, 53).addIngredients(recipe.getIngredients().get(5));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 34).addItemStack(recipe.getResult());
    }

    public boolean isHandled(StarlitFactoryRecipe recipe) {
        return true;
    }
}
