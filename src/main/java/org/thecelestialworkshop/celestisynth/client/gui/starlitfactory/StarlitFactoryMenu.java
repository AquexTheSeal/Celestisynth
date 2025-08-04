package org.thecelestialworkshop.celestisynth.client.gui.starlitfactory;

import org.thecelestialworkshop.celestisynth.common.block.StarlitFactoryBlockEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSMenuTypes;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StarlitFactoryMenu extends AbstractContainerMenu {
    public static final int BASE_MATERIAL_SLOT = 0, BASE_MATERIAL_SLOT_1 = 1, BASE_MATERIAL_SLOT_2 = 2;
    public static final int SUPPORTING_MATERIAL_SLOT = 3, SUPPORTING_MATERIAL_SLOT_1 = 4, SUPPORTING_MATERIAL_SLOT_2 = 5;
    public static final int FUEL_SLOT = 6;
    public static final int RESULT_SLOT = 7;

    private static final int INV_SLOT_START = 8;
    private static final int INV_SLOT_END = 35;

    public final Container container;
    public final ContainerData data;
    public final Level level;

    public StarlitFactoryMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, new SimpleContainer(8), new SimpleContainerData(6));
    }

    public StarlitFactoryMenu(int pContainerId, Inventory pPlayerInventory, Container pContainer, ContainerData pData) {
        super(CSMenuTypes.STARLIT_FACTORY.get(), pContainerId);

        checkContainerSize(pContainer, 8);
        checkContainerDataCount(pData, 6);

        this.container = pContainer;
        this.data = pData;
        this.level = pPlayerInventory.player.level();

        // BASE MATERIAL SLOTS
        this.addSlot(new Slot(container, BASE_MATERIAL_SLOT, 8, 17));
        this.addSlot(new Slot(container, BASE_MATERIAL_SLOT_1, 44, 17));
        this.addSlot(new Slot(container, BASE_MATERIAL_SLOT_2, 26, 53));

        // SUPPORTING MATERIAL SLOTS
        this.addSlot(new Slot(container, SUPPORTING_MATERIAL_SLOT, 108, 17));
        this.addSlot(new Slot(container, SUPPORTING_MATERIAL_SLOT_1, 108, 35));
        this.addSlot(new Slot(container, SUPPORTING_MATERIAL_SLOT_2, 108, 53));

        // ENERGY FUEL SLOT
        this.addSlot(new Slot(container, FUEL_SLOT, 134, 35) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return StarlitFactoryBlockEntity.getFuelMap().containsKey(pStack.getItem());
            }
        });

        // RESULT SLOT
        this.addSlot(new Slot(container, RESULT_SLOT, 76, 34) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }
        });

        // INVENTORY SLOTS
        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) addSlot(new Slot(pPlayerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
        }

        for (int l = 0; l < 9; ++l) addSlot(new Slot(pPlayerInventory, l, 8 + l * 18, 142));

        this.addDataSlots(pData);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot slot = this.slots.get(pIndex);
        IntList modifiableContainerSlots = IntList.of(BASE_MATERIAL_SLOT, BASE_MATERIAL_SLOT_1, BASE_MATERIAL_SLOT_2, SUPPORTING_MATERIAL_SLOT, SUPPORTING_MATERIAL_SLOT_1, SUPPORTING_MATERIAL_SLOT_2, FUEL_SLOT);

        if (slot.hasItem()) {
            ItemStack highlightedItemStack = slot.getItem();
            ItemStack clonedStack = highlightedItemStack.copy();

            if (pIndex == RESULT_SLOT) {
                if (!this.moveItemStackTo(highlightedItemStack, INV_SLOT_START, INV_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(highlightedItemStack, clonedStack);

            } else if (!modifiableContainerSlots.contains(pIndex)) {
                if (this.isFuel(highlightedItemStack)) {
                    if (!this.moveItemStackTo(highlightedItemStack, FUEL_SLOT, FUEL_SLOT + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(highlightedItemStack, BASE_MATERIAL_SLOT, SUPPORTING_MATERIAL_SLOT_2 + 1, false)) {
                    return ItemStack.EMPTY;
                } else if (pIndex >= INV_SLOT_START && pIndex < INV_SLOT_END - 9) {
                    if (!this.moveItemStackTo(highlightedItemStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= INV_SLOT_END - 9 && pIndex < INV_SLOT_END && !this.moveItemStackTo(highlightedItemStack, INV_SLOT_START, INV_SLOT_END - 9, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(highlightedItemStack, INV_SLOT_START, INV_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (highlightedItemStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (highlightedItemStack.getCount() == clonedStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, highlightedItemStack);
        }
        return ItemStack.EMPTY;
    }

    protected boolean isFuel(ItemStack pStack) {
        return StarlitFactoryBlockEntity.getFuelMap().containsKey(pStack.getItem());
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }
}
