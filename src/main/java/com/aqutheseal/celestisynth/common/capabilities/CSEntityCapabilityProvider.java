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

public class CSEntityCapabilityProvider extends CapabilityAttacher {
    public static Capability<CSEntityCapability> CAPABILITY = getCapability(new CapabilityToken<>(){});

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static CSEntityCapability unwrap(LivingEntity entity) {
        return get(entity).orElse(null);
    }

    public static LazyOptional<CSEntityCapability> get(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new CSEntityCapability(entity), CAPABILITY, Celestisynth.prefix(CSEntityCapability.ID));
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CSEntityCapability.class);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, CSEntityCapabilityProvider::attach, CSEntityCapabilityProvider::get, false);
        SimpleEntityCapabilityStatusPacket.register(CSNetworkManager.INSTANCE, CSNetworkManager.PACKET_ID++);
        SimpleEntityCapabilityStatusPacket.registerRetriever(Celestisynth.prefix(CSEntityCapability.ID), CSEntityCapabilityProvider::unwrap);
    }
}
