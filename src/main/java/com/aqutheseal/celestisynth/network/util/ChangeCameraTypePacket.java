package com.aqutheseal.celestisynth.network.util;

import com.aqutheseal.celestisynth.item.weapons.AquafloraItem;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ChangeCameraTypePacket {
    private final int playerTarget;
    private final int enumID;

    public ChangeCameraTypePacket(int target, int enumID) {
        this.playerTarget = target;
        this.enumID = enumID;
    }

    public ChangeCameraTypePacket(FriendlyByteBuf buf) {
        this.playerTarget = buf.readInt();
        this.enumID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(playerTarget);
        buf.writeInt(enumID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            if (instance.player.getId() == playerTarget) {
                instance.options.setCameraType(CameraType.values()[enumID]);
            }
        });
        return true;
    }
}
