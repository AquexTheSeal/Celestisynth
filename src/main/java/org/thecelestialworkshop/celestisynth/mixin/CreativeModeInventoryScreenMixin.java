package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.client.renderers.misc.tooltips.CSTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    void mouseScrolled(double pMouseX, double pMouseY, double pDeltaX, double pDeltaY, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance().player != null) {
            CSTooltipRenderer.manageTooltipScrolling(pDeltaY);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance().player != null) {
            CSTooltipRenderer.manageKeyPress(pKeyCode);
        }
    }
}
