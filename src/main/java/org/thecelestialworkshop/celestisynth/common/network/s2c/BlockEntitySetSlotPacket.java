package org.thecelestialworkshop.celestisynth.common.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BlockEntitySetSlotPacket {
    private final BlockPos blockEntityPosition;
    private final int itemSlot;
    private final ItemStack itemStack;

    public BlockEntitySetSlotPacket(BlockPos blockEntityPosition, int itemSlot, ItemStack itemStack) {
        this.blockEntityPosition = blockEntityPosition;
        this.itemSlot = itemSlot;
        this.itemStack = itemStack;
    }

    public BlockEntitySetSlotPacket(FriendlyByteBuf buf) {
        this.blockEntityPosition = buf.readBlockPos();
        this.itemSlot = buf.readInt();
        this.itemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockEntityPosition);
        buf.writeInt(itemSlot);
        buf.writeItem(itemStack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            BlockEntity blockEntity = instance.level.getBlockEntity(blockEntityPosition);
            if (blockEntity instanceof Container container) {
                container.setItem(itemSlot, itemStack);
            }
        });
        return true;
    }
}
