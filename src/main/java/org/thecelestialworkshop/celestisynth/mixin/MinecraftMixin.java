package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.api.item.CSWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.jetbrains.annotations.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;

    private MinecraftMixin() {
        throw new IllegalAccessError("Attempted to instantiate a Mixin Class!");
    }

    @Inject(method = "handleKeybinds", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Inventory;selected:I", shift = At.Shift.BEFORE), cancellable = true)
    private void celestisynth$handleKeybinds(CallbackInfo info) {
        if (player == null) return;
        var controllerTag = org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(player.getInventory().getSelected(), org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER);
        if (player.getInventory().getSelected().getItem() instanceof CSWeapon && controllerTag != null && controllerTag.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
            info.cancel();
        }
    }
}
