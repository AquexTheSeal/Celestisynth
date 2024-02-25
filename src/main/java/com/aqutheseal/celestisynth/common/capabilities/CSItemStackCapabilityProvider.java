package com.aqutheseal.celestisynth.common.capabilities;

import com.aqutheseal.celestisynth.Celestisynth;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

public class CSItemStackCapabilityProvider extends CapabilityAttacher {
    private static final Class<CSItemStackCapability> CAPABILITY_CLASS = CSItemStackCapability.class;
    public static Capability<CSItemStackCapability> CAPABILITY = getCapability(new CapabilityToken<>(){});
    public static final ResourceLocation CS_ENTITY_CAP_RL = Celestisynth.prefix(CSItemStackCapability.ID);

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static CSItemStackCapability unwrap(ItemStack itemStack) {
        return get(itemStack).orElse(null);
    }

    public static LazyOptional<CSItemStackCapability> get(ItemStack itemStack) {
        return itemStack.getCapability(CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<ItemStack> event, ItemStack itemStack) {
        genericAttachCapability(event, new CSItemStackCapability(itemStack), CAPABILITY, CS_ENTITY_CAP_RL);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerItemStackAttacher(CSItemStackCapabilityProvider::attach, CSItemStackCapabilityProvider::get);
    }
}
