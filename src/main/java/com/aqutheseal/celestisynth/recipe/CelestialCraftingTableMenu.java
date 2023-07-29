package com.aqutheseal.celestisynth.recipe;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Block;

public class CelestialCraftingTableMenu extends CraftingMenu {
    private final Block table;
    private final ContainerLevelAccess pos;

    public CelestialCraftingTableMenu(int id, Inventory playerInv, ContainerLevelAccess worldPos, Block workbench) {
        super(id, playerInv, worldPos);
        this.table = workbench;
        this.pos = worldPos;
    }

    protected static boolean isWithinUsableDistance(ContainerLevelAccess worldPos, Player playerIn, Block targetBlock) {
        return worldPos.evaluate((world, pos) ->
                world.getBlockState(pos).getBlock() == targetBlock && playerIn.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return isWithinUsableDistance(this.pos, playerIn, this.table);
    }
}
