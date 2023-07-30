package com.aqutheseal.celestisynth.block.render;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.block.CelestialCraftingTableTileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CelestialCraftingTableModel extends AnimatedGeoModel<CelestialCraftingTableTileEntity> {
    @Override
    public ResourceLocation getAnimationResource(CelestialCraftingTableTileEntity animatable) {
        return new ResourceLocation(Celestisynth.MODID, "animations/celestial_crafting_table.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(CelestialCraftingTableTileEntity animatable) {
        return new ResourceLocation(Celestisynth.MODID, "geo/celestial_crafting_table.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CelestialCraftingTableTileEntity entity) {
        return new ResourceLocation(Celestisynth.MODID, "textures/block/celestisynth.png");
    }
}
