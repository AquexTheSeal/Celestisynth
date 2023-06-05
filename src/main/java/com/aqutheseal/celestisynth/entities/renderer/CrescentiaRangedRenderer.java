package com.aqutheseal.celestisynth.entities.renderer;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.CrescentiaRanged;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CrescentiaRangedRenderer extends EntityRenderer<CrescentiaRanged> {
    public CrescentiaRangedRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(CrescentiaRanged p_114482_) {
        return new ResourceLocation(Celestisynth.MODID, "textures/entity/solaris_air");
    }
}
