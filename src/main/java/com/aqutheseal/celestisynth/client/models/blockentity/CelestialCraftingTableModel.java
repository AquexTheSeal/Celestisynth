package com.aqutheseal.celestisynth.client.models.blockentity;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.block.CelestialCraftingTableBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CelestialCraftingTableModel extends GeoModel<CelestialCraftingTableBlockEntity> {
    @Override
    public ResourceLocation getAnimationResource(CelestialCraftingTableBlockEntity animatable) {
        return Celestisynth.prefix("animations/celestial_crafting_table.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(CelestialCraftingTableBlockEntity animatable) {
        return Celestisynth.prefix("geo/celestial_crafting_table.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CelestialCraftingTableBlockEntity entity) {
        return Celestisynth.prefix("textures/block/celestial_crafting_table.png");
    }
}
