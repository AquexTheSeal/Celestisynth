package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.api.mixin.PlayerMixinSupport;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerMixinSupport {
    private static final String
            SCREENSHAKE_DURATION = "cs.screenShakeDuration",
            SCREENSHAKE_FADEOUTBEGIN = "cs.screenShakeFadeoutStart",
            SCREENSHAKE_INTENSITY = "cs.screenShakeIntensity"
                    ;

    private PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract Inventory getInventory();

    @Inject(method = "attack", at = @At(value = "HEAD"), cancellable = true)
    public void celestisynth$attack(Entity pTarget, CallbackInfo ci) {
        if (cancelCI(getMainHandItem()) || cancelCI(getOffhandItem())) ci.cancel();
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void celestisynth$tick(CallbackInfo ci) {
        if (level.isClientSide()) {
            if (getScreenShakeDuration() > 0) {
                if (getScreenShakeIntensity() > 1.0f) setScreenShakeIntensity(1.0f);

                setScreenShakeDuration(getScreenShakeDuration() - 1);

                if (getScreenShakeDuration() < getScreenShakeFadeoutBegin()) setScreenShakeIntensity(Math.max(0, getScreenShakeIntensity() - 0.001F));
            }
        }

        if (getMainHandItem().getItem() instanceof CSWeapon cs) cs.forceTick(getMainHandItem(), level, this, getInventory().selected, getInventory().getSelected() == getMainHandItem());
        if (getOffhandItem().getItem() instanceof CSWeapon cs) cs.forceTick(getOffhandItem(), level, this, Inventory.SLOT_OFFHAND, getInventory().getSelected() == getOffhandItem());
    }

    @Override
    public int getScreenShakeDuration() {
        return getPersistentData().getInt(SCREENSHAKE_DURATION);
    }

    @Override
    public void setScreenShakeDuration(int duration) {
        getPersistentData().putInt(SCREENSHAKE_DURATION, duration);
    }

    @Override
    public int getScreenShakeFadeoutBegin() {
            return getPersistentData().getInt(SCREENSHAKE_FADEOUTBEGIN);
    }

    @Override
    public void setScreenShakeFadeoutBegin(int beginByValue) {
        getPersistentData().putInt(SCREENSHAKE_FADEOUTBEGIN, beginByValue);
    }

    @Override
    public float getScreenShakeIntensity() {
        return getPersistentData().getFloat(SCREENSHAKE_INTENSITY);
    }

    @Override
    public void setScreenShakeIntensity(float intensity) {
        getPersistentData().putFloat(SCREENSHAKE_INTENSITY, intensity);
    }

    private boolean cancelCI(ItemStack stack) {
        if (stack.getItem() instanceof CSWeapon) {
            CompoundTag controllerTag = stack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);

            if (controllerTag != null) return controllerTag.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY);
        }

        return false;
    }
}
