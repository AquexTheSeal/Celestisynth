package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.client.renderers.misc.tooltips.CSTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    public InventoryScreenMixin(InventoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDeltaX, double pDeltaY) {
        if (Minecraft.getInstance().player != null) {
            CSTooltipRenderer.manageTooltipScrolling(pDeltaY);
        }
        return super.mouseScrolled(pMouseX, pMouseY, pDeltaX, pDeltaY);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (Minecraft.getInstance().player != null) {
            CSTooltipRenderer.manageKeyPress(pKeyCode);
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
