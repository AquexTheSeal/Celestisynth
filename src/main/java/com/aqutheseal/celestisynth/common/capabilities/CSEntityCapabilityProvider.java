package com.aqutheseal.celestisynth.common.capabilities;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class CSEntityCapabilityProvider extends CapabilityAttacher {
    private static final Class<CSEntityCapability> CAPABILITY_CLASS = CSEntityCapability.class;
    public static Capability<CSEntityCapability> CAPABILITY = getCapability(new CapabilityToken<>(){});
    public static final ResourceLocation CS_ENTITY_CAP_RL = Celestisynth.prefix(CSEntityCapability.ID);

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static CSEntityCapability unwrap(LivingEntity entity) {
        return get(entity).orElse(null);
    }

    public static LazyOptional<CSEntityCapability> get(LivingEntity entity) {
        return entity.getCapability(CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Entity> event, LivingEntity entity) {
        genericAttachCapability(event, new CSEntityCapability(entity), CAPABILITY, CS_ENTITY_CAP_RL);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, CSEntityCapabilityProvider::attach, CSEntityCapabilityProvider::get, false);
        SimpleEntityCapabilityStatusPacket.register(CSNetworkManager.INSTANCE, CSNetworkManager.PACKET_ID++);
        SimpleEntityCapabilityStatusPacket.registerRetriever(Celestisynth.prefix(CSEntityCapability.ID), CSEntityCapabilityProvider::unwrap);
    }
}
