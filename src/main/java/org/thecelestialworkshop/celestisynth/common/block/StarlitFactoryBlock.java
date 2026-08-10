package org.thecelestialworkshop.celestisynth.common.block;

import org.thecelestialworkshop.celestisynth.common.block.enumproperties.TriPart;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StarlitFactoryBlock extends BaseEntityBlock {
    public static final com.mojang.serialization.MapCodec<StarlitFactoryBlock> CODEC = simpleCodec(StarlitFactoryBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<TriPart> TRI_PART = EnumProperty.create("tri_part", TriPart.class);
    public static final BooleanProperty FORGING = BooleanProperty.create("forging");

    public StarlitFactoryBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRI_PART, TriPart.MIDDLE).setValue(FORGING, false));
    }

    @Override
    protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.box(0, 0, 0, 1, 0.95, 1);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            Direction direction = pState.getValue(FACING);
            BlockEntity blockentity = switch (pState.getValue(TRI_PART)) {
                case LEFT -> pLevel.getBlockEntity(pPos.relative(direction.getCounterClockWise()));
                case MIDDLE -> pLevel.getBlockEntity(pPos);
                case RIGHT -> pLevel.getBlockEntity(pPos.relative(direction.getClockWise()));
            };

            if (blockentity instanceof StarlitFactoryBlockEntity) {
                pPlayer.openMenu((MenuProvider) blockentity);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            Direction direction = pState.getValue(FACING);
            switch (pState.getValue(TRI_PART)) {
                case LEFT:
                    this.removeNeighboringBlock(pLevel, pPlayer, pState, pPos.relative(direction.getCounterClockWise()));
                    this.removeNeighboringBlock(pLevel, pPlayer, pState, pPos.relative(direction.getCounterClockWise(), 2));
                case RIGHT:
                    this.removeNeighboringBlock(pLevel, pPlayer, pState, pPos.relative(direction.getClockWise()));
                    this.removeNeighboringBlock(pLevel, pPlayer, pState, pPos.relative(direction.getClockWise(), 2));
                case MIDDLE:
                    this.removeNeighboringBlock(pLevel, pPlayer, pState, pPos.relative(direction.getClockWise()));
                    this.removeNeighboringBlock(pLevel, pPlayer, pState, pPos.relative(direction.getCounterClockWise()));
            }
        }
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockPos left = blockpos.relative(direction.getCounterClockWise());
        BlockPos right = blockpos.relative(direction.getClockWise());
        if (!(level.getBlockState(left).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(left))) {
            return null;
        }
        if (!(level.getBlockState(right).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(right))) {
            return null;
        }
        return this.defaultBlockState().setValue(FACING, direction);
    }

    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            Direction direction = pState.getValue(FACING);
            pLevel.setBlock(pPos, pState.setValue(TRI_PART, TriPart.MIDDLE), 3);
            BlockPos left = pPos.relative(direction.getClockWise());
            pLevel.setBlock(left, pState.setValue(TRI_PART, TriPart.LEFT), 3);
            BlockPos right = pPos.relative(direction.getCounterClockWise());
            pLevel.setBlock(right, pState.setValue(TRI_PART, TriPart.RIGHT), 3);

            pLevel.blockUpdated(pPos, Blocks.AIR);
            pState.updateNeighbourShapes(pLevel, pPos, 3);
        }
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return blockState.getValue(TRI_PART).equals(TriPart.MIDDLE) ? RenderShape.ENTITYBLOCK_ANIMATED : RenderShape.INVISIBLE;
    }

    public void removeNeighboringBlock(Level pLevel, Player pPlayer, BlockState pState, BlockPos neighborPos) {
        BlockState neighborState = pLevel.getBlockState(neighborPos);
        if (neighborState.is(this) && neighborState.getValue(TRI_PART) != pState.getValue(TRI_PART)) {
            pLevel.setBlock(neighborPos, Blocks.AIR.defaultBlockState(), 3);
            pLevel.levelEvent(pPlayer, 2001, neighborPos, Block.getId(neighborState));
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, CSBlockEntityTypes.STARLIT_FACTORY_TILE.get(), (level, blockPos, blockState, blockEntity) -> blockEntity.serverTick(level, blockPos, blockState));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (pState.getValue(TRI_PART) == TriPart.MIDDLE) {
            return new StarlitFactoryBlockEntity(pPos, pState);
        }
        return null;
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof StarlitFactoryBlockEntity) {
                if (pLevel instanceof ServerLevel) {
                    Containers.dropContents(pLevel, pPos, (StarlitFactoryBlockEntity) blockentity);
                }

                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, TRI_PART, FORGING);
    }

    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(pLevel.getBlockEntity(pPos));
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
}
