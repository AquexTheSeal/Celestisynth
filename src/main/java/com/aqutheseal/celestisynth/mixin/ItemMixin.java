package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.common.capabilities.CSItemStackCapabilityProvider;
import com.aqutheseal.celestisynth.util.SkinUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "initializeClient", at = @At("TAIL"), remap = false)
    public void initializeClient(Consumer<IClientItemExtensions> consumer, CallbackInfo ci) {
        if (this instanceof CSGeoItem geoItem) {
            geoItem.initGeo(consumer);
        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected, CallbackInfo ci) {
        pStack.getCapability(CSItemStackCapabilityProvider.CAPABILITY).ifPresent(data -> {
            if (SkinUtil.getAquaSkinWhitelist().contains(pEntity.getUUID())) {
                data.setSkinIndex(1);
            } else {
                data.setSkinIndex(0);
            }
        });
    }
}
