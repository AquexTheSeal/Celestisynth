package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.common.item.weapons.RainfallSerenityItem;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", shift = At.Shift.AFTER))
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci,
                             @Local Player player, @Local float charge, @Local AbstractArrow existingArrow, @Local ArrowItem arrowitem, @Local(ordinal = 1) ItemStack projectileStack) {

        if (pStack.getItem() instanceof RainfallSerenityItem rs) {
            rs.release(pStack, pLevel, pEntityLiving, pTimeLeft, player, charge, arrowitem, projectileStack, existingArrow);
        }
    }
}
