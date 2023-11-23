package com.aqutheseal.celestisynth.common.block;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.gui.celestialcrafting.CelestialCraftingMenu;
import com.aqutheseal.celestisynth.common.registry.CSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CelestialCraftingTable extends Block implements EntityBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("gui." + Celestisynth.MODID + ".celestial_crafting");

    public CelestialCraftingTable(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) return InteractionResult.SUCCESS;
        else {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, pState.getMenuProvider(pLevel, pPos), pPos);
            pPlayer.openMenu(pState.getMenuProvider(pLevel, pPos));
            pPlayer.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((containerId, targetPlayerInv, ownerPlayer) -> new CelestialCraftingMenu(containerId, targetPlayerInv, ContainerLevelAccess.create(pLevel, pPos)), CONTAINER_TITLE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(24) == 0) pLevel.playLocalSound((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.3F, false);
        if (pRandom.nextInt(24) == 0) pLevel.playLocalSound((double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, SoundSource.BLOCKS, 1.0F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.3F, false);

        for (int i = 0; i < 2; i++) {
            double rX = -0.2 + pRandom.nextDouble() * 0.4;
            double rY = pRandom.nextDouble() * 0.2;
            double rZ = -0.2 + pRandom.nextDouble() * 0.4;

            pLevel.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pPos.getX() + 0.5D, pPos.getY() + 0.75D, pPos.getZ() + 0.5D, rX, rY, rZ);
            pLevel.addParticle(ParticleTypes.SOUL, pPos.getX() + 0.5D, pPos.getY() + 0.75D, pPos.getZ() + 0.5D, rZ, rY, rX);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return CSBlockEntityTypes.CELESTIAL_CRAFTING_TABLE_TILE.get().create(pPos, pState);
    }
}
