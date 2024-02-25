package com.aqutheseal.celestisynth.util;

import com.aqutheseal.celestisynth.common.capabilities.CSItemStackCapabilityProvider;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkinUtil {

    public static List<UUID> getAquaSkinWhitelist() {
        List<UUID> whitelist = new ArrayList<>();
        //whitelist.add(UUID.fromString("380df991-f603-344c-a090-369bad2a924a"));
        whitelist.add(UUID.fromString("4cbe462d-50b3-4c85-add1-b7e4d5b5673b"));
        return whitelist;
    }

    public static boolean isUsingAquaSkin(ItemStack stack) {
        AtomicBoolean aquaSkinFlag = new AtomicBoolean(false);
        stack.getCapability(CSItemStackCapabilityProvider.CAPABILITY).ifPresent(data -> {
            if (data.usingAquaSkin()) {
                aquaSkinFlag.set(true);
            }
        });
        return aquaSkinFlag.get();
    }
}
