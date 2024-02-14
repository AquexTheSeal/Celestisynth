package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.api.item.CSWeapon;
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

    private MinecraftMixin() {
        throw new IllegalAccessError("Attempted to instantiate a Mixin Class!");
    }

    @Inject(method = "handleKeybinds", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", shift = At.Shift.BEFORE), cancellable = true)
    private void celestisynth$handleKeybinds(CallbackInfo info) {
        if (player.getInventory().getSelected().getItem() instanceof CSWeapon && this.player.getInventory().getSelected().getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT) != null && this.player.getInventory().getSelected().getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
            info.cancel();
        }
    }
}
