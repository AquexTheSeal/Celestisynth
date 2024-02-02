package com.aqutheseal.celestisynth.client.gui.celestialcrafting;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class CelestialCraftingScreen extends AbstractContainerScreen<CelestialCraftingMenu> implements RecipeUpdateListener {
    private static final ResourceLocation CRAFTING_TABLE_LOCATION = Celestisynth.prefix("textures/gui/celestial_crafting_table.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = Celestisynth.prefix("textures/gui/celestial_recipe_button.png");
    private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();
    private boolean widthTooNarrow;

    public CelestialCraftingScreen(CelestialCraftingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);

        addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (targetButton) -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);

            targetButton.setPosition(this.leftPos + 5, this.height / 2 - 49);
        }));
        addWidget(this.recipeBookComponent);
        setInitialFocus(this.recipeBookComponent);

        this.titleLabelX = 29;
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        } else {
            this.recipeBookComponent.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.recipeBookComponent.renderGhostRecipe(pGuiGraphics, this.leftPos, this.topPos, true, pPartialTick);
        }

        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        this.recipeBookComponent.renderTooltip(pGuiGraphics, this.leftPos, this.topPos, pMouseX, pMouseY);
    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pX, int pY) {
        pGuiGraphics.blit(CRAFTING_TABLE_LOCATION, super.leftPos, super.topPos, 0, 0, this.imageWidth, this.imageHeight);

        //EXPERIMENTAL FEATURE - Currently only support Minecraft default Latin/English alphabet / font with NO Forced Unicode Mode

        //calculating top left of the label
        final int topLeftX = super.leftPos + super.titleLabelX - 6;
        final int topLeftY = super.topPos + super.titleLabelY - 4;

        //calculating text length
        final int stringWidth = super.font.width(super.title);

        //calculating top right of the label
        final int topRightX = topLeftX + 9 + stringWidth;

        //render text border left
        pGuiGraphics.blit(CRAFTING_TABLE_LOCATION, topLeftX, topLeftY, 176, 0, 3, 14);

        //render text border
        pGuiGraphics.blit(CRAFTING_TABLE_LOCATION, topLeftX + 3, topLeftY, 0, 205, stringWidth + 6, 14);

        //render text border right
        pGuiGraphics.blit(CRAFTING_TABLE_LOCATION, topRightX, topLeftY, 182, 0, 3, 14);
    }

    protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(pX, pY, pWidth, pHeight, pMouseX, pMouseY);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.recipeBookComponent.mouseClicked(pMouseX, pMouseY, pButton)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(pMouseX, pMouseY, pButton);
        }
    }

    protected boolean hasClickedOutside(double pMouseX, double pMouseY, int pGuiLeft, int pGuiTop, int pMouseButton) {
        boolean flag = pMouseX < (double)pGuiLeft || pMouseY < (double)pGuiTop || pMouseX >= (double)(pGuiLeft + this.imageWidth) || pMouseY >= (double)(pGuiTop + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(pMouseX, pMouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, pMouseButton) && flag;
    }

    protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
        super.slotClicked(pSlot, pSlotId, pMouseButton, pType);
        this.recipeBookComponent.slotClicked(pSlot);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
