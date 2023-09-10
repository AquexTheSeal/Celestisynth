package com.aqutheseal.celestisynth.network;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.network.animation.SetAnimationServerPacket;
import com.aqutheseal.celestisynth.network.animation.SetAnimationToAllPacket;
import com.aqutheseal.celestisynth.network.util.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CSNetwork {

    public static SimpleChannel INSTANCE;

    private static int packetId = 0;

    public static void register() {
        SimpleChannel network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Celestisynth.MODID, "messages")).networkProtocolVersion(() -> "1.0").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE = network;

        network.messageBuilder(CSSpawnParticlePacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CSSpawnParticlePacket::new)
                .encoder(CSSpawnParticlePacket::toBytes)
                .consumerMainThread(CSSpawnParticlePacket::handle)
                .add();

        network.messageBuilder(SetAnimationToAllPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetAnimationToAllPacket::new)
                .encoder(SetAnimationToAllPacket::toBytes)
                .consumerMainThread(SetAnimationToAllPacket::handle)
                .add();

        network.messageBuilder(SetAnimationServerPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetAnimationServerPacket::new)
                .encoder(SetAnimationServerPacket::toBytes)
                .consumerMainThread(SetAnimationServerPacket::handle)
                .add();

        network.messageBuilder(ShakeScreenToAllPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ShakeScreenToAllPacket::new)
                .encoder(ShakeScreenToAllPacket::toBytes)
                .consumerMainThread(ShakeScreenToAllPacket::handle)
                .add();

        network.messageBuilder(ShakeScreenServerPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ShakeScreenServerPacket::new)
                .encoder(ShakeScreenServerPacket::toBytes)
                .consumerMainThread(ShakeScreenServerPacket::handle)
                .add();

        network.messageBuilder(ChangeCameraTypePacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChangeCameraTypePacket::new)
                .encoder(ChangeCameraTypePacket::toBytes)
                .consumerMainThread(ChangeCameraTypePacket::handle)
                .add();

        network.messageBuilder(SetPersistentIntPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetPersistentIntPacket::new)
                .encoder(SetPersistentIntPacket::toBytes)
                .consumerMainThread(SetPersistentIntPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearby(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersNearbyAndSelf(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
    }

    public static <MSG> void sendToAll(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
