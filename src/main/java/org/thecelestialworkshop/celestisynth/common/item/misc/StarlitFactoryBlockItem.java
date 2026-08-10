package org.thecelestialworkshop.celestisynth.common.item.misc;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class StarlitFactoryBlockItem extends BlockItem implements GeoAnimatable {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public StarlitFactoryBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private StarlitFactoryBlockItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null)
                    this.renderer = new StarlitFactoryBlockItemRenderer();

                return this.renderer;
            }
        });
    }

    public static class StarlitFactoryBlockItemRenderer extends GeoItemRenderer<StarlitFactoryBlockItem> {
        private static final ResourceLocation GLOW_LAYER = Celestisynth.prefix("textures/block/starlit_factory_glow.png");

        public StarlitFactoryBlockItemRenderer() {
            super(new StarlitFactoryBlockItemModel());
        }
        @Override
        public void renderRecursively(PoseStack poseStack, StarlitFactoryBlockItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
            assert Minecraft.getInstance().player != null;
            float modifiedAlpha = 0.6F + Mth.sin(Minecraft.getInstance().player.tickCount * 0.4F) * 0.4F;
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, bufferSource.getBuffer(RenderType.eyes(GLOW_LAYER)), isReRender, partialTick, packedLight, packedOverlay, net.minecraft.util.FastColor.ARGB32.colorFromFloat(1.0F, modifiedAlpha, modifiedAlpha, 1.0F));
        }
    }

    public static class StarlitFactoryBlockItemModel extends GeoModel<StarlitFactoryBlockItem> {
        @Override
        public ResourceLocation getAnimationResource(StarlitFactoryBlockItem animatable) {
            return Celestisynth.prefix("animations/starlit_factory.animation.json");
        }
        @Override
        public ResourceLocation getModelResource(StarlitFactoryBlockItem animatable) {
            return Celestisynth.prefix("geo/starlit_factory.geo.json");
        }
        @Override
        public ResourceLocation getTextureResource(StarlitFactoryBlockItem entity) {
            return Celestisynth.prefix("textures/block/starlit_factory.png");
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }
}
