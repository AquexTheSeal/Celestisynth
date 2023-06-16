package com.aqutheseal.celestisynth.common.packet.network;

import net.minecraft.server.level.ServerPlayer;

public abstract class CSNetwork{
    public void register(){}

    public static <MSG> void sendToServer(MSG message) {
    }

    public <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
    }

    public <MSG> void sendToPlayersNearby(MSG message, ServerPlayer player) {
    }

    public static <MSG> void sendToAll(MSG message) {
    }
}
