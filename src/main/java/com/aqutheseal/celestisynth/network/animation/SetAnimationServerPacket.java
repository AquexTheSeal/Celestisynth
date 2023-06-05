package com.aqutheseal.celestisynth.network.animation;

import com.aqutheseal.celestisynth.network.CSNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetAnimationServerPacket {
    private final int animId;

    public SetAnimationServerPacket(int animId) {
        this.animId = animId;
    }

    public SetAnimationServerPacket(FriendlyByteBuf buf) {
        this.animId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(animId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            CSNetwork.sendToPlayersNearbyAndSelf(new SetAnimationToAllPacket(context.getSender().getId(), this.animId), context.getSender());
        });
        return true;
    }
}
