package com.aqutheseal.celestisynth.network.animation;

import com.aqutheseal.celestisynth.network.ForgeCSNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetAnimationServerPacket {
    private final boolean isOtherLayer;
    private final int animId;

    public SetAnimationServerPacket(boolean isOtherLayer, int animId) {
        this.isOtherLayer = isOtherLayer;
        this.animId = animId;
    }

    public SetAnimationServerPacket(FriendlyByteBuf buf) {
        this.isOtherLayer = buf.readBoolean();
        this.animId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isOtherLayer);
        buf.writeInt(animId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ForgeCSNetwork.sendToPlayersNearbyAndSelf(new SetAnimationToAllPacket(isOtherLayer, context.getSender().getId(), this.animId), context.getSender()));
        return true;
    }
}
