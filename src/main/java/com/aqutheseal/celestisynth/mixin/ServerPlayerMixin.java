package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Inject(method = "drop(Z)Z", at = @At("HEAD"), cancellable = true)
    public void celestisynth$drop(boolean val, CallbackInfoReturnable<Boolean> cir) {
        Inventory inventory = getInventory();
        ItemStack selected = inventory.getSelected();

        if (selected.getItem() instanceof CSWeapon) {
            CompoundTag controllerTag = selected.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);

            if (controllerTag != null && controllerTag.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) cir.setReturnValue(false);
        }
    }
}
