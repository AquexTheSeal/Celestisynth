package com.aqutheseal.celestisynth.network.util;

import com.aqutheseal.celestisynth.PlayerMixinSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShakeScreenToAllPacket {
    private final int playerId;
    private final int duration;
    private final int fadeOutStart;
    private final float intensity;

    public ShakeScreenToAllPacket(int playerId, int duration, int fadeOutStart, float intensity) {
        this.playerId = playerId;
       this.duration = duration;
       this.fadeOutStart = fadeOutStart;
       this.intensity = intensity;
    }

    public ShakeScreenToAllPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readInt();
        this.duration = buf.readInt();
        this.fadeOutStart = buf.readInt();
        this.intensity = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(playerId);
        buf.writeInt(duration);
        buf.writeInt(fadeOutStart);
        buf.writeFloat(intensity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            var player = instance.level.getEntity(playerId);
            if (player instanceof PlayerMixinSupport pms) {
                pms.setScreenShakeDuration(duration);
                pms.setScreenShakeFadeoutBegin(fadeOutStart);
                pms.setScreenShakeIntensity(intensity);
            }
        });
        return true;
    }
}
