package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Entity.class)
public abstract class EntityMixin extends CapabilityProvider<Entity> implements Nameable, EntityAccess, CommandSource, net.minecraftforge.common.extensions.IForgeEntity {

    protected EntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    public void isInvisible(CallbackInfoReturnable<Boolean> cir) {
        if (celestisynth$thisEntity() instanceof LivingEntity living) {
            AtomicBoolean flag = new AtomicBoolean(false);
            CSEntityCapabilityProvider.get(living).ifPresent(data -> flag.set(data.getTrueInvisibility() > 0));
            if (flag.get()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    public void isInvisibleTo(Player pPlayer, CallbackInfoReturnable<Boolean> cir) {
        if (celestisynth$thisEntity() instanceof LivingEntity living) {
            AtomicBoolean flag = new AtomicBoolean(false);
            CSEntityCapabilityProvider.get(living).ifPresent(data -> flag.set(data.getTrueInvisibility() > 0));
            if (flag.get()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Unique
    public Entity celestisynth$thisEntity() {
        return (Entity) (Object) this;
    }
}
