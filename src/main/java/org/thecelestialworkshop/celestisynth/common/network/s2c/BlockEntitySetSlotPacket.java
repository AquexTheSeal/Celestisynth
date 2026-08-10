package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BlockEntitySetSlotPacket(BlockPos blockEntityPosition, int itemSlot, ItemStack itemStack) implements CustomPacketPayload {
    public static final Type<BlockEntitySetSlotPacket> TYPE = new Type<>(Celestisynth.prefix("block_entity_set_slot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntitySetSlotPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BlockEntitySetSlotPacket::blockEntityPosition,
            ByteBufCodecs.INT, BlockEntitySetSlotPacket::itemSlot,
            ItemStack.OPTIONAL_STREAM_CODEC, BlockEntitySetSlotPacket::itemStack,
            BlockEntitySetSlotPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BlockEntitySetSlotPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            if (instance.level == null) return;
            BlockEntity blockEntity = instance.level.getBlockEntity(packet.blockEntityPosition());
            if (blockEntity instanceof Container container) {
                container.setItem(packet.itemSlot(), packet.itemStack());
            }
        });
    }
}
