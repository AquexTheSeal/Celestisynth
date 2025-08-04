package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateAnimationToAllPacket;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetAnimationPacket {
    private final int layerIndex;
    private final ResourceLocation animId;

    public SetAnimationPacket(int layerIndex, ResourceLocation animId) {
        this.layerIndex = layerIndex;
        this.animId = animId;
    }

    public SetAnimationPacket(FriendlyByteBuf buf) {
        this.layerIndex = buf.readInt();
        this.animId = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(layerIndex);
        buf.writeResourceLocation(animId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> CSNetworkManager.sendToPlayersNearbyAndSelf(new UpdateAnimationToAllPacket(layerIndex, context.getSender().getId(), this.animId), context.getSender()));
        return true;
    }
}