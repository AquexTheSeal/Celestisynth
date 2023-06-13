package com.aqutheseal.celestisynth.network.animation;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.animation.CSAnimator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetAnimationToAllPacket {
    private final boolean isOtherLayer;
    private final int playerId;
    private final int animId;

    public SetAnimationToAllPacket(boolean isOtherLayer, int playerId, int animId) {
        this.isOtherLayer = isOtherLayer;
        this.playerId = playerId;
        this.animId = animId;
    }

    public SetAnimationToAllPacket(FriendlyByteBuf buf) {
        this.isOtherLayer = buf.readBoolean();
        this.playerId = buf.readInt();
        this.animId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isOtherLayer);
        buf.writeInt(playerId);
        buf.writeInt(animId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            var player = instance.level.getEntity(playerId);
            animatePlayer(isOtherLayer, animId, (AbstractClientPlayer) player);
        });
        return true;
    }

    public static void animatePlayer(boolean isOtherLayer, int animId, AbstractClientPlayer player) {
        var animation = isOtherLayer ? CSAnimator.otherAnimationData.get(player) : CSAnimator.animationData.get(player);
        if (animation != null) {
            AnimationManager.playAnimation(AnimationManager.getAnimFromId(animId).getAnimation(), animation);
        }
    }
}
