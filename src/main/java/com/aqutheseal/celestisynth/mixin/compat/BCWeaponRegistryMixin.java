package com.aqutheseal.celestisynth.mixin.compat;

import net.bettercombat.logic.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WeaponRegistry.class)
public class BCWeaponRegistryMixin {

    /**
    @Inject(method = "getAttributes(Lnet/minecraft/world/item/ItemStack;)Lnet/bettercombat/api/WeaponAttributes;", at = @At("HEAD"), remap = false, cancellable = true)
    private static void injectCelestisynthAttributes(ItemStack itemStack, CallbackInfoReturnable<WeaponAttributes> cir) {
        for (Pair<Item, WeaponAttributes> weaponAttributesPair : CSCompatBC.specialProperties()) {
            for (Item registryItem : ForgeRegistries.ITEMS.getValues()) {
                if (registryItem == weaponAttributesPair.getFirst()) {
                    cir.setReturnValue(weaponAttributesPair.getSecond());
                }
            }
        }
    }
    **/
}
