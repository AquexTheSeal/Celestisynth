package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.common.registry.CSDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private DamageSource source;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "playHurtSound", at = @At("HEAD"), cancellable = true)
    protected void playHurtSound(DamageSource pSource, CallbackInfo ci) {
        if (pSource.is(CSDamageTypes.RAPID_PLAYER_ATTACK) && !(tickCount % 5 == 0)) {
            ci.cancel();
        }
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V", shift = At.Shift.BEFORE))
    private void capture(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        this.source = source;
    }

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"), index = 0)
    private double modifyApplyKnockbackArgs(double pStrength) {
        if(this.source.is(CSDamageTypes.BASIC_PLAYER_ATTACK_NOKB) || this.source.is(CSDamageTypes.RAPID_PLAYER_ATTACK_NOKB)) {
            return 0;
        }
        return pStrength;
    }
}
