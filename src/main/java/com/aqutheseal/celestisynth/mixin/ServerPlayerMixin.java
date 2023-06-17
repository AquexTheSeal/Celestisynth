package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level p_219727_, BlockPos p_219728_, float p_219729_, GameProfile p_219730_, @Nullable ProfilePublicKey p_219731_) {
        super(p_219727_, p_219728_, p_219729_, p_219730_, p_219731_);
    }

    @Inject(method = "drop(Z)Z", at = @At("HEAD"), cancellable = true)
    public void dropMixin(boolean val, CallbackInfoReturnable<Boolean> cir) {
        Inventory inventory = this.getInventory();
        ItemStack selected = inventory.getSelected();
        if (selected.getItem() instanceof CSWeapon) {
            if (selected.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                cir.setReturnValue(false);
            }
        }
    }
}
