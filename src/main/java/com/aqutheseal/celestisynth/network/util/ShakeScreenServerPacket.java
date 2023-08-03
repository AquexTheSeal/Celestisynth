package com.aqutheseal.celestisynth.network.util;

import com.aqutheseal.celestisynth.network.CSNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ShakeScreenServerPacket {
    private final UUID playerId;
    private final int duration;
    private final int fadeOutStart;
    private final float intensity;

    public ShakeScreenServerPacket(UUID playerId, int duration, int fadeOutStart, float intensity) {
        this.playerId = playerId;
       this.duration = duration;
       this.fadeOutStart = fadeOutStart;
       this.intensity = intensity;
    }

    public ShakeScreenServerPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.duration = buf.readInt();
        this.fadeOutStart = buf.readInt();
        this.intensity = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerId);
        buf.writeInt(duration);
        buf.writeInt(fadeOutStart);
        buf.writeFloat(intensity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> CSNetwork.sendToAll(new ShakeScreenToAllPacket(playerId, duration, fadeOutStart, intensity)));
        return true;
    }
}
