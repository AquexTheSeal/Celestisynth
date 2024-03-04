package com.aqutheseal.celestisynth.api.item;

import com.aqutheseal.celestisynth.client.renderers.item.CSGeoWeaponRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public interface CSGeoItem extends GeoItem {

    String geoIdentifier();

    default String model(ItemStack stack) {
        return geoIdentifier();
    }

    default String texture(ItemStack stack) {
        return geoIdentifier();
    }

    GeoAnimatable cacheItem();

    default HumanoidModel.ArmPose getArmPose() {
        return HumanoidModel.ArmPose.ITEM;
    }

    @Override
    default AnimatableInstanceCache getAnimatableInstanceCache() {
        return GeckoLibUtil.createInstanceCache(cacheItem());
    }

    default <T extends Item & CSGeoItem> void initGeo(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CSGeoWeaponRenderer<T> renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new CSGeoWeaponRenderer<>();
                return this.renderer;
            }

            @Nullable @Override public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return CSGeoItem.this.getArmPose();
            }
        });
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("animation.weapon.none")))
        );
    }
}
