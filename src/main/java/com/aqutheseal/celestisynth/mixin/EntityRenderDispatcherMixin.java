package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.common.entity.base.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
    private static void renderHitbox(PoseStack pPoseStack, VertexConsumer pBuffer, Entity pEntity, float pPartialTicks, CallbackInfo ci) {
        if (pEntity.getType() == CSEntityTypes.CS_EFFECT.get() || pEntity.getType() == CSEntityTypes.RAINFALL_LASER_MARKER.get() || pEntity instanceof EffectControllerEntity) {
            ci.cancel();
        }
    }
}
