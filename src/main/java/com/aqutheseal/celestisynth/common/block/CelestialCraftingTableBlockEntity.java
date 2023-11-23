package com.aqutheseal.celestisynth.common.block;

import com.aqutheseal.celestisynth.common.registry.CSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CelestialCraftingTableBlockEntity extends BlockEntity implements IAnimatable {
    private static final AnimationBuilder IDLE_ANIMATION = new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
    private final AnimationFactory manager = GeckoLibUtil.createFactory(this);
    private final AnimationController<CelestialCraftingTableBlockEntity> mainController = new AnimationController<>(this, "maincctbecontroller", 0, this::predicate);


    public CelestialCraftingTableBlockEntity(BlockPos pos, BlockState state) {
        super(CSBlockEntityTypes.CELESTIAL_CRAFTING_TABLE_TILE.get(), pos, state);
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(IDLE_ANIMATION);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(mainController);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.manager;
    }
}
