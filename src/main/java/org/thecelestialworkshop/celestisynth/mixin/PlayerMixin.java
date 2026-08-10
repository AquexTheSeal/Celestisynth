package org.thecelestialworkshop.celestisynth.mixin;

import org.thecelestialworkshop.celestisynth.api.item.CSWeapon;
import org.thecelestialworkshop.celestisynth.api.mixin.PlayerMixinSupport;
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
            SCREENSHAKE_INTENSITY = "cs.screenShakeIntensity";

    private static final String
            PULSE_SCALE = "cs.pulseScale",
            PULSE_TIME_SPEED = "cs.pulseTimeSpeed";

    private static final String
            CHANT_MESSAGE = "cs.chantMessage",
            CHANT_TIME = "cs.chantTime",
            CHANT_COLOR = "cs.chantColor";

    private static final String
            KERES_TIME = "cs.keresTime",
            KERES_IMAGE = "cs.keresImage";


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
        if (level().isClientSide()) {
            if (getScreenShakeDuration() > 0) {
                if (getScreenShakeIntensity() > 1.0f) setScreenShakeIntensity(1.0f);

                setScreenShakeDuration(getScreenShakeDuration() - 1);

                if (getScreenShakeDuration() < getScreenShakeFadeoutBegin()) setScreenShakeIntensity(Math.max(0, getScreenShakeIntensity() - 0.001F));
            }

            if (getPulseScale() > 0) {
                setPulseScale(Math.max(0, getPulseScale() - getPulseTimeSpeed()));
            }

            if (getChantMark() < 20) {
                setChantMark(Math.min(getChantMark() + 1, 20));
            }

            if (getTexturePulseMark() < 20) {
                setTexturePulseMark(Math.min(getTexturePulseMark() + 1, 20));
            }
        }
    }

    @Override public String getChantMessage() {
        return getPersistentData().getString(CHANT_MESSAGE);
    }
    @Override public void setChantMessage(String message) {
        getPersistentData().putString(CHANT_MESSAGE, message);
    }
    @Override public int getChantMark() {
        return getPersistentData().getInt(CHANT_TIME);
    }
    @Override public void setChantMark(int time) {
        getPersistentData().putInt(CHANT_TIME, time);
    }
    @Override public int getChantColor() {
        return getPersistentData().getInt(CHANT_COLOR);
    }
    @Override public void setChantColor(int color) {
        getPersistentData().putInt(CHANT_COLOR, color);
    }

    @Override public int getTexturePulseMark() {
        return getPersistentData().getInt(KERES_TIME);
    }
    @Override public void setTexturePulseMark(int time) {
        getPersistentData().putInt(KERES_TIME, time);
    }
    @Override public String getTexturePulseImage() {
        return getPersistentData().getString(KERES_IMAGE);
    }
    @Override public void setTexturePulseImage(String order) {
        getPersistentData().putString(KERES_IMAGE, order);
    }

    @Override public int getPulseScale() {
        return getPersistentData().getInt(PULSE_SCALE);
    }
    @Override public void setPulseScale(int scale) {
        getPersistentData().putInt(PULSE_SCALE, scale);
    }
    @Override public int getPulseTimeSpeed() {
        return getPersistentData().getInt(PULSE_TIME_SPEED);
    }
    @Override public void setPulseTimeSpeed(int speed) {
        getPersistentData().putInt(PULSE_TIME_SPEED, speed);
    }

    @Override public int getScreenShakeDuration() {
        return getPersistentData().getInt(SCREENSHAKE_DURATION);
    }
    @Override public void setScreenShakeDuration(int duration) {
        getPersistentData().putInt(SCREENSHAKE_DURATION, duration);
    }
    @Override public int getScreenShakeFadeoutBegin() {
            return getPersistentData().getInt(SCREENSHAKE_FADEOUTBEGIN);
    }
    @Override public void setScreenShakeFadeoutBegin(int beginByValue) {
        getPersistentData().putInt(SCREENSHAKE_FADEOUTBEGIN, beginByValue);
    }
    @Override public float getScreenShakeIntensity() {
        return getPersistentData().getFloat(SCREENSHAKE_INTENSITY);
    }
    @Override public void setScreenShakeIntensity(float intensity) {
        getPersistentData().putFloat(SCREENSHAKE_INTENSITY, intensity);
    }

    private boolean cancelCI(ItemStack stack) {
        if (stack.getItem() instanceof CSWeapon) {
            CompoundTag controllerTag = org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getLiveTag(stack, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_CONTROLLER);
            if (controllerTag != null) return controllerTag.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY);
        }

        return false;
    }
}
