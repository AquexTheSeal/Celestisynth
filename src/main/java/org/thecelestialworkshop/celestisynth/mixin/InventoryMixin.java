package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.api.animation.player.AnimationManager;
import org.thecelestialworkshop.celestisynth.api.item.CSWeapon;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    private InventoryMixin() {
        throw new IllegalAccessError("Attempted to instantiate a Mixin Class!");
    }

    @Shadow
    public abstract ItemStack getSelected();

    @Shadow @Final public Player player;

    @Inject(method = "setPickedItem", at = @At("HEAD"), cancellable = true)
    public void celestisynth$setPickedItem(ItemStack stack, CallbackInfo ci) {
        cancelCI(ci);
    }

    @Inject(method = "pickSlot", at = @At("HEAD"), cancellable = true)
    public void celestisynth$pickSlot(int slot, CallbackInfo ci) {
        cancelCI(ci);

        if (!ci.isCancelled()) {
            AnimationManager.playAnimation(player.level(), CSPlayerAnimations.CLEAR.get());
        }
    }

    @Inject(method = "swapPaint", at = @At("HEAD"), cancellable = true)
    public void celestisynth$swapPaint(double slot, CallbackInfo ci) {
        cancelCI(ci);

        if (!ci.isCancelled()) {
            AnimationManager.playAnimation(player.level(), CSPlayerAnimations.CLEAR.get());
        }
    }

    private void cancelCI(CallbackInfo ci) {
        ItemStack selected = getSelected();

        if (selected.getItem() instanceof CSWeapon) {
            CompoundTag controllerTag = selected.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);

            if (controllerTag != null &&  controllerTag.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) ci.cancel();
        }
    }
}