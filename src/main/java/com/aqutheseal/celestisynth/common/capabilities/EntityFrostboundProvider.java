package com.aqutheseal.celestisynth.common.capabilities;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class EntityFrostboundProvider extends CapabilityAttacher {
    public static Capability<EntityFrostboundCapability> ENTITY_FROSTBOUND = getCapability(new CapabilityToken<>(){});

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static EntityFrostboundCapability getEntityFrostboundUnwrap(LivingEntity entity) {
        return getEntityFrostbound(entity).orElse(null);
    }

    public static LazyOptional<EntityFrostboundCapability> getEntityFrostbound(LivingEntity entity) {
        return entity.getCapability(ENTITY_FROSTBOUND);
    }

    private static void attach(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new EntityFrostboundCapability(entity), ENTITY_FROSTBOUND, Celestisynth.prefix(EntityFrostboundCapability.ID));
    }

    public static void register() {
        CapabilityAttacher.registerCapability(EntityFrostboundCapability.class);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, EntityFrostboundProvider::attach, EntityFrostboundProvider::getEntityFrostbound, true);
        SimpleEntityCapabilityStatusPacket.register(CSNetworkManager.INSTANCE, CSNetworkManager.PACKET_ID++);
        SimpleEntityCapabilityStatusPacket.registerRetriever(Celestisynth.prefix(EntityFrostboundCapability.ID), EntityFrostboundProvider::getEntityFrostboundUnwrap);
    }
}
