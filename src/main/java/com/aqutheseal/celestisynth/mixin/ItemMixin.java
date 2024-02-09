package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "initializeClient", at = @At("TAIL"), remap = false)
    public void initGeoRenderer(Consumer<IClientItemExtensions> consumer, CallbackInfo ci) {
        if (this instanceof CSGeoItem geoItem) {
            geoItem.initGeo(consumer);
        }
    }
}
