package com.aqutheseal.celestisynth.manager;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.network.animation.SetAnimationServerPacket;
import com.aqutheseal.celestisynth.common.network.animation.SetAnimationToAllPacket;
import com.aqutheseal.celestisynth.common.network.util.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CSNetworkManager {
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(Celestisynth.prefix("messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    public static int PACKET_ID = 0;

    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        registerC2SPackets();
        registerS2CPackets();
    }

    private static void registerC2SPackets() {
        INSTANCE.messageBuilder(SetAnimationServerPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetAnimationServerPacket::new)
                .encoder(SetAnimationServerPacket::toBytes)
                .consumerMainThread(SetAnimationServerPacket::handle)
                .add();

        INSTANCE.messageBuilder(ShakeScreenServerPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ShakeScreenServerPacket::new)
                .encoder(ShakeScreenServerPacket::toBytes)
                .consumerMainThread(ShakeScreenServerPacket::handle)
                .add();
    }

    private static void registerS2CPackets() {
        INSTANCE.messageBuilder(ChangeCameraTypePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChangeCameraTypePacket::new)
                .encoder(ChangeCameraTypePacket::toBytes)
                .consumerMainThread(ChangeCameraTypePacket::handle)
                .add();

        INSTANCE.messageBuilder(ShakeScreenToAllPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ShakeScreenToAllPacket::new)
                .encoder(ShakeScreenToAllPacket::toBytes)
                .consumerMainThread(ShakeScreenToAllPacket::handle)
                .add();

        INSTANCE.messageBuilder(CSSpawnParticlePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CSSpawnParticlePacket::new)
                .encoder(CSSpawnParticlePacket::toBytes)
                .consumerMainThread(CSSpawnParticlePacket::handle)
                .add();

        INSTANCE.messageBuilder(SetAnimationToAllPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SetAnimationToAllPacket::new)
                .encoder(SetAnimationToAllPacket::toBytes)
                .consumerMainThread(SetAnimationToAllPacket::handle)
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
