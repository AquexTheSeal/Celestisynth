package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "handleKeybinds", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", shift = At.Shift.BEFORE), cancellable = true)
    private void setSelectedSlot(CallbackInfo info) {
        if (this.player.getInventory().getSelected().getItem() instanceof CSWeapon) {
            if (this.player.getInventory().getSelected().getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                info.cancel();
            }
        }
    }
}
