package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.item.weapons.RainfallSerenityItem;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends ProjectileWeaponItem {

    public BowItemMixin(Properties pProperties) {
        super(pProperties);
    }

    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"))
    private float getPowerForTime(int pCharge) {
        if (this == CSItemRegistry.RAINFALL_SERENITY.get()) {
            return RainfallSerenityItem.getPowerForTime(pCharge);
        } else {
            float f = (float)pCharge / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if (f > 1.0F) {
                f = 1.0F;
            }

            return f;
        }
    }
}
