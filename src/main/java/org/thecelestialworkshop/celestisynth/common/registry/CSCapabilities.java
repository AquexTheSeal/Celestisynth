package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapability;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CSCapabilities {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Celestisynth.MODID);

    public static final Supplier<AttachmentType<CSEntityCapability>> CS_ENTITY_DATA = ATTACHMENT_TYPES.register(
            "entity_data", () -> AttachmentType.serializable(CSEntityCapability::new).copyOnDeath().build());
}
