package org.thecelestialworkshop.celestisynth.mixin;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thecelestialworkshop.celestisynth.api.item.CSGeoItem;

import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "initializeClient", at = @At("TAIL"), remap = false)
    public void initializeClient(Consumer<IClientItemExtensions> consumer, CallbackInfo ci) {
        if (this instanceof CSGeoItem geoItem) {
            geoItem.initGeo(consumer);
        }
    }
}
