package org.thecelestialworkshop.celestisynth.manager;

import org.thecelestialworkshop.celestisynth.common.network.c2s.ShakeScreenForAllPacket;
import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateAnimationToAllPacket;
import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateParticlePacket;
import org.thecelestialworkshop.celestisynth.common.network.s2c.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CSNetworkManager {

    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        // C2S
        registrar.playToServer(SetAnimationPacket.TYPE, SetAnimationPacket.STREAM_CODEC, SetAnimationPacket::handle);
        registrar.playToServer(ShakeScreenForAllPacket.TYPE, ShakeScreenForAllPacket.STREAM_CODEC, ShakeScreenForAllPacket::handle);
        registrar.playToServer(UpdateParticlePacket.TYPE, UpdateParticlePacket.STREAM_CODEC, UpdateParticlePacket::handle);

        // S2C
        registrar.playToClient(ChangeCameraTypePacket.TYPE, ChangeCameraTypePacket.STREAM_CODEC, ChangeCameraTypePacket::handle);
        registrar.playToClient(ShakeScreenToAllPacket.TYPE, ShakeScreenToAllPacket.STREAM_CODEC, ShakeScreenToAllPacket::handle);
        registrar.playToClient(UpdateGroupedParticlePacket.TYPE, UpdateGroupedParticlePacket.STREAM_CODEC, UpdateGroupedParticlePacket::handle);
        registrar.playToClient(UpdateAnimationToAllPacket.TYPE, UpdateAnimationToAllPacket.STREAM_CODEC, UpdateAnimationToAllPacket::handle);
        registrar.playToClient(BlockEntitySetSlotPacket.TYPE, BlockEntitySetSlotPacket.STREAM_CODEC, BlockEntitySetSlotPacket::handle);
        registrar.playToClient(EntityCapabilitySyncPacket.TYPE, EntityCapabilitySyncPacket.STREAM_CODEC, EntityCapabilitySyncPacket::handle);
    }

    public static void sendToServer(CustomPacketPayload message) {
        PacketDistributor.sendToServer(message);
    }

    public static void sendToPlayer(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, message);
    }

    public static void sendToPlayersNearby(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayersTrackingEntity(player, message);
    }

    public static void sendToPlayersNearbyAndSelf(CustomPacketPayload message, ServerPlayer player) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, message);
    }

    public static void sendToPlayersTrackingEntityAndSelf(CustomPacketPayload message, Entity entity) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, message);
    }

    public static void sendToAll(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers(message);
    }
}
