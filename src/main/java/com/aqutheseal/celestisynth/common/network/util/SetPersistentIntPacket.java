package com.aqutheseal.celestisynth.common.network.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetPersistentIntPacket {
    private final String id;
    private final int value;

    public SetPersistentIntPacket(String id, int value) {
        this.id = id;
        this.value = value;
    }

    public SetPersistentIntPacket(FriendlyByteBuf buf) {
        this.id = buf.readUtf();
        this.value = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(id);
        buf.writeInt(value);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            CompoundTag persistentData = context.getSender().getPersistentData();
            persistentData.putInt(id, value);
        });
        return true;
    }
}
