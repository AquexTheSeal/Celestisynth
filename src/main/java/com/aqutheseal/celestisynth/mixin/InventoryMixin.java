package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Shadow public abstract ItemStack getSelected();

    @Inject(method = "setPickedItem", at = @At("HEAD"), cancellable = true)
    public void setPickedItem(ItemStack stack, CallbackInfo ci) {
        cancelCI(ci);
    }

    @Inject(method = "pickSlot", at = @At("HEAD"), cancellable = true)
    public void pickSlot(int slot, CallbackInfo ci) {
        cancelCI(ci);
    }

    @Inject(method = "swapPaint", at = @At("HEAD"), cancellable = true)
    public void swapPaint(double slot, CallbackInfo ci) {
        cancelCI(ci);
    }

    public void cancelCI(CallbackInfo ci) {
        ItemStack selected = getSelected();
        if (selected.getItem() instanceof CSWeapon) {
            CompoundTag tag = selected.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
            if (tag != null) {
                if (tag.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                    ci.cancel();
                }
            }
        }
    }
}