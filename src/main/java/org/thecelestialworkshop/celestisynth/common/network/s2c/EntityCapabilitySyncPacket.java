package org.thecelestialworkshop.celestisynth.common.network.s2c;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Replaces the CapabilitySyncer library's SimpleEntityCapabilityStatusPacket:
 * pushes the full attachment state of one entity to tracking clients.
 */
public record EntityCapabilitySyncPacket(int entityId, CompoundTag tag) implements CustomPacketPayload {
    public static final Type<EntityCapabilitySyncPacket> TYPE = new Type<>(Celestisynth.prefix("entity_capability_sync"));

    public static final StreamCodec<FriendlyByteBuf, EntityCapabilitySyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, EntityCapabilitySyncPacket::entityId,
            ByteBufCodecs.COMPOUND_TAG, EntityCapabilitySyncPacket::tag,
            EntityCapabilitySyncPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(EntityCapabilitySyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            if (instance.level == null) return;
            if (instance.level.getEntity(packet.entityId()) instanceof LivingEntity living) {
                CSEntityCapabilityProvider.unwrap(living).readSyncTag(packet.tag());
            }
        });
    }
}
