package org.thecelestialworkshop.celestisynth.manager;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.network.c2s.ShakeScreenForAllPacket;
import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateAnimationToAllPacket;
import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateParticlePacket;
import org.thecelestialworkshop.celestisynth.common.network.s2c.*;
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
        INSTANCE.messageBuilder(SetAnimationPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(SetAnimationPacket::new)
                .encoder(SetAnimationPacket::toBytes)
                .consumerMainThread(SetAnimationPacket::handle)
                .add();

        INSTANCE.messageBuilder(ShakeScreenForAllPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ShakeScreenForAllPacket::new)
                .encoder(ShakeScreenForAllPacket::toBytes)
                .consumerMainThread(ShakeScreenForAllPacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateParticlePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateParticlePacket::new)
                .encoder(UpdateParticlePacket::toBytes)
                .consumerMainThread(UpdateParticlePacket::handle)
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

        INSTANCE.messageBuilder(UpdateGroupedParticlePacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateGroupedParticlePacket::new)
                .encoder(UpdateGroupedParticlePacket::toBytes)
                .consumerMainThread(UpdateGroupedParticlePacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateAnimationToAllPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateAnimationToAllPacket::new)
                .encoder(UpdateAnimationToAllPacket::toBytes)
                .consumerMainThread(UpdateAnimationToAllPacket::handle)
                .add();

        INSTANCE.messageBuilder(BlockEntitySetSlotPacket.class, PACKET_ID++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BlockEntitySetSlotPacket::new)
                .encoder(BlockEntitySetSlotPacket::toBytes)
                .consumerMainThread(BlockEntitySetSlotPacket::handle)
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
