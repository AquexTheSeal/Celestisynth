package com.aqutheseal.celestisynth.client.gui.celestialcrafting;

import com.aqutheseal.celestisynth.common.events.CSRecipeBookSetupEvents;
import com.aqutheseal.celestisynth.common.recipe.celestialcrafting.CelestialCraftingRecipe;
import com.aqutheseal.celestisynth.common.registry.CSBlocks;
import com.aqutheseal.celestisynth.common.registry.CSMenuTypes;
import com.aqutheseal.celestisynth.common.registry.CSRecipeTypes;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CelestialCraftingMenu extends RecipeBookMenu<CraftingContainer> {
    private final ContainerLevelAccess access;
    private final Player player;
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();

    public CelestialCraftingMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }


    public CelestialCraftingMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(CSMenuTypes.CELESTIAL_CRAFTING.get(), pContainerId);
        this.access = pAccess;
        this.player = pPlayerInventory.player;

        addSlot(new ResultSlot(pPlayerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35) {
            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                if (pPlayer.level().isClientSide()) addCraftedEffect(pPlayer);

                super.onTake(pPlayer, pStack);
            }
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) addSlot(new Slot(this.craftSlots, j + i * 3, 30 + j * 18, 17 + i * 18));
        }

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) addSlot(new Slot(pPlayerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
        }

        for (int l = 0; l < 9; ++l) addSlot(new Slot(pPlayerInventory, l, 8 + l * 18, 142));
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu pMenu, Level pLevel, Player pPlayer, CraftingContainer pContainer, ResultContainer pResult) {
        if (!pLevel.isClientSide) {
            ServerPlayer craftingServerPlayer = (ServerPlayer) pPlayer;
            ItemStack resultStack = ItemStack.EMPTY;
            Optional<CelestialCraftingRecipe> potentialCelestialRecipe = pLevel.getServer().getRecipeManager().getRecipeFor(CSRecipeTypes.CELESTIAL_CRAFTING_TYPE.get(), pContainer, pLevel);

            if (potentialCelestialRecipe.isPresent()) {
                CelestialCraftingRecipe curRecipe = potentialCelestialRecipe.get();
                if (pResult.setRecipeUsed(pLevel, craftingServerPlayer, curRecipe)) resultStack = curRecipe.assemble(pContainer, pLevel.registryAccess());
            }

            pResult.setItem(0, resultStack);
            pMenu.setRemoteSlot(0, resultStack);
            craftingServerPlayer.connection.send(new ClientboundContainerSetSlotPacket(pMenu.containerId, pMenu.incrementStateId(), 0, resultStack));
        }
    }

    public void addCraftedEffect(Player player) {
        player.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 0.1F);
    }

    @Override
    public void slotsChanged(Container pInventory) {
        this.access.execute((curLevel, targetPos) -> slotChangedCraftingGrid(this, curLevel, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents pItemHelper) {
        this.craftSlots.fillStackedContents(pItemHelper);
    }

    @Override
    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> pRecipe) {
        return pRecipe.matches(this.craftSlots, this.player.level());
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);

        this.access.execute((curLevel, targetPos) -> clearContainer(pPlayer, this.craftSlots));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, CSBlocks.CELESTIAL_CRAFTING_TABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack targetStack = ItemStack.EMPTY;
        Slot targetSlot = this.slots.get(pIndex);

        if (targetSlot != null && targetSlot.hasItem()) {
            ItemStack targetSlotItem = targetSlot.getItem();
            targetStack = targetSlotItem.copy();

            if (pIndex == 0) {
                this.access.execute((curLevel, targetPos) -> targetSlotItem.getItem().onCraftedBy(targetSlotItem, curLevel, pPlayer));
                if (!moveItemStackTo(targetSlotItem, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                targetSlot.onQuickCraft(targetSlotItem, targetStack);
            } else if (pIndex >= 10 && pIndex < 46) {
                if (!moveItemStackTo(targetSlotItem, 1, 10, false)) {
                    if (pIndex < 37 && !moveItemStackTo(targetSlotItem, 37, 46, false)) return ItemStack.EMPTY;
                    else if (!moveItemStackTo(targetSlotItem, 10, 37, false)) return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(targetSlotItem, 10, 46, false)) return ItemStack.EMPTY;

            if (targetSlotItem.isEmpty()) targetSlot.set(ItemStack.EMPTY);
            else targetSlot.setChanged();

            if (targetSlotItem.getCount() == targetStack.getCount()) return ItemStack.EMPTY;

            targetSlot.onTake(pPlayer, targetSlotItem);

            if (pIndex == 0) pPlayer.drop(targetSlotItem, false);
        }
        return targetStack;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }

    @Override
    public int getSize() {
        return 10;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return CSRecipeBookSetupEvents.CELESTIAL_CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int pSlotIndex) {
        return pSlotIndex != getResultSlotIndex();
    }
}
