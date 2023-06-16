package com.aqutheseal.celestisynth.client;

import com.aqutheseal.celestisynth.common.entity.CSEntities;
import com.aqutheseal.celestisynth.common.entity.renderer.CSEffectRenderer;
import com.aqutheseal.celestisynth.common.entity.renderer.NullRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityRenderers {

    public static <T extends Entity> void register(RegisterStrategy registerStrategy) {
        registerStrategy.register(CSEntities.CS_EFFECT.get(), CSEffectRenderer::new);
        registerStrategy.register(CSEntities.CRESCENTIA_RANGED.get(), NullRenderer::new);
        registerStrategy.register(CSEntities.BREEZEBREAKER_TORNADO.get(), NullRenderer::new);
    }

    @FunctionalInterface
    public interface RegisterStrategy {
        <T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);
    }
}
