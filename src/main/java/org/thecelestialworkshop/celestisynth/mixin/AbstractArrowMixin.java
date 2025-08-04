package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.common.entity.projectile.RainfallArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {

    private AbstractArrowMixin(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;length()D"))
    private double onHitModifyDelta(Vec3 instance) {
        return getClass().isAssignableFrom(RainfallArrow.class) ? 1 : instance.length();
    }

    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    protected void onHitEntity(AbstractArrow instance, Vec3 vec3) {
        if (getClass().isAssignableFrom(RainfallArrow.class)) {
            vec3 = instance.getDeltaMovement();
        }
    }
}
