package com.aqutheseal.celestisynth.common.packet.network.animation;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;


public class SetAnimationToAllPacket {

    public SetAnimationToAllPacket(boolean isOtherLayer, int playerId, int animId) {

    }

    public SetAnimationToAllPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void animatePlayer(boolean isOtherLayer, int animId, AbstractClientPlayer player) {
    }
}
