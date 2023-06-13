package com.aqutheseal.celestisynth.entities.renderer;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class NullRenderer<T extends Entity> extends EntityRenderer<T> {
    public NullRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return new ResourceLocation(Celestisynth.MODID, "textures/entity/solaris_air");
    }
}
