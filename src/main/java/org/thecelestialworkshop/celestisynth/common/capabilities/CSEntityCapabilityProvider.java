package org.thecelestialworkshop.celestisynth.common.capabilities;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.network.s2c.EntityCapabilitySyncPacket;
import org.thecelestialworkshop.celestisynth.common.registry.CSCapabilities;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Optional;

/**
 * Attachment access + sync triggers (start-tracking, login, respawn,
 * dimension change), formerly handled by the CapabilitySyncer library.
 */
public class CSEntityCapabilityProvider {
    public static final ResourceLocation CS_ENTITY_CAP_RL = Celestisynth.prefix("entity_data");

    public static CSEntityCapability unwrap(LivingEntity entity) {
        return entity.getData(CSCapabilities.CS_ENTITY_DATA);
    }

    public static Optional<CSEntityCapability> get(LivingEntity entity) {
        return Optional.of(unwrap(entity));
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity target && event.getEntity() instanceof ServerPlayer player) {
            CSNetworkManager.sendToPlayer(new EntityCapabilitySyncPacket(target.getId(), unwrap(target).writeSyncTag()), player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncSelf(event);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncSelf(event);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncSelf(event);
    }

    private static void syncSelf(PlayerEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CSNetworkManager.sendToPlayer(new EntityCapabilitySyncPacket(player.getId(), unwrap(player).writeSyncTag()), player);
        }
    }
}
