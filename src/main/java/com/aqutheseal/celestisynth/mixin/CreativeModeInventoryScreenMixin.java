package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.client.renderers.misc.CSTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Inject(method = "mouseScrolled", at = @At("HEAD"))
    void mouseScrolled(double pMouseX, double pMouseY, double pDelta, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance().player != null) {
            CSTooltipRenderer.manageTooltipScrolling(pDelta);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance().player != null) {
            CSTooltipRenderer.manageKeyPress(pKeyCode);
        }
    }
}
